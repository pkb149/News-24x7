package com.pkb149.news24x7;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.pkb149.news24x7.MainActivityTab1.CUSTOM_INTENT;
import static com.pkb149.news24x7.MainActivityTab1.latest;
import static com.pkb149.news24x7.MainActivityTab1.sortBy;
import static com.pkb149.news24x7.MainActivityTab2.TRENDING_INTENT;
import static com.pkb149.news24x7.MainActivityTab2.popular;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_AUTHOR;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_CREATEDDATE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_DESCRIPTION;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_TITLE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_URL;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_URLTOIMAGE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.TABLE_NAME;
import static com.pkb149.news24x7.NewsDB.NewsEntry.TABLE_NAME_TRENDING;
import static com.pkb149.news24x7.Utility.getResponseFromHttpUrl;

/**
 * Created by CoderGuru on 03-07-2017.
 */

public class LoadDataBasedOnSelection extends IntentService {
    private SQLiteDatabase mDb;
    public LoadDataBasedOnSelection() {
        super("LoadDataBasedOnSelection");
    }
    String sorting;

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo =  connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (isNetworkAvailable()) {
            sorting = intent.getExtras().getString(sortBy);
            Log.e(getApplicationContext().toString(), "Service Started");
            //query the table
            NewsDBHelper dbHelper = new NewsDBHelper(this);
            URL requestURL;
            List<CardViewData> simpleJsonNewsData;
            mDb = dbHelper.getWritableDatabase();
            Cursor c = mDb.rawQuery("SELECT source FROM sources WHERE status = 1", null);
            while (c.moveToNext()) {
                Log.e(getApplicationContext().toString(), "Data Loaded from DB, Now loading news from internet");
                String data = c.getString(c.getColumnIndex("source"));
                String url = "https://newsapi.org/v1/articles?source=" + data + "&sortBy=" + sorting + "&apiKey=573168e5e81b4dbfb64b49418d171e2b";
                Log.e(getApplicationContext().toString(), url);
                try {
                    requestURL = new URL(url);
                    Log.e(getApplicationContext().toString(), "Starting http request");
                    String Response = getResponseFromHttpUrl(requestURL);
                    simpleJsonNewsData = parseJSON.getSimpleMovieStringsFromJson(this, Response);
                    for (int i = 0; i < simpleJsonNewsData.size(); i++) {
                        Log.e(getApplicationContext().toString(), "Got the data, writing into local DB");
                        ContentValues cv = new ContentValues();
                        TimeZone tz = TimeZone.getDefault();
                        Calendar cal = Calendar.getInstance(tz);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        sdf.setCalendar(cal);
                        String str = simpleJsonNewsData.get(i).getPublishedAt();
                        if (str.contains(".")) {
                            cal.setTime(sdf2.parse(str));
                        } else {
                            cal.setTime(sdf.parse(str));
                        }
                        Date date = cal.getTime();
                        //java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        String dateStr = sdf.format(date);
                        cv.put(COLUMN_CREATEDDATE, dateStr);
                        cv.put(COLUMN_AUTHOR, simpleJsonNewsData.get(i).getAuthor());
                        cv.put(COLUMN_TITLE, simpleJsonNewsData.get(i).getTitle());
                        cv.put(COLUMN_DESCRIPTION, simpleJsonNewsData.get(i).getDescription());
                        cv.put(COLUMN_URL, simpleJsonNewsData.get(i).getUrl());
                        cv.put(COLUMN_URLTOIMAGE, simpleJsonNewsData.get(i).getUrlToImage());
                        if (sorting.equals(latest)) {
                            Cursor check = mDb.rawQuery("SELECT * FROM news where createdDate ='" + dateStr + "'", null);

                            if (check.getCount() == 0) {
                                mDb.insert(TABLE_NAME, null, cv);
                                Log.e(getApplicationContext().toString(), "Written in Local DB");
                            }
                        } else if (sorting.equals(popular)) {
                            //code to update trending table
                            Cursor check = mDb.rawQuery("SELECT * FROM trending where createdDate ='" + dateStr + "'", null);
                            if (check.getCount() == 0) {
                                mDb.insert(TABLE_NAME_TRENDING, null, cv);
                                Log.e(getApplicationContext().toString(), "Written in Local DB");
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            mDb.close();
            c.close();

            if (sorting.equals(latest)) {
                Intent abc = new Intent();
                abc.setAction(CUSTOM_INTENT);
                getApplicationContext().sendBroadcast(abc);
            } else if (sorting.equals(popular)) {
                Intent abc = new Intent();
                abc.setAction(TRENDING_INTENT);
                getApplicationContext().sendBroadcast(abc);
            }

        }
    }
}
