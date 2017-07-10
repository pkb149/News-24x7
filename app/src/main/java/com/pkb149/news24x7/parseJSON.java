package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 25-06-2017.
 */

import android.content.Context;
import android.os.Parcel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public final class parseJSON {


    public static List<CardViewData> getSimpleMovieStringsFromJson(Context context, String newsJsonStr)
            throws JSONException {

        JSONObject newsJson = new JSONObject(newsJsonStr);
        JSONArray newsArray = newsJson.getJSONArray("articles");
        List<CardViewData> parsedNewsData = new ArrayList<CardViewData>();
        if(newsJson.getString("status").equals("ok")){

        for (int i = 0; i < newsArray.length(); i++) {
            JSONObject news = newsArray.getJSONObject(i);
            if (news != null) {
                CardViewData data = new CardViewData(Parcel.obtain());
                data.setShare(R.drawable.ic_share_black_24dp);
                data.setSave(R.drawable.save);
                if(news.getString("author").equals("null")){
                    data.setAuthor(newsJson.getString("source"));
                }
                else{
                    data.setAuthor(news.getString("author"));
                }
                data.setTitle(news.getString("title"));
                data.setDescription(news.getString("description"));
                data.setUrl(news.getString("url"));
                data.setUrlToImage(news.getString("urlToImage"));
                data.setPublishedAt(news.getString("publishedAt"));
                parsedNewsData.add(data);
            }
        }
    }
        return parsedNewsData;
    }
}