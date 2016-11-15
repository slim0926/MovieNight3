package com.slim0926.movienight3.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sue on 8/2/16.
 */

public class Filters implements Parcelable {
    private float mRatingThreshold;
    private int mMinNumOfRatings;
    private String mMinDate;
    private String mMaxDate;

    public Filters() {

    }

    public float getRatingThreshold() {
        return mRatingThreshold;
    }

    public void setRatingThreshold(float ratingThreshold) {
        mRatingThreshold = ratingThreshold;
    }

    public int getMinNumOfRatings() {
        return mMinNumOfRatings;
    }

    public void setMinNumOfRatings(int minNumOfRatings) {
        mMinNumOfRatings = minNumOfRatings;
    }

    public String getMinDate() {
        return mMinDate;
    }

    public void setMinDate(String minDate) {
        mMinDate = minDate;
    }

    public String getMaxDate() {
        return mMaxDate;
    }

    public void setMaxDate(String maxDate) {
        mMaxDate = maxDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(mRatingThreshold);
        parcel.writeInt(mMinNumOfRatings);
        parcel.writeString(mMinDate);
        parcel.writeString(mMaxDate);
    }

    private Filters(Parcel in) {
        mRatingThreshold = in.readFloat();
        mMinNumOfRatings = in.readInt();
        mMinDate = in.readString();
        mMaxDate = in.readString();
    }

    public static final Creator<Filters> CREATOR = new Creator<Filters>() {
        @Override
        public Filters createFromParcel(Parcel parcel) {
            return new Filters(parcel);
        }

        @Override
        public Filters[] newArray(int i) {
            return new Filters[0];
        }
    };
}
