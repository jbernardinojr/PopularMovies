package com.example.jbsjunior.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jbsjunior.popularmovies.Fragment.MoviesFragment;
import com.example.jbsjunior.popularmovies.sync.MovieSyncAdapter;
import com.example.jbsjunior.popularmovies.util.Utils;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {

    private String mViewMode;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        if (id==R.id.action_settings) {
            startActivity(intentSettings);
        }

        return super.onOptionsItemSelected(item);
    }
}
