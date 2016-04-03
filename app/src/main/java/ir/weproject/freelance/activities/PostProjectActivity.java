package ir.weproject.freelance.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.weproject.freelance.helper.ImageHelper;
import ir.weproject.freelance.helper.RoundedNetworkImageView;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.ir.weproject.poem.objects.CropOption;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.R;

public class PostProjectActivity extends AppCompatActivity {
    private Uri mImageCaptureUri;
    private ImageView imgPreview;
    private Button previewButton;
    private Button postButton;
    private Button backButton;

    //post items
    EditText titleEdit;
    EditText poetEdit;
    EditText contentEdit;

    //Preview items
    TextView titlePreview;
    TextView authorNamePreview;
    TextView datePreview;
    TextView poetPreview;
    TextView contentPreview;
    ImageView postImagePreview;
    RoundedNetworkImageView authorImagePreview;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;

    private static enum ACTIONS { POST_POEM };

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

        setContentView(R.layout.activity_post_poem);

        session = new SessionManager(PostProjectActivity.this);

        db = new SQLiteHandler(PostProjectActivity.this);

        titlePreview = (TextView) findViewById(R.id.poem_post_title_pewview);
        authorNamePreview = (TextView) findViewById(R.id.author_name_preview);
        datePreview = (TextView) findViewById(R.id.post_date_preview);
        poetPreview = (TextView) findViewById(R.id.poem_post_poet_preview);
        contentPreview = (TextView) findViewById(R.id.poem_post_content_preview);
        postImagePreview = (ImageView) findViewById(R.id.post_image_preview);
        authorImagePreview = (RoundedNetworkImageView) findViewById(R.id.author_image_preview);

        titleEdit = (EditText) findViewById(R.id.title_edit);
        poetEdit = (EditText) findViewById(R.id.poet_edit);
        contentEdit = (EditText) findViewById(R.id.content_edit);

        previewButton = (Button) findViewById(R.id.preview_button);
        backButton = (Button) findViewById(R.id.cancel_btton);
        postButton = (Button) findViewById(R.id.post_button);

        ImageHelper.loadNetworkImage(this, authorImagePreview, session.getCurrentUserImageURL());

        final String [] items			= new String [] {"Take from camera", "Select from gallery"};
        ArrayAdapter<String> adapter	= new ArrayAdapter<> (this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder		= new AlertDialog.Builder(this);

        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

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
        });

        final AlertDialog dialog = builder.create();

        imgPreview = (ImageView) findViewById(R.id.post_image);

        Button btnAttach = (Button) findViewById(R.id.attach_iamge);

        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        View.OnClickListener viewFlipperListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        };

        backButton.setOnClickListener(viewFlipperListener);

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorNamePreview.setText(session.getCurrentUserName());
                titlePreview.setText(titleEdit.getText().toString());
                datePreview.setText("emrooz");
                poetPreview.setText(poetEdit.getText().toString());
                contentPreview.setText(contentEdit.getText().toString());
                if(photo != null) postImagePreview.setImageBitmap(photo);
                viewFlipper.showNext();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> params = new HashMap<>();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                params.put("poem_image", encodedImage);
                params.put("poem_title", titleEdit.getText().toString());
                params.put("poem_poet", poetEdit.getText().toString());
                params.put("poem_content", contentEdit.getText().toString());
                params.put("user_id", String.valueOf(session.getCurrentUserID()));
                params.put("action", ACTIONS.POST_POEM.name());

                postAction(ACTIONS.POST_POEM, params);
            }
        });
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

                    imgPreview.setImageBitmap(photo);
                }

                //new UploadFileToServer().execute();

                //File f = new File(mImageCaptureUri.getPath());

                //if (f.exists()) f.delete();

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

            intent.putExtra("outputX", 500);
            intent.putExtra("outputY", 500);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("crop", true);
            intent.putExtra("return-data", true);

//            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            /*} else {
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
            }*/
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
//                ImageHelper.loadNetworkImage(PostProjectActivity.this, imgPreview, imageURL);
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
        String action_URL = AppConfig.URL_POST_POEM;
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

                Toast.makeText(PostProjectActivity.this, desc, Toast.LENGTH_LONG).show();

                switch (action){
                    case POST_POEM:

                        break;
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                Toast.makeText(PostProjectActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return data;
            }
        };
        if( MainActivity.isNetworkAvailable(PostProjectActivity.this)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    @Override
    public void onBackPressed() {
        if( titleEdit.getText().toString().matches("") &&
                contentEdit.getText().toString().matches("") &&
                poetEdit.getText().toString().matches("") &&
                photo == null) finish();
        else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Closing Activity")
                    .setMessage("Are you sure you want to close this activity?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
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