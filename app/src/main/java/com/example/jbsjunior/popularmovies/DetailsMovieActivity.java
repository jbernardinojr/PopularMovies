package com.example.jbsjunior.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.squareup.picasso.Picasso;

public class DetailsMovieActivity extends AppCompatActivity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle bundle = getIntent().getExtras();

        if (bundle!=null) {
            this.movie = (Movie) bundle.getParcelable(Movie.PARCELABLE_KEY);
            float avgRatingStars = ((float) movie.getVoteAverage() * 5) / 10;

            ScrollView sv = (ScrollView) findViewById(R.id.scroll_detail_activity);
            TextView txtMovieTitle = (TextView) findViewById(R.id.txtMovieTitle);
            TextView txtMovieDate = (TextView) findViewById(R.id.txtReleaseDate);
            TextView txtMovieDescription = (TextView) findViewById(R.id.txtMovieDescription);
            ImageView imgViewPoster = (ImageView) findViewById(R.id.imgDetailMoviePoster);
            RatingBar voteAvgBar = (RatingBar) findViewById(R.id.voteAverageBar);

            sv.setFillViewport(true);
            txtMovieDate.setText(movie.getReleaseDate().substring(0, 4));
            txtMovieTitle.setText(movie.getOriginalTitle());
            txtMovieDescription.setText(movie.getOverview());
            Picasso.with(DetailsMovieActivity.this)
                    .load(URLServer.URL_IMAGE_MOVIE + movie.getPosterPath())
                    .into(imgViewPoster);

            voteAvgBar.setRating(avgRatingStars);
            txtMovieDescription.setMovementMethod(new ScrollingMovementMethod());

        }
    }

}
