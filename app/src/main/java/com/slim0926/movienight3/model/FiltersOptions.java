package com.slim0926.movienight3.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sue on 8/29/16.
 */

public class FiltersOptions {

    private String[] mRatingThresholdOptions;
    public static final int STARTING_MIN_NUM_OF_RATINGS = 100;
    private ArrayList<String> mReleaseYears;
    private String[] mReleaseMonths;
    private String[] mReleaseDays;

    public FiltersOptions() {
        mRatingThresholdOptions = new String[10];
        mReleaseYears = new ArrayList<String>();
        mReleaseMonths = new String[12];
        mReleaseDays = new String[31];

        for (int i = 0, j = 9; i < 10; i++, j--) {
            mRatingThresholdOptions[i] = (j + 1.0f) + "";
        }

        DateFormat dateFormatYear = new SimpleDateFormat("yyyy");

        Date date = new Date();
        String strThisYear = dateFormatYear.format(date);

        int thisYear = Integer.parseInt(strThisYear);

        for (int i = thisYear; i >= 1900; i--) {
            mReleaseYears.add(i + "");
        }

        for (int i = 0; i < 12; i++) {
            if (i < 9) {
                mReleaseMonths[i] = "0" + (i + 1);
            } else {
                mReleaseMonths[i] = "" + (i + 1);
            }
        }

        for (int i = 0; i < 31; i++) {
            if (i < 9) {
                mReleaseDays[i] = "0" + (i + 1);
            } else {
                mReleaseDays[i] = "" + (i + 1);
            }
        }

    }

    public String[] getRatingThresholdOptions() {
        return mRatingThresholdOptions;
    }

    public ArrayList getReleaseYears() {
        return mReleaseYears;
    }

    public String[] getReleaseMonths() {
        return mReleaseMonths;
    }

    public String[] getReleaseDays() {
        return mReleaseDays;
    }
}
