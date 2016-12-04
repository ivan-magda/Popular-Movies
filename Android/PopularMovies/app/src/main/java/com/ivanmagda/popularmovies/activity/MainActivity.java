package com.ivanmagda.popularmovies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.ivanmagda.popularmovies.MovieAdapter;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.network.Resource;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.ivanmagda.popularmovies.network.Webservice;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private GridView mGridView;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configure();
        new FetchMoviesTask().execute(TMDbApi.getPopularMovies());
    }

    private void configure() {
        mGridView = (GridView) findViewById(R.id.gv_movies);
        mMovieAdapter = new MovieAdapter(this);
        mGridView.setAdapter(mMovieAdapter);
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
            mMovieAdapter.updateWithNewData(Arrays.asList(movies));
        }
    }
}
