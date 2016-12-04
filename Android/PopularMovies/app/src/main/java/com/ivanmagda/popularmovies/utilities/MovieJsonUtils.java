package com.ivanmagda.popularmovies.utilities;

import com.ivanmagda.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle TMDb movie JSON data.
 */
public final class MovieJsonUtils {

    /* Response keys. */
    private static final String RESULTS_RESPONSE_KEY = "results";
    private static final String ID_RESPONSE_KEY = "id";
    private static final String POSTER_PATH_RESPONSE_KEY = "poster_path";
    private static final String OVERVIEW_RESPONSE_KEY = "overview";
    private static final String RELEASE_DATE_RESPONSE_KEY = "release_date";
    private static final String GENRE_IDS_RESPONSE_KEY = "genre_ids";
    private static final String TITLE_RESPONSE_KEY = "original_title";
    private static final String VIDEO_RESPONSE_KEY = "video";
    private static final String RATING_RESPONSE_KEY = "vote_average";

    private MovieJsonUtils() {
    }

    public static Movie[] buildMoviesFromResponse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            JSONArray movies = json.getJSONArray(RESULTS_RESPONSE_KEY);
            return parseMovies(movies);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Movie[] parseMovies(JSONArray jsonMovies) {
        try {
            Movie[] movies = new Movie[jsonMovies.length()];

            for (int i = 0; i < jsonMovies.length(); i++) {
                JSONObject jsonMovie = jsonMovies.getJSONObject(i);
                movies[i] = parseMovie(jsonMovie);
            }

            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Movie parseMovie(JSONObject jsonObject) {
        try {
            int id = jsonObject.getInt(ID_RESPONSE_KEY);
            String posterPath = jsonObject.getString(POSTER_PATH_RESPONSE_KEY);
            String overview = jsonObject.getString(OVERVIEW_RESPONSE_KEY);
            String releaseDate = jsonObject.getString(RELEASE_DATE_RESPONSE_KEY);
            String title = jsonObject.getString(TITLE_RESPONSE_KEY);
            boolean hasVideo = jsonObject.getBoolean(VIDEO_RESPONSE_KEY);
            double rating = jsonObject.getDouble(RATING_RESPONSE_KEY);

            JSONArray genreIdsJsonArray = jsonObject.getJSONArray(GENRE_IDS_RESPONSE_KEY);
            int[] genreIds = new int[genreIdsJsonArray.length()];

            for (int i = 0; i < genreIdsJsonArray.length(); i++) {
                genreIds[i] = genreIdsJsonArray.getInt(i);
            }

            return new Movie(id, posterPath, overview, releaseDate, genreIds, title, hasVideo, rating);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
