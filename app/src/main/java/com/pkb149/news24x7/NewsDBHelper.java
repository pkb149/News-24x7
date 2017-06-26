package com.pkb149.news24x7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CoderGuru on 26-06-2017.
 */

public class NewsDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "newsDB.db";
    private static final int VERSION = 1;

    NewsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + NewsDB.NewsEntry.TABLE_NAME + " (" +
                NewsDB.NewsEntry._ID                + " INTEGER, " +
                NewsDB.NewsEntry.COLUMN_CREATEDDATE + " DATE PRIMARY KEY NOT NULL, " +
                NewsDB.NewsEntry.COLUMN_AUTHOR + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_TITLE + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_DESCRIPTION + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_URL + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_URLTOIMAGE   + " TEXT);";

        db.execSQL(CREATE_TABLE);
        db.execSQL("CREATE TABLE sources ( _id INTEGER AUTO_INCREMENT, source TEXT NOT NULL PRIMARY KEY, status INTEGER DEFAULT 0);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }
}
