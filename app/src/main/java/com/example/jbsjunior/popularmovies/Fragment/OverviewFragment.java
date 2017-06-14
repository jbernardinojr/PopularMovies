package com.example.jbsjunior.popularmovies.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.DetailsMovieActivity;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.R;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment {

    private static Movie movie;
    private static int position;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance (int position, Movie movie){
        OverviewFragment overviewFragment = new OverviewFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelable("movie", movie);
        overviewFragment.setArguments(args);

        return overviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
        movie = getArguments().getParcelable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        if (movie != null) {
            float avgRatingStars = ((float) movie.getVoteAverage() * 5) / 10;

            ScrollView sv = (ScrollView) view.findViewById(R.id.scroll_detail);
            TextView txtMovieTitle = (TextView) view.findViewById(R.id.txtMovieTitle);
            TextView txtMovieDate = (TextView) view.findViewById(R.id.txtReleaseDate);
            TextView txtMovieDescription = (TextView) view.findViewById(R.id.txtMovieDescription);
            ImageView imgViewPoster = (ImageView) view.findViewById(R.id.imgDetailMoviePoster);
            RatingBar voteAvgBar = (RatingBar) view.findViewById(R.id.voteAverageBar);

            sv.setFillViewport(true);
            txtMovieDate.setText(movie.getReleaseDate().substring(0, 4));
            txtMovieTitle.setText(movie.getTitle());
            txtMovieDescription.setText(movie.getOverview());
            Picasso.with(getContext())
                    .load(URLServer.URL_IMAGE_MOVIE + movie.getPosterPath())
                    .into(imgViewPoster);

            voteAvgBar.setRating(avgRatingStars);
            txtMovieDescription.setMovementMethod(new ScrollingMovementMethod());
        }

        return view;
    }

}
