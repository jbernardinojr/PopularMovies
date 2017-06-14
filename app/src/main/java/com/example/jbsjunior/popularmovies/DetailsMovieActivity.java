package com.example.jbsjunior.popularmovies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.Model.Movie;
import com.example.jbsjunior.popularmovies.server.URLServer;
import com.squareup.picasso.Picasso;

public class DetailsMovieActivity extends AppCompatActivity {

    private Movie movie;
    private TabLayout tabs;
    private ViewPager pager;
    private TabsPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            this.movie = bundle.getParcelable(Movie.PARCELABLE_KEY);
        }

        tabs = (TabLayout) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabsPagerAdapter(getSupportFragmentManager(), movie);

        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
    }

    /**
     * Adding custom view to tab
     */
    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Overview");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_overview, 0, 0);
        tabs.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Trailer");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_launcher, 0, 0);
        tabs.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Reviews");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_review, 0, 0);
        tabs.getTabAt(2).setCustomView(tabThree);
    }

}
