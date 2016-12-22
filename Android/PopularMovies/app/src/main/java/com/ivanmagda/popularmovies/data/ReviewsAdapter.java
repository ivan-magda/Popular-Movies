package com.ivanmagda.popularmovies.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

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

    private List<Review> mReviews;

    /**
     * Constructor for GreenAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     * @param reviews  Reviews to display in list
     * @param listener An on-click handler
     */
    public ReviewsAdapter(List<Review> reviews, ListItemClickListener listener) {
        this.mReviews = reviews;
        this.mOnClickListener = listener;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, viewGroup, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return (mReviews == null ? 0 : mReviews.size());
    }

    public void updateWithNewData(List<Review> newData) {
        mReviews.clear();
        if (newData != null && !newData.isEmpty()) {
            mReviews.addAll(newData);
        }
        notifyDataSetChanged();
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_review_content)
        TextView reviewContent;

        @BindView(R.id.tv_author)
        TextView reviewAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        private void bind(int position) {
            Review review = mReviews.get(position);
            reviewContent.setText(review.getContent());
            reviewAuthor.setText(review.getAuthor());
        }

    }

}
