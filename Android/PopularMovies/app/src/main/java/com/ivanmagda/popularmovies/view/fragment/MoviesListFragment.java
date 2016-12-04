package com.ivanmagda.popularmovies.view.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.model.MovieAdapter;
import com.ivanmagda.popularmovies.network.Resource;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.ivanmagda.popularmovies.network.Webservice;
import com.ivanmagda.popularmovies.view.activity.MovieDetailActivity;
import com.ivanmagda.popularmovies.view.activity.MoviesListActivity;

import java.util.Arrays;

import static com.ivanmagda.popularmovies.view.fragment.MoviesListFragment.SortOrder.MOST_POPULAR;
import static com.ivanmagda.popularmovies.view.fragment.MoviesListFragment.SortOrder.TOP_RATED;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesListFragment extends Fragment {

    private static final String SORT_ORDER_STATE_KEY = "sortOrder";
    private static final String MOVIES_STATE_KEY = "movies";

    protected enum SortOrder {
        MOST_POPULAR,
        TOP_RATED
    }

    private GridView mGridView;
    private MovieAdapter mMovieAdapter;

    private SortOrder mSortOrder = MOST_POPULAR;
    private Movie[] mMovies;

    public MoviesListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MoviesListFragment.
     */
    public static MoviesListFragment newInstance() {
        return new MoviesListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mSortOrder = (SortOrder) savedInstanceState.getSerializable(SORT_ORDER_STATE_KEY);
            mMovies = (Movie[]) savedInstanceState.getParcelableArray(MOVIES_STATE_KEY);
        }

        fetchMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        setHasOptionsMenu(true);
        configure(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(SORT_ORDER_STATE_KEY, mSortOrder);
        savedInstanceState.putParcelableArray(MOVIES_STATE_KEY, mMovies);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SortOrder currentSortOrder = mSortOrder;

        switch (item.getItemId()) {
            case R.id.action_by_popularity:
                mSortOrder = MOST_POPULAR;
                break;
            case R.id.action_by_rating:
                mSortOrder = TOP_RATED;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        updateTitle();

        if (currentSortOrder != mSortOrder) {
            fetchMovies();
        }

        return true;
    }

    private void configure(View view) {
        updateTitle();

        mGridView = (GridView) view.findViewById(R.id.gv_movies);
        mMovieAdapter = new MovieAdapter(getActivity());
        mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMovieDetails(mMovieAdapter.getItem(i));
            }
        });

        if (mMovies != null) {
            mMovieAdapter.updateWithNewData(Arrays.asList(mMovies));
        }
    }

    private void showMovieDetails(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(Extras.EXTRA_MOVIE_TRANSFER, movie);
        startActivity(intent);
    }

    private void updateTitle() {
        switch (mSortOrder) {
            case MOST_POPULAR:
                getActionBar().setTitle(R.string.popular_bar_title);
                break;
            case TOP_RATED:
                getActionBar().setTitle(R.string.top_rated_bar_title);
                break;
        }
    }

    private ActionBar getActionBar() {
        return ((MoviesListActivity) getActivity()).getSupportActionBar();
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

            // Check for activity existing, when thread is executing.
            if (!isVisible() || isCancelled() ||
                    (getActivity() != null && getActivity().isFinishing())) {
                return;
            }

            if (movies == null) {
                return;
            }

            mMovies = movies;
            mMovieAdapter.updateWithNewData(Arrays.asList(movies));
        }
    }
}
