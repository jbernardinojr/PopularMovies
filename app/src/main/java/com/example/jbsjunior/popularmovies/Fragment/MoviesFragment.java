package com.example.jbsjunior.popularmovies.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jbsjunior.popularmovies.DetailsMovieActivity;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.MovieTaskCallBack;
import com.example.jbsjunior.popularmovies.MoviesTask;
import com.example.jbsjunior.popularmovies.MyCustomAdapter;
import com.example.jbsjunior.popularmovies.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements MovieTaskCallBack {

    List<Movie> mMovies;
    ListView lv;
    MyCustomAdapter mMyCustomAdapter;

    public MoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        lv = (ListView) rootView.findViewById(R.id.listview_movies);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                String jsonMovies = gson.toJson(mMovies);

                Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
                intent.putExtra("list_movies", jsonMovies);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        MoviesTask moviesTask = new MoviesTask(rootView.getContext(), MoviesFragment.this);
        moviesTask.execute();

        return rootView;
    }

    @Override
    public void onTaskCallBack(List<Movie> lm) {
        mMovies = lm;
        mMyCustomAdapter = new MyCustomAdapter(getContext(), R.id.listview_movies, mMovies);
        for (int i=0; i<0; i++) {
            mMyCustomAdapter.add(mMovies.get(i));
        }
        lv.setAdapter(mMyCustomAdapter);
    }

}
