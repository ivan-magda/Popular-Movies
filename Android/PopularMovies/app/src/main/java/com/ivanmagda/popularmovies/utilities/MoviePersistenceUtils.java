package com.ivanmagda.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.persistence.MovieContract.MovieEntry;

public final class MoviePersistenceUtils {

    private static final int COLUMN_NOT_EXIST = -1;

    private MoviePersistenceUtils() {
    }

    public static Movie makeFromCursor(@NonNull final Cursor cursor) {
        int id = -1;
        if (isColumnExist(cursor, MovieEntry.COLUMN_MOVIE_ID)) {
            id = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID));
        }

        String title = null;
        if (isColumnExist(cursor, MovieEntry.COLUMN_TITLE)) {
            title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
        }

        byte[] poster = null;
        if (isColumnExist(cursor, MovieEntry.COLUMN_POSTER)) {
            poster = cursor.getBlob(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER));
        }

        String posterPath = null;
        if (isColumnExist(cursor, MovieEntry.COLUMN_POSTER_PATH)) {
            posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        }

        double rating = 0;
        if (isColumnExist(cursor, MovieEntry.COLUMN_RATING)) {
            rating = cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_RATING));
        }

        String date = null;
        if (isColumnExist(cursor, MovieEntry.COLUMN_RELEASE_DATE)) {
            date = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
        }

        String overview = null;
        if (isColumnExist(cursor, MovieEntry.COLUMN_OVERVIEW)) {
            overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
        }

        return new Movie(id, posterPath, overview, date, title, rating, poster);
    }

    private static boolean isColumnExist(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName) != COLUMN_NOT_EXIST;
    }

    public static ContentValues valuesFromMovie(@NonNull final Movie movie) {
        ContentValues contentValues = new ContentValues(7);

        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_RATING, movie.getRating());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDateString());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        byte[] poster = movie.getPoster();
        if (poster != null && poster.length != 0) {
            contentValues.put(MovieEntry.COLUMN_POSTER, poster);
        }

        return contentValues;
    }

    public static boolean addMovieToFavorites(@NonNull final Movie movie, @NonNull final Context context) {
        Uri uri = MovieEntry.CONTENT_URI;
        ContentValues values = MoviePersistenceUtils.valuesFromMovie(movie);
        return context.getContentResolver().insert(uri, values) != null;
    }

    public static boolean removeMovieFromFavorites(@NonNull final Movie movie, @NonNull final Context context) {
        Uri uri = MovieEntry.buildFavoriteMovieUriWithId(movie.getId());
        return context.getContentResolver().delete(uri, null, null) != 0;
    }

}
