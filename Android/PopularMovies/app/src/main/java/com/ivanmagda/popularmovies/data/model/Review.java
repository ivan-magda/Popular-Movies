package com.ivanmagda.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;

public final class Review implements Parcelable {

    private final String mId;
    private final String mAuthor;
    private final String mContent;
    private final String mUrl;

    public Review(String id, String author, String content, String url) {
        this.mId = id;
        this.mAuthor = author;
        this.mContent = content;
        this.mUrl = url;
    }

    public Review(Parcel in) {
        this.mId = in.readString();
        this.mAuthor = in.readString();
        this.mContent = in.readString();
        this.mUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mUrl);
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getId() {
        return mId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public String getStringUrl() {
        return mUrl;
    }

    public URL getUrl() throws MalformedURLException {
        return new URL(mUrl);
    }

    @Override
    public String toString() {
        return "Review{" +
                "mId='" + mId + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }

}
