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

import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;
import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.AppController;
import ir.weproject.freelance.freelance.R;


public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            AppConfig.REGISTRATION_LEVEL level = session.getRegistrationLevel();
            Intent intent = null;
            switch (level){
                case WELCOME:
                    intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                    break;
                case PERSONAL_INFORMATION:
                    intent = new Intent(RegisterActivity.this, PersonalInformationActivity.class);
                    break;
                case PROFILE_IMAGE:
                    intent = new Intent(RegisterActivity.this, UploadProfileImageActivity.class);
                    break;
                case SELECT_ROLE:
                    intent = new Intent(RegisterActivity.this, RoleSelectActivity.class);
                    break;
                case SKILL_INFOR:
                    intent = new Intent(RegisterActivity.this, UpdateSkillsActivity.class);
                    break;
                case COMPLETE:
                    if( session.getCurrentUserRole().equalsIgnoreCase(AppConfig.EXTRA_KEY_FREELANCER_ROLE)) {
                        intent = new Intent(RegisterActivity.this, MainActivityFreelancer.class);
                    }
                    else{
                        intent = new Intent(RegisterActivity.this, MainActivity.class);
                    }
                    break;
                default:
                    break;
            }
            // User is already logged in. Take him to main activity
            intent.putExtra(AppConfig.EXTRA_KEY_USER_ID, session.getCurrentUserID());
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
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
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String uid = user.getString("user_id");

                        // Inserting row in users table
                        db.addUser(name, email, uid);

                        session.setLogin(true);
                        session.setCurrentUser(uid, email, name);

                        Toast.makeText(getApplicationContext(), "User successfully registered.", Toast.LENGTH_LONG).show();

                        //update registration level
                        session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.WELCOME);

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                ProfileActivity.class);
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "REGISTER");
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

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