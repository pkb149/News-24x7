package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 24-06-2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.List;


public class MainActivityTab1 extends Fragment implements AsyncResponse, RecyclerViewAdapter.NewsListItemClickListener{
    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    RecyclerViewAdapter.NewsListItemClickListener listener;
    RecyclerView recyclerView;
    List<CardViewData> data;
    RecyclerViewAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_main_tab1,container,false);
        data = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        listener=this;
        NewsDataTask asyncTask = new NewsDataTask(getContext());
        asyncTask.delegate = this;
        String latest="https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=573168e5e81b4dbfb64b49418d171e2b";
        asyncTask.execute(latest);

        adapter = new RecyclerViewAdapter(data, getActivity(),this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = (ProgressBar) container.findViewById(R.id.progress);
//        progressBar.setVisibility(View.VISIBLE);

        final EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener= new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                // Send an API request to retrieve appropriate paginated data
                //  --> Send the request including an offset value (i.e `page`) as a query parameter.
                //  --> Deserialize and construct new model objects from the API response
                //  --> Append the new data objects to the existing set of items inside the array of items
                //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()
                //TODO
                //fillWithData(page);
                //start thread again
                view.getAdapter().notifyDataSetChanged();
            }
        };
        scrollListener=endlessRecyclerViewScrollListener;


        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //TODO
               // adapter.clear();
                //endlessRecyclerViewScrollListener.resetState();
                //fillWithData(0);
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    @Override
    public void processFinish(NewsDataTask asyncTask) {
        //TODO
        //set the UI
        //make progress bar invisible
        // set adapter
        data=asyncTask.simpleJsonNewsData;
        adapter.addAll(data);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Start another Activity
        //TODO
    }
}