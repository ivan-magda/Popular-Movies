package com.ivanmagda.popularmovies.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.view.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_DETAIL_FRAGMENT_TAG = "movieDetai";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra(Extras.EXTRA_MOVIE_TRANSFER)) {
                Movie movie = intent.getParcelableExtra(Extras.EXTRA_MOVIE_TRANSFER);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.activity_movie_detail, MovieDetailFragment.newInstance(movie),
                                MOVIE_DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        }
    }
}
