package com.example.jbsjunior.popularmovies.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    private static Movie movie;
    private static int position;

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
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

}
