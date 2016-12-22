package com.ivanmagda.popularmovies.utilities.json;

import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Utility functions to handle TMDb movie YouTube videos JSON data.
 */
public final class YouTubeTrailerJsonUtils {

    /**
     * Response keys.
     */
    private static final String RESULTS_RESPONSE_KEY = "results";
    private static final String ID_RESPONSE_KEY = "id";
    private static final String KEY_RESPONSE_KEY = "key";
    private static final String NAME_RESPONSE_KEY = "name";
    private static final String SITE_RESPONSE_KEY = "site";
    private static final String YOUTUBE_SITE = "YouTube";

    private YouTubeTrailerJsonUtils() {
    }

    public static YouTubeTrailer[] buildTrailersFromResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            JSONArray videos = json.getJSONArray(RESULTS_RESPONSE_KEY);
            return parseVideos(videos);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static YouTubeTrailer[] parseVideos(JSONArray jsonVideos) throws JSONException {
        List<YouTubeTrailer> trailers = JsonUtils.parseJsonArray(
                jsonVideos,
                new JsonUtils.Parcelable<YouTubeTrailer>() {
                    @Override
                    public YouTubeTrailer parse(JSONObject jsonObject) throws JSONException {
                        return parseVideo(jsonObject);
                    }
                }
        );
        return trailers.toArray(new YouTubeTrailer[trailers.size()]);
    }

    public static YouTubeTrailer parseVideo(JSONObject jsonObject) throws JSONException {
        String site = jsonObject.getString(SITE_RESPONSE_KEY);
        if (YOUTUBE_SITE.equals(site)) {
            String id = jsonObject.getString(ID_RESPONSE_KEY);
            String key = jsonObject.getString(KEY_RESPONSE_KEY);
            String name = jsonObject.getString(NAME_RESPONSE_KEY);
            return new YouTubeTrailer(id, key, name);
        } else {
            return null;
        }
    }

}
