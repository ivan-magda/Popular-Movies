package com.ivanmagda.popularmovies.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ivanmagda.popularmovies.R;
import com.ivanmagda.popularmovies.data.model.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private List<Review> mReviews;

    public ReviewsAdapter() {
        this.mReviews = new ArrayList<>();
    }

    public ReviewsAdapter(List<Review> reviews) {
        this.mReviews = reviews;
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

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_review_content)
        TextView reviewContent;

        @BindView(R.id.tv_author)
        TextView reviewAuthor;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(int position) {
            Review review = mReviews.get(position);
            reviewContent.setText(review.getContent());
            reviewAuthor.setText(review.getAuthor());
        }

    }

}
