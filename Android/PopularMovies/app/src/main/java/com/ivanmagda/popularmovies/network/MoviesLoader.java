package com.ivanmagda.popularmovies.network;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ivanmagda.popularmovies.data.model.Movie;

import java.util.List;

public final class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    private Resource<List<Movie>> mResource;

    public MoviesLoader(Context context, Resource<List<Movie>> resource) {
        super(context);
        this.mResource = resource;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mResource == null) {
            return null;
        }
        return Webservice.load(mResource);
    }

}
