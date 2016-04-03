package ir.weproject.freelance.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import ir.weproject.freelance.helper.AndroidMultiPartEntity;
import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.RoundedNetworkImageView;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.CropOption;
import ir.weproject.freelance.ir.weproject.poem.objects.CropOptionAdapter;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.R;

public class ProfileActivity extends AppCompatActivity {
    private Uri mImageCaptureUri;
    private RoundedNetworkImageView imgPreview;

    LinearLayout emailLayout;
    LinearLayout nameLayout;
    LinearLayout passwordLayout;

    TextView email;
    TextView name;
    TextView displayName;
    TextView description;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    private static enum ACTIONS { CHANGE_NAME, CHANGE_EMAIL, CHANGE_PASSWORD};

//    private ProgressBar progressBar;
    private String filePath = null;
//    private TextView txtPercentage;
//    private Button btnUpload;
    long totalSize = 0;
    SessionManager session;
    SQLiteHandler db;
    private Bitmap photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<> (this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int item ) { //pick from camera
                if (item == 0) {
                    Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                    try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else { //pick from file
                    Intent intent = new Intent();

                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                }
            }
        } );

        final AlertDialog dialog = builder.create();

        ImageButton button 	= (ImageButton) findViewById(R.id.btn_crop);
        imgPreview = (RoundedNetworkImageView) findViewById(R.id.iv_photo);
        displayName = (TextView) findViewById(R.id.display_name);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        description = (TextView) findViewById(R.id.desc);

        emailLayout = (LinearLayout) findViewById(R.id.email_layout);
        nameLayout = (LinearLayout) findViewById(R.id.name_layout);
        passwordLayout = (LinearLayout) findViewById(R.id.password_layout);

