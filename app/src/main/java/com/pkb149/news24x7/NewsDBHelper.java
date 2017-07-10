package com.pkb149.news24x7;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.pkb149.news24x7.NewsDB.NewsEntry.TABLE_NAME;
import static com.pkb149.news24x7.NewsDB.NewsEntry.TABLE_NAME_SAVED;
import static com.pkb149.news24x7.NewsDB.NewsEntry.TABLE_NAME_TRENDING;

/**
 * Created by CoderGuru on 26-06-2017.
 */

public class NewsDBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "newsDB.db";
    private static final int VERSION = 4;

    NewsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       final String CREATE_TABLE = "CREATE TABLE "  + TABLE_NAME + " (" +
                NewsDB.NewsEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsDB.NewsEntry.COLUMN_CREATEDDATE + " TEXT NOT NULL, " +
                NewsDB.NewsEntry.COLUMN_AUTHOR + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_TITLE + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_DESCRIPTION + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_URL + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_SAVED + " INTEGER DEFAULT 0, " +
                NewsDB.NewsEntry.COLUMN_TABLE + " INTEGER DEFAULT 0, " +
                NewsDB.NewsEntry.COLUMN_URLTOIMAGE   + " TEXT);";

        final String CREATE_TABLE_SAVED = "CREATE TABLE "  + TABLE_NAME_SAVED + " (" +
                NewsDB.NewsEntry._ID                + " INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                NewsDB.NewsEntry.COLUMN_IDFROMNEWS + " INTEGER, " +
                NewsDB.NewsEntry.COLUMN_IDFROMTRENDING   + " INTEGER);";

        final String CREATE_TABLE_TRENDING = "CREATE TABLE "  + TABLE_NAME_TRENDING + " (" +
                NewsDB.NewsEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NewsDB.NewsEntry.COLUMN_CREATEDDATE + " TEXT NOT NULL, " +
                NewsDB.NewsEntry.COLUMN_AUTHOR + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_TITLE + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_DESCRIPTION + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_URL + " TEXT, " +
                NewsDB.NewsEntry.COLUMN_SAVED + " INTEGER DEFAULT 0, " +
                NewsDB.NewsEntry.COLUMN_TABLE + " INTEGER DEFAULT 1, " +
                NewsDB.NewsEntry.COLUMN_URLTOIMAGE   + " TEXT);";

        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_SAVED);
        db.execSQL(CREATE_TABLE_TRENDING);
        db.execSQL("CREATE TABLE IF NOT EXISTS sources ( _id INTEGER AUTO_INCREMENT, source TEXT NOT NULL PRIMARY KEY, status INTEGER DEFAULT 0);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SAVED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TRENDING);
        onCreate(db);
    }
}
