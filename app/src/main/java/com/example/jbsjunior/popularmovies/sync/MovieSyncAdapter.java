package com.example.jbsjunior.popularmovies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.R;
import com.example.jbsjunior.popularmovies.data.MovieContract;
import com.example.jbsjunior.popularmovies.server.ApiKey;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.example.jbsjunior.popularmovies.util.Utils;
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
import java.util.Vector;

import static com.example.jbsjunior.popularmovies.server.URLServer.URL_MOVIE_POPULAR;
import static com.example.jbsjunior.popularmovies.server.URLServer.URL_MOVIE_TOP_RATED;
import static com.example.jbsjunior.popularmovies.util.Utils.MOVIE_POPULAR_PREFERENCE;
import static com.example.jbsjunior.popularmovies.util.Utils.MOVIE_TOP_RATED_PREFERENCE;

/**
 * Created by JBSJunior on 05/04/2017.
 */

public class MovieSyncAdapter extends AbstractThreadedSyncAdapter {

    private final static String LOG_TAG = MovieSyncAdapter.class.getName();

    public MovieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr;

        String sApi = ApiKey.API_KEY; //put your movie db API Key Here
        String sViewModeMovie = "";
        if (MOVIE_POPULAR_PREFERENCE.equals(Utils.getPreferredView(getContext()))) {
            sViewModeMovie =  URL_MOVIE_POPULAR;
        } else if (MOVIE_TOP_RATED_PREFERENCE.equals(Utils.getPreferredView(getContext()))) {
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
            inputStream = urlConnection.getInputStream();

            if (inputStream == null) {
                // Nothing to do.
                throw new NullPointerException("inputStream is null");
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine())!= null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line);
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                throw new NullPointerException("Buffer length is 0");
            }
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
                    Log.d(LOG_TAG, movie.getTitle());
                }

                int inserted = 0;
                // add to database
                if ( cVVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
                }

                Log.d(LOG_TAG, "MovieTask Complete. " + inserted + " Inserted");
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.toString());
            }
        } catch (IOException |NullPointerException e) {
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
    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */

        //ToDo verificar possibilidade do sync automático

       // SunshineSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }
}
