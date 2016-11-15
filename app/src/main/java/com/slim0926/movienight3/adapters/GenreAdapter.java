package com.slim0926.movienight3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.slim0926.movienight3.R;
import com.slim0926.movienight3.model.Genre;

/**
 * Created by sue on 9/1/16.
 */

public class GenreAdapter extends BaseAdapter {

    private Context mContext;
    private Genre[] mGenres;
    private boolean[] mIsChecked;

    public GenreAdapter(Context context, Genre[] genres) {
        mContext = context;
        mGenres = genres;

        mIsChecked = new boolean[genres.length];
    }

    public boolean[] getIsChecked() {
        return mIsChecked;
    }

    @Override
    public int getCount() {
        return mGenres.length;
    }

    @Override
    public Object getItem(int position) {
        return mGenres[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.genre_list_item, null);
            holder = new ViewHolder();
            holder.genreCheckBox = (CheckBox) convertView.findViewById(R.id.genreCheckBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Genre genre = mGenres[position];

        holder.genreCheckBox.setText(genre.getGenreName());

        holder.genreCheckBox.setChecked(mIsChecked[position]);

        holder.genreCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    mIsChecked[position] = true;
                } else {
                    mIsChecked[position] = false;
                }
            }
        });


        return convertView;
    }

    private static class ViewHolder {
        CheckBox genreCheckBox;
    }
}
