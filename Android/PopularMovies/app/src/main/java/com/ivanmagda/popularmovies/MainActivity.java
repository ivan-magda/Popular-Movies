package com.ivanmagda.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ivanmagda.popularmovies.network.Resource;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.ivanmagda.popularmovies.network.Webservice;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TestTask().execute(TMDbApi.getPopularMovies());
    }

    private class TestTask extends AsyncTask<Resource<String>, Void, String> {
        @SafeVarargs
        @Override
        protected final String doInBackground(Resource<String>... resources) {
            Resource<String> resource = resources[0];
            return Webservice.load(resource);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(LOG_TAG, "\n\n\nResponse :\n" + s);
        }
    }
}
