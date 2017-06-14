package com.example.jbsjunior.popularmovies.sync;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jbsjunior.popularmovies.Interface.ReviewTaskCallBack;
import com.example.jbsjunior.popularmovies.Interface.VideoTaskCallBack;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.Model.Review;
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
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junior on 14/06/17.
 */

public class ReviewSync extends AsyncTask<Long, Void, List<Review>> {

    private ReviewTaskCallBack listener;

    public ReviewSync(ReviewTaskCallBack listener) {
        this.listener = listener;
    }


    private List<Review> mReviews;

    @Override
    protected List<Review> doInBackground(Long... params) {
        long movieId = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr;
        String key = "";

        String sApi = ApiKey.API_KEY; //put your movie db API Key Here

        String sUrlReview = URLServer.URL_MOVIE_REVIEWS.replaceFirst("id", String.valueOf(movieId));

        Uri uriBuilder = Uri
                .parse(URLServer.URL_BASE_API_MOVIE + sUrlReview)
                .buildUpon()
                .appendQueryParameter(URLServer.API_KEY, sApi)
                .build();

        String sUri = uriBuilder.toString();
        Log.d("Bernardino", "URI=" + sUri);

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
            while ((line = reader.readLine()) != null) {
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
            Log.d("Bernardino", "Json = " + movieJsonStr);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a"); //Format of our JSON dates
            Gson gson = gsonBuilder.create();
            JSONObject mJsonObjReviews;
            JSONObject mReviewJson = new JSONObject(movieJsonStr);
            JSONArray mRespArray = mReviewJson.getJSONArray("results");
            mReviews = new ArrayList<>();
            
            for (int i = 0; i<mRespArray.length(); i++){
                mJsonObjReviews = mRespArray.getJSONObject(i);
                Review review = gson.fromJson(mJsonObjReviews.toString(), Review.class);
                mReviews.add(review);
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mReviews;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        listener.onTaskCompleted(reviews);
        Log.d("Bernardino", "Reviews" + reviews.size());
    }
}
