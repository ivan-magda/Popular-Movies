package com.ivanmagda.popularmovies.utilities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;

public final class YouTubeTrailerUtils {

    private YouTubeTrailerUtils() {
    }

    public static void openVideoInWeb(@NonNull final Context context,
                                      @NonNull final YouTubeTrailer trailer) {
        Intent playTrailerIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = YouTubeTrailerUtils.buildVideoUriForTrailer(trailer);
        playTrailerIntent.setData(uri);
        context.startActivity(playTrailerIntent);
    }

    public static Uri buildVideoThumbnailUriForTrailer(YouTubeTrailer trailer) {
        return buildVideoThumbnailUriWithId(trailer.getKey());
    }

    public static Uri buildVideoThumbnailUriWithId(String videoId) {
        return Uri.parse("http://img.youtube.com/vi/" + videoId + "/0.jpg");
    }

    public static Uri buildVideoUriForTrailer(@NonNull final YouTubeTrailer trailer) {
        return buildVideoUriWithId(trailer.getKey());
    }

    public static Uri buildVideoUriWithId(@NonNull final String videoId) {
        return Uri.parse("https://www.youtube.com/watch?v=" + videoId);
    }

}
