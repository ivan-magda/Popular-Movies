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

package com.ivanmagda.popularmovies.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.ivanmagda.popularmovies.data.model.Movie;
import com.ivanmagda.popularmovies.data.model.Review;
import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;
import com.ivanmagda.popularmovies.utilities.json.MovieJsonUtils;
import com.ivanmagda.popularmovies.utilities.json.ReviewsJsonUtils;
import com.ivanmagda.popularmovies.utilities.json.YouTubeTrailerJsonUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TMDbApi {

    private static final String LOG_TAG = TMDbApi.class.getSimpleName();

    // TODO: Replace TMDb API Key with your own.
    private static final String API_KEY = "REPLACE_WITH_YOUR_OWN_API_KEY";
    private static final String API_SCHEME = "https";
    private static final String API_HOST = "api.themoviedb.org";
    private static final String IMAGE_API_HOST = "image.tmdb.org";
    private static final String API_PATH = "3";
    private static final String IMAGE_API_PATH = "t/p";
    private static final String DEFAULT_POSTER_SIZE = "w185";
    public static final String API_KEY_PARAM = "api_key";
    public static final String LANGUAGE_PARAM = "language";
    public static final String PAGE_PARAM = "page";
    private static final String POPULAR_MOVIES_PATH = "movie/popular";
    private static final String TOP_RATED_MOVIES_PATH = "movie/top_rated";

    private TMDbApi() {
    }

    /**
     * @return The popular movies Resource.
     */
    public static Resource<List<Movie>> getPopularMovies() {
        URL url = buildUrl(POPULAR_MOVIES_PATH, getDefaultMethodParameters());
        return new Resource<>(
                url,
                new Resource.Parse<List<Movie>>() {
                    @Override
                    public List<Movie> parse(String response) {
                        return MovieJsonUtils.buildMoviesFromResponse(response);
                    }
                }
        );
    }

    /**
     * @return The top rated movies resource.
     */
    public static Resource<List<Movie>> getTopRatedMovies() {
        URL url = buildUrl(TOP_RATED_MOVIES_PATH, getDefaultMethodParameters());
        return new Resource<>(
                url,
                new Resource.Parse<List<Movie>>() {
                    @Override
                    public List<Movie> parse(String response) {
                        return MovieJsonUtils.buildMoviesFromResponse(response);
                    }
                }
        );
    }

    public static Resource<List<YouTubeTrailer>> getVideosForMovie(@NonNull final Movie movie) {
        final String path = "movie/" + String.valueOf(movie.getId()) + "/videos";
        URL url = buildUrl(path, getDefaultMethodParameters());
        return new Resource<>(
                url,
                new Resource.Parse<List<YouTubeTrailer>>() {
                    @Override
                    public List<YouTubeTrailer> parse(String response) {
                        return YouTubeTrailerJsonUtils.buildTrailersFromResponse(response);
                    }
                }
        );
    }

    public static Resource<List<Review>> getReviewsForMovie(@NonNull final Movie movie) {
        final String path = "movie/" + String.valueOf(movie.getId()) + "/reviews";
        URL url = buildUrl(path, getDefaultMethodParameters());
        return new Resource<>(
                url,
                new Resource.Parse<List<Review>>() {
                    @Override
                    public List<Review> parse(String response) {
                        return ReviewsJsonUtils.buildReviewsFromResponse(response);
                    }
                }
        );
    }

    public static URL buildPosterUrlForMovie(Movie movie) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(API_SCHEME)
                .authority(IMAGE_API_HOST)
                .appendPath(IMAGE_API_PATH)
                .appendPath(DEFAULT_POSTER_SIZE);

        if (!TextUtils.isEmpty(movie.getPosterPath())) {
            builder.appendPath(movie.getPosterPath());
        }

        return urlFormBuilder(builder);
    }

    /**
     * @return The default http method parameters.
     */
    private static Map<String, String> getDefaultMethodParameters() {
        Map<String, String> parameters = new HashMap<>(2);
        parameters.put(API_KEY_PARAM, API_KEY);
        parameters.put(LANGUAGE_PARAM, "en-US");
        parameters.put(PAGE_PARAM, Integer.toString(1));

        return parameters;
    }

    /**
     * Builds the URL used to talk to the TMDb API.
     *
     * @param methodParameters The method parameters that will be applied for the URL.
     * @return The URL to use to query the weather server.
     */
    private static URL buildUrl(String path, Map<String, String> methodParameters) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(API_SCHEME)
                .authority(API_HOST)
                .appendPath(API_PATH);

        if (!TextUtils.isEmpty(path)) {
            builder.appendPath(path);
        }

        if (methodParameters != null && !methodParameters.isEmpty()) {
            for (Map.Entry<String, String> parameter : methodParameters.entrySet()) {
                builder.appendQueryParameter(parameter.getKey(), parameter.getValue());
            }
        }

        URL url = urlFormBuilder(builder);
        Log.d(LOG_TAG, "TMDb URL: " + url);

        return url;
    }

    private static URL urlFormBuilder(Uri.Builder builder) {
        URL url = null;
        try {
            String urlString = builder.build().toString();
            url = new URL(URLDecoder.decode(urlString, "UTF-8"));
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
