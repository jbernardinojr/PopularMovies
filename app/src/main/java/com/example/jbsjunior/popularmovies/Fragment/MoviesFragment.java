package com.example.jbsjunior.popularmovies.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jbsjunior.popularmovies.DetailsMovieActivity;
import com.example.jbsjunior.popularmovies.Interface.MovieTaskCallBack;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.MovieTask;
import com.example.jbsjunior.popularmovies.MyCustomAdapter;
import com.example.jbsjunior.popularmovies.R;
import com.example.jbsjunior.popularmovies.data.MovieDb;
import com.example.jbsjunior.popularmovies.util.Utils;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements MovieTaskCallBack {

    private List<Movie> mMovies;
    private GridView gv;
    private MyCustomAdapter mMyCustomAdapter;
    private NetworkDialogFragment mDialogFragment;
    private MovieDb movieDb;
    private CoordinatorLayout coordinatorLayout;

    public MoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieDb = new MovieDb(getContext());
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
        coordinatorLayout = (CoordinatorLayout) container.findViewById(R.id.coordinatorLayout_main);

        gv = (GridView) rootView.findViewById(R.id.gridview_movies);

        registerForContextMenu(gv);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
                intent.putExtra(Movie.PARCELABLE_KEY, mMovies.get(position));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.gridview_movies) {
            String[] menuItems = getResources().getStringArray(R.array.menu_context);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = menuInfo.position;
        long resp = 0L;

        Movie movieFavorite = mMovies.get(position);
        if (item.getItemId()==0){
            movieFavorite.setFavorite(Movie.MOVIE_IS_FAVORITE);
        } else if (item.getItemId()==1){
            movieFavorite.setFavorite(Movie.MOVIE_IS_NOT_FAVORITE);
        }

        resp = movieDb.atualizar(movieFavorite);
        if (resp > 0) {
            if (movieFavorite.isFavorite())
                Toast.makeText(getContext(), "Filme adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Filme removido dos favoritos!", Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
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
        if (Utils.isOnline(getContext())) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String view_mode = prefs.getString(getString(R.string.pref_view_key), getString(R.string.pref_view_default));

            MovieTask moviesTask = new MovieTask(getContext(), MoviesFragment.this, movieDb);
            moviesTask.execute(view_mode);
        } else {
            showDialog();
        }
    }

    @Override
    public void onTaskCallBack(List<Movie> lm) {
        mMovies = lm;
        mMyCustomAdapter = new MyCustomAdapter(getContext(), R.id.gridview_movies, mMovies);
        gv.setAdapter(mMyCustomAdapter);
    }

    private void showDialog() {
        FragmentManager fm = getFragmentManager();
        mDialogFragment = NetworkDialogFragment.newInstance(R.string.no_network_conn_title);
        mDialogFragment.setTargetFragment(MoviesFragment.this, 0);
        mDialogFragment.show(fm,"");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        if (mDialogFragment != null) {
            mDialogFragment.dismiss();
            updateViewMode();
        }
    }
}
