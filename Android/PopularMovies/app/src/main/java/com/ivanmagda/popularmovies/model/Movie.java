package com.ivanmagda.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ivanmagda.popularmovies.utilities.MovieDateUtils;

import java.util.Arrays;
import java.util.Date;

public final class Movie implements Parcelable {

    private final int mId;
    private final String mPosterPath;
    private final String mOverview;
    private final String mReleaseDateString;
    private final int[] mGenreIds;
    private final String mTitle;
    private final boolean mHasVideo;
    private final double mRating;

    public Movie(int id, String posterPath, String overview, String releaseDateString,
                 int[] genreIds, String title, boolean hasVideo, double rating) {
        this.mId = id;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mReleaseDateString = releaseDateString;
        this.mGenreIds = genreIds;
        this.mTitle = title;
        this.mHasVideo = hasVideo;
        this.mRating = rating;
    }

    public Movie(Parcel in) {
        this.mId = in.readInt();
        this.mPosterPath = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDateString = in.readString();
        this.mGenreIds = in.createIntArray();
        this.mTitle = in.readString();
        this.mHasVideo = in.readByte() != 0;
        this.mRating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(mId);
        dest.writeString(mPosterPath);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDateString);
        dest.writeIntArray(mGenreIds);
        dest.writeString(mTitle);
        dest.writeByte((byte) (mHasVideo ? 1 : 0));
        dest.writeDouble(mRating);
    }

    static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getReleaseDateString() {
        return mReleaseDateString;
    }

    public int[] getGenreIds() {
        return mGenreIds;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean isHasVideo() {
        return mHasVideo;
    }

    public double getRating() {
        return mRating;
    }

    public Date getDate() {
        return MovieDateUtils.fromString(mReleaseDateString);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + mId +
                ", posterPath='" + mPosterPath + '\'' +
                ", overview='" + mOverview + '\'' +
                ", releaseDate='" + mReleaseDateString + '\'' +
                ", genreIds=" + Arrays.toString(mGenreIds) +
                ", title='" + mTitle + '\'' +
                ", hasVideo=" + mHasVideo +
                ", rating=" + mRating +
                '}';
    }
}
