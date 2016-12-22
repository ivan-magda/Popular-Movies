package com.ivanmagda.popularmovies.data.model;

public final class YouTubeTrailer {

    private final String mId;
    private final String mKey;
    private final String mName;

    public YouTubeTrailer(String id, String key, String name) {
        this.mId = id;
        this.mKey = key;
        this.mName = name;
    }

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
