package com.example.delllatitude.appolog.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.activities.DetailedBlogActivity;
import com.example.delllatitude.appolog.models.Blog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.richeditor.RichEditor;

public class UserBlogsAdapter extends RecyclerView.Adapter<UserBlogsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Blog> blogArrayList;

    public UserBlogsAdapter(Context context, ArrayList<Blog> blogArrayList) {
        this.context = context;
        this.blogArrayList = blogArrayList;
    }

    @NonNull
    @Override
    public UserBlogsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_blog_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserBlogsAdapter.MyViewHolder holder, int position) {
        final Blog currBlog = blogArrayList.get(position);

        //we divide the height by 6 as it was the layout weight of the image view in its xml
        if(currBlog.getMainImage()!=null){
            Picasso.get().load(currBlog.getMainImage()).placeholder(R.drawable.image_placeholder_green)
                    .resize(140,
                            200/6)
                    .centerCrop()
                    .into(holder.mainImage);
        }else{
            holder.mainImage.setVisibility(View.GONE);
        }

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
        return blogArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mainImage;
        TextView date, title, likes, content;

        public MyViewHolder(View itemView) {
            super(itemView);

            mainImage = itemView.findViewById(R.id.item_ub_iv_title_image);
            date = itemView.findViewById(R.id.item_ub_tv_date);
            title = itemView.findViewById(R.id.item_ub_tv_title);
            content = itemView.findViewById(R.id.item_ub_tv_content);
            likes = itemView.findViewById(R.id.item_ub_tv_likes);
        }
    }
}

