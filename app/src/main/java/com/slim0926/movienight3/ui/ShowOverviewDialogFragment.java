package com.slim0926.movienight3.ui;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by sue on 8/31/16.
 */

public class ShowOverviewDialogFragment extends DialogFragment {

    private String mTitle;
    private String mOverview;

    public static ShowOverviewDialogFragment newInstance(String title, String message) {
        ShowOverviewDialogFragment dialog = new ShowOverviewDialogFragment();
        dialog.mTitle = title;
        dialog.mOverview = message;
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstancesState) {
        setRetainInstance(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mOverview);
        builder.setTitle(mTitle);
        return builder.create();
    }
}
