package com.pkb149.news24x7;

/**
 * Created by CoderGuru on 24-06-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder> {


    Context context;
    List<CardViewData> cardViewDatas= new ArrayList<>();
    NewsListItemClickListener listener;
    Integer callingFragment;


    public interface NewsListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecyclerViewAdapter(List<CardViewData> cardViewDatas, Context context, NewsListItemClickListener listener, Integer callingFragment) {
        this.cardViewDatas = cardViewDatas;
        this.context = context;
        this.listener=listener;
        this.callingFragment=callingFragment;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        NewsViewHolder holder = new NewsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.save.setImageResource(cardViewDatas.get(position).getSave());
        holder.share.setImageResource(cardViewDatas.get(position).getShare());

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER);
        Picasso.with(context).load(cardViewDatas.get(position).getUrlToImage())
                .placeholder(R.drawable.progress_animation)

                .into(holder.imageView,new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        holder.heading.setText(cardViewDatas.get(position).getTitle());
        holder.source.setText(cardViewDatas.get(position).getAuthor());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, cardViewDatas.get(position).getUrl());
                view.getContext().startActivity(Intent.createChooser(shareIntent, "Share link using"));
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "ITEM PRESSED = " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                if(cardViewDatas.get(position).getSaved()==0){
                    Log.e("Saved:","False");
                    cardViewDatas.get(position).setSaved(1);
                    cardViewDatas.get(position).setSave(R.drawable.saved);
                    Integer[] integer={cardViewDatas.get(position).getId(),1,callingFragment,cardViewDatas.get(position).getTable()};
                    Log.e("Id:",integer[0].toString());
                    Log.e("time:",cardViewDatas.get(position).getPublishedAt());
                    writeToSavedTableThread asyncTask = new writeToSavedTableThread(view.getContext());
                    asyncTask.execute(integer);
                    notifyDataSetChanged();
                }
                else{
                    Log.e("Saved:","True");
                    cardViewDatas.get(position).setSaved(0);
                    cardViewDatas.get(position).setSave(R.drawable.save);
                    Integer[] integer={cardViewDatas.get(position).getId(),0,callingFragment,cardViewDatas.get(position).getTable()};
                    Log.e("Id:",integer[0].toString());
                    Log.e("time:",cardViewDatas.get(position).getPublishedAt());
                    writeToSavedTableThread asyncTask = new writeToSavedTableThread(view.getContext());
                    asyncTask.execute(integer);
                    notifyDataSetChanged();
                }

            }
        });

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
        cardViewDatas.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<CardViewData>  data) {
        cardViewDatas.clear();
        cardViewDatas.addAll(data);
        notifyDataSetChanged();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cv;
        ImageView share;
        ImageView save;
        ImageView imageView;
        TextView heading;
        TextView source;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            share = (ImageView) itemView.findViewById(R.id.share);
            save = (ImageView) itemView.findViewById(R.id.save);
            imageView=(ImageView) itemView.findViewById(R.id.viewGif);
            heading=(TextView)itemView.findViewById(R.id.heading);
            source=(TextView)itemView.findViewById(R.id.source);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listener.onListItemClick(clickedPosition);
            Uri uri = Uri.parse(cardViewDatas.get(clickedPosition).getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }

}