package com.example.delllatitude.appolog.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.delllatitude.appolog.BlogApplication;
import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.models.Blog;

import java.util.ArrayList;

public class UserBlogsActivity extends AppCompatActivity {

    ArrayList<Blog> currUserBlogsArrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blogs);
        currUserBlogsArrayList = BlogApplication.getUserBlogsArrayList();
    }
}
