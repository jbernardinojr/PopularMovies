package com.example.jbsjunior.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Model.Review;
import com.example.jbsjunior.popularmovies.R;

import java.util.List;

/**
 * Created by junior on 14/06/17.
 */

public class ReviewsAdapter extends BaseAdapter{

    private final List<Review> mReviews;
    private final Context mContext;
    private TextView mUserName;
    private TextView mUserReview;

    public ReviewsAdapter(Context context, List<Review> reviews) {
        this.mContext = context;
        this.mReviews = reviews;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Object getItem(int i) {
        return mReviews.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Long.parseLong(mReviews.get(i).getId());
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Review review = mReviews.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View returnView = view;

        if (returnView == null) {
            returnView = inflater.inflate(R.layout.custom_reviews_list, viewGroup, false);
        }

        mUserName = (TextView) returnView.findViewById(R.id.tv_user_name);
        mUserReview = (TextView) returnView.findViewById(R.id.tv_user_review);

        mUserName.setText(review.getAuthor());
        mUserReview.setText(review.getContent());

        return returnView;
    }
}
