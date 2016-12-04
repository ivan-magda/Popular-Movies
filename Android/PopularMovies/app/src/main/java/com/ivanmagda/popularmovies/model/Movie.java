package com.ivanmagda.popularmovies.model;

import java.util.Arrays;

public final class Movie {

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
