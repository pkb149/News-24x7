package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 25-06-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_AUTHOR;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_CREATEDDATE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_DESCRIPTION;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_SAVED;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_TABLE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_TITLE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_URL;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_URLTOIMAGE;
import static com.pkb149.news24x7.Utility.getResponseFromHttpUrl;

public class NewsDataTask extends AsyncTask<String, Void, List<CardViewData>> {

    public AsyncResponse delegate = null;
    List<CardViewData> simpleJsonNewsData;
    Context context;
    private SQLiteDatabase mDb;


    public NewsDataTask(Context context){
        this.context=context;
    }

    @Override
    protected List<CardViewData> doInBackground(String... params) {
        NewsDBHelper dbHelper = new NewsDBHelper(context);
        mDb = dbHelper.getWritableDatabase();
        simpleJsonNewsData=new ArrayList<CardViewData>();
        String str=params[0];
        Cursor c = mDb.rawQuery("SELECT * FROM "+str+" order by createdDate desc", null);
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                CardViewData cardViewData = new CardViewData(Parcel.obtain());
                cardViewData.setShare(R.drawable.ic_share_black_24dp);
                if(c.getInt(c.getColumnIndex(COLUMN_SAVED))==0){
                    cardViewData.setSave(R.drawable.save);
                }
                else{
                    cardViewData.setSave(R.drawable.saved);
                }
                cardViewData.setId(c.getInt(c.getColumnIndex(_ID)));
                cardViewData.setAuthor(c.getString(c.getColumnIndex(COLUMN_AUTHOR)));
                cardViewData.setTitle(c.getString(c.getColumnIndex(COLUMN_TITLE)));
                cardViewData.setDescription(c.getString(c.getColumnIndex(COLUMN_DESCRIPTION)));
                cardViewData.setUrl(c.getString(c.getColumnIndex(COLUMN_URL)));
                cardViewData.setUrlToImage(c.getString(c.getColumnIndex(COLUMN_URLTOIMAGE)));
                cardViewData.setSaved(c.getInt(c.getColumnIndex(COLUMN_SAVED)));
                cardViewData.setTable(c.getInt(c.getColumnIndex(COLUMN_TABLE)));
                cardViewData.setPublishedAt(c.getString(c.getColumnIndex(COLUMN_CREATEDDATE)));
                simpleJsonNewsData.add(cardViewData);
            }
            mDb.close();
            return simpleJsonNewsData;
        }
        else{
            return null;
        }

    }

    @Override
    protected void onPostExecute(List<CardViewData> cardViewDatas) {
        delegate.processFinish(this);
    }
}

