package com.example.jbsjunior.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.example.jbsjunior.popularmovies.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * Created by JBSJunior on 03/04/2017.
 */

public class Utils {

    public static final String MOVIE_POPULAR_PREFERENCE = "/movie/popular";
    public static final String MOVIE_TOP_RATED_PREFERENCE = "/movie/top_rated";
    public static final String MOVIE_FAVORITE_PREFERENCE = "/movie/favorite";


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);// Pego a conectividade do contexto
        NetworkInfo netInfo = cm.getActiveNetworkInfo();// Crio o objeto netInfo que recebe as informacoes da Network
        if ((netInfo != null) && (netInfo.isConnectedOrConnecting()) && (netInfo.isAvailable())) { // Se nao tem conectividade retorna false
            return true;
        }
        return false;
    }

    public static void showSnackBar(Context context, CoordinatorLayout coordinatorLayout, String msg){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.show();
    }

    public static String getPreferredView(Context context) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String view_mode = prefs.getString(context.getString(R.string.pref_view_key), context.getString(R.string.pref_view_default));

        return view_mode;
    }
}
