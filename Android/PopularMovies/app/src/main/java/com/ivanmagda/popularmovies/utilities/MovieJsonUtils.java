/**
 * Copyright (c) 2016 Ivan Magda
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ivanmagda.popularmovies.utilities;

import com.ivanmagda.popularmovies.data.model.Movie;

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
            double rating = jsonObject.getDouble(RATING_RESPONSE_KEY);

            return new Movie(id, posterPath, overview, releaseDate, title, rating, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
