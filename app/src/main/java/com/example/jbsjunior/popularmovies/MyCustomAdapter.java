package com.example.jbsjunior.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JBSJunior on 01/11/2016.
 */

public class MyCustomAdapter extends ArrayAdapter {

    private final LayoutInflater mInflater;
    private final List<Movie> mMovies;

    public MyCustomAdapter(Context context, int resource, List<Movie> movies) {
        super(context, resource, movies);
        mMovies = movies;
        mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void add(Object object) {
        super.add(object);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mRow = convertView;
        DataHandler dataHandler = new DataHandler();
        if (mRow == null) {
            mRow = mInflater.inflate(R.layout.movies_item, parent, false);
            dataHandler.imageMovie = (ImageView) mRow.findViewById(R.id.imgMovie);
            mRow.setTag(dataHandler);
        }else {
            dataHandler = (DataHandler) mRow.getTag();
        }
        Movie movie = mMovies.get(position);
        Picasso.with(getContext())
                .load(URLServer.URL_IMAGE_MOVIE + movie.getPosterPath())
                .placeholder(R.drawable.defaultposter)
                .into(dataHandler.imageMovie);

        return mRow;
    }

    private class DataHandler {
        ImageView imageMovie;
    }
}
