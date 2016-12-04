package com.ivanmagda.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.ivanmagda.popularmovies.model.Movie;
import com.ivanmagda.popularmovies.network.TMDbApi;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context) {
        super(context, 0, new ArrayList<Movie>());
    }

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context The current context. Used to inflate the layout file.
     * @param movies  A List of Movies objects to display in a list
     */
    public MovieAdapter(Activity context, List<Movie> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        View listView = convertView;

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (listView == null) {
            listView = LayoutInflater.from(getContext())
                    .inflate(R.layout.movie_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.posterImageView = (ImageView) listView.findViewById(R.id.iv_item_movie_poster);

            listView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) listView.getTag();
        }

        URL posterUrl = TMDbApi.buildPosterUrlForMovie(getItem(position));
        Picasso.with(getContext())
                .load(posterUrl.toString())
                .into(viewHolder.posterImageView);

        return listView;
    }

    private static class ViewHolder {
        ImageView posterImageView;
    }

    public void updateWithNewData(List<Movie> newData) {
        clear();
        if (newData != null && !newData.isEmpty()) {
            addAll(newData);
        }
        notifyDataSetChanged();
    }
}
