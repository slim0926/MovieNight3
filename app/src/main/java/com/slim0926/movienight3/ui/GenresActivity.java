package com.slim0926.movienight3.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.slim0926.movienight3.R;
import com.slim0926.movienight3.adapters.GenreAdapter;
import com.slim0926.movienight3.model.Genre;
import com.slim0926.movienight3.model.Genres;
import com.slim0926.movienight3.model.ShowType;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.slim0926.movienight3.ui.MainActivity.IS_MOVIE;
import static com.slim0926.movienight3.ui.MainActivity.IS_TV;

/**
 * Created by sue on 10/12/16.
 */

public class GenresActivity extends ListActivity {

    public static final String PICKED_MOVIE_GENRES = "PICKED_MOVIE_GENRES";
    public static final String PICKED_TV_GENRES = "PICKED_TV_GENRES";
    public static final String MOVIE_TYPE = "MOVIE_TYPE";
    public static final String TV_TYPE = "TV_TYPE";

    private Genre[] mMovieGenres;
    private Genre[] mTVGenres;
    private GenreAdapter mMovieGenreAdapter;
    private GenreAdapter mTVGenreAdapter;
    private ArrayList<Genre> mMovieGenresArrayList = new ArrayList<>();
    private ArrayList<Genre> mTVGenresArrayList = new ArrayList<>();
    private Genres mUserMovieGenreChoices;
    private Genres mUserTVGenreChoices;
    private ShowType mShowTypeTemp;
    private ShowType mShowType;
    private Intent intent;

    @BindView(R.id.genresLabel) TextView mGenresLabel;
    @BindView(R.id.chooseButton) Button mChooseButton;
    @BindView(R.id.continueButton) Button mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_genres);
        ButterKnife.bind(this);

        intent = getIntent();


        mShowTypeTemp = new ShowType();
        mShowType = new ShowType();
        mShowTypeTemp.setMovie(intent.getBooleanExtra(IS_MOVIE, false));
        mShowTypeTemp.setTV(intent.getBooleanExtra(IS_TV, false));
        mShowType.setMovie(mShowTypeTemp.isMovie());
        mShowType.setTV(mShowTypeTemp.isTV());

        if (mShowTypeTemp.isMovie()) {
            Parcelable[] movieParcelables = intent.getParcelableArrayExtra(MainActivity.MOVIE_GENRES);
            mMovieGenres = Arrays.copyOf(movieParcelables, movieParcelables.length, Genre[].class);
            mMovieGenreAdapter = new GenreAdapter(this, mMovieGenres);
            setListAdapter(mMovieGenreAdapter);

            if (mShowTypeTemp.isTV()) {
                getTVParcelables();
            }

        } else {
            getTVParcelables();
            setTVAdapter();
        }

        if (mShowTypeTemp.isMovie()) {
            mGenresLabel.setText("MOVIE GENRES");

        } else {
            mGenresLabel.setText("TV GENRES");

        }

        mContinueButton.setClickable(false);
        mContinueButton.setTextColor(Color.GRAY);
    }

    private void getTVParcelables() {
        Parcelable[] tvParcelables = intent.getParcelableArrayExtra(MainActivity.TV_GENRES);
        mTVGenres = Arrays.copyOf(tvParcelables, tvParcelables.length, Genre[].class);
    }

    @OnClick (R.id.chooseButton)
    public void getGenreChoices() {

        if (mShowTypeTemp.isMovie()) {

            getUserGenreChoices(mMovieGenreAdapter, mMovieGenresArrayList);
            if (mMovieGenresArrayList.isEmpty()) {
                Toast.makeText(this, "You have not made any choices! Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                Genre[] movieGenreChoices = mMovieGenresArrayList.toArray(new Genre[mMovieGenresArrayList.size()]);
                mUserMovieGenreChoices = new Genres();
                mUserMovieGenreChoices.setGenres(movieGenreChoices);
                continueNotChoose();
                mShowTypeTemp.setMovie(false);
            }

        } else {

            getUserGenreChoices(mTVGenreAdapter, mTVGenresArrayList);
            if (mTVGenresArrayList.isEmpty()) {
                Toast.makeText(this, "You have not made any choices! Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                Genre[] tvGenreChoices = new Genre[mTVGenresArrayList.size()];
                tvGenreChoices = mTVGenresArrayList.toArray(tvGenreChoices);
                mUserTVGenreChoices = new Genres();
                mUserTVGenreChoices.setGenres(tvGenreChoices);

                mShowTypeTemp.setTV(false);
                continueNotChoose();

            }

        }



    }

    private void continueNotChoose() {
        mContinueButton.setClickable(true);
        mContinueButton.setTextColor(Color.WHITE);

        mChooseButton.setClickable(false);
        mChooseButton.setTextColor(Color.GRAY);
    }

    private void getUserGenreChoices(GenreAdapter adapter, ArrayList<Genre> userPickedGenres) {

        boolean[] genresIsChecked = adapter.getIsChecked();

        for (int i = 0; i < genresIsChecked.length; i++) {
            if (genresIsChecked[i]) {
                userPickedGenres.add((Genre)adapter.getItem(i));
            }
        }

    }

    @OnClick (R.id.continueButton)
    public void refreshOrStartFilterActivity(View view) {

        if (mShowTypeTemp.isTV()) {
            setTVAdapter();
            mGenresLabel.setText("TV GENRES");

            mContinueButton.setClickable(false);
            mContinueButton.setTextColor(Color.GRAY);

            mChooseButton.setClickable(true);
            mChooseButton.setTextColor(Color.WHITE);
        } else {
            Intent intent = new Intent(this, FiltersActivity.class);
            if (!mMovieGenresArrayList.isEmpty()) {
                intent.putExtra(PICKED_MOVIE_GENRES, mUserMovieGenreChoices.getGenres());
                intent.putExtra(MOVIE_TYPE, mShowType.isMovie());
            }
            if (!mTVGenresArrayList.isEmpty()) {
                intent.putExtra(PICKED_TV_GENRES, mUserTVGenreChoices.getGenres());
                intent.putExtra(TV_TYPE, mShowType.isTV());
            }
            startActivity(intent);
        }
    }

    private void setTVAdapter() {
        mTVGenreAdapter = new GenreAdapter(this, mTVGenres);
        setListAdapter(mTVGenreAdapter);
    }
}
