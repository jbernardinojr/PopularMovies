package com.example.jbsjunior.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jbsjunior.popularmovies.Interface.MovieTaskCallBack;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.data.MovieContract;
import com.example.jbsjunior.popularmovies.data.MovieDb;
import com.example.jbsjunior.popularmovies.server.ApiKey;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static com.example.jbsjunior.popularmovies.server.URLServer.URL_MOVIE_POPULAR;
import static com.example.jbsjunior.popularmovies.server.URLServer.URL_MOVIE_TOP_RATED;

/**
 * Created by JBSJunior on 26/10/2016.
 */

public class MovieTask extends AsyncTask<String, Object, List<Movie>> {

    private static final String LOG_TAG = "MoviesTask";
    private final Context mContext;
    private ProgressDialog dialog;
    private final MovieTaskCallBack mMovieTaskCallBack;
    private List<Movie> movies;
    private MovieDb movieDb;
    public MovieTask(Context context, MovieTaskCallBack movieTaskCallBack, MovieDb db) {
        mContext = context;
        mMovieTaskCallBack = movieTaskCallBack;
        movies = null;
        movieDb = db;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (movies!=null) {
            movies.clear();
        }
        dialog = ProgressDialog.show(mContext, "Wait", "Downloading...");
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr;

        publishProgress(1);
        String sApi = ApiKey.API_KEY; //put you movie db API Key Here
        String sViewModeMovie = "";
        if ("/movie/popular".equals(params[0])) {
            sViewModeMovie =  URL_MOVIE_POPULAR;
        } else if ("/movie/top_rated".equals(params[0])) {
            sViewModeMovie =  URL_MOVIE_TOP_RATED;
        }

        Uri uriBuilder = Uri
                .parse(URLServer.URL_BASE_API_MOVIE + sViewModeMovie.trim())
                .buildUpon()
                .appendQueryParameter(URLServer.API_KEY, sApi)
                .build();

        // Construct the URL for the TheMovieDB query
        String sUri = uriBuilder.toString();

        Log.d(LOG_TAG, sUri);
        publishProgress(2);

        URL url = null;
        try {
            url = new URL(sUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Read the input stream into a String
        InputStream inputStream;
        StringBuffer buffer = new StringBuffer();
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            publishProgress(3);
            inputStream = urlConnection.getInputStream();

            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!= null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
            }
            publishProgress(5);

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            publishProgress(6);
            movieJsonStr = buffer.toString();

            try {
                JSONObject mMoviesJson;
                JSONObject mJsonObjMovies;
                JSONArray mRespArray;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a"); //Format of our JSON dates
                Gson gson = gsonBuilder.create();
                mMoviesJson = new JSONObject(movieJsonStr);
                mRespArray = mMoviesJson.getJSONArray("results");
                movies = new ArrayList<>();
                Vector<ContentValues> cVVector = new Vector<ContentValues>(mRespArray.length());

                for (int i=0; i < mRespArray.length(); i++) {
                    mJsonObjMovies = mRespArray.getJSONObject(i);
                    Movie movie = gson.fromJson(mJsonObjMovies.toString(), Movie.class);

                    ContentValues movieValues = new ContentValues();
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdropPath());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY, movie.getPopularity());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE, movie.isFavorite() ? 1 : 0);
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
                    movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());

                    cVVector.add(movieValues);
                    movies.add(movie);
                    Log.d(LOG_TAG, movie.getTitle());
                }

                int inserted = 0;
                // add to database
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = movieDb.bulkInsert(cvArray);
                }

                Log.d(LOG_TAG, "MovieTask Complete. " + inserted + " Inserted");
                publishProgress(7);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.toString());
            }
        } catch (IOException|NullPointerException e) {
            Log.e(LOG_TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return movies;
    }

    @Override
    protected void onPostExecute(List<Movie> retMovies) {
        mMovieTaskCallBack.onTaskCallBack(retMovies);
        dialog.dismiss();
    }

}
