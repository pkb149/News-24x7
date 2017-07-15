package com.pkb149.news24x7;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by CoderGuru on 24-06-2017.
 */

public class CardViewData implements Parcelable{

    private int share;
    private int save;
    private int id=0;
    private String author=null;
    private String title=null;
    private String description=null;
    private String url=null;
    private String urlToImage=null;
    private int saved;
    private int table;
    private String publishedAt=null;

    CardViewData(Parcel in) {
        this.share=in.readInt();
        this.save=in.readInt();
        this.id = in.readInt();
        this.author = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.urlToImage = in.readString();
        this.saved = in.readInt();
        this.table = in.readInt();
        this.publishedAt=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(share);
        dest.writeInt(save);
        dest.writeInt(id);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeInt(saved);
        dest.writeInt(table);
        dest.writeString(publishedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CardViewData> CREATOR = new Parcelable.Creator<CardViewData>() {

        @Override
        public CardViewData createFromParcel(Parcel source) {
            return new CardViewData(source);
        }

        @Override
        public CardViewData[] newArray(int size) {
            return new CardViewData[size];
        }
    };

    public int getShare() {
        return share;
    }

    public int getSave() {
        return save;
    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public int getSaved() {
        return saved;
    }

    public int getTable() {
        return table;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSave(int save) {
        this.save = save;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSaved(int saved) {
        this.saved = saved;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

}
