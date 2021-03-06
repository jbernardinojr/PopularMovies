package com.example.jbsjunior.popularmovies.Fragment;


import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Interface.ReviewTaskCallBack;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.Model.Review;
import com.example.jbsjunior.popularmovies.R;
import com.example.jbsjunior.popularmovies.adapter.ReviewsAdapter;
import com.example.jbsjunior.popularmovies.sync.ReviewSync;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment implements ReviewTaskCallBack {

    private static Movie movie;
    private static int position;
    private ListView listReview;
    private TextView txtReview;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance(int position, Movie movie) {

        Bundle args = new Bundle();

        ReviewFragment fragment = new ReviewFragment();
        args.putParcelable("movie", movie);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.movie = getArguments().getParcelable("movie");
        this.position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Bernardino", "ReviewFragment");

        View view = inflater.inflate(R.layout.fragment_review, container, false);

        txtReview = (TextView) view.findViewById(R.id.tv_no_review);
        listReview = (ListView) view.findViewById(R.id.lt_review);
        ReviewSync reviewSync = new ReviewSync(this);
        reviewSync.execute(movie.getId());

        return view;
    }

    @Override
    public void onTaskCompleted(List<Review> review) {

        if (review.size()>0) {
            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), review);
            listReview.setAdapter(reviewsAdapter);
        } else {
            txtReview.setVisibility(View.VISIBLE);
            listReview.setVisibility(View.GONE);
        }
    }
}
