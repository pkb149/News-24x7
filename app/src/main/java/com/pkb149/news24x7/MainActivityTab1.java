package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 24-06-2017.
 */

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
import android.support.v4.widget.ContentLoadingProgressBar;
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

public class MainActivityTab1 extends Fragment implements AsyncResponse, RecyclerViewAdapter.NewsListItemClickListener, InterfaceToUpdateUI{
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerViewAdapter.NewsListItemClickListener listener;
    RecyclerView recyclerView;
    List<CardViewData> data;
    RecyclerViewAdapter adapter;
    public AsyncResponse asyncResponse;
    public ProgressBar loader;
    public Context context;
    public static final String sortBy="SORT_BY";
    public static final String latest="latest";
    private static int pageNo;
    public static final String CUSTOM_INTENT = "com.pkb149.news24x7.intent.action.UPDATE_HOME";


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo =  connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private MyReceiver receiver;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_main_tab1,container,false);
        data = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        loader=(ProgressBar) view.findViewById(R.id.loader);
        context=view.getContext();
        listener=this;

        NewsDataTask asyncTask = new NewsDataTask(getContext());
        asyncTask.delegate = this;
        asyncResponse=this;

        receiver = new MyReceiver(new Handler()); // Create the receiver
        receiver.interfaceToUpdateUI=this;
        getContext().registerReceiver(receiver, new IntentFilter(CUSTOM_INTENT));


        if(isNetworkAvailable()){
            asyncTask.execute("news");
            Log.e(this.toString(),"1");
            Intent intent=new Intent(getContext(),LoadDataBasedOnSelection.class);
            intent.putExtra(sortBy, latest);
            getActivity().startService(intent);
            Log.e(this.toString(),"2");
        }
        else{
            Toast.makeText(getContext(),"Internet connectivity is not available loading local data.",Toast.LENGTH_SHORT).show();
            asyncTask.execute("news");
        }

        adapter = new RecyclerViewAdapter(data, getContext(),this,1);
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
                /*pageNo=page;
                Log.e("Page","**************************************************");
                Log.e("Page Number: ",Integer.toString(page));
                NewsDataTask asyncTask = new NewsDataTask(context,page,data);
                asyncTask.delegate = asyncResponse;
                asyncTask.execute("news");
                */
            }
        };
        scrollListener=endlessRecyclerViewScrollListener;

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                endlessRecyclerViewScrollListener.resetState();
                Intent intent=new Intent(getContext(),LoadDataBasedOnSelection.class);
                intent.putExtra(sortBy, latest);
                getActivity().startService(intent);
            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void processFinish(NewsDataTask asyncTask) {
        data=asyncTask.simpleJsonNewsData;
        if(data.isEmpty()){
            Toast.makeText(getContext(),"No Local Data, Please Wait till we load data from Internet.",Toast.LENGTH_SHORT).show();
            loader.setVisibility(View.VISIBLE);
        }
        else{
            loader.setVisibility(View.INVISIBLE);
            Log.e("Calling Add all","Addall()");
            adapter.addAll(data);
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }


    @Override
    public void updateUI(Context context) {
        Log.e("BR","called");
        NewsDataTask asyncTask = new NewsDataTask(context);
        asyncTask.delegate = this;
        asyncTask.execute("news");
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
        context.unregisterReceiver(receiver);
        }
        catch (Exception e){
           // Log.d("Some tag", Log.getStackTraceString(e.getCause().getCause()));
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if(asyncResponse!=null) {
              NewsDataTask asyncTask = new NewsDataTask(context);
                asyncTask.delegate = asyncResponse;
                asyncTask.execute("news");
            }
        }
    }
}