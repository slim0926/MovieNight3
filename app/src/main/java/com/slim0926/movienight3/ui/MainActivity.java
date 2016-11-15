package com.slim0926.movienight3.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.slim0926.movienight3.R;
import com.slim0926.movienight3.model.Genre;
import com.slim0926.movienight3.model.Genres;
import com.slim0926.movienight3.model.ShowType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String API_KEY = "761ae9f5d432609a0aebe4dd9464aac0";
    public static final String TV_GENRES = "TV_GENRES";
    public static final String MOVIE_GENRES = "MOVIE_GENRES";
    public static final String IS_MOVIE = "IS_MOVIE";
    public static final String IS_TV = "IS_TV";

    private ShowType mShowType;
    private Genres mTVGenres;
    private Genres mMovieGenres;

    @BindView(R.id.titleTextView) TextView mTitleTextView;
    @BindView(R.id.introTextView1) TextView mIntroTextView1;
    @BindView(R.id.introTextView2) TextView mIntroTextView2;
    @BindView(R.id.introTextView3) TextView mIntroTextView3;
    @BindView(R.id.movieImageView) ImageView mMovieImageView;
    @BindView(R.id.tvImageView) ImageView mTVImageView;
    @BindView(R.id.movieCheckBox) CheckBox mMovieCheckBox;
    @BindView(R.id.tvCheckBox) CheckBox mTVCheckBox;
    @BindView(R.id.setShowTypeButton) Button mSetShowTypeButton;
    @BindView(R.id.continueButton) Button mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContinueButton.setClickable(false);
        mContinueButton.setTextColor(Color.GRAY);

    }

    @OnClick (R.id.setShowTypeButton)
    public void setShowType() {
        mShowType = new ShowType();

        if (mMovieCheckBox.isChecked()) {
            mShowType.setMovie(true);
        }

        if (mTVCheckBox.isChecked()) {
            mShowType.setTV(true);
        }

        if (!mMovieCheckBox.isChecked() && !mTVCheckBox.isChecked()) {
            Toast.makeText(this, "You have not made any choices! Please check a box.", Toast.LENGTH_SHORT).show();
        } else {

            if (mShowType.isMovie()) {
                getGenreList("movie");

                if (mShowType.isTV()) {
                    getGenreList("tv");
                }
            } else {
                getGenreList("tv");
            }

            mContinueButton.setClickable(true);
            mContinueButton.setTextColor(Color.WHITE);
        }

    }

    @OnClick (R.id.continueButton)
    public void StartGenreActivity(View view) {
        Intent intent = new Intent(this, GenresActivity.class);
        if (mShowType.isMovie()) {
            if (mMovieGenres == null) {
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            intent.putExtra(MOVIE_GENRES, mMovieGenres.getGenres());
        }
        if (mShowType.isTV()) {
            if (mTVGenres == null) {
                Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            intent.putExtra(TV_GENRES, mTVGenres.getGenres());
        }
        intent.putExtra(IS_MOVIE, mShowType.isMovie());
        intent.putExtra(IS_TV, mShowType.isTV());
        startActivity(intent);

        mContinueButton.setClickable(false);
        mContinueButton.setTextColor(Color.GRAY);
    }

    private void getGenreList(final String showType) {
        String genreListUrl = "https://api.themoviedb.org/3/genre/" + showType + "/list?api_key=" + API_KEY;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(genreListUrl).build();

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
                        if (showType.equalsIgnoreCase("movie")){
                            mMovieGenres = parseGenreDetails(jsonData);
                        } else {
                            mTVGenres = parseGenreDetails(jsonData);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                } catch (JSONException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }
            }
        });

    }

    private Genres parseGenreDetails(String jsonData) throws JSONException {
        Genres genres = new Genres();

        genres.setGenres(getGenres(jsonData));

        return genres;
    }

    private Genre[] getGenres(String jsonData) throws JSONException {
        JSONObject genres = new JSONObject(jsonData);
        JSONArray data = genres.getJSONArray("genres");

        Genre[] showGenres = new Genre[data.length()];

        for (int i=0; i<data.length(); i++) {
            JSONObject jsonShow = data.getJSONObject(i);
            Genre showGenre = new Genre();

            showGenre.setGenreName(jsonShow.getString("name"));
            showGenre.setGenreID(jsonShow.getInt("id"));

            showGenres[i] = showGenre;
        }

        return showGenres;
    }
}
