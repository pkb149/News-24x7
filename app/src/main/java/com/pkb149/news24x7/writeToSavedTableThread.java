package com.pkb149.news24x7;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.pkb149.news24x7.NewsDB.NewsEntry.COLUMN_IDFROMTRENDING;
import static com.pkb149.news24x7.NewsDB.NewsEntry.TABLE_NAME_TRENDING;


/**
 * Created by CoderGuru on 08-07-2017.
 */


public class writeToSavedTableThread extends AsyncTask<Integer[], Void, List<CardViewData>> {

    //public AsyncResponse delegate = null;
    List<CardViewData> simpleJsonNewsData;
    Context context;
    private SQLiteDatabase mDb;

    public writeToSavedTableThread(Context context){
        this.context=context;
    }

    @Override
    protected List<CardViewData> doInBackground(Integer[]... params) {
        Integer[] id=params[0];
        Integer callingFragment=id[2];
        Integer table=id[3];
        NewsDBHelper dbHelper = new NewsDBHelper(context);
        mDb = dbHelper.getWritableDatabase();
        simpleJsonNewsData=new ArrayList<CardViewData>();
        if(callingFragment==1) {
            String strSQL = "UPDATE news SET saved = " + id[1] + " WHERE _id = " + id[0];
            Log.e("Query is: ", strSQL);
            mDb.execSQL(strSQL);

            if (id[1] == 1) {

                ContentValues cv = new ContentValues();
                cv.put("idNews", id[0]);
                mDb.insert("saved", null, cv);
            } else {
                String query = "DELETE from saved where idNews=" + id[0];
                mDb.execSQL(query);
            }
        }
        else if(callingFragment==2){
            String strSQL = "UPDATE trending SET saved = " + id[1] + " WHERE _id = " + id[0];
            Log.e("Query is: ", strSQL);
            mDb.execSQL(strSQL);

            if (id[1] == 1) {
                ContentValues cv = new ContentValues();
                cv.put("idTrending", id[0]);
                mDb.insert("saved", null, cv);
            } else {
                String query = "DELETE from saved where idTrending=" + id[0];
                mDb.execSQL(query);
            }
        }
        else if(callingFragment==3){
            if (id[1] == 1) {
                if(table==0) {
                    ContentValues cv = new ContentValues();
                    cv.put("idNews", id[0]);
                    mDb.insert("saved", null, cv);

                    String strSQL = "UPDATE news SET saved = " + 1 + " WHERE _id = " + id[0];
                    Log.e("Query is: ", strSQL);
                    mDb.execSQL(strSQL);
                }
                else{
                    ContentValues cv = new ContentValues();
                    cv.put("idTrending", id[0]);
                    mDb.insert("saved", null, cv);

                    String strSQL = "UPDATE trending SET saved = " + 0 + " WHERE _id = " + id[0];
                    Log.e("Query is: ", strSQL);
                    mDb.execSQL(strSQL);
                }
            } else {
                Cursor check = mDb.rawQuery("SELECT * FROM saved where idNews =" + id[0], null);
                if(check.getCount() != 0){
                    String query = "DELETE from saved where idNews=" + id[0];
                    mDb.execSQL(query);

                    String strSQL = "UPDATE news SET saved = " + 0 + " WHERE _id = " + id[0];
                    Log.e("Query is: ", strSQL);
                    mDb.execSQL(strSQL);

                }
                else{
                    String query = "DELETE from saved where idTrending=" + id[0];
                    mDb.execSQL(query);

                    String strSQL = "UPDATE trending SET saved = " + 0 + " WHERE _id = " + id[0];
                    Log.e("Query is: ", strSQL);
                    mDb.execSQL(strSQL);
                }
            }
        }

        return  null;
    }

    @Override
    protected void onPostExecute(List<CardViewData> cardViewDatas) {
    }
}


