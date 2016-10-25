package com.example.jbsjunior.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(imageView);

        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/lZpWprJqbIFpEV5uoHfoK0KCnTW.jpg").into(imageView2);

        ImageView imageView4 = (ImageView) findViewById(R.id.imageView4);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/t0IfTnOnWBrLQPoFxdtHmr4BnVS.jpg").into(imageView4);

        ImageView imageView5 = (ImageView) findViewById(R.id.imageView5);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/ixZn1uPR3nw51plNEOpLuchWe7K.jpg").into(imageView5);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
