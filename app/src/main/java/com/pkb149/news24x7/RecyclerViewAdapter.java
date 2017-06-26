package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 24-06-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder> {


    Context context;
    List<CardViewData> cardViewDatas= new ArrayList<>();
    NewsListItemClickListener listener;


    public interface NewsListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecyclerViewAdapter(List<CardViewData> cardViewDatas, Context context, NewsListItemClickListener listener) {
        this.cardViewDatas = cardViewDatas;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        NewsViewHolder holder = new NewsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.save.setImageResource(cardViewDatas.get(position).getSave());
        holder.share.setImageResource(cardViewDatas.get(position).getShare());
        //holder.thumbsDown.setImageResource(list.get(position).thumbsDown);
       // holder.share.setImageResource(list.get(position).share);
        //holder.save.setImageResource(list.get(position).save);
        //final ProgressBar progressBar = holder.progressBar;
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, cardViewDatas.get(position).getUrl());
                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
        //TODO

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return cardViewDatas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, CardViewData data) {
        //list.add(position, data);
        //cardViewData.notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(CardViewData data) {
        //int position = list.indexOf(data);
        //list.remove(position);
       // notifyItemRemoved(position);
    }
    public void clear() {
        //list.clear();
        //notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List data) {
        cardViewDatas.addAll(data);
        notifyDataSetChanged();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        ImageView share;
        ImageView save;
        ImageView imageView;
        ProgressBar progressBar;

        NewsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            share = (ImageView) itemView.findViewById(R.id.share);
            save = (ImageView) itemView.findViewById(R.id.save);
            imageView=(ImageView) itemView.findViewById(R.id.viewGif);
            progressBar=(ProgressBar) itemView.findViewById(R.id.progress);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
        }
    }

}