package com.ivanmagda.popularmovies.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.ReviewsAdapter;
import com.ivanmagda.popularmovies.data.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {

    @BindView(R.id.rv_reviews)
    RecyclerView recyclerView;

    private ArrayList<Review> mReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        configure();
    }

    private void configure() {
        Intent intent = getIntent();
        if (intent.hasExtra(Extras.EXTRA_REVIEW_TRANSFER)) {
            mReviews = intent.getParcelableArrayListExtra(Extras.EXTRA_REVIEW_TRANSFER);
        }
        assert mReviews != null;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_reviews_title);
        }

        ReviewsAdapter adapter = new ReviewsAdapter(mReviews);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
