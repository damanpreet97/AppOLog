package com.example.delllatitude.appolog.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.adapters.MyFirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RecentTopFragment extends Fragment{

    private RecyclerView recyclerView;
    private TextView tvNoFavourite;
    private MyFirebaseRecyclerAdapter myFirebaseRecyclerAdapter;


    public static RecentTopFragment newInstance(String tabName) {
        Bundle args = new Bundle();
        args.putString("tab", tabName);
//        args.putParcelableArrayList("BlogsArrayList", blogArrayList);
        RecentTopFragment fragment = new RecentTopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_top_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.rvLatest);
        tvNoFavourite = view.findViewById(R.id.tvLatestNoFavourite);
        tvNoFavourite.setVisibility(View.GONE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String tabName = getArguments().getString("tab");
        Query query;
        if(tabName.equals("Recent")) {
             query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Blogs")
                    .limitToLast(50);
        }else if(tabName.equals("Top")){
             query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Blogs")
                    .orderByChild("reads");
        }else {
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Blogs").orderByKey()
                    .limitToLast(50);
        }
        myFirebaseRecyclerAdapter = new MyFirebaseRecyclerAdapter(query, getContext(), null);
        recyclerView.setAdapter(myFirebaseRecyclerAdapter.getFirebaseRecyclerAdapter());
    }

    @Override
    public void onStart() {
        super.onStart();
        myFirebaseRecyclerAdapter.getFirebaseRecyclerAdapter().startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myFirebaseRecyclerAdapter.getFirebaseRecyclerAdapter().stopListening();
    }
}