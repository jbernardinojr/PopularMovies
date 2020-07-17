package com.example.jbsjunior.popularmovies;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jbsjunior.popularmovies.Fragment.MoviesFragment;
import com.example.jbsjunior.popularmovies.data.MovieContract;
import com.example.jbsjunior.popularmovies.sync.MovieSyncAdapter;
import com.example.jbsjunior.popularmovies.util.Utils;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private String mViewMode;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //listener on changed sort order preference:
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mViewMode = Utils.getPreferredView(this);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.content_main, new MoviesFragment(), MoviesFragment.MOVIE_TAG)
                    .commit();
        }
       // MoviesFragment mf = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_movies);
        MovieSyncAdapter.initializeSyncAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String viewMode = Utils.getPreferredView(this);
        if (viewMode != null && !viewMode.equals(mViewMode)) {
            MoviesFragment mf = (MoviesFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
            if (null != mf) {
                mf.onViewModeChanged();
            }
            mViewMode = viewMode;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        SharedPreferences.Editor prefEditor = prefs.edit();

        switch (id) {
            case R.id.action_view_favorite:
                prefEditor.putString(getApplicationContext().getString(R.string.pref_view_key), Utils.MOVIE_FAVORITE_PREFERENCE);
                prefEditor.commit();
                if (hasFavorite()) {
                    onResume();
                } else {
                    Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.has_no_favorite), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_view_top_rated:
                prefEditor.putString(getApplicationContext().getString(R.string.pref_view_key), Utils.MOVIE_TOP_RATED_PREFERENCE);
                prefEditor.commit();
                onResume();
                return true;
            case R.id.action_view_popular:
                prefEditor.putString(getApplicationContext().getString(R.string.pref_view_key), Utils.MOVIE_POPULAR_PREFERENCE);
                prefEditor.commit();
                onResume();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean hasFavorite() {
        ContentResolver cr = getContentResolver();
        String [] favoriteColumn = {MovieContract.MovieEntry.COLUMN_MOVIE_ID};
        String [] favoriteFlag = {"1"};
        Cursor c = cr.query(MovieContract.MovieEntry.CONTENT_URI, favoriteColumn, MovieContract.MovieEntry.COLUMN_MOVIE_FAVORITE + " = ?", favoriteFlag, "");
        if (c.getCount() == 0) {
            c.close();
            return false;
        }
        c.close();
        return true;
    }
}
