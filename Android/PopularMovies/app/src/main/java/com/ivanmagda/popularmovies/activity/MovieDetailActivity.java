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

package com.ivanmagda.popularmovies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mRatingTextView;
    private TextView mOverviewTextView;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        configure();
    }

    private void configure() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.activity_detail_title);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(Extras.EXTRA_MOVIE_TRANSFER)) {
            mMovie = intent.getParcelableExtra(Extras.EXTRA_MOVIE_TRANSFER);
        }

        mTitleTextView = (TextView) findViewById(R.id.tv_detail_movie_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_detail_movie_poster);
        mReleaseDateTextView = (TextView) findViewById(R.id.tv_detail_movie_release_date);
        mRatingTextView = (TextView) findViewById(R.id.tv_detail_movie_rating);
        mOverviewTextView = (TextView) findViewById(R.id.tv_detail_movie_overview);

        updateUI();
    }

    private void updateUI() {
        mTitleTextView.setText(mMovie.getTitle());
        mOverviewTextView.setText(mMovie.getOverview());

        String releaseYear = getString(R.string.release_year_name) + DATE_FORMAT.format(mMovie.getDate());
        mReleaseDateTextView.setText(releaseYear);

        String rating = getString(R.string.rating_name) + String.valueOf(mMovie.getRating());
        mRatingTextView.setText(rating);

        URL url = TMDbApi.buildPosterUrlForMovie(mMovie);
        Picasso.with(this).load(url.toString()).into(mPosterImageView);
    }
}
