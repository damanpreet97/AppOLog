package com.example.delllatitude.appolog.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delllatitude.appolog.PrefManager;
import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.models.User;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

    Button buttonProceed;
    FirebaseUser currUser;
    TextView welcomeTitle;
    DatabaseReference currUserDetailsReference;
    String currUserDpUri;
    PrefManager prefManager;
    int currUserBlogsPosted = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
//        buttonBack = findViewById(R.id.loginButtonBack);
        buttonProceed = findViewById(R.id.loginButtonProceed);

        welcomeTitle = findViewById(R.id.welcomeAppTitle);
        welcomeTitle.setPaintFlags(welcomeTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        prefManager = new PrefManager(this);

        FirebaseAuth.getInstance().addAuthStateListener(this);

//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.PhoneBuilder().build()))
                        .build();
                startActivity(loginIntent);
            }
        });

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        currUser = firebaseAuth.getCurrentUser();
//        Intent returnIntent =new Intent();
        if(currUser!=null){
            if(prefManager.isFirstTimeLaunch()) {
                prefManager.setFirstTimeLaunch(false);
            }
            setResult(Activity.RESULT_OK);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

            DatabaseReference rootReference = firebaseDatabase.getReference();
            currUserDetailsReference = rootReference.child("Users").child(currUser.getUid()).child("Details");

//This block checks for the new User
            currUserDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getChildrenCount()==0){
                        //new user
                        pushUserToDatabase();
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this, "Database Error. Please try again", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void pushUserToDatabase() {
        if(currUser.getPhotoUrl()!=null){
            currUserDpUri = currUser.getPhotoUrl().toString();
        }
        User user = new User(currUser.getUid(), currUser.getDisplayName(), currUser.getEmail(), currUser.getPhoneNumber(), currUserDpUri, currUserBlogsPosted);
        currUserDetailsReference.setValue(user);
    }

}
