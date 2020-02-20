package com.example.delllatitude.appolog.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.models.Blog;
import com.example.delllatitude.appolog.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailedBlogActivity extends AppCompatActivity {

    Blog blog;
    ImageView imageView;
    TextView title, content, date, author;
    FloatingActionButton fabFavourite, fabLike;
    CircleImageView circleImageView;
    User blogAuthor;
    private boolean alreadyLiked;
    String currUserId;
    DatabaseReference currBlogLikersRef;
    DatabaseReference currBlogRef;
    FirebaseUser currUser;
    DatabaseReference currUserFavouritesRef;
    private boolean alreadyFav;
//    LinearLayout linearLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_blog);
        blog = getIntent().getParcelableExtra("blog");
        imageView = findViewById(R.id.detailIv);
        title = findViewById(R.id.detailTvTitle);
        content = findViewById(R.id.detailTvContent);
        date = findViewById(R.id.detailTvDate);
        author = findViewById(R.id.detailTvAuthor);
        fabFavourite = findViewById(R.id.detailFabFavourite);
        fabLike = findViewById(R.id.detailFabLike);
        circleImageView =findViewById(R.id.detailCiv);

        currBlogRef = FirebaseDatabase.getInstance().getReference().child("Blogs")
                .child(blog.getBlogID());
        currBlogLikersRef = currBlogRef.child("Likers");

        currUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currUser!=null) {
            currUserId = currUser.getUid();
            setLikeIcon();
            setFavIcon();
        }
//        if(FirebaseAuth.getInstance().getCurrentUser()==null){
//            fabFavourite.setVisibility(View.GONE);
//            fabLike.setVisibility(View.GONE);
//        }
//        linearLayout = findViewById(R.id.detailLlAuthor);
        setDetails();
        getBlogAuthorObject();
    }

    private void setFavIcon() {
        currUserFavouritesRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currUserId)
                .child("Favourites");

        currUserFavouritesRef.child(blog.getBlogID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                if (value == null) {
                    alreadyFav = false;
                    fabFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    }else{
                    alreadyFav = true;
                    fabFavourite.setImageResource(R.drawable.ic_favourite_filled);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailedBlogActivity.this, "Error in fetching database",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLikeIcon() {
        currBlogLikersRef.child(currUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                if(value==null){
                    alreadyLiked = false;
                    fabLike.setImageResource(R.drawable.ic_like_white_44dp);
                    }else{
                    alreadyLiked = true;
                    fabLike.setImageResource(R.drawable.ic_like_filled);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailedBlogActivity.this, "Error in fetching database",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    //    get the blog's author details as they will be required when the user clicks on the author name
    private void getBlogAuthorObject() {
        final String blogAuthorID = blog.getAuthorID();
        DatabaseReference blogAuthorDetailsRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(blogAuthorID).child("Details");
        blogAuthorDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blogAuthor = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDetails() {
        Log.e("TAG", "setDetails: "+ blog.getMainImage() +", "+ blog.getContent() +", "+ blog.getTitle() +", "+ blog.getAuthor());
        if(blog.getMainImage()!=null){
            Picasso.get().load(blog.getMainImage()).placeholder(R.drawable.image_placeholder_green)
                    .centerCrop()
                    .fit()
                    .into(imageView);
        }else{
            imageView.setVisibility(View.GONE);
        }

        title.setText(blog.getTitle());

        content.setText(Html.fromHtml(blog.getContent()));

        author.setText(blog.getAuthor() + " \u2022 ");
        if(blog.getAuthorImage()!=null){
            Picasso.get().load(blog.getAuthorImage()).resize(36,36).centerCrop()
                    .into(circleImageView);
        }else{
            Picasso.get().load(R.drawable.ic_person_gray_24dp).into(circleImageView);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(blog.getTime());
        String date = calendar.get(Calendar.DATE) + "/"  + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
        this.date.setText(date);

        //opens user profile's page
//        author.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                launchDetailedUserActivity(blogAuthor);
//            }
//        });


        fabFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(DetailedBlogActivity.this, "Kindly Login to add to favourites", Toast.LENGTH_SHORT).show();
                } else {
                    if (!alreadyFav) {
                        currUserFavouritesRef.child(blog.getBlogID()).setValue(blog.getBlogID());
                        fabFavourite.setImageResource(R.drawable.ic_favourite_filled);
                        Toast.makeText(DetailedBlogActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        alreadyFav = true;
                    } else {
                        currUserFavouritesRef.child(blog.getBlogID()).removeValue();
                        Toast.makeText(DetailedBlogActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                        fabFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
                        alreadyFav = false;
                    }
                }
            }
        });

        fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(DetailedBlogActivity.this, "Kindly Login to like", Toast.LENGTH_SHORT).show();
                }else{
                    if(!alreadyLiked){
                        currBlogLikersRef.child(currUserId).setValue(currUserId);
                        int updatedLikes = blog.getLikes()+1;
                        blog.setLikes(updatedLikes);
                        fabLike.setImageResource(R.drawable.ic_like_filled);
                        currBlogRef.child("likes").setValue(updatedLikes);
                        alreadyLiked = true;

                    }else{
                        currBlogLikersRef.child(currUserId).removeValue();
                        int updatedLikes = blog.getLikes()-1;
                        if(updatedLikes<0){
                            updatedLikes=0;
                        }
                        fabLike.setImageResource(R.drawable.ic_like_white_44dp);
                        blog.setLikes(updatedLikes);
                        currBlogRef.child("likes").setValue(updatedLikes);
                        alreadyLiked = false;
                    }

                }
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            content.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
    }

//    private boolean isAlreadyFavourite(DatabaseReference databaseReference) {
//        final boolean[] isFavourite = new boolean[1];
//        databaseReference.child(blog.getBlogID()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String value = (String) dataSnapshot.getValue();
//                if(value==null){
//                    isFavourite[0] = false;
//                }else{
//                    isFavourite[0] = true;
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(DetailedBlogActivity.this, "Error in fetching database", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return isFavourite[0];
//    }

    //Launches the Detailed Blog Activity with the blog's author object as an intent extra
    private void launchDetailedUserActivity(User blogAuthor) {
        Intent intent = new Intent(DetailedBlogActivity.this, DetailedUserActivity.class);
        intent.putExtra("user", blogAuthor);
        startActivity(intent);
    }
}
