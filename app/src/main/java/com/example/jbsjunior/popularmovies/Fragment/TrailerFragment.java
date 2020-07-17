package com.example.jbsjunior.popularmovies.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Interface.VideoTaskCallBack;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.R;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.example.jbsjunior.popularmovies.sync.VideoSync;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailerFragment extends Fragment implements VideoTaskCallBack{

    private static Movie movie;
    private static int position;
    private TextView txtVideoTrailer;

    public TrailerFragment() {
        // Required empty public constructor
    }

    public static TrailerFragment newInstance(int position, Movie movie) {

        Bundle args = new Bundle();

        TrailerFragment trailerFragment = new TrailerFragment();
        args.putParcelable("movie", movie);
        args.putInt("position", position);
        trailerFragment.setArguments(args);
        return trailerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getArguments().getParcelable("movie");
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailer, container, false);
        txtVideoTrailer = (TextView) view.findViewById(R.id.tv_video_trailer);

        VideoSync videoSync = new VideoSync(this);
        videoSync.execute(movie.getId());

        Log.d("Bernardino", "TrailerFragment id= " + movie.getId());
        return view;
    }

    @Override
    public void onTaskCompleted(final String key) {

        StringBuffer sb = new StringBuffer(64);

        if (txtVideoTrailer!=null){
            sb.append(getResources().getString(R.string.click_trailer) + "\n");
            sb.append(movie.getTitle());
            txtVideoTrailer.setText(sb.toString());
            txtVideoTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URLServer.URL_MOVIE_YOUTUBE_WATCH + key)));
                }
            });
        } else {
            txtVideoTrailer.setText(R.string.trailer_no_avaiable);
        }
    }
}
