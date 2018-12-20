package com.example.delllatitude.appolog.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.delllatitude.appolog.BlogApplication;
import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.adapters.LatestRecyclerViewAdapter;
import com.example.delllatitude.appolog.models.Blog;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment{
//
//    DatabaseReference currUserFavouritesFragmentRef, blogsRef;
//    ArrayList<String> favouriteBlogIDArrayList = new ArrayList<>();
//    static ArrayList<Blog> favBlogArrayList = new ArrayList<>();
    ArrayList<Blog> favBlogArrayList;
    RecyclerView recyclerView;
    TextView tvNoFavourite;
    LatestRecyclerViewAdapter latestRecyclerViewAdapter;
//    ChildEventListener blogIDchildEventListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreate: ");
        super.onCreate(savedInstanceState);

        favBlogArrayList = BlogApplication.getFavBlogsArrayList();
        latestRecyclerViewAdapter = new LatestRecyclerViewAdapter(getContext(), favBlogArrayList);

//        currUserFavouritesFragmentRef = FirebaseDatabase.getInstance().getReference().child("Users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favourites");
//        blogsRef = FirebaseDatabase.getInstance().getReference().child("Blogs");

//        fillAllFavouriteBlogIDs();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(favouriteBlogIDArrayList.size()!=0){
//                    tvNoFavourite.setVisibility(View.GONE);
//                }
//                fillFavouriteBlogs();
//            }
//        },500);

    }


//    private void fillFavouriteBlogs() {
//        Log.e("TAG", "fillFavouriteBlogs: " + favouriteBlogIDArrayList.size());
//        for(final String blogID : favouriteBlogIDArrayList){
//            blogsRef.child(blogID).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Log.e("TAG", "onDataChange: "+ blogID);
//                    Blog blog = dataSnapshot.getValue(Blog.class);
//                    if(!favBlogArrayList.contains(blog)) {
//                        favBlogArrayList.add(blog);
//                        latestRecyclerViewAdapter.notifyDataSetChanged();
////                    Log.e("TAG", "onDataChange: " + blog.getAuthorID() );
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Toast.makeText(getContext(), "Error fetching database. Please Try again", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

//    private void fillAllFavouriteBlogIDs() {
//        createChildListener();
//        currUserFavouritesFragmentRef.addChildEventListener(blogIDchildEventListener);
//    }
//
//    private void createChildListener() {
//       blogIDchildEventListener =  new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                String blogID = (String)dataSnapshot.getValue();
//                favouriteBlogIDArrayList.add(blogID);
//                Log.e("TAG", "onChildAdded: " + favouriteBlogIDArrayList.size());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                String blogID = (String)dataSnapshot.getValue();
//                favouriteBlogIDArrayList.remove(blogID);
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(), "Error fetching database. Please Try again", Toast.LENGTH_SHORT).show();
//            }
//        };
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_recent_top_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("TAG", "onViewCreated: ");
        recyclerView = view.findViewById(R.id.rvLatest);
        tvNoFavourite = view.findViewById(R.id.tvLatestNoFavourite);
        if(favBlogArrayList.size()!=0){
            tvNoFavourite.setVisibility(View.GONE);
        }
        setAdapter();
    }

    private void setAdapter() {

        Log.e("TAG", "setAdapter: " + favBlogArrayList.size());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(latestRecyclerViewAdapter);
//        }
//        Log.e("TAG", "setAdapter: "+ favBlogArrayList.get(0).getAuthorID());
    }


    @Override
    public void onResume() {
        super.onResume();
        favBlogArrayList = BlogApplication.getFavBlogsArrayList();
        latestRecyclerViewAdapter.notifyDataSetChanged();
//        if(favBlogArrayList.size()!=0){
//            tvNoFavourite.setVisibility(View.GONE);
//        }
    }

}
