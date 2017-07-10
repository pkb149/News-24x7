package com.pkb149.news24x7;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */






public class MainActivityTab3 extends Fragment implements AsyncResponseTab3, RecyclerViewAdapter.NewsListItemClickListener{
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerViewAdapter.NewsListItemClickListener listener;
    RecyclerView recyclerView;
    List<CardViewData> data;
    RecyclerViewAdapter adapter;
    Context context;
    public AsyncResponseTab3 asyncResponse;
    static Boolean noDataVariable=false;

    public MainActivityTab3() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_main_tab1,container,false);
        data = new ArrayList<>();
        LoadSavedDataAsyncTask asyncTask = new LoadSavedDataAsyncTask(getContext());
        asyncTask.deligate=this;
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        listener=this;
        asyncResponse=this;
        context=getContext();

        asyncTask.execute();


        adapter = new RecyclerViewAdapter(data, view.getContext(),this,3);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        final EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener= new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //TODO
                //last thing to do..
                //fetch only 50 records from DB at a time to insert into list
                // and load more recored here.. 50 each

            }
        };
        scrollListener=endlessRecyclerViewScrollListener;

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO
                endlessRecyclerViewScrollListener.resetState();
                LoadSavedDataAsyncTask asyncTask = new LoadSavedDataAsyncTask(getContext());
                asyncTask.deligate = asyncResponse;
                asyncTask.execute();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void processFinish(LoadSavedDataAsyncTask asyncTask) {
        data=asyncTask.simpleJsonNewsData;
        if(data.isEmpty()){
            adapter.clear();
            //addAll clears and adds all and it calles notifydatasetchanged()
        }
        else{
            adapter.addAll(data);
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            SQLiteDatabase mDb;
            NewsDBHelper dbHelper = new NewsDBHelper(context);
            mDb = dbHelper.getReadableDatabase();
            Cursor c=mDb.rawQuery("SELECT * from saved",null);
            if(c.getCount()==0){
                Toast.makeText(context,"No saved News",Toast.LENGTH_SHORT).show();
                LoadSavedDataAsyncTask asyncTask = new LoadSavedDataAsyncTask(getContext());
                asyncTask.deligate = asyncResponse;
                asyncTask.execute();
            }
            else{
                LoadSavedDataAsyncTask asyncTask = new LoadSavedDataAsyncTask(getContext());
                asyncTask.deligate = asyncResponse;
                asyncTask.execute();
            }
            mDb.close();
        }
    }
}

