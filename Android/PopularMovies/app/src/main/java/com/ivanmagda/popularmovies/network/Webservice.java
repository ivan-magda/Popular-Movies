package com.ivanmagda.popularmovies.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Webservice {

    /* Log tag for debug statements. */
    private static String LOG_TAG = Webservice.class.getSimpleName();

    private Webservice() {
    }

    /**
     * @param resource The resourse to be loaded using HttpUrlConnection.
     * @param <A>      The generic return parameter, that will return resourse.parseBlock.parse function.
     * @return Result after parsing.
     */
    public static <A> A load(Resource<A> resource) {
        // Getting a connection to the resource referred to by this URL
        // and trying to connect.
        HttpURLConnection connection = null;
        URL url = resource.url;

        if (url == null) {
            return null;
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod(resource.httpMethodName);
            connection.setDoInput(true);
            connection.connect();

            String response = processOnResponse(connection);

            return resource.parseBlock.parse(response);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to download raw data", exception);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    private static String processOnResponse(HttpURLConnection connection) {
        try {
            // Did we receive a successful 2XX status code.
            int responseCode = connection.getResponseCode();
            if (responseCode < HttpURLConnection.HTTP_OK || responseCode > 299) {
                Log.w(LOG_TAG, "Received status code other then 2XX, status code: " + responseCode);
                return null;
            }
            Log.d(LOG_TAG, "Response status code: " + responseCode
                    + "for URL: " + connection.getURL());
            return readInputs(connection.getInputStream());
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to process on http response", exception);
        }

        return null;
    }

    private static String readInputs(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));

        // Reading each line of the data.
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }
}
