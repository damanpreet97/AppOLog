package com.example.delllatitude.appolog.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Blog implements Parcelable {

    private String title, author, content, authorImage, mainImage, authorID, blogID;
    private int likes;
    private long time;

    public Blog() {

    }

    @Override
    public boolean equals(Object obj) {
//        if(obj.getClass().equals(Blog.class))
        Blog blog = (Blog)obj;
//        Log.e("TAG", "equals: "+"hi" );
        return this.getBlogID().equals(blog.getBlogID());
    }

    public Blog(long time, String title, String author, String authorImage, String mainImage, int likes, String content, String authorID, String blogID) {
        this.time = time;
        this.title = title;
        this.author = author;
        this.authorImage = authorImage;
        this.mainImage = mainImage;
        this.likes = likes;
        this.content = content;
//        this.calendar.setTimeInMillis(time);
        this.authorID = authorID;
        this.blogID = blogID;
    }

    protected Blog(Parcel in) {
        time = in.readLong();
        title = in.readString();
        author = in.readString();
        authorImage = in.readString();
        mainImage = in.readString();
        likes = in.readInt();
        content = in.readString();
        authorID = in.readString();
        blogID = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

//    public Calendar getCalendar() {
//        return calendar;
//    }

//    public void setCalendar(Calendar calendar) {
//        this.calendar = calendar;
//    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getBlogID() {
        return blogID;
    }

    public void setBlogID(String blogID) {
        this.blogID = blogID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(authorImage);
        dest.writeString(mainImage);
        dest.writeInt(likes);
        dest.writeString(content);
        dest.writeString(authorID);
        dest.writeString(blogID);
    }

}
