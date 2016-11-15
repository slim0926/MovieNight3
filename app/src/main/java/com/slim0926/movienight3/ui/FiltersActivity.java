package com.slim0926.movienight3.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.slim0926.movienight3.R;
import com.slim0926.movienight3.model.Filters;
import com.slim0926.movienight3.model.FiltersOptions;
import com.slim0926.movienight3.model.Genre;
import com.slim0926.movienight3.model.Show;
import com.slim0926.movienight3.model.ShowType;
import com.slim0926.movienight3.model.Shows;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.slim0926.movienight3.ui.GenresActivity.MOVIE_TYPE;
import static com.slim0926.movienight3.ui.GenresActivity.TV_TYPE;
import static com.slim0926.movienight3.ui.MainActivity.API_KEY;

/**
 * Created by sue on 10/13/16.
 */

public class FiltersActivity extends AppCompatActivity {

    public static final String TAG = FiltersActivity.class.getSimpleName();
    public static final String MOVIE_SEARCH = "MOVIE_SEARCH";
    public static final String TV_SEARCH = "TV_SEARCH";
    public static final String IS_IT_MOVIES = "IS_IT_MOVIES";
    public static final String IS_IT_TVSHOWS = "IS_IT_TVSHOWS";

    private Genre[] mMovieGenres;
    private Genre[] mTVGenres;
    private ShowType mShowType;
    private ShowType mShowTypeTemp;
    private FiltersOptions mFiltersOptions;
    private Filters mFilters;
    private Shows mMovies;
    private Shows mTVShows;

    @BindView(R.id.ratingThresholdSpinner) Spinner mRatingThresholdSpinner;
    @BindView(R.id.minRatingNumEditText) EditText mMinRatingsNumEditText;
    @BindView(R.id.maxYearSpinner) Spinner mMaxYearSpinner;
    @BindView(R.id.maxMonthSpinner) Spinner mMaxMonthSpinner;
    @BindView(R.id.maxDaySpinner) Spinner mMaxDaySpinner;
    @BindView(R.id.minYearSpinner) Spinner mMinYearSpinner;
    @BindView(R.id.minMonthSpinner) Spinner mMinMonthSpinner;
    @BindView(R.id.minDaySpinner) Spinner mMinDaySpinner;
    @BindView(R.id.searchButton) Button mSearchButton;
    @BindView(R.id.setFiltersButton) Button mSetFiltersButton;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mShowType = new ShowType();
        mShowTypeTemp = new ShowType();
        mShowType.setMovie(intent.getBooleanExtra(MOVIE_TYPE, false));
        mShowType.setTV(intent.getBooleanExtra(TV_TYPE, false));
        mShowTypeTemp.setMovie(mShowType.isMovie());
        mShowTypeTemp.setTV(mShowType.isTV());

        if (mShowTypeTemp.isMovie()) {
            Parcelable[] movieParcelables = intent.getParcelableArrayExtra(GenresActivity.PICKED_MOVIE_GENRES);
            mMovieGenres = Arrays.copyOf(movieParcelables, movieParcelables.length, Genre[].class);
        }

        if (mShowTypeTemp.isTV()) {
            Parcelable[] tvParcelables = intent.getParcelableArrayExtra(GenresActivity.PICKED_TV_GENRES);
            mTVGenres = Arrays.copyOf(tvParcelables, tvParcelables.length, Genre[].class);
        }

        mFiltersOptions = new FiltersOptions();
        loadFilterOptions();

        mSearchButton.setClickable(false);
        mSearchButton.setTextColor(Color.GRAY);

