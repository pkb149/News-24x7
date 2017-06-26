package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 25-06-2017.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.pkb149.news24x7.Utility.getResponseFromHttpUrl;

public class NewsDataTask extends AsyncTask<String, Void, List<CardViewData>> {

    public AsyncResponse delegate = null;
    List<CardViewData> simpleJsonNewsData;
    Context context;


    public NewsDataTask(Context context){
        this.context=context;
    }

    @Override
    protected List<CardViewData> doInBackground(String... sortBy) {
        if (sortBy.length == 0) {
            return null;
        }
        String preference = sortBy[0];
        URL requestURL=null;
        try {
            requestURL = new URL(preference);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (requestURL == null || requestURL.equals("")) {
            Log.w(this.toString(), "URL is NULL");
        } else {
            Log.w(this.toString(), requestURL.toString());
        }
        try {
            String Response = getResponseFromHttpUrl(requestURL);
            simpleJsonNewsData = parseJSON.getSimpleMovieStringsFromJson(context, Response);
            return simpleJsonNewsData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<CardViewData> cardViewDatas) {
        delegate.processFinish(this);
    }
}

