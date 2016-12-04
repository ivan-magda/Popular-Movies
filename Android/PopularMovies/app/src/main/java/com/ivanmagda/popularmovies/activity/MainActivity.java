package com.ivanmagda.popularmovies.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

    private enum SortOrder {
        MOST_POPULAR,
        TOP_RATED
    }

    private GridView mGridView;
    private MovieAdapter mMovieAdapter;

    private SortOrder mSortOrder = SortOrder.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configure();
        fetchMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SortOrder currentSortOrder = mSortOrder;

        switch (item.getItemId()) {
            case R.id.action_by_popularity:
                mSortOrder = SortOrder.MOST_POPULAR;
                break;
            case R.id.action_by_rating:
                mSortOrder = SortOrder.TOP_RATED;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        if (currentSortOrder != mSortOrder) {
            fetchMovies();
        }

        return true;
    }

    private void configure() {
        mGridView = (GridView) findViewById(R.id.gv_movies);
        mMovieAdapter = new MovieAdapter(this);
        mGridView.setAdapter(mMovieAdapter);
    }

    private void fetchMovies() {
        switch (mSortOrder) {
            case MOST_POPULAR:
                new FetchMoviesTask().execute(TMDbApi.getPopularMovies());
                break;
            case TOP_RATED:
                new FetchMoviesTask().execute(TMDbApi.getTopRatedMovies());
                break;
        }
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
