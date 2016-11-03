package com.example.jbsjunior.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.jbsjunior.popularmovies.server.URLServer.URL_IMAGE_MOVIE;

/**
 * Created by JBSJunior on 01/11/2016.
 */

public class MyCustomAdapter extends ArrayAdapter {

    private LayoutInflater mInflater;
    private List<Movie> mMovies;

    public MyCustomAdapter(Context context, int resource, List<Movie> movies) {
        super(context, resource, movies);
        mMovies = movies;
        mInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void add(Object object) {
        super.add(object);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mRow = convertView;
        DataHandler handler = new DataHandler();
        if (mRow == null) {
            mRow = mInflater.from(getContext()).inflate(R.layout.movies_item, parent, false);
            handler.imageMovie = (ImageView) mRow.findViewById(R.id.imgMovie);
            handler.txtMovieName = (TextView) mRow.findViewById(R.id.list_item_movie_textView);
            mRow.setTag(handler);
        }else {
            handler = (DataHandler) mRow.getTag();
        }

        Movie movie = (Movie) this.getItem(position);
        handler.txtMovieName.setText(movie.getTitle());
        Picasso.with(getContext()).load(URL_IMAGE_MOVIE + movie.getPosterPath()).into(handler.imageMovie);
        return mRow;
    }

    private class DataHandler {
        ImageView imageMovie;
        TextView txtMovieName;
    }
}
