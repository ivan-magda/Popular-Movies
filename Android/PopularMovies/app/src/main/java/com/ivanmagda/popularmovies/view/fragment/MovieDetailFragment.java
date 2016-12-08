/**
 * Copyright (c) 2016 Ivan Magda
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ivanmagda.popularmovies.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {

    private static final String MOVIE_STATE_KEY = "movie";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());

    @BindView(R.id.iv_detail_movie_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_detail_movie_title) TextView mTitleTextView;
    @BindView(R.id.tv_detail_movie_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_detail_movie_rating) TextView mRatingTextView;
    @BindView(R.id.tv_detail_movie_overview) TextView mOverviewTextView;

    private Movie mMovie;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        configure(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(MOVIE_STATE_KEY, mMovie);
    }

    private void configure(View view) {
        ButterKnife.bind(this, view);
        updateUI();
    }

    private void updateUI() {
        getActionBar().setTitle(R.string.activity_detail_title);

        mTitleTextView.setText(mMovie.getTitle());
        mOverviewTextView.setText(mMovie.getOverview());

        String releaseYear = getString(R.string.release_year_name) + DATE_FORMAT.format(mMovie.getDate());
        mReleaseDateTextView.setText(releaseYear);

        String rating = getString(R.string.rating_name) + String.valueOf(mMovie.getRating());
        mRatingTextView.setText(rating);

        URL url = TMDbApi.buildPosterUrlForMovie(mMovie);
        Picasso.with(getActivity()).load(url.toString()).into(mPosterImageView);
    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }
}
