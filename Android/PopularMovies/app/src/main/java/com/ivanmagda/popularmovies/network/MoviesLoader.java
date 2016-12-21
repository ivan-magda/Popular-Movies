package com.ivanmagda.popularmovies.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ivanmagda.popularmovies.data.model.Movie;

public final class MoviesLoader extends AsyncTaskLoader<Movie[]> {

    private Resource<Movie[]> mResource;

    public MoviesLoader(Context context, Resource<Movie[]> resource) {
        super(context);
        this.mResource = resource;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Movie[] loadInBackground() {
        if (mResource == null) {
            return null;
        }
        return Webservice.load(mResource);
    }

}
