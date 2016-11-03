package com.example.jbsjunior.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

public class DetailsMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String movieAsString = intent.getStringExtra("list_movies");
        int position = intent.getIntExtra("position", 0);

        Log.d("Teste", movieAsString);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Movie>>(){}.getType();
        final List<Movie> movies = gson.fromJson(movieAsString, type);
        Movie movie = movies.get(position);

        TextView txtMovieTitle = (TextView) findViewById(R.id.txtMovieTitle);
        TextView txtMovieDescription = (TextView) findViewById(R.id.txtMovieDescription);
        ImageView imgViewPoster = (ImageView) findViewById(R.id.imgDetailMoviePoster);

        txtMovieTitle.setText(movie.getTitle());
        txtMovieDescription.setText(movie.getOverview());
        Picasso.with(DetailsMovieActivity.this)
                .load(URLServer.URL_IMAGE_MOVIE + movie.getPosterPath())
                .into(imgViewPoster);
    }

}
