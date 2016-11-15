package com.slim0926.movienight3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.slim0926.movienight3.R;
import com.slim0926.movienight3.adapters.ShowAdapter;
import com.slim0926.movienight3.model.Show;
import com.slim0926.movienight3.model.ShowType;

import java.util.Arrays;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.slim0926.movienight3.ui.FiltersActivity.IS_IT_MOVIES;
import static com.slim0926.movienight3.ui.FiltersActivity.IS_IT_TVSHOWS;


/**
 * Created by sue on 8/30/16.
 */

public class ShowResultsActivity extends AppCompatActivity implements OnItemSelectedListener{

    public static final String TAG = ShowResultsActivity.class.getSimpleName();

    private ShowType mShowType;
    private ArrayAdapter<String> mSortByAdapter;
    private ArrayAdapter<String> mAscDescAdapter;
    private ShowAdapter adapter;
    private Show[] mMoviesArray;
    private Show[] mTVShowsArray;
    private String mPage;

    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.sortBySpinner) Spinner mSortBySpinner;
    @BindView(R.id.ascDescSpinner) Spinner mAscDescSpinner;
    @BindView(R.id.toTVShowsButton) Button mToTVShowsButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mShowType = new ShowType();

        mShowType.setMovie(intent.getBooleanExtra(IS_IT_MOVIES, false));
        mShowType.setTV(intent.getBooleanExtra(IS_IT_TVSHOWS, false));


        if (mShowType.isMovie()) {

            Parcelable[] movieParcelables = intent.getParcelableArrayExtra(FiltersActivity.MOVIE_SEARCH);
            mMoviesArray = Arrays.copyOf(movieParcelables, movieParcelables.length, Show[].class);

            for (Show movie : mMoviesArray) {
                movie.setShowIconImageId(Show.getIconId("movie_icon"));
            }
            mPage = "movie";
            adapter = new ShowAdapter(this, mMoviesArray);
        }

        if (mShowType.isTV()) {

            Parcelable[] tvParcelables = intent.getParcelableArrayExtra(FiltersActivity.TV_SEARCH);
            mTVShowsArray = Arrays.copyOf(tvParcelables, tvParcelables.length, Show[].class);

            for (Show tvShow : mTVShowsArray) {
                tvShow.setShowIconImageId(Show.getIconId("tv_icon"));
            }

            if (!mShowType.isMovie()) {

                mPage = "tv";
                adapter = new ShowAdapter(this, mTVShowsArray);
            }

        }

        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        fillSortBySpinner();
        fillAscDescSpinner();

        if (mShowType.isMovie() && mShowType.isTV()) {
            mToTVShowsButton.setVisibility(View.VISIBLE);
        }
    }

    private void fillSortBySpinner() {
        String[] sortBy = {
                "Popularity",
                "Release Date",
                "Average Vote",
                "# of Votes"
        };

        mSortByAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortBy);
        mSortByAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortBySpinner.setAdapter(mSortByAdapter);
        mSortBySpinner.setOnItemSelectedListener(this);
    }

    private void fillAscDescSpinner() {
        String[] ascDesc = {
                "Descending", "Ascending"
        };

        mAscDescAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ascDesc);
        mAscDescAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAscDescSpinner.setAdapter(mAscDescAdapter);
        mAscDescSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected = parent.getItemAtPosition(position).toString();

        if (parent.getId() == R.id.sortBySpinner) {

            switch (selected) {
                case "Popularity":
                    if (mAscDescSpinner.getSelectedItem().toString().equalsIgnoreCase("Descending")) {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByPopularityDesc(mMoviesArray);
                        } else {
                            sortByPopularityDesc(mTVShowsArray);
                        }

                    } else {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByPopularityAsc(mMoviesArray);
                        } else {
                            sortByPopularityAsc(mTVShowsArray);
                        }
                    }

                    break;
                case "Release Date":
                    if (mAscDescSpinner.getSelectedItem().toString().equalsIgnoreCase("Descending")) {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByReleaseDateDesc(mMoviesArray);
                        } else {
                            sortByReleaseDateDesc(mTVShowsArray);
                        }

                    } else {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByReleaseDateAsc(mMoviesArray);
                        } else {
                            sortByReleaseDateAsc(mTVShowsArray);
                        }
                    }
                    break;
                case "Average Vote":
                    if (mAscDescSpinner.getSelectedItem().toString().equalsIgnoreCase("Descending")) {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByAverageVoteDesc(mMoviesArray);
                        } else {
                            sortByAverageVoteDesc(mTVShowsArray);
                        }

                    } else {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByAverageVoteAsc(mMoviesArray);
                        } else {
                            sortByAverageVoteAsc(mTVShowsArray);
                        }
                    }
                    break;
                case "# of Votes":
                    if (mAscDescSpinner.getSelectedItem().toString().equalsIgnoreCase("Descending")) {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByVoteCountDesc(mMoviesArray);
                        } else {
                            sortByVoteCountDesc(mTVShowsArray);
                        }

                    } else {
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByVoteCountAsc(mMoviesArray);
                        } else {
                            sortByVoteCountAsc(mTVShowsArray);
                        }
                    }
                    break;
                default:
                    break;
            }

        } else if (parent.getId() == R.id.ascDescSpinner) {

            if (selected.equalsIgnoreCase("Descending")) {
                switch (mSortBySpinner.getSelectedItem().toString()) {
                    case "Popularity":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByPopularityDesc(mMoviesArray);
                        } else {
                            sortByPopularityDesc(mTVShowsArray);
                        }
                        break;
                    case "Release Date":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByReleaseDateDesc(mMoviesArray);
                        } else {
                            sortByReleaseDateDesc(mTVShowsArray);
                        }
                        break;
                    case "Average Vote":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByAverageVoteDesc(mMoviesArray);
                        } else {
                            sortByAverageVoteDesc(mTVShowsArray);
                        }
                        break;
                    case "# of Votes":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByVoteCountDesc(mMoviesArray);
                        } else {
                            sortByVoteCountDesc(mTVShowsArray);
                        }
                        break;
                    default:
                        break;

                }

            } else {
                switch (mSortBySpinner.getSelectedItem().toString()) {
                    case "Popularity":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByPopularityAsc(mMoviesArray);
                        } else {
                            sortByPopularityAsc(mTVShowsArray);
                        }
                        break;
                    case "Release Date":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByReleaseDateAsc(mMoviesArray);
                        } else {
                            sortByReleaseDateAsc(mTVShowsArray);
                        }
                        break;
                    case "Average Vote":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByAverageVoteAsc(mMoviesArray);
                        } else {
                            sortByAverageVoteAsc(mTVShowsArray);
                        }
                        break;
                    case "# of Votes":
                        if (mPage.equalsIgnoreCase("movie")) {
                            sortByVoteCountAsc(mMoviesArray);
                        } else {
                            sortByVoteCountAsc(mTVShowsArray);
                        }
                        break;
                    default:
                        break;

                }
            }

        }
        if (mPage.equalsIgnoreCase("movie")) {
            adapter = new ShowAdapter(this, mMoviesArray);
        } else {
            adapter = new ShowAdapter(this, mTVShowsArray);
        }
        mRecyclerView.setAdapter(adapter);

    }

    private void sortByReleaseDateAsc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return show1.getReleaseDate().compareTo(show2.getReleaseDate());
            }
        });
    }

    private void sortByReleaseDateDesc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return show2.getReleaseDate().compareTo(show1.getReleaseDate());
            }
        });
    }

    private void sortByVoteCountAsc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return show1.getVoteCount() - show2.getVoteCount();
            }
        });
    }

    private void sortByVoteCountDesc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return show2.getVoteCount() - show1.getVoteCount();
            }
        });
    }

    private void sortByAverageVoteAsc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return Double.compare(show1.getVoteAverage(), show2.getVoteAverage());
            }
        });
    }

    private void sortByAverageVoteDesc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return Double.compare(show2.getVoteAverage(), show1.getVoteAverage());
            }
        });
    }

    private void sortByPopularityAsc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return Double.compare(show1.getPopularity(), show2.getPopularity());
            }
        });
    }

    private void sortByPopularityDesc(Show[] showArray) {
        Arrays.sort(showArray, new Comparator<Show>() {
            @Override
            public int compare(Show show1, Show show2) {
                return Double.compare(show2.getPopularity(), show1.getPopularity());
            }
        });
    }


    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @OnClick(R.id.toTVShowsButton)
    public void goToTVShows() {
        mToTVShowsButton.setVisibility(View.GONE);

        mPage = "tv";

        adapter = new ShowAdapter(this, mTVShowsArray);
        mRecyclerView.setAdapter(adapter);
        mSortBySpinner.setSelection(0);
        mAscDescSpinner.setSelection(0);
    }
}
