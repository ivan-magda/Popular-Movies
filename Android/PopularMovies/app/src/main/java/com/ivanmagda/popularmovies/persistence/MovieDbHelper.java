package com.ivanmagda.popularmovies.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ivanmagda.popularmovies.persistence.MovieContract.MovieEntry;

public final class MovieDbHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file.
     */
    private static final String DATABASE_NAME = "movie.db";

    /**
     * Database version.
     */
    private static final int DATABASE_VERSION = 1;

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String TEXT_TYPE = " TEXT";
    private static final String NOT_NULL_ATR = " NOT NULL";
    private static final String COMMA_SEP = ", ";

    private static final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
            MovieEntry.TABLE_NAME + " (" +
            MovieEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            MovieEntry.COLUMN_MOVIE_ID + INTEGER_TYPE + NOT_NULL_ATR + COMMA_SEP +
            MovieEntry.COLUMN_OVERVIEW + TEXT_TYPE + NOT_NULL_ATR + COMMA_SEP +
            MovieEntry.COLUMN_POSTER + " BLOB" + COMMA_SEP +
            MovieEntry.COLUMN_POSTER_PATH + TEXT_TYPE + NOT_NULL_ATR + COMMA_SEP +
            MovieEntry.COLUMN_RATING + REAL_TYPE + COMMA_SEP +
            MovieEntry.COLUMN_RELEASE_DATE + TEXT_TYPE + NOT_NULL_ATR + COMMA_SEP +
            MovieEntry.COLUMN_TITLE + TEXT_TYPE + NOT_NULL_ATR + ");";

    private static final String SQL_DROP_MOVIE_TABLE = "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    /**
     * Constructs a new instance of {@link SQLiteOpenHelper}.
     *
     * @param context of the app
     */
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DROP_MOVIE_TABLE);
        onCreate(sqLiteDatabase);
    }

}
