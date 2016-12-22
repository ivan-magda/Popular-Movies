/**
 * Copyright (c) 2016 Ivan Magda
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ivanmagda.popularmovies.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.FavoriteMoviesAdapter;
import com.ivanmagda.popularmovies.data.FavoriteMoviesLoaderCallbacksAdapter;
import com.ivanmagda.popularmovies.data.MovieAdapter;
import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.network.MoviesLoader;
import com.ivanmagda.popularmovies.network.Resource;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.ivanmagda.popularmovies.utilities.ConnectivityUtils;
import com.ivanmagda.popularmovies.utilities.MoviePersistenceUtils;
import com.ivanmagda.popularmovies.view.activity.MovieDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ivanmagda.popularmovies.view.fragment.MoviesListFragment.SortOrder.FAVORITE;
import static com.ivanmagda.popularmovies.view.fragment.MoviesListFragment.SortOrder.MOST_POPULAR;
import static com.ivanmagda.popularmovies.view.fragment.MoviesListFragment.SortOrder.TOP_RATED;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>>,
        FavoriteMoviesLoaderCallbacksAdapter.Delegate {

    /**
     * Identifies a particular Loader being used in this component.
     */
    private static final int MOVIES_LOADER = 1;
    private static final int FAVORITE_MOVIES_LOADER = 2;

    private static final String SORT_ORDER_STATE_KEY = "sortOrder";

    protected enum SortOrder {
        FAVORITE,
        MOST_POPULAR,
        TOP_RATED
    }

    @BindView(R.id.gv_movies)
    GridView mGridView;

    private MovieAdapter mMovieAdapter;
    private FavoriteMoviesAdapter mFavoriteMoviesAdapter;
    private FavoriteMoviesLoaderCallbacksAdapter mFavoriteMoviesCallbacks;

    private SortOrder mSortOrder = MOST_POPULAR;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        setHasOptionsMenu(true);
        configure(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(SORT_ORDER_STATE_KEY, mSortOrder);
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
            case R.id.action_favorite:
                mSortOrder = FAVORITE;
                break;
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
        ButterKnife.bind(this, view);
        updateTitle();

        mFavoriteMoviesCallbacks = new FavoriteMoviesLoaderCallbacksAdapter(getContext(), this);
        mFavoriteMoviesAdapter = new FavoriteMoviesAdapter(getContext(), null);

        mMovieAdapter = new MovieAdapter(getContext());
        mGridView.setAdapter(mMovieAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = (mSortOrder == FAVORITE ?
                        MoviePersistenceUtils.makeFromCursor(mFavoriteMoviesAdapter.getCursor())
                        : mMovieAdapter.getItem(i));
                showMovieDetails(movie);
            }
        });

        fetchMovies();
    }

    private void showMovieDetails(Movie movie) {
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra(Extras.EXTRA_MOVIE_TRANSFER, movie);
        startActivity(intent);
    }

    private void updateTitle() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar == null) return;
        switch (mSortOrder) {
            case FAVORITE:
                actionBar.setTitle(R.string.favorite_bar_title);
                break;
            case MOST_POPULAR:
                actionBar.setTitle(R.string.popular_bar_title);
                break;
            case TOP_RATED:
                actionBar.setTitle(R.string.top_rated_bar_title);
                break;
        }
    }

    private void fetchMovies() {
        switch (mSortOrder) {
            case FAVORITE:
                getLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER, null, mFavoriteMoviesCallbacks);
                mGridView.setAdapter(mFavoriteMoviesAdapter);
                break;
            case MOST_POPULAR:
            case TOP_RATED:
                if (!ConnectivityUtils.isOnline(getContext())) {
                    Toast.makeText(getContext(), R.string.no_internet_connection_message, Toast.LENGTH_SHORT).show();
                    mMovieAdapter.updateWithNewData(null);
                } else {
                    getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
                }
                mGridView.setAdapter(mMovieAdapter);
                break;
            default:
                throw new IllegalArgumentException("Unsupported sort order: " + String.valueOf(mSortOrder));
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case MOVIES_LOADER:
                Resource<List<Movie>> resource = (mSortOrder == MOST_POPULAR ?
                        TMDbApi.getPopularMovies() :
                        TMDbApi.getTopRatedMovies()
                );
                return new MoviesLoader(getContext(), resource);
            default:
                throw new IllegalArgumentException("Unsupported loader with id: " + String.valueOf(id));
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        if (movies != null) {
            mMovieAdapter.updateWithNewData(movies);
        } else {
            mMovieAdapter.updateWithNewData(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> movies) {
        mMovieAdapter.updateWithNewData(null);
    }

    /**
     * Implement FavoriteMoviesLoaderCallbacksAdapter.Delegate interface for getting know when data
     * loaded.
     */

    @Override
    public void didFinishLoading(FavoriteMoviesLoaderCallbacksAdapter callbacksAdapter, Cursor cursor) {
        mFavoriteMoviesAdapter.swapCursor(cursor);
    }

    @Override
    public void didLoadReset(FavoriteMoviesLoaderCallbacksAdapter callbacksAdapter) {
        mFavoriteMoviesAdapter.swapCursor(null);
    }

}
