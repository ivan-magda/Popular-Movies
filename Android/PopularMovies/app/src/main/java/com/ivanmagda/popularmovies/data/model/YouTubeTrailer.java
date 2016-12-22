package com.ivanmagda.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class YouTubeTrailer implements Parcelable {

    private final String mId;
    private final String mKey;
    private final String mName;

    public YouTubeTrailer(String id, String key, String name) {
        this.mId = id;
        this.mKey = key;
        this.mName = name;
    }

    public YouTubeTrailer(Parcel in) {
        this.mId = in.readString();
        this.mKey = in.readString();
        this.mName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
    }

    public static final Parcelable.Creator<YouTubeTrailer> CREATOR = new Parcelable.Creator<YouTubeTrailer>() {
        @Override
        public YouTubeTrailer createFromParcel(Parcel parcel) {
            return new YouTubeTrailer(parcel);
        }

        @Override
        public YouTubeTrailer[] newArray(int size) {
            return new YouTubeTrailer[size];
        }
    };

    public String getId() {
        return mId;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return "YouTubeTrailer{" +
                "mId='" + mId + '\'' +
                ", mKey='" + mKey + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }

}
