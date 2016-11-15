package com.slim0926.movienight3.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slim0926.movienight3.R;
import com.slim0926.movienight3.model.Show;
import com.slim0926.movienight3.ui.ShowOverviewDialogFragment;

/**
 * Created by sue on 8/30/16.
 */

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder> {

    private Show[] mShows;
    private Context mContext;

    public ShowAdapter(Context context, Show[] shows) {
        mContext = context;
        mShows = shows;
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_list_item, parent, false);
        ShowViewHolder viewHolder = new ShowViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShowViewHolder holder, int position) {
        holder.bindShow(mShows[position]);
    }

    @Override
    public int getItemCount() {
        return mShows.length;
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mShowIconImageView;
        public TextView mShowTitleLabel;

        public ShowViewHolder(View itemView) {
            super(itemView);

            mShowIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            mShowTitleLabel = (TextView) itemView.findViewById(R.id.showTitleLabel);

            itemView.setOnClickListener(this);
        }

        public void bindShow(Show show) {
            mShowIconImageView.setImageResource(show.getShowIconImageId());
            mShowTitleLabel.setText(show.getTitle());
        }

        @Override
        public void onClick(View view) {
            String showTitle;
            showTitle = mShows[getAdapterPosition()].getTitle();
            String showOverview;
            showOverview = mShows[getAdapterPosition()].getOverview();
            //Toast.makeText(mContext, showOverview, Toast.LENGTH_LONG).show();


            ShowOverviewDialogFragment newFragment = ShowOverviewDialogFragment.newInstance(showTitle, showOverview);
            newFragment.show(((Activity) mContext).getFragmentManager(), "overview_dialog");
        }
    }
}
