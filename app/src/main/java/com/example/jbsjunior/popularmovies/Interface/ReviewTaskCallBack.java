package com.example.jbsjunior.popularmovies.Interface;

import com.example.jbsjunior.popularmovies.Model.Review;

import java.util.List;

/**
 * Created by junior on 14/06/17.
 */

public interface ReviewTaskCallBack {
    void onTaskCompleted(List<Review> review);
}
