package com.example.jbsjunior.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jbsjunior.popularmovies.Fragment.MoviesFragment;
import com.example.jbsjunior.popularmovies.Fragment.NetworkDialogFragment;

public class MainActivity extends AppCompatActivity {

    private NetworkDialogFragment mDialogFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (isOnline(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, new MoviesFragment())
                        .commit();
            } else {
                showDialog();
            }
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

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);// Pego a conectividade do contexto
        NetworkInfo netInfo = cm.getActiveNetworkInfo();// Crio o objeto netInfo que recebe as informacoes da Network
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) { // Se nao tem conectividade retorna false
            return true;
        }
        return false;
    }

    void showDialog() {
        mDialogFragment = NetworkDialogFragment.newInstance(R.string.no_network_conn_title);
        mDialogFragment.show(getFragmentManager(), "dialog");
    }

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
        if (mDialogFragment != null) {
            mDialogFragment.dismiss();
            finish();
        }

    }
}
