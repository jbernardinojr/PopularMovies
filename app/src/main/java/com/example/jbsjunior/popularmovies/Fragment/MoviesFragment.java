package com.example.jbsjunior.popularmovies.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jbsjunior.popularmovies.MoviesTask;
import com.example.jbsjunior.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        MoviesTask moviesTask = new MoviesTask(rootView.getContext());
        moviesTask.execute();

        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg")
                .into(imageView);

        ImageView imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/lZpWprJqbIFpEV5uoHfoK0KCnTW.jpg")
                .into(imageView2);

        ImageView imageView4 = (ImageView) rootView.findViewById(R.id.imageView4);
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/t0IfTnOnWBrLQPoFxdtHmr4BnVS.jpg")
                .into(imageView4);

        ImageView imageView5 = (ImageView) rootView.findViewById(R.id.imageView5);
        Picasso.with(getContext())
                .load("http://image.tmdb.org/t/p/w185/ixZn1uPR3nw51plNEOpLuchWe7K.jpg")
                .into(imageView5);

        return rootView;
    }
}
