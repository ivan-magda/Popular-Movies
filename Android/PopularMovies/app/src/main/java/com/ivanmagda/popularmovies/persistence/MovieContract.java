package com.ivanmagda.popularmovies.persistence;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the movie database.
 */
public final class MovieContract {

    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String AUTHORITY = "com.ivanmagda.popularmovies";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for PopularMovies.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that PopularMovies
     * can handle.
     */
    public static final String PATH_MOVIE = "favorite";

    public static final class MovieEntry implements BaseColumns {

        /**
         * Used internally as the name of our movie table.
         */
        public static final String TABLE_NAME = "movie";

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single product.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;

        /**
         * TaskEntry content URI = base content URI + path
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        /**
         * TMDb movie id.
         * <p>
         * Type: INTEGER
         */
        public static final String COLUMN_MOVIE_ID = "movie_id";

        /**
         * Title of the movie.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_TITLE = "title";

        /**
         * Poster of the movie.
         * <p/>
         * Type: BLOB
         */
        public static final String COLUMN_POSTER = "poster";

        /**
         * Poster path - url string.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_POSTER_PATH = "poster_path";

        /**
         * Rating of the movie.
         * <p>
         * Type: REAL
         */
        public static final String COLUMN_RATING = "rating";

        /**
         * Release date of the movie.
         * Stores as String with format "2016-12-14" - "yyyy-MM-dd".
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_RELEASE_DATE = "date";

        /**
         * Synopsis of the movie.
         * <p>
         * Type: TEXT
         */
        public static final String COLUMN_OVERVIEW = "overview";

        /**
         * Builds a URI that adds the movie id to the end of the movie content URI path.
         * This is used to query details about a single movie entry by movie id.
         *
         * @param movieId Id of the movie in the TMDb.
         * @return Uri to query details about a single movie entry
         */
        public static Uri buildFavoriteMovieUriWithId(int movieId) {
            return ContentUris.withAppendedId(CONTENT_URI, movieId);
        }
    }

}
