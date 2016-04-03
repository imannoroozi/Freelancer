package ir.weproject.freelance.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;


public class PersonalInformationActivity extends Activity {
    private static final String TAG = PersonalInformationActivity.class.getSimpleName();

    private Button btnNext, placePickerBtn;
    private EditText inputFullName, inputEmail, inputBriefIntro, inputSummary, inputCity, inputContactNumber;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    int PLACE_PICKER_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personel_info);

        inputFullName = (EditText) findViewById(R.id.display_name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputBriefIntro = (EditText) findViewById(R.id.brief_intro);
        inputSummary = (EditText) findViewById(R.id.summary);
        inputCity = (EditText) findViewById(R.id.address);
        inputContactNumber = (EditText) findViewById(R.id.contactNumber);

        btnNext = (Button) findViewById(R.id.btnNext);

        placePickerBtn = (Button) findViewById(R.id.placePicker);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        inputFullName.setText(session.getCurrentUserName());
        inputEmail.setText(session.getCurrentUserEmail());

        // Register Button Click event
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String briefIntro = inputBriefIntro.getText().toString().trim();
                String summary = inputSummary.getText().toString().trim();
                String city = inputCity.getText().toString().trim();
                String contactNumber = inputContactNumber.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() ) {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("name", name);
                    data.put("email", email);
                    data.put("briefIntro", briefIntro);
                    data.put("summary", summary);
                    data.put("city", city);
                    data.put("contactNumber", contactNumber);
                    data.put("user_id", String.valueOf(session.getCurrentUserID()));
                    updatePersonalInfo(data);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        placePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(PersonalInformationActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                /*Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();*/
            }
        }
    }

    /**
     * Function to store user in MySQL database will post params to update url
     * */
    private void updatePersonalInfo(final HashMap<String,String> data) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "User successfully registered.", Toast.LENGTH_LONG).show();

                        //update registration level
                        session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.SKILL_INFOR);

                        // Launch login activity
                        Intent intent = new Intent(
                                PersonalInformationActivity.this,
                                UpdateSkillsActivity.class);
//                        intent.putExtra(AppConfig.EXTRA_KEY_USER_ID, session.getCurrentUserID());
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("desc");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = data;
                params.put("action", "UPDATE_PERSONAL_INFO");

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}