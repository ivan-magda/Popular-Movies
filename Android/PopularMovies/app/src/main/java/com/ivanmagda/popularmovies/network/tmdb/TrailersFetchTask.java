package com.ivanmagda.popularmovies.network.tmdb;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;
import com.ivanmagda.popularmovies.network.Webservice;

import java.util.List;

public final class TrailersFetchTask extends AsyncTask<Void, Void, List<YouTubeTrailer>> {

    public interface CompletionHandler {
        void block(List<YouTubeTrailer> trailers);
    }

    private Movie mMovie;
    private CompletionHandler mCompletionHandler;

    public TrailersFetchTask(@NonNull final Movie movie, CompletionHandler completionHandler) {
        this.mMovie = movie;
        this.mCompletionHandler = completionHandler;
    }

    public static void load(@NonNull final Movie movie, CompletionHandler completionHandler) {
        new TrailersFetchTask(movie, completionHandler).execute();
    }

    @Override
    protected List<YouTubeTrailer> doInBackground(Void... params) {
        return Webservice.load(TMDbApi.getVideosForMovie(mMovie));
    }

    @Override
    protected void onPostExecute(List<YouTubeTrailer> youTubeTrailers) {
        if (mCompletionHandler != null) {
            mCompletionHandler.block(youTubeTrailers);
        }
    }

}
