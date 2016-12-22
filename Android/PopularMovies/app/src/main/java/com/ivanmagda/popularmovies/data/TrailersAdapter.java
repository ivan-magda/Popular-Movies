package com.ivanmagda.popularmovies.data;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.model.YouTubeTrailer;
import com.ivanmagda.popularmovies.utilities.YouTubeTrailerUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * An on-click handler that make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;

    private List<YouTubeTrailer> mTrailers;

    /**
     * Constructor for GreenAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param trailers Trailers to display in list
     * @param listener An on-click handler
     */
    public TrailersAdapter(List<YouTubeTrailer> trailers, ListItemClickListener listener) {
        this.mTrailers = trailers;
        this.mOnClickListener = listener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_item, viewGroup, false);
        return new TrailersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (mTrailers == null ? 0 : mTrailers.size());
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ib_trailer)
        ImageButton mTrailerImageButton;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mTrailerImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        private void bind(int position) {
            Uri uri = YouTubeTrailerUtils.buildVideoThumbnailUriForTrailer(mTrailers.get(position));
            Picasso.with(itemView.getContext()).load(uri).into(mTrailerImageButton);
        }

    }

}
