package com.example.jbsjunior.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.jbsjunior.popularmovies.Fragment.MoviesFragment;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.data.MovieContract;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.squareup.picasso.Picasso;

/**
 * Created by JBSJunior on 01/11/2016.
 */

public class MoviesAdapter extends CursorAdapter {

    private static final String LOG_TAG = MoviesAdapter.class.getName();
    Context mContext;
    private final LayoutInflater mInflater;

    public MoviesAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.movies_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        Log.d(LOG_TAG, "newView viewHolder = " + viewHolder.toString());

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //Read view holder with layout components mapped
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Log.d(LOG_TAG, "bindView viewHolder = " + viewHolder.toString());

        Picasso.with(mContext)
                .load(URLServer.URL_IMAGE_MOVIE + cursor.getString(MoviesFragment.COLUMN_MOVIE_POSTER_PATH))
                .placeholder(R.drawable.defaultposter)
                .into(viewHolder.imgMovie);
    }

    private class DataHandler {
        ImageView imageMovie;
    }

    /**
     * Defines a class that hold resource IDs of each item layout
     * row to prevent having to look them up each time data is
     * bound to a row.
     */
    private static class ViewHolder {
        public final ImageView imgMovie;

        public ViewHolder (View view) {
            imgMovie = (ImageView) view.findViewById(R.id.imgMovie);
        }
    }

    public Movie get(int position) {
        Cursor cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, "", null, null);
        Movie movie = null;
        if(cursor.moveToPosition(position)) {
            movie = new Movie();
            movie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH )));
            movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY)));
            movie.setVoteCount(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT)));
            movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_HAS_VIDEO)) == 1 ? true : false);
            movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH)));
            movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE)));
            movie.setFavorite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE)));
        }

        return movie;
    }
}
