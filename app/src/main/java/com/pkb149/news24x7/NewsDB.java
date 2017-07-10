package com.pkb149.news24x7;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by CoderGuru on 26-06-2017.
 */

public class NewsDB {

    public static final String AUTHORITY = "com.pkb149.news24x7";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "news" directory
    public static final String PATH_TASKS = "news";

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class NewsEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();


        // Task table and column names
        public static final String TABLE_NAME = "news";
        public static final String TABLE_NAME_SAVED = "saved";
        public static final String TABLE_NAME_TRENDING = "trending";

        // Since TaskEntry implements the interface "BaseColumns", it has an automatically produced
        // "_ID" column in addition to the two below


        public static final String COLUMN_CREATEDDATE="createdDate";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_DESCRIPTION= "description";
        public static final String COLUMN_URL= "url";
        public static final String COLUMN_IDFROMNEWS= "idNews";
        public static final String COLUMN_IDFROMTRENDING= "idTrending";
        public static final String COLUMN_URLTOIMAGE= "urlToImage";
        public static final String COLUMN_SAVED= "saved";
        public static final String COLUMN_TABLE= "tableName"; //0  for news 1 for trending

    }
}