//        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
//        btnUpload = (Button) findViewById(R.id.btnUpload);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        session = new SessionManager(ProfileActivity.this);

        db = new SQLiteHandler(ProfileActivity.this);

        //set user info
        displayName.setText(session.getCurrentUserName());
        name.setText(session.getCurrentUserName());
        email.setText(session.getCurrentUserEmail());
        ImageHelper.loadNetworkImage(ProfileActivity.this, imgPreview, session.getCurrentUserImageURL());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ProfileActivity.this, "Not editable!", Toast.LENGTH_SHORT).show();
            }
        });

        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameRequest(session.getCurrentUserID());
            }
        });

        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordRequest(session.getCurrentUserID());
            }
        });

        /*btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // uploading the file to server
                new UploadFileToServer().execute();
            }
        });*/
    }

    private void changeNameRequest(int currentUserID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change name");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        input.setText(session.getCurrentUserName());

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               if( input.getText() == null || input.getText().toString().trim().equals("")){
                   Toast.makeText(ProfileActivity.this, "Empty field", Toast.LENGTH_SHORT).show();
                   return;
               }else if(input.getText().toString().trim().equals(session.getCurrentUserName())){
                   Toast.makeText(ProfileActivity.this, "No Change", Toast.LENGTH_SHORT).show();
                   return;
               }
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.EXTRA_KEY_USER_NAME, input.getText().toString());
                params.put(AppConfig.EXTRA_KEY_USER_ID, String.valueOf(session.getCurrentUserID()));
                postAction(ACTIONS.CHANGE_NAME, params);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void changePasswordRequest(int currentUserID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change name");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.change_password_dialog, null);

        // Set up the input
        builder.setView(dialogView);

        final EditText pass = (EditText) dialogView.findViewById(R.id.currentPass);
        final EditText newPass = (EditText) dialogView.findViewById(R.id.newPass);
        final EditText confirmPass = (EditText) dialogView.findViewById(R.id.confirmPass);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (pass.getText() == null || newPass.getText() == null || confirmPass.getText() == null ||
                        pass.getText().toString().trim().equals("") || newPass.getText().toString().trim().equals("") || confirmPass.getText().toString().trim().equals("")){
                    Toast.makeText(ProfileActivity.this, "Empty field", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( !newPass.getText().toString().trim().equals(confirmPass.getText().toString().trim())){
                    Toast.makeText(ProfileActivity.this, "Passwords are not matched", Toast.LENGTH_SHORT).show();
                    return;
                }

                if( newPass.getText().toString().trim().length() < 6 ){
                    Toast.makeText(ProfileActivity.this, "Password is not strong enough", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> params = new HashMap<String, String>();
                params.put(AppConfig.EXTRA_KEY_USER_PASSWORD, newPass.getText().toString());
                params.put(AppConfig.EXTRA_KEY_USER_PASSWORD_OLD, pass.getText().toString());
                params.put(AppConfig.EXTRA_KEY_USER_ID, String.valueOf(session.getCurrentUserID()));
                postAction(ACTIONS.CHANGE_PASSWORD, params);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();

                break;

            case PICK_FROM_FILE:
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();

                if (extras != null) {
                    photo = extras.getParcelable("data");

//                    imgPreview.setImageBitmap(photo);
                }

                new UploadFileToServer().execute();

                File f = new File(mImageCaptureUri.getPath());

                if (f.exists()) f.delete();

                break;

        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
                    public void onClick( DialogInterface dialog, int item ) {
                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
                    }
                });

                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel( DialogInterface dialog ) {

                        if (mImageCaptureUri != null ) {
                            getContentResolver().delete(mImageCaptureUri, null, null );
                            mImageCaptureUri = null;
                        }
                    }
                } );

                AlertDialog alert = builder.create();

                alert.show();
            }
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
//            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
//            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
//            progressBar.setProgress(progress[0]);

            // updating percentage value
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(AppConfig.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

//                File sourceFile = new File(filePath);

                // Adding file data to http body
//                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("image", new StringBody(encodedImage));
                // Extra parameters if you want to pass to server
                entity.addPart("user_id", new StringBody(String.valueOf(session.getCurrentUserID())));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {

            // showing the server response in an alert dialog
//            processResponse(result);
            processResponse(result);
            super.onPostExecute(result);
        }

    }

    private void processResponse(String message) {
        try {
            JSONObject jo = new JSONObject(message);
            if (!jo.getBoolean("error")) {
                String imageURL = jo.getString("file_path");
                session.updateCurrentUserURL(imageURL);
                HashMap<String, String> user = db.getUserDetails();
                db.updateUser(user.get("name"), user.get("email"), user.get("uid"), imageURL);
                ImageHelper.loadNetworkImage(ProfileActivity.this, imgPreview, imageURL);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Upload Not successful", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void postAction(final ACTIONS action, final HashMap<String, String> data) {

        String tag_string_req = "posts_req";
        String action_URL = AppConfig.URL_UPDATE_PROFILE;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                JSONObject jObj = null;
                boolean error = true;
                String desc = "";
                try {
                    jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");
                    desc = jObj.getString("desc");
                } catch (JSONException e) {}

                Toast.makeText(ProfileActivity.this, desc, Toast.LENGTH_LONG).show();

                switch (action){
                    case CHANGE_NAME:
                        try {
                            name.setText(jObj.getString("name"));
                            displayName.setText(jObj.getString("name"));
                            session.updateCurrentUserName(jObj.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                switch (action){
                    case CHANGE_PASSWORD:
                        params.put("user_id", data.get(AppConfig.EXTRA_KEY_USER_ID));
                        params.put("old_password", data.get(AppConfig.EXTRA_KEY_USER_PASSWORD));
                        params.put("new_password", data.get(AppConfig.EXTRA_KEY_USER_PASSWORD_OLD));
                        params.put("action", action.name());
                        break;
                    case CHANGE_NAME:
                        params.put("user_id", data.get(AppConfig.EXTRA_KEY_USER_ID));
                        params.put("name", data.get(AppConfig.EXTRA_KEY_USER_NAME));
                        params.put("action", action.name());
                        break;
                    default:
                        break;
                }
                // Posting parameters to url
                return params;
            }
        };
        if( MainActivity.isNetworkAvailable(ProfileActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //set action bar
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}