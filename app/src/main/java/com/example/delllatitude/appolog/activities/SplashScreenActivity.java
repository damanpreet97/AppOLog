package com.example.delllatitude.appolog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.delllatitude.appolog.PrefManager;
import com.example.delllatitude.appolog.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private PrefManager prefManager;

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                prefManager = new PrefManager(SplashScreenActivity.this);
                if (prefManager.isFirstTimeLaunch()) {
                    i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                } else {
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
