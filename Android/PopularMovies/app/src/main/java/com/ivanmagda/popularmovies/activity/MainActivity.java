package com.ivanmagda.popularmovies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.network.Resource;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.ivanmagda.popularmovies.network.Webservice;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchMoviesTask().execute(TMDbApi.getPopularMovies());
    }

    private class FetchMoviesTask extends AsyncTask<Resource<Movie[]>, Void, Movie[]> {
        @SafeVarargs
        @Override
        protected final Movie[] doInBackground(Resource<Movie[]>... resources) {
            return Webservice.load(resources[0]);
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            Log.d(LOG_TAG, "Fetched movie sample: \n" + movies[0]);
            Log.d(LOG_TAG, "Poster URL: \n" + TMDbApi.buildPosterUrlForMovie(movies[0]));
        }
    }
}
