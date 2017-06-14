package com.example.jbsjunior.popularmovies;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.jbsjunior.popularmovies.Fragment.DetailPageLayout;
import com.example.jbsjunior.popularmovies.Fragment.OverviewFragment;
import com.example.jbsjunior.popularmovies.Fragment.ReviewFragment;
import com.example.jbsjunior.popularmovies.Fragment.TrailerFragment;
import com.example.jbsjunior.popularmovies.Model.Movie;

/**
 * Created by junior on 08/06/17.
 */


public class TabsPagerAdapter extends FragmentPagerAdapter {

    Movie movie;

    public TabsPagerAdapter(FragmentManager fm, Movie movie) {
        super(fm);
        this.movie = movie;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;

        switch (position) {
            case 0: // Fragment # 0 - This will show Movie Overview
                fragment = OverviewFragment.newInstance(position, movie);
                break;
            case 1: // Fragment # 1 - This will show Movie trailer
                fragment = TrailerFragment.newInstance(position, movie);
                break;
            case 2:// Fragment # 2 - Will show Reviews
                fragment = ReviewFragment.newInstance(position, movie);
                break;
            default:
                fragment = OverviewFragment.newInstance(position, movie);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "TAB " + (position + 1);
    }
}
