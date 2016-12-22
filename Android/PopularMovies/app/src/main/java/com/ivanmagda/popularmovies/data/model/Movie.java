/**
 * Copyright (c) 2016 Ivan Magda
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ivanmagda.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ivanmagda.popularmovies.utilities.MovieDateUtils;

import java.util.Date;

/**
 * Defines TMDb movie object.
 */
public final class Movie implements Parcelable {

    private final int mId;
    private final String mPosterPath;
    private final String mOverview;
    private final String mReleaseDateString;
    private final String mTitle;
    private final double mRating;
    private byte[] mPoster;

    public Movie(int id, String posterPath, String overview, String releaseDateString, String title,
                 double rating, byte[] poster) {
        this.mId = id;
        this.mPosterPath = posterPath;
        this.mOverview = overview;
        this.mReleaseDateString = releaseDateString;
        this.mTitle = title;
        this.mRating = rating;
        this.mPoster = poster;
    }

    public Movie(Parcel in) {
        this.mId = in.readInt();
        this.mPosterPath = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDateString = in.readString();
        this.mTitle = in.readString();
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
        dest.writeString(mTitle);
        dest.writeDouble(mRating);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
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

    public String getTitle() {
        return mTitle;
    }

    public double getRating() {
        return mRating;
    }

    public Date getDate() {
        return MovieDateUtils.fromString(mReleaseDateString);
    }

    public byte[] getPoster() {
        return mPoster;
    }

    public void setPoster(byte[] mPoster) {
        this.mPoster = mPoster;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + mId +
                ", posterPath='" + mPosterPath + '\'' +
                ", overview='" + mOverview + '\'' +
                ", releaseDate='" + mReleaseDateString + '\'' +
                ", title='" + mTitle + '\'' +
                ", rating=" + mRating +
                '}';
    }
}
