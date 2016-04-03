package ir.weproject.freelance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ir.weproject.freelance.freelance.AppConfig;
import ir.weproject.freelance.freelance.R;
import ir.weproject.freelance.helper.SessionManager;


public class WelcomeActivity extends AppCompatActivity {

    Button okButton;
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        okButton = (Button) findViewById(R.id.okBtn);
        session = new SessionManager(this);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //update registration level
                session.setRegistrationLevel(AppConfig.REGISTRATION_LEVEL.SELECT_ROLE);

                Intent roleActivity = new Intent(WelcomeActivity.this, RoleSelectActivity.class);
                startActivity(roleActivity);
                finish();
            }
        });
    }

}