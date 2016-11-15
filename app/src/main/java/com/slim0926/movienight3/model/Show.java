package com.slim0926.movienight3.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.slim0926.movienight3.R;

/**
 * Created by sue on 8/30/16.
 */

public class Show implements Parcelable {

    private String mTitle;
    private String mOverview;
    private int mShowIconImageId;

    private String mReleaseDate;
    private int mVoteCount;
    private double mVoteAverage;
    private double mPopularity;


    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Show() {

    }

    public static int getIconId(String iconString) {
        int iconId = R.drawable.movie_icon_sm;

        if (iconString.equalsIgnoreCase("movie_icon")) {
            iconId = R.drawable.movie_icon_sm;
        } else {
            iconId = R.drawable.tv_icon_sm;
        }
        return iconId;
    }

    public int getShowIconImageId() {
        return mShowIconImageId;
    }

    public void setShowIconImageId(int showIconImageId) {
        mShowIconImageId = showIconImageId;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public int getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(int voteCount) {
        mVoteCount = voteCount;
    }

    public double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        mVoteAverage = voteAverage;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeInt(mVoteCount);
        parcel.writeDouble(mVoteAverage);
        parcel.writeDouble(mPopularity);
    }

    private Show(Parcel in) {
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteCount = in.readInt();
        mVoteAverage = in.readDouble();
        mPopularity = in.readDouble();
    }

    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel parcel) {
            return new Show(parcel);
        }

        @Override
        public Show[] newArray(int i) {
            return new Show[i];
        }
    };


}