        if (mShowTypeTemp.isTV() && !mShowTypeTemp.isMovie()) {
            mSetFiltersButton.setText("SET TV FILTERS");
        }
    }

    private void loadFilterOptions() {
        loadSpinner(mRatingThresholdSpinner, mFiltersOptions.getRatingThresholdOptions());
        mMinRatingsNumEditText.setText(FiltersOptions.STARTING_MIN_NUM_OF_RATINGS + "");
        loadSpinner(mMaxMonthSpinner, mFiltersOptions.getReleaseMonths());
        loadSpinner(mMinMonthSpinner, mFiltersOptions.getReleaseMonths());
        loadSpinner(mMaxDaySpinner, mFiltersOptions.getReleaseDays());
        loadSpinner(mMinDaySpinner, mFiltersOptions.getReleaseDays());
        loadSpinner(mMaxYearSpinner, mFiltersOptions.getReleaseYears());
        loadSpinner(mMinYearSpinner, mFiltersOptions.getReleaseYears());

        mRatingThresholdSpinner.setSelection(3);
        mMaxMonthSpinner.setSelection(11);

    }

    private void loadSpinner(Spinner spinner, String[] releaseDate) {
        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, releaseDate);
        spinner.setAdapter(adapter);
    }

    private void loadSpinner(Spinner spinner, ArrayList<String> releaseYear) {
        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, releaseYear);
        spinner.setAdapter(adapter);
    }

    private void getFilterInput() {
        mFilters= new Filters();
        String strRatingThreshold = String.valueOf(mRatingThresholdSpinner.getSelectedItem());
        mFilters.setRatingThreshold(Float.parseFloat(strRatingThreshold));

        String strMinRatingsNum = String.valueOf(mMinRatingsNumEditText.getText());
        mFilters.setMinNumOfRatings(Integer.parseInt(strMinRatingsNum));

        String strMaxReleaseDate = mMaxYearSpinner.getSelectedItem() + "-" +
                mMaxMonthSpinner.getSelectedItem() + "-" +
                mMaxDaySpinner.getSelectedItem();


        String strMinReleaseDate = mMinYearSpinner.getSelectedItem() + "-" +
                mMinMonthSpinner.getSelectedItem() + "-" +
                mMinDaySpinner.getSelectedItem();

        mFilters.setMaxDate(strMaxReleaseDate);
        mFilters.setMinDate(strMinReleaseDate);

    }

    @OnClick(R.id.setFiltersButton)
    public void getShowInfo() {

        getFilterInput();

        String showInfoUrl = "";

        if (mShowTypeTemp.isMovie()) {
            showInfoUrl = "https://api.themoviedb.org/3/discover/movie?vote_average.gte=";
        } else if (mShowTypeTemp.isTV()) {
            showInfoUrl = "https://api.themoviedb.org/3/discover/tv?vote_average.gte=";
        }

        showInfoUrl += mFilters.getRatingThreshold() + "&vote_count.gte=" +
                mFilters.getMinNumOfRatings() + "&release_date.lte=" +
                mFilters.getMaxDate() + "&release_date.gte=" +
                mFilters.getMinDate() + "&api_key=" + API_KEY;

        showInfoUrl += "&with_genres=";

        if (mShowTypeTemp.isMovie()) {
            for (int i = 0; i < mMovieGenres.length; i++) {
                if (i < (mMovieGenres.length - 1)) {
                    showInfoUrl += mMovieGenres[i].getGenreID() + "|";
                } else {
                    showInfoUrl += mMovieGenres[i].getGenreID() + "";
                }
            }
        } else if (mShowTypeTemp.isTV()) {
            for (int i = 0; i < mTVGenres.length; i++) {
                if (i < (mTVGenres.length - 1)) {
                    showInfoUrl += mTVGenres[i].getGenreID() + "|";
                } else {
                    showInfoUrl += mTVGenres[i].getGenreID() + "";
                }
            }
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(showInfoUrl).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    String jsonData = response.body().string();
                    Log.v(TAG, jsonData);
                    if (response.isSuccessful()) {
                        if (mShowTypeTemp.isMovie()) {
                            mMovies = parseShowResultsDetails(jsonData);
                            mShowTypeTemp.setMovie(false);

                        } else if (mShowTypeTemp.isTV()) {
                            mTVShows = parseShowResultsDetails(jsonData);
                            mShowTypeTemp.setTV(false);

                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });

        mSearchButton.setTextColor(Color.WHITE);
        mSearchButton.setClickable(true);

        mSetFiltersButton.setTextColor(Color.GRAY);
        mSetFiltersButton.setClickable(false);

    }

    private Shows parseShowResultsDetails(String jsonData) throws JSONException {
        Shows showResults = new Shows();

        showResults.setShowResults(getShow(jsonData));

        return showResults;
    }

    private Show[] getShow(String jsonData) throws JSONException {
        JSONObject showResults = new JSONObject(jsonData);
        JSONArray data = showResults.getJSONArray("results");

        Show[] shows = new Show[data.length()];

        for (int i=0; i<data.length(); i++) {
            JSONObject jsonShow = data.getJSONObject(i);
            Show show = new Show();

            if (mShowTypeTemp.isMovie()) {
                show.setTitle(jsonShow.getString("title"));
                show.setReleaseDate(jsonShow.getString("release_date"));


            } else if (mShowTypeTemp.isTV()) {
                show.setTitle(jsonShow.getString("name"));
                show.setReleaseDate(jsonShow.getString("first_air_date"));

            }
            show.setOverview(jsonShow.getString("overview"));
            show.setPopularity(jsonShow.getDouble("popularity"));

            show.setVoteAverage(jsonShow.getDouble("vote_average"));
            show.setVoteCount(jsonShow.getInt("vote_count"));

            shows[i] = show;
        }

        return shows;
    }

    @OnClick (R.id.searchButton)
    public void startShowActivity() {

        if (mShowTypeTemp.isTV()) {
            mSetFiltersButton.setText("SET TV FILTERS");

            mSearchButton.setTextColor(Color.GRAY);
            mSearchButton.setClickable(false);

            mSetFiltersButton.setClickable(true);
            mSetFiltersButton.setTextColor(Color.WHITE);


        } else {
            Intent intent = new Intent(this, ShowResultsActivity.class);
            if (mShowType.isMovie()) {
                intent.putExtra(MOVIE_SEARCH, mMovies.getShowResults());
            }
            if (mShowType.isTV()) {
                intent.putExtra(TV_SEARCH, mTVShows.getShowResults());
            }

            intent.putExtra(IS_IT_MOVIES, mShowType.isMovie());
            intent.putExtra(IS_IT_TVSHOWS, mShowType.isTV());
            startActivity(intent);
        }
    }
}
