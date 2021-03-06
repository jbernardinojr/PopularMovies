package com.example.jbsjunior.popularmovies.Fragment;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

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

            final ScrollView sv = (ScrollView) view.findViewById(R.id.scroll_detail);
            TextView txtMovieTitle = (TextView) view.findViewById(R.id.txtMovieTitle);
            TextView txtMovieDate = (TextView) view.findViewById(R.id.txtReleaseDate);
            final TextView txtMovieDescription = (TextView) view.findViewById(R.id.txtMovieDescription);
            txtMovieDescription.setMovementMethod(new ScrollingMovementMethod());
            ImageView imgViewPoster = (ImageView) view.findViewById(R.id.imgDetailMoviePoster);
            RatingBar voteAvgBar = (RatingBar) view.findViewById(R.id.voteAverageBar);

            txtMovieDate.setText(movie.getReleaseDate().substring(0, 4));
            txtMovieTitle.setText(movie.getTitle());
            txtMovieDescription.setText(movie.getOverview());
            Picasso.with(getContext())
                    .load(URLServer.URL_IMAGE_MOVIE + movie.getPosterPath())
                    .into(imgViewPoster);

            voteAvgBar.setRating(avgRatingStars);
            LayerDrawable stars = (LayerDrawable) voteAvgBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

            sv.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    txtMovieDescription.getParent().requestDisallowInterceptTouchEvent(false);

                    return false;
                }
            });

            txtMovieDescription.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    txtMovieDescription.getParent().requestDisallowInterceptTouchEvent(true);

                    return false;
                }
            });
        }

        return view;
    }

}
