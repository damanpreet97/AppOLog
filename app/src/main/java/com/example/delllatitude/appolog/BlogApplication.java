package com.example.delllatitude.appolog;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.delllatitude.appolog.models.Blog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BlogApplication extends Application {


    private static ArrayList<Blog> currUserBlogsArrayList = new ArrayList<>();
    DatabaseReference currUserFavouritesFragmentRef, blogsRef, currUserBloglistRef;;
//    ArrayList<String> favouriteBlogIDArrayList = new ArrayList<>();
    private static ArrayList<Blog> favBlogArrayList = new ArrayList<>();
    ChildEventListener favBlogIDsChildEventListener, currUserBlogIDsChildEventListener;

//    static ArrayList<Blog> recentBlogsArrayList = new ArrayList<>();
//    static ArrayList<Blog> topBlogsArrayList = new ArrayList<>();
//    DatabaseReference blogsReference;

    @Override
    public void onCreate() {
        super.onCreate();
        blogsRef = FirebaseDatabase.getInstance().getReference().child("Blogs");
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
//            currUserFavouritesFragmentRef = FirebaseDatabase.getInstance().getReference().child("Users")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Favourites");
//            blogsRef = FirebaseDatabase.getInstance().getReference().child("Blogs");
//
//            getAllFavouriteBlogIDs();
//        }else{
            FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    favBlogArrayList.clear();
                    currUserBlogsArrayList.clear();
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
                        currUserFavouritesFragmentRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(currUser.getUid()).child("Favourites");
                        currUserBloglistRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(currUser.getUid()).child("BlogList");

                        getAllFavouriteBlogIDs();
                    }
                }
            });
        }
//        FirebaseApp.initializeApp(this);
//        blogsReference = FirebaseDatabase.getInstance().getReference().child("Blogs");
//        blogsReference.addChildEventListener(this);
//
//        blogsReference.orderByChild("reads").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Blog blog = dataSnapshot.getValue(Blog.class);
//                topBlogsArrayList.add(0, blog);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Blog blog = dataSnapshot.getValue(Blog.class);
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    public static ArrayList<Blog> getRecentBlogsArrayList(){
//        return recentBlogsArrayList;
//    }
//
//    public static ArrayList<Blog> getTopBlogsArrayList(){
//        return topBlogsArrayList;
//    }
//
//
//    @Override
//    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//        Blog blog = dataSnapshot.getValue(Blog.class);
//        recentBlogsArrayList.add(0, blog);
//    }
//
//    @Override
//    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//    }
//
//    @Override
//    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
////        Blog blog = dataSnapshot.getValue(Blog.class);
////        recentBlogsArrayList.remove(blog);
//    }
//
//    @Override
//    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError databaseError) {
//

    private void getAllCurrUserBlogsIDs(){
        createChildListenerForCurrUserBlogs();
        currUserBloglistRef.addChildEventListener(currUserBlogIDsChildEventListener);
    }

    private void createChildListenerForCurrUserBlogs() {
        currUserBlogIDsChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String BlogID = (String) dataSnapshot.getValue();
                addBlogToCurrUserBlogArrayList(BlogID);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String BlogID = (String) dataSnapshot.getValue();
                removeBlogFromCurrUserBlogArrayList(BlogID);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    private void removeBlogFromCurrUserBlogArrayList(String blogID) {
        blogsRef.child(blogID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Blog blog = dataSnapshot.getValue(Blog.class);
                currUserBlogsArrayList.remove(blog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addBlogToCurrUserBlogArrayList(String blogID) {
        blogsRef.child(blogID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Blog blog = dataSnapshot.getValue(Blog.class);
                currUserBlogsArrayList.add(blog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getAllFavouriteBlogIDs() {
        createChildListenerForFavBlogs();
        currUserFavouritesFragmentRef.addChildEventListener(favBlogIDsChildEventListener);
    }

//    This listener also adds and removes blog to arraylist
    private void createChildListenerForFavBlogs() {
        favBlogIDsChildEventListener =  new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String blogID = (String)dataSnapshot.getValue();
//                favouriteBlogIDArrayList.add(blogID);
                addBlogToFavArrayList(blogID);
//                Log.e("TAG", "onChildAdded: " + blogID);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String blogID = (String)dataSnapshot.getValue();
//                Log.e("TAG", "onChildRemoved: " +blogID+"from datasase");
//                favouriteBlogIDArrayList.remove(blogID);
                removeBlogFromFavArrayList(blogID);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Error fetching database. Please Try again", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void addBlogToFavArrayList(String blogID){
        blogsRef.child(blogID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e("TAG", "onDataChange: "+ blogID);
                Blog blog = dataSnapshot.getValue(Blog.class);
//                if(!favBlogArrayList.contains(blog)) {
                    favBlogArrayList.add(blog);
//                    latestRecyclerViewAdapter.notifyDataSetChanged();
//                    Log.e("TAG", "onDataChange: " + blog.getAuthorID() );
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Error fetching database. Please Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeBlogFromFavArrayList(String blogID){
        blogsRef.child(blogID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e("TAG", "onDataChange: "+ blogID);
                Blog blog = dataSnapshot.getValue(Blog.class);
//                if(!favBlogArrayList.contains(blog)) {
//                Log.e("TAG", "onDataChange: " +"removing");
                boolean b = favBlogArrayList.remove(blog);
//                Log.e("TAG", "onDataChange: " +b);

//                    latestRecyclerViewAdapter.notifyDataSetChanged();
//                    Log.e("TAG", "onDataChange: " + blog.getAuthorID() );
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Error fetching database. Please Try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ArrayList<Blog> getFavBlogsArrayList(){
        return favBlogArrayList;
    }

    public static ArrayList<Blog> getUserBlogsArrayList(){
        return currUserBlogsArrayList;
    }
}
