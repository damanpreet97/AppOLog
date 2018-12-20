package com.example.delllatitude.appolog.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.adapters.MyFirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchableActivity extends AppCompatActivity {

    private TextView searchHeading;
    private RecyclerView recyclerView;
    private MyFirebaseRecyclerAdapter myFirebaseRecyclerAdapter;
    private TextView tvNoSearchResults;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e("TAG", "onCreate: SA");
        setContentView(R.layout.activity_searchable);
        searchHeading = findViewById(R.id.act_searchable_heading);
        recyclerView = findViewById(R.id.rvSearchable);
        tvNoSearchResults = findViewById(R.id.tvNoSearchResult);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            Intent intent = getIntent();
            setIntent(intent);
            handleIntent(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("TAG", "NI: SA");
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.e("TAG", "HI: SA");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query =
                    intent.getStringExtra(SearchManager.QUERY);
            searchHeading.setText("Search Results for: " + query);
            doSearch(query);
        }
    }

    private void doSearch(String query) {
        Log.e("TAG", "DS: SA");
        Query firebaseQuery = FirebaseDatabase.getInstance().getReference().child("Blogs").orderByChild("title")
                .startAt(query).endAt(query+"\uf8ff");
        myFirebaseRecyclerAdapter = new MyFirebaseRecyclerAdapter(firebaseQuery, getBaseContext(), tvNoSearchResults);
        myFirebaseRecyclerAdapter.getFirebaseRecyclerAdapter().startListening();
        recyclerView.setAdapter(myFirebaseRecyclerAdapter.getFirebaseRecyclerAdapter());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(myFirebaseRecyclerAdapter.getItemCount() == 0){
                    Log.e("TAG", "doSearch: " + myFirebaseRecyclerAdapter.getItemCount());
            tvNoSearchResults.setVisibility(View.VISIBLE);
                }
            }
        },500);

    }

}