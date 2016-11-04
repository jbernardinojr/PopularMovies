package com.example.jbsjunior.popularmovies.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
    GridView gv;
    MyCustomAdapter mMyCustomAdapter;

    public MoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViewMode();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
        gv = (GridView) rootView.findViewById(R.id.gridview_movies);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        updateViewMode();
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateViewMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateViewMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String view_mode = prefs.getString(getString(R.string.pref_view_key), getString(R.string.pref_view_default));

        MoviesTask moviesTask = new MoviesTask(getContext(), MoviesFragment.this);
        moviesTask.execute(view_mode);
    }

    @Override
    public void onTaskCallBack(List<Movie> lm) {
        mMovies = lm;
        mMyCustomAdapter = new MyCustomAdapter(getContext(), R.id.gridview_movies, mMovies);
        gv.setAdapter(mMyCustomAdapter);
        Log.d("Teste", "onTaskCallBack "  + lm.size());
    }
}
