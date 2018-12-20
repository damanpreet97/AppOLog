package com.example.delllatitude.appolog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.delllatitude.appolog.R;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    private int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    //resets the count every time the activity resumes and checks back for login after sending the Login intent
    @Override
    protected void onResume() {
        super.onResume();
        count = 0;
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
            launchMainActivity();
//            finish();
        }
    }

    //launches the login activity and finishes the current activity
    private void launchMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //launches the login activity and finishes the current activity
    public void launchLoginActivity(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }


    //prevents unintentional back press
    @Override
    public void onBackPressed() {
        if(count==0){
            count++;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show();
        }else {
            super.onBackPressed();
        }
    }
}
