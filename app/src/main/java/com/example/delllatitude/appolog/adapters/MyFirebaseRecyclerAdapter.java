package com.example.delllatitude.appolog.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.activities.DetailedBlogActivity;
import com.example.delllatitude.appolog.models.Blog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFirebaseRecyclerAdapter {

    private Query query;
    private Context context;
    private FirebaseRecyclerOptions<Blog> options;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private TextView tvNoResults;

    public MyFirebaseRecyclerAdapter(Query query, Context context, TextView tvNoResults) {
        this.query = query;
        this.context = context;
        this.tvNoResults = tvNoResults;
        options = new FirebaseRecyclerOptions.Builder<Blog>()
                .setQuery(query, Blog.class).build();
        initializeAdapter();
    }

    private void initializeAdapter(){

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, MyBlogHolder>(options) {

                @Override
                public int getItemCount() {
                    return super.getItemCount();
                }

            //this reverses the order of display, in a way it is sorting highest to lowest priority
                @NonNull
                @Override
                public Blog getItem(int position) {
                    return super.getItem(getItemCount() - (position + 1));
                }

                @NonNull
                @Override
                public MyBlogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(context).inflate(R.layout.item_card_latest, parent, false);
                    return new MyBlogHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull MyBlogHolder holder, int position, @NonNull final Blog model) {
//            Blog currBlog = blogArrayList.get(position);
                    if(tvNoResults != null) {
                        tvNoResults.setVisibility(View.GONE);
                    }
                    final Blog currBlog = model;
//                    final DatabaseReference currBlogRef = getRef(position);
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            currBlog.setLikes(currBlog.getLikes() + 1);
//                            currBlogRef.child("likes").setValue(currBlog.getLikes());
//                        }
//                    });
                    if(currBlog.getAuthorImage()!=null){
                        Picasso.get().load(currBlog.getAuthorImage()).into(holder.circleImageView);
                    }else{
                        holder.circleImageView.setImageResource(R.drawable.ic_person_gray_24dp);
                    }
                    holder.authName.setText(currBlog.getAuthor());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(currBlog.getTime());

                    holder.date.setText(calendar.get(Calendar.DATE) +"/"+ calendar.get(Calendar.MONTH)+"/"+ calendar.get(Calendar.YEAR));
                    if(currBlog.getTitle()!=null)
                        holder.title.setText(currBlog.getTitle());
//        holder.content.setText(Html.fromHtml(currBlog.getContent(), Html.FROM_HTML_MODE_COMPACT));
                    holder.likes.setText(currBlog.getLikes()+ " Likes");

                    if(currBlog.getContent()!=null)
                        holder.content.setText(Html.fromHtml(currBlog.getContent()));

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, DetailedBlogActivity.class);
                            intent.putExtra("blog", model);
                            context.startActivity(intent);
                        }
                    });
                }
            };

    }

    public FirebaseRecyclerAdapter getFirebaseRecyclerAdapter() {
        return firebaseRecyclerAdapter;
    }

    public int getItemCount(){
        return firebaseRecyclerAdapter.getItemCount();
    }

    class MyBlogHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView authName, date, title, content, likes;

        MyBlogHolder(View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.ivAuthor);
            authName = itemView.findViewById(R.id.tvAuthName);
            date = itemView.findViewById(R.id.tvDate);
            title = itemView.findViewById(R.id.tvTitle);
            content = itemView.findViewById(R.id.tvContent);
            likes = itemView.findViewById(R.id.tvLikes);
        }
    }
}
