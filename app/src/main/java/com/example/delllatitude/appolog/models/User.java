package com.example.delllatitude.appolog.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String userID, userName, userEmail, userMobNumber, userPhoto;
//    private Uri userPhoto;
    private int blogsPosted;

    public User() {
    }

    public User(String userID, String userName, String userEmail, String userMobNumber, String userPhoto, int blogsPosted) {
        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userMobNumber = userMobNumber;
        this.userPhoto = userPhoto;
        this.blogsPosted = blogsPosted;
    }

    protected User(Parcel in) {
        userID = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userMobNumber = in.readString();
        userPhoto = in.readString();
        blogsPosted = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobNumber() {
        return userMobNumber;
    }

    public void setUserMobNumber(String userMobNumber) {
        this.userMobNumber = userMobNumber;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getBlogsPosted() {
        return blogsPosted;
    }

    public void setBlogsPosted(int blogsPosted) {
        this.blogsPosted = blogsPosted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userMobNumber);
        dest.writeString(userPhoto);
        dest.writeInt(blogsPosted);
    }

    //    public Uri getUserPhoto() {
//        return userPhoto;
//    }
//
//    public void setUserPhoto(Uri userPhoto) {
//        this.userPhoto = userPhoto;
//    }
}
