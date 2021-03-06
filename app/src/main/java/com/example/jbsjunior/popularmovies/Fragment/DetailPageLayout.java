package com.example.jbsjunior.popularmovies.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jbsjunior.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailPageLayout extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";

    public DetailPageLayout() {
        // Required empty public constructor
    }

    public static DetailPageLayout newInstance(int page) {
        DetailPageLayout fragment = new DetailPageLayout();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_page_layout, container, false);

        TextView txt = (TextView) rootView.findViewById(R.id.page_number_label);
        int page = getArguments().getInt(ARG_PAGE_NUMBER, -1);
        txt.setText(String.format("Page %d", page));

        return rootView;
    }
}
