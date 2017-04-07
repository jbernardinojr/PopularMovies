package com.example.jbsjunior.popularmovies.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.MoviesAdapter;
import com.example.jbsjunior.popularmovies.R;
import com.example.jbsjunior.popularmovies.data.MovieContract;
import com.example.jbsjunior.popularmovies.sync.MovieSyncAdapter;
import com.example.jbsjunior.popularmovies.util.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MoviesFragment.class.getSimpleName();
    private static final String POSITION_KEY = "position";
    private static final int MOVIE_LOADER = 0;

    private GridView gv;
    private MoviesAdapter mMoviesAdapter;
    private NetworkDialogFragment mDialogFragment;
    private Uri mUri;
    private int mPosition;
    private ContentResolver mCr;
    private ProgressDialog mProgressDialog;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_POPULARITY,
            MovieContract.MovieEntry.COLUMN_MOVIE_HAS_VIDEO,
            MovieContract.MovieEntry.COLUMN_MOVIE_ORIGINAL_LANGUAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE
    };

    // These indices are tied to MOVIE_COLUMNS. If MOVIE_COLUMNS changes, these
    // must change.
    public static final int _ID = 0;
    public static final int ID = 1;
    public static final int COLUMN_MOVIE_TITLE = 2;
    public static final int COLUMN_MOVIE_POSTER_PATH = 3;
    public static final int COLUMN_MOVIE_POPULARITY = 4;
    public static final int COLUMN_MOVIE_HAS_VIDEO = 5;
    public static final int COLUMN_MOVIE_ORIGINAL_LANGUAGE = 6;
    public static final int COLUMN_MOVIE_OVERVIEW = 7;
    public static final int COLUMN_MOVIE_RELEASE_DATE = 8;
    public static final int COLUMN_MOVIE_BACKDROP_PATH = 9;
    public static final int COLUMN_MOVIE_VOTE_AVERAGE = 10;
    public static final int COLUMN_MOVIE_FAVORITE = 11;

    public MoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mUri = MovieContract.MovieEntry.CONTENT_URI;
        mCr = getContext().getContentResolver();
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
        Cursor cursor = mCr.query(MovieContract.MovieEntry.CONTENT_URI, null, "", null, null);

        mMoviesAdapter = new MoviesAdapter(getActivity(), cursor, 0);
        gv = (GridView) rootView.findViewById(R.id.gridview_movies);
        gv.setAdapter(mMoviesAdapter);
        registerForContextMenu(gv);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsMovieActivity.class);
                intent.putExtra(Movie.PARCELABLE_KEY, mMoviesAdapter.get(position));
                startActivity(intent);
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mPosition = savedInstanceState.getInt(POSITION_KEY);
        }
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
        Movie movieFavorite = mMoviesAdapter.get(position);

        if (item.getItemId() == 0){
            movieFavorite.setFavorite(Movie.MOVIE_IS_FAVORITE);
        } else if (item.getItemId() == 1){
            movieFavorite.setFavorite(Movie.MOVIE_IS_NOT_FAVORITE);
        }

        ContentValues cv = new ContentValues();
        cv.put(MOVIE_COLUMNS[1],  movieFavorite.getId());
        cv.put(MOVIE_COLUMNS[2],  movieFavorite.getTitle());
        cv.put(MOVIE_COLUMNS[3],  movieFavorite.getPosterPath());
        cv.put(MOVIE_COLUMNS[4],  movieFavorite.getPopularity());
        cv.put(MOVIE_COLUMNS[5],  movieFavorite.isVideo() ? 1 : 0);
        cv.put(MOVIE_COLUMNS[6],  movieFavorite.getOriginalLanguage());
        cv.put(MOVIE_COLUMNS[7],  movieFavorite.getOverview());
        cv.put(MOVIE_COLUMNS[8],  movieFavorite.getReleaseDate());
        cv.put(MOVIE_COLUMNS[9],  movieFavorite.getBackdropPath());
        cv.put(MOVIE_COLUMNS[10], movieFavorite.getVoteAverage());
        cv.put(MOVIE_COLUMNS[11], movieFavorite.isFavorite() ? 1 : 0);

        resp = mCr.update(MovieContract.MovieEntry.CONTENT_URI, cv, " id = ? ",
                new String[] {movieFavorite.getId() + ""});

        if (resp > 0) {
            if (movieFavorite.isFavorite()) {
                Toast.makeText(getContext(), getString(R.string.movie_add_favorite), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.movie_remove_favorite), Toast.LENGTH_SHORT).show();
            }
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
            MovieSyncAdapter.syncImmediately(getActivity());
        } else {
            showDialog();
        }
    }

    private void showDialog() {
        FragmentManager fm = getFragmentManager();
        mDialogFragment = NetworkDialogFragment.newInstance(R.string.no_network_conn_title);
        mDialogFragment.setTargetFragment(MoviesFragment.this, 0);
        mDialogFragment.show(fm,"");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.d(LOG_TAG, "Positive click!");
        if (mDialogFragment != null) {
            mDialogFragment.dismiss();
            updateViewMode();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "In onCreateLoader");

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading data");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        if (null != mUri) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (mPosition != GridView.INVALID_POSITION) {
            gv.smoothScrollToPosition(mPosition);
        }
        mMoviesAdapter.swapCursor(cursor);
        mProgressDialog.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { mMoviesAdapter.swapCursor(null); }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(POSITION_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
}
