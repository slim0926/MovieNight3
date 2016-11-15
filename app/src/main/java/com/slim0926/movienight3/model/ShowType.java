package com.slim0926.movienight3.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sue on 10/12/16.
 */

public class ShowType {

    private boolean mIsMovie;
    private boolean mIsTV;

    public boolean isMovie() {
        return mIsMovie;
    }

    public void setMovie(boolean movie) {
        mIsMovie = movie;
    }

    public boolean isTV() {
        return mIsTV;
    }

    public void setTV(boolean TV) {
        mIsTV = TV;
    }
}
