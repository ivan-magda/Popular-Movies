package com.ivanmagda.popularmovies.data.model;

import java.net.MalformedURLException;
import java.net.URL;

public final class Review {

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
