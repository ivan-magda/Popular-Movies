package com.ivanmagda.popularmovies.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.view.fragment.MoviesListFragment;

public class MoviesListActivity extends AppCompatActivity {

    private static final String MOVIES_LIST_FRAGMENT_TAG = "moviesLisFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_movies_list, MoviesListFragment.newInstance(),
                            MOVIES_LIST_FRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.putFragment(
                outState,
                MOVIES_LIST_FRAGMENT_TAG,
                manager.findFragmentByTag(MOVIES_LIST_FRAGMENT_TAG));
    }
}
