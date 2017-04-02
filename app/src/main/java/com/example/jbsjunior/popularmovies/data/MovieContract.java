package com.example.jbsjunior.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JBSJunior on 29/03/2017.
 */

public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.jbsjunior.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIES = "movie";
    public static final String PATH_FAVORITE_MOVIES = "favorite_movie";

    /* Inner class that defines the table contents of the location table */
    public static final class MovieEntry implements BaseColumns {

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Table name
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_MOVIE_TITLE = "title";

        public static final String COLUMN_MOVIE_POSTER_PATH = "posterPath";

        public static final String COLUMN_MOVIE_ADULT = "adult";

        public static final String COLUMN_MOVIE_POPULARITY = "popularity";

        public static final String COLUMN_MOVIE_VOTE_COUNT = "vote_count";

        public static final String COLUMN_MOVIE_HAS_VIDEO = "video";

        public static final String COLUMN_MOVIE_ORIGINAL_LANGUAGE = "original_language";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_MOVIE_FAVORITE = "favorite";


        public static Uri buildWeatherUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
