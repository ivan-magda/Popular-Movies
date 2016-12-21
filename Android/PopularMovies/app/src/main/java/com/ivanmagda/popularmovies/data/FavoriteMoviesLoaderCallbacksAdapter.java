package com.ivanmagda.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.ivanmagda.popularmovies.persistence.MovieContract;

public final class FavoriteMoviesLoaderCallbacksAdapter implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Delegate {
        public void didFinishLoading(FavoriteMoviesLoaderCallbacksAdapter callbacksAdapter, Cursor cursor);

        public void didLoadReset(FavoriteMoviesLoaderCallbacksAdapter callbacksAdapter);
    }

    private Context mContext;
    private Delegate mDelegate;

    public FavoriteMoviesLoaderCallbacksAdapter(Context context, Delegate delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        String sortOrder = MovieContract.MovieEntry.COLUMN_RATING + " DESC";
        return new CursorLoader(mContext, uri, null, null, null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (mDelegate != null) {
            mDelegate.didFinishLoading(this, cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        if (mDelegate != null) {
            mDelegate.didLoadReset(this);
        }
    }

}
