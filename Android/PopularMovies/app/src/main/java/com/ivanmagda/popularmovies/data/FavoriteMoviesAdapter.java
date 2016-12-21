package com.ivanmagda.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.utilities.ImageUtils;
import com.ivanmagda.popularmovies.utilities.MoviePersistenceUtils;

public final class FavoriteMoviesAdapter extends CursorAdapter {

    public FavoriteMoviesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Movie movie = MoviePersistenceUtils.makeFromCursor(cursor);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_movie_poster);
        imageView.setImageBitmap(ImageUtils.bitmapFromBytes(movie.getPoster()));
    }

}
