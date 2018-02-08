package com.pkb149.news24x7;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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

import static com.pkb149.news24x7.MainActivityTab1.sortBy;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityTab2 extends Fragment implements InterfaceToUpdateUiOfTrending, RecyclerViewAdapter.NewsListItemClickListener, AsyncResponse{
    public static final String TRENDING_INTENT = "com.pkb149.news24x7.intent.action.UPDATE_TRENDING";
    public static final String popular="top";
    private MyReceiver receiver;

    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    RecyclerViewAdapter.NewsListItemClickListener listener;
    RecyclerView recyclerView;
    List<CardViewData> data;
    RecyclerViewAdapter adapter;
    public AsyncResponse asyncResponse;
    ProgressBar loader;
    Context context;
    int pageNo;

    public MainActivityTab2() {
        // Required empty public constructor
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo =  connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.activity_main_tab1, container, false);
        data = new ArrayList<>();
        listener=this;
        context=view.getContext();
        asyncResponse=this;
        loader=(ProgressBar) view.findViewById(R.id.loader);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        receiver = new MyReceiver(new Handler()); // Create the receiver
        receiver.interfaceToUpdateUiOfTrending=this;
        getContext().registerReceiver(receiver, new IntentFilter(TRENDING_INTENT));


        NewsDataTask asyncTask = new NewsDataTask(getContext());
        asyncTask.delegate = this;
        asyncResponse=this;


        if(isNetworkAvailable()){
            asyncTask.execute("trending");
            Log.e(this.toString(),"1");
            Intent intent=new Intent(getContext(),LoadDataBasedOnSelection.class);
            intent.putExtra(sortBy, popular);
            getActivity().startService(intent);
            Log.e(this.toString(),"2");
        }
        else{
            Toast.makeText(getContext(),"Internet connectivity is not available loading local data.",Toast.LENGTH_SHORT).show();
            asyncTask.execute("trending");
        }

        adapter = new RecyclerViewAdapter(data, getContext(),this,2);
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
                NewsDataTask asyncTask = new NewsDataTask(context,page,data);
                asyncTask.delegate = asyncResponse;
                asyncTask.execute("trending");
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
                intent.putExtra(sortBy, popular);
                getActivity().startService(intent);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        recyclerView.addOnScrollListener(scrollListener);

        return view;
    }

    @Override
    public void updateUI(Context context) {
        Log.e("MainActivity2 Boradca","called");
        NewsDataTask asyncTask = new NewsDataTask(context);
        asyncTask.delegate = this;
        asyncTask.execute("trending");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    @Override
    public void processFinish(NewsDataTask asyncTask) {
        data=asyncTask.simpleJsonNewsData;
        if(data.isEmpty()){
            loader.setVisibility(View.VISIBLE);
        }
        else{
            adapter.addAll(data);
            loader.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            context.unregisterReceiver(receiver);
        }
        catch (Exception e){
            //Log.d("Some tag", Log.getStackTraceString(e.getCause().getCause()));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            NewsDataTask asyncTask = new NewsDataTask(getContext());
            asyncTask.delegate = this;
            asyncTask.execute("trending");
        }
    }

}
