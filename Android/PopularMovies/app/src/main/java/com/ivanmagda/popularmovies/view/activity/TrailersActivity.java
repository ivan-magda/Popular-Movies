package com.ivanmagda.popularmovies.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ivanmagda.popularmovies.Extras;
import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.TrailersAdapter;
import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;
import com.ivanmagda.popularmovies.utilities.YouTubeTrailerUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailersActivity extends AppCompatActivity implements TrailersAdapter.ListItemClickListener {

    @BindView(R.id.rv_trailers)
    RecyclerView recyclerView;

    private ArrayList<YouTubeTrailer> mTrailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
        ButterKnife.bind(this);
        configure();
    }

    private void configure() {
        Intent intent = getIntent();
        if (intent.hasExtra(Extras.EXTRA_TRAILER_TRANSFER)) {
            mTrailers = intent.getParcelableArrayListExtra(Extras.EXTRA_TRAILER_TRANSFER);
        }
        assert mTrailers != null;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.activity_trailers_title);
        }

        TrailersAdapter adapter = new TrailersAdapter(mTrailers, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        YouTubeTrailerUtils.openVideoInWeb(this, mTrailers.get(clickedItemIndex));
    }

}
