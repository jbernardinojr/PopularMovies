package com.example.jbsjunior.popularmovies.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.jbsjunior.popularmovies.DetailsMovieActivity;
import com.example.jbsjunior.popularmovies.Interface.MovieTaskCallBack;
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.MovieTask;
import com.example.jbsjunior.popularmovies.MyCustomAdapter;
import com.example.jbsjunior.popularmovies.R;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements MovieTaskCallBack {

    private List<Movie> mMovies;
    private GridView gv;
    private MyCustomAdapter mMyCustomAdapter;
    private NetworkDialogFragment mDialogFragment;

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
                Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
                intent.putExtra(Movie.PARCELABLE_KEY, mMovies.get(position));
                startActivity(intent);
            }
        });
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

        if (isOnline(getContext())) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String view_mode = prefs.getString(getString(R.string.pref_view_key), getString(R.string.pref_view_default));

            MovieTask moviesTask = new MovieTask(getContext(), MoviesFragment.this);
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

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);// Pego a conectividade do contexto
        NetworkInfo netInfo = cm.getActiveNetworkInfo();// Crio o objeto netInfo que recebe as informacoes da Network
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) { // Se nao tem conectividade retorna false
            return true;
        }
        return false;
    }

    void showDialog() {
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
/*
    public void doNegativeClick() {
        if (mDialogFragment != null) {
            mDialogFragment.dismiss();
            try {
                getActivity().finish();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }
*/
}
