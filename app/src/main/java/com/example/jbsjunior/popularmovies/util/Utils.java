package com.example.jbsjunior.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.R;

/**
 * Created by JBSJunior on 03/04/2017.
 */

public class Utils {

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
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        snackbar.show();
    }
}
