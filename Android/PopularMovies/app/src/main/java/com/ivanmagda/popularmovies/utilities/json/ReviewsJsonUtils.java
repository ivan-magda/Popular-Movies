package com.ivanmagda.popularmovies.utilities.json;

import android.text.TextUtils;

import com.ivanmagda.popularmovies.data.model.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Utility functions to handle TMDb movie reviews JSON data.
 */
public final class ReviewsJsonUtils {

    /**
     * Response keys.
     */
    private static final String RESULTS_RESPONSE_KEY = "results";
    private static final String ID_RESPONSE_KEY = "id";
    private static final String AUTHOR_RESPONSE_KEY = "author";
    private static final String CONTENT_RESPONSE_KEY = "content";
    private static final String URL_RESPONSE_KEY = "url";

    private ReviewsJsonUtils() {
    }

    public static List<Review> buildReviewsFromResponse(String response) {
        if (TextUtils.isEmpty(response)) return null;
        try {
            JSONObject json = new JSONObject(response);
            JSONArray reviews = json.getJSONArray(RESULTS_RESPONSE_KEY);
            return parseReviews(reviews);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Review> parseReviews(JSONArray jsonArray) throws JSONException {
        return JsonUtils.parseJsonArray(
                jsonArray,
                new JsonUtils.Parcelable<Review>() {
                    @Override
                    public Review parse(JSONObject jsonObject) throws JSONException {
                        return parseReview(jsonObject);
                    }
                }
        );
    }

    public static Review parseReview(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString(ID_RESPONSE_KEY);
        String author = jsonObject.getString(AUTHOR_RESPONSE_KEY);
        String content = jsonObject.getString(CONTENT_RESPONSE_KEY).trim();
        String url = jsonObject.getString(URL_RESPONSE_KEY);
        return new Review(id, author, content, url);
    }

}
