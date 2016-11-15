package com.slim0926.movienight3.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sue on 9/1/16.
 */

public class Genre implements Parcelable {

    private String mGenreName;
    private int mGenreID;

    public Genre() {

    }

    public String getGenreName() {
        return mGenreName;
    }

    public void setGenreName(String genreName) {
        mGenreName = genreName;
    }

    public int getGenreID() {
        return mGenreID;
    }

    public void setGenreID(int genreID) {
        mGenreID = genreID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mGenreName);
        parcel.writeInt(mGenreID);
    }

    private Genre(Parcel in) {
        mGenreName = in.readString();
        mGenreID = in.readInt();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel parcel) {
            return new Genre(parcel);
        }

        @Override
        public Genre[] newArray(int i) {
            return new Genre[i];
        }
    };
}
