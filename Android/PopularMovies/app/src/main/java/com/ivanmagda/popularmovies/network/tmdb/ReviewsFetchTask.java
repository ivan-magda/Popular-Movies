package com.ivanmagda.popularmovies.network.tmdb;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.data.model.Review;
import com.ivanmagda.popularmovies.network.Webservice;

import java.util.List;

public final class ReviewsFetchTask extends AsyncTask<Void, Void, List<Review>> {

    public interface CompletionHandler {
        void block(List<Review> reviews);
    }

    private Movie mMovie;
    private CompletionHandler mCompletionHandler;

    public ReviewsFetchTask(@NonNull final Movie movie, CompletionHandler completionHandler) {
        this.mMovie = movie;
        this.mCompletionHandler = completionHandler;
    }

    public static void load(@NonNull final Movie movie, CompletionHandler completionHandler) {
        new ReviewsFetchTask(movie, completionHandler).execute();
    }

    @Override
    protected List<Review> doInBackground(Void... params) {
        return Webservice.load(TMDbApi.getReviewsForMovie(mMovie));
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        if (mCompletionHandler != null) {
            mCompletionHandler.block(reviews);
        }
    }

}
