package com.example.delllatitude.appolog.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.delllatitude.appolog.BlogApplication;
import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.adapters.UserBlogsAdapter;
import com.example.delllatitude.appolog.models.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UserBlogsActivity extends AppCompatActivity {

    ArrayList<Blog> currUserBlogsArrayList = new ArrayList<>();
    RecyclerView topRecyclerView, recentRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blogs);

        recentRecyclerView = findViewById(R.id.act_user_blog_rv_Recent);
        topRecyclerView = findViewById(R.id.act_user_blog_rv_top);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currUserBlogsArrayList = BlogApplication.getUserBlogsArrayList();
        setAdapterRecentView();
        setAdapterTopView();
    }

    private void setAdapterRecentView() {
        ArrayList<Blog> reverseSortedBlogs = new ArrayList<Blog>();
        reverseSortedBlogs.addAll(currUserBlogsArrayList);
        Collections.reverse(reverseSortedBlogs);
        UserBlogsAdapter userBlogsAdapter = new UserBlogsAdapter(this, reverseSortedBlogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        recentRecyclerView.setLayoutManager(linearLayoutManager);
        recentRecyclerView.setAdapter(userBlogsAdapter);
    }

    private void setAdapterTopView() {
    //First we sort the arrayList according to likes in desc. order
    ArrayList<Blog> likeSortedBlogs = new ArrayList<Blog>();
    likeSortedBlogs.addAll(currUserBlogsArrayList);
        Collections.sort(likeSortedBlogs, new Comparator<Blog>() {
            @Override
            public int compare(Blog o1, Blog o2) {
//                if(o1.getLikes() == o2.getLikes())
//                    return 0;
//                else if(o1.getLikes() > o2.getLikes())
//                    return 1;
//                else
//                    return -1;
            // descending order sorting
                return o2.getLikes() - o1.getLikes();
            }
        });

        UserBlogsAdapter userBlogsAdapter = new UserBlogsAdapter(this, likeSortedBlogs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        topRecyclerView.setLayoutManager(linearLayoutManager);
        topRecyclerView.setAdapter(userBlogsAdapter);
    }
}
