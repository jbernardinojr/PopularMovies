package com.example.jbsjunior.popularmovies.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.example.jbsjunior.popularmovies.R;

/**
 * Created by JBSJunior on 08/11/2016.
 */

public class NetworkDialogFragment extends DialogFragment {
    public static NetworkDialogFragment newInstance(int title) {
        NetworkDialogFragment fragNetwork = new NetworkDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        fragNetwork.setArguments(args);
        return fragNetwork;
    }

    @Override
    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(R.string.no_network_conn_message)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MoviesFragment)getTargetFragment()).doPositiveClick();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((MoviesFragment)getTargetFragment()).doNegativeClick();
                            }
                        }
                )
                .create();
    }
}
