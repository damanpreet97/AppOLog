package com.example.delllatitude.appolog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.activities.LoginActivity;
import com.example.delllatitude.appolog.activities.UserBlogsActivity;
import com.example.delllatitude.appolog.activities.UserProfileActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class YouFragment extends Fragment {

    TextView tvProfile, tvBlogs, tvLogout, tvNoLoginMsg, tvLegalAbout;
    Button btnLogin;
    LinearLayout llList;
    FirebaseUser currUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_you, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvProfile = view.findViewById(R.id.frag_you_tv_profile);
        tvLogout = view.findViewById(R.id.frag_you_tv_logout);
        tvBlogs = view.findViewById(R.id.frag_you_tv_blogs);
        tvNoLoginMsg = view.findViewById(R.id.frag_you_tv_no_login_msg);
        tvLegalAbout = view.findViewById(R.id.frag_you_tv_legal_about);
        btnLogin = view.findViewById(R.id.frag_you_btn_login);
        llList = view.findViewById(R.id.frag_you_ll_account);

        if (currUser==null){
            tvNoLoginMsg.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            llList.setVisibility(View.GONE);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLoginActivity();
            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachAnimation(v);
                launchUserProfileActivity();
            }
        });
                tvBlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachAnimation(v);
                launchUserBlogsActivity();
            }
        });
                tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachAnimation(v);
                logout();
            }
        });
                tvLegalAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachAnimation(v);
                launchLegalAboutActivity();
            }
        });

    }

    private void launchUserBlogsActivity() {
        Intent userBlogsActivityintent = new Intent(getContext(), UserBlogsActivity.class);
        startActivity(userBlogsActivityintent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: ");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currUser==null){
            tvNoLoginMsg.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            llList.setVisibility(View.GONE);
        }else{
            tvNoLoginMsg.setVisibility(View.GONE);
            btnLogin.setVisibility(View.INVISIBLE);
            llList.setVisibility(View.VISIBLE);
        }
    }

    private void launchLegalAboutActivity() {
    }

    private void launchUserProfileActivity() {
        Intent userProfileIntent = new Intent(getContext(), UserProfileActivity.class);
        startActivity(userProfileIntent);
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        launchLoginActivity();
    }

    private void launchLoginActivity() {
        Intent loginIntent = new Intent(getContext(), LoginActivity.class);
        startActivity(loginIntent);
    }

    public void attachAnimation(View view) {
        Animation animFadein = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
        view.startAnimation(animFadein);
    }
}
