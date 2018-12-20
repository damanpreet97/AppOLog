package com.example.delllatitude.appolog.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.activities.DetailedBlogActivity;
import com.example.delllatitude.appolog.models.Blog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class LatestRecyclerViewAdapter extends RecyclerView.Adapter<LatestRecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Blog> blogArrayList;
    DatabaseReference blogsRef = FirebaseDatabase.getInstance().getReference().child("Blogs");

    public LatestRecyclerViewAdapter(Context context, ArrayList<Blog> blogArrayList) {
        this.context = context;
        this.blogArrayList = blogArrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        //        view items here
        CircleImageView circleImageView;
        TextView authName, date, title, content, likes;


        public MyViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.ivAuthor);
            authName = itemView.findViewById(R.id.tvAuthName);
            date = itemView.findViewById(R.id.tvDate);
            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvContent);
            likes = itemView.findViewById(R.id.tvLikes);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    final Blog currBlog = blogArrayList.get(getAdapterPosition());
//                    currBlog.setLikes(currBlog.getLikes() + 1);
//                    blogsRef.orderByChild("time")
//                            .equalTo(currBlog.getTime())
//                            .addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                    String key = dataSnapshot.getKey();
//                                    blogsRef.child(key).child("likes").setValue(currBlog.getLikes());
//                                }
//
//                                @Override
//                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
////                    currBlogRef.getRef().child("likes").setValue(currBlog.getLikes());
//                }
//            });
        }
    }

    @NonNull
    @Override
    public LatestRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("TAG", "onCreateViewHolder: ");
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_latest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LatestRecyclerViewAdapter.MyViewHolder holder, int position) {
//        set each item in the view
        Log.e("TAG", "onBindViewHolder: ");
        final Blog currBlog = blogArrayList.get(position);
//        Picasso.get().load(currBlog.getAuthorImage()).into(holder.circleImageView);
//        holder.authName.setText(currBlog.getAuthor());
////        holder.date.setText("Posted on : " + currBlog.getCalendar().get(Calendar.DATE) +"/"+ currBlog.getCalendar().get(Calendar.MONTH));
//        holder.title.setText(currBlog.getTitle());
////        holder.content.setText(Html.fromHtml(currBlog.getContent(), Html.FROM_HTML_MODE_COMPACT));
//        holder.likes.setText(currBlog.getLikes() + "");

        if(currBlog.getAuthorImage()!=null){
            Picasso.get().load(currBlog.getAuthorImage()).into(holder.circleImageView);
        }else{
            holder.circleImageView.setImageResource(R.drawable.ic_person_gray_24dp);
        }
        holder.authName.setText(currBlog.getAuthor());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currBlog.getTime());

        holder.date.setText(calendar.get(Calendar.DATE) +"/"+ calendar.get(Calendar.MONTH)+"/"+ calendar.get(Calendar.YEAR));
        holder.title.setText(currBlog.getTitle());
//        holder.content.setText(Html.fromHtml(currBlog.getContent(), Html.FROM_HTML_MODE_COMPACT));
        holder.likes.setText(currBlog.getLikes()+ " Likes");
//                    if(currBlog.getContent().length()>=100) {
//                        holder.content.setText(Html.fromHtml(currBlog.getContent()).subSequence(0, 100)+"....");
//                    }else{
        holder.content.setText(Html.fromHtml(currBlog.getContent()));
//                    }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedBlogActivity.class);
                intent.putExtra("blog", currBlog);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
//        Log.e("TAG", "getItemCount: " + blogArrayList.size() );
        return blogArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
