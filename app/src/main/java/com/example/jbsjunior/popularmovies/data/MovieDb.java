package com.example.jbsjunior.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jbsjunior.popularmovies.Model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JBSJunior on 03/04/2017.
 */

public class MovieDb {
    private SQLiteDatabase db;

    public MovieDb(Context context) {
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert (Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE, movie.isFavorite() ?
                Movie.MOVIE_IS_FAVORITE : Movie.MOVIE_IS_NOT_FAVORITE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());

        return db.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
    }

    public long atualizar (Movie movie) {
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE, movie.isFavorite() ?
                Movie.MOVIE_IS_FAVORITE : Movie.MOVIE_IS_NOT_FAVORITE);
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());

        return db.update(MovieContract.MovieEntry.TABLE_NAME, cv, " id = ? ",
                new String[]{String.valueOf(movie.getId())});
    }

    public int delete (Movie movie) {
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, " _id = " + movie.getId(), null);
    }

    public List<Movie> busca() {

        List<Movie> movies = new ArrayList<>();

        String [] columns = new String []{
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT,
            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE
        };

        Cursor c = db.query(MovieContract.MovieEntry.TABLE_NAME, columns, null, null, null, null, null, null);

        if (c.getCount()>0) {
            c.moveToFirst();
            do {
                Movie movie = new Movie();
                movie.setBackdropPath(c.getString(0));
                movie.setOriginalLanguage(c.getString(1));
                movie.setOverview(c.getString(2));
                movie.setPopularity(c.getDouble(3));
                movie.setTitle(c.getString(4));
                movie.setPosterPath(c.getString(5));
                movie.setVoteCount(c.getLong(6));
                movie.setFavorite(c.getInt(7));
                movie.setVoteAverage(c.getDouble(8));
                movie.setReleaseDate(c.getString(9));
                movies.add(movie);
            } while (c.moveToNext());
        }

        c.close();
        return movies;
    }

    public int bulkInsert(ContentValues[] values) {
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return returnCount;
    }
}
