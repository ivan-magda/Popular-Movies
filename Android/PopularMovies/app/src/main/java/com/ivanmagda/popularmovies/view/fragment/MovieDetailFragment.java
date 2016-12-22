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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.data.model.Review;
import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.ivanmagda.popularmovies.network.Webservice;
import com.ivanmagda.popularmovies.persistence.MovieContract.MovieEntry;
import com.ivanmagda.popularmovies.utilities.ConnectivityUtils;
import com.ivanmagda.popularmovies.utilities.ImageUtils;
import com.ivanmagda.popularmovies.utilities.MoviePersistenceUtils;
import com.ivanmagda.popularmovies.utilities.YouTubeTrailerUtils;
import com.ivanmagda.popularmovies.view.activity.ReviewsActivity;
import com.ivanmagda.popularmovies.view.activity.TrailersActivity;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        View.OnClickListener {

    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    /**
     * Identifies a particular Loader being used in this component.
     */
    private static final int MOVIE_LOADER = 1;

    private static final String MOVIE_STATE_KEY = "movie";
    private static final String FAVORITE_STATE_KEY = "favorite";

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());

    @BindView(R.id.iv_detail_movie_poster)
    ImageView mPosterImageView;

    @BindView(R.id.tv_detail_movie_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_detail_movie_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.tv_detail_movie_rating)
    TextView mRatingTextView;

    @BindView(R.id.tv_detail_movie_overview)
    TextView mOverviewTextView;

    @BindView(R.id.bt_favorite)
    Button mFavoriteButton;

    @BindView(R.id.tv_review_content)
    TextView mContentTextView;

    @BindView(R.id.tv_author)
    TextView mAuthorTextView;

    @BindView(R.id.bt_show_more_reviews)
    Button mShowMoreReviewsButton;

    @BindView(R.id.ib_trailer)
    ImageButton mTrailerImageButton;

    @BindView(R.id.tv_trailer_message)
    TextView mTrailerMessageTextView;

    @BindView(R.id.bt_show_more_trailers)
    Button mShowMoreTrailersButton;

    /**
     * The detail movie.
     */
    private Movie mMovie;

    private List<Review> mReviews = null;
    private List<YouTubeTrailer> mTrailers = null;

    /**
     * Helps to determine where's movie is in favorites or not.
     */
    private boolean mIsFavorite = false;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.mMovie = movie;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(MOVIE_STATE_KEY);
            mIsFavorite = savedInstanceState.getBoolean(FAVORITE_STATE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        configure(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(MOVIE_STATE_KEY, mMovie);
        savedInstanceState.putBoolean(FAVORITE_STATE_KEY, mIsFavorite);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return false;
        }
    }

    private void configure(View view) {
        ButterKnife.bind(this, view);
        updateUI();

        getLoaderManager().initLoader(MOVIE_LOADER, null, this);

        if (!ConnectivityUtils.isOnline(getContext())) {
            Toast.makeText(getContext(), R.string.no_internet_connection_message,
                    Toast.LENGTH_SHORT).show();
            updateReviews();
            updateTrailers();
        } else {
            new ReviewsFetchTask().execute();
            new TrailersFetchTask().execute();
        }

        mFavoriteButton.setOnClickListener(this);
        mShowMoreReviewsButton.setOnClickListener(this);
        mShowMoreTrailersButton.setOnClickListener(this);
        mTrailerImageButton.setOnClickListener(this);
    }

    private void updateUI() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_detail_title);
        }

        mTitleTextView.setText(mMovie.getTitle());
        mOverviewTextView.setText(mMovie.getOverview());

        String releaseYear = getString(R.string.release_year_name) + DATE_FORMAT.format(mMovie.getDate());
        mReleaseDateTextView.setText(releaseYear);

        String rating = getString(R.string.rating_name) + String.valueOf(mMovie.getRating());
        mRatingTextView.setText(rating);

        URL url = TMDbApi.buildPosterUrlForMovie(mMovie);
        Picasso.with(getActivity()).load(url.toString()).into(mPosterImageView);

        updateFavoriteButtonTitle();
    }

    private void updateFavoriteButtonTitle() {
        mFavoriteButton.setText(mIsFavorite ?
                R.string.bt_remove_from_favorites_title :
                R.string.bt_mark_favorite_title
        );
    }

    private void toggleFavorite() {
        if (mIsFavorite) {
            removeFromFavorite();
        } else {
            addToFavorite();
        }
    }

    private void removeFromFavorite() {
        if (MoviePersistenceUtils.removeMovieFromFavorites(mMovie, getContext())) {
            Toast.makeText(getContext(), "Successfully remove movie from favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to remove movie from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToFavorite() {
        if (mMovie.getPoster() == null) {
            mMovie.setPoster(ImageUtils.bytesFromImageView(mPosterImageView));
        }

        if (MoviePersistenceUtils.addMovieToFavorites(mMovie, getContext())) {
            Toast.makeText(getContext(), "Successfully added to favorite", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to favorite", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Implement LoaderManager.LoaderCallbacks<Cursor> for getting know is this mMovies in favorites
     * or not.
     */

    /**
     * Creates loader that queries currently presenting movie.
     *
     * @param loaderId Id of the loader.
     * @param args     Additional loader arguments.
     * @return CursorLoader that queries movie.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case MOVIE_LOADER:
                Uri uri = MovieEntry.buildFavoriteMovieUriWithId(mMovie.getId());
                String[] projection = new String[]{MovieEntry._ID};
                return new CursorLoader(getContext(), uri, projection, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mIsFavorite = (cursor != null && cursor.getCount() != 0);
        updateFavoriteButtonTitle();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        switch (id) {
            case R.id.bt_favorite:
                toggleFavorite();
                break;
            case R.id.bt_show_more_reviews:
                Intent reviewsIntent = new Intent(getContext(), ReviewsActivity.class);
                reviewsIntent.putParcelableArrayListExtra(Extras.EXTRA_REVIEW_TRANSFER,
                        (ArrayList<Review>) mReviews);
                startActivity(reviewsIntent);
                break;
            case R.id.bt_show_more_trailers:
                Intent trailersIntent = new Intent(getContext(), TrailersActivity.class);
                trailersIntent.putParcelableArrayListExtra(Extras.EXTRA_TRAILER_TRANSFER,
                        (ArrayList<YouTubeTrailer>) mTrailers);
                startActivity(trailersIntent);
                break;
            case R.id.ib_trailer:
                YouTubeTrailerUtils.openVideoInWeb(getContext(), mTrailers.get(0));
                break;
            default:
                throw new UnsupportedOperationException("Unsupported click handle with id: " + id);
        }
    }

    /**
     * Fetch movie additional info:
     * 1) Reviews.
     * 2) Trailers.
     */

    private class ReviewsFetchTask extends AsyncTask<Void, Void, List<Review>> {
        @Override
        protected List<Review> doInBackground(Void... params) {
            if (!ConnectivityUtils.isOnline(getContext())) {
                return null;
            } else {
                return Webservice.load(TMDbApi.getReviewsForMovie(mMovie));
            }
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            mReviews = reviews;
            updateReviews();
        }
    }

    private void updateReviews() {
        if (mReviews == null || mReviews.size() == 0) {
            mContentTextView.setText(R.string.no_reviews_message);
            mAuthorTextView.setVisibility(View.GONE);
            mShowMoreReviewsButton.setVisibility(View.GONE);
        } else {
            mAuthorTextView.setVisibility(View.VISIBLE);
            mShowMoreReviewsButton.setVisibility(View.VISIBLE);

            Review review = mReviews.get(0);
            mContentTextView.setText(review.getContent());
            mAuthorTextView.setText(review.getAuthor());
        }
    }

    private class TrailersFetchTask extends AsyncTask<Void, Void, List<YouTubeTrailer>> {
        @Override
        protected List<YouTubeTrailer> doInBackground(Void... params) {
            if (!ConnectivityUtils.isOnline(getContext())) {
                return null;
            } else {
                return Webservice.load(TMDbApi.getVideosForMovie(mMovie));
            }
        }

        @Override
        protected void onPostExecute(List<YouTubeTrailer> youTubeTrailers) {
            mTrailers = youTubeTrailers;
            updateTrailers();
        }
    }

    private void updateTrailers() {
        if (mTrailers == null || mTrailers.size() == 0) {
            mTrailerMessageTextView.setVisibility(View.VISIBLE);
            mTrailerImageButton.setVisibility(View.GONE);
            mShowMoreTrailersButton.setVisibility(View.INVISIBLE);
        } else {
            mTrailerMessageTextView.setVisibility(View.GONE);
            mTrailerImageButton.setVisibility(View.VISIBLE);
            mShowMoreTrailersButton.setVisibility(View.VISIBLE);

            Uri uri = YouTubeTrailerUtils.buildVideoThumbnailUriForTrailer(mTrailers.get(0));
            Picasso.with(getContext()).load(uri).into(mTrailerImageButton);
        }
    }

}
