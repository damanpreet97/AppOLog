package com.example.delllatitude.appolog.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.models.User;

public class DetailedUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_user);

        User user = getIntent().getParcelableExtra("user");
    }
}
