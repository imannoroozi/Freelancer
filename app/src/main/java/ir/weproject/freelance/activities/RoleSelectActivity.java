package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.helper.SQLiteHandler;
import ir.weproject.freelance.helper.SessionManager;


public class RoleSelectActivity extends AppCompatActivity {


    Button employerBtn, freelancerBtn;
    private SessionManager session;
    private SQLiteHandler db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);

        employerBtn = (Button) findViewById(R.id.employerBtn);
        freelancerBtn = (Button) findViewById(R.id.employeeBtn);

        session = new SessionManager(this);
        db = new SQLiteHandler(this);

        employerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update registration level
                session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.PROFILE_IMAGE);

                session.setRole(AppConfig.EXTRA_KEY_EMPLOYER_ROLE);
                Intent home = new Intent(RoleSelectActivity.this, UploadProfileImageActivity.class);
                startActivity(home);
                finish();
            }
        });

        freelancerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update registration level
                session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.PERSONAL_INFORMATION);

                session.setRole(AppConfig.EXTRA_KEY_FREELANCER_ROLE);
                Intent info = new Intent(RoleSelectActivity.this, PersonalInformationActivity.class);
                startActivity(info);
                finish();
            }
        });
    }

}