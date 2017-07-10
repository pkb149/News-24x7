package com.pkb149.news24x7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_AUTHOR;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_CREATEDDATE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_DESCRIPTION;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_IDFROMNEWS;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_IDFROMTRENDING;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_SAVED;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_TABLE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_TITLE;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_URL;
import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_URLTOIMAGE;


public class LoadSavedDataAsyncTask extends AsyncTask<Void, Void, List<CardViewData>> {

    public AsyncResponseTab3 deligate = null;
    List<CardViewData> simpleJsonNewsData;
    Context context;
    private SQLiteDatabase mDb;


    public LoadSavedDataAsyncTask(Context context){
        this.context=context;
    }

    @Override
    protected List<CardViewData> doInBackground(Void... params) {
        NewsDBHelper dbHelper = new NewsDBHelper(context);
        mDb = dbHelper.getWritableDatabase();
        simpleJsonNewsData=new ArrayList<CardViewData>();

        Cursor c = mDb.rawQuery("SELECT * FROM saved order by _id desc", null);
        if(c.getCount()!=0) {
            while (c.moveToNext()) {
                CardViewData cardViewData = new CardViewData(Parcel.obtain());
                if(c.isNull(c.getColumnIndex(COLUMN_IDFROMNEWS))){

                    Cursor cursor = mDb.rawQuery("SELECT * FROM trending where _id="+c.getInt(c.getColumnIndex(COLUMN_IDFROMTRENDING)), null);
                    while(cursor.moveToNext()) {
                        cardViewData.setShare(R.drawable.ic_share_black_24dp);
                        cardViewData.setSave(R.drawable.saved);
                        cardViewData.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                        cardViewData.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                        cardViewData.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                        cardViewData.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                        cardViewData.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                        cardViewData.setUrlToImage(cursor.getString(cursor.getColumnIndex(COLUMN_URLTOIMAGE)));
                        cardViewData.setSaved(cursor.getInt(cursor.getColumnIndex(COLUMN_SAVED)));
                        cardViewData.setTable(cursor.getInt(cursor.getColumnIndex(COLUMN_TABLE)));
                        cardViewData.setPublishedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATEDDATE)));
                        //take data from trending table
                    }
                }
                else {
                    Cursor cursor = mDb.rawQuery("SELECT * FROM news where _id="+c.getInt(c.getColumnIndex(COLUMN_IDFROMNEWS)), null);
                    while(cursor.moveToNext()) {
                        cardViewData.setShare(R.drawable.ic_share_black_24dp);
                        cardViewData.setSave(R.drawable.saved);
                        cardViewData.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
                        cardViewData.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                        cardViewData.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                        cardViewData.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                        cardViewData.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));
                        cardViewData.setUrlToImage(cursor.getString(cursor.getColumnIndex(COLUMN_URLTOIMAGE)));
                        cardViewData.setSaved(cursor.getInt(cursor.getColumnIndex(COLUMN_SAVED)));
                        cardViewData.setTable(cursor.getInt(cursor.getColumnIndex(COLUMN_TABLE)));
                        cardViewData.setPublishedAt(cursor.getString(cursor.getColumnIndex(COLUMN_CREATEDDATE)));
                        //take data from trending table
                    }
                }
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
        deligate.processFinish(this);
    }
}

