package ru.myitschool.travamd.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.adapters.MovieAdapter;
import ru.myitschool.travamd.adapters.MovieHorizontalAdapter;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Networking;
import ru.myitschool.travamd.utils.Utils;

public class MainFragment extends Fragment {
    private MovieHorizontalAdapter mMovieHorizontalAdapter;
    private MovieAdapter movieAdapterInTheatre, movieAdapterPopular;
    private final ArrayList<Movie> movieListPopular = new ArrayList<>();
    private final ArrayList<Movie> movieListInTheatre = new ArrayList<>();
    private ImageView RecImage;
    private TextView RecTitle, RecDesc;

    ToggleButton LikePop, LikeTheatre, LikeRec;
    final Random random = new Random();
    String coverImageRec = "";
    String movieNameRec = "";
    String movieOverviewRec = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mRecyclerViewInTheatre = view.findViewById(R.id.recycler_view_in_theatre);
        LinearLayoutManager linearLayoutManagerInTheatre = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewInTheatre.setLayoutManager(linearLayoutManagerInTheatre);
        movieAdapterInTheatre = new MovieAdapter(movieListInTheatre, 3, mChangeFragmentListener);
        mRecyclerViewInTheatre.setNestedScrollingEnabled(true);
        mRecyclerViewInTheatre.setHasFixedSize(true);
        mRecyclerViewInTheatre.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewInTheatre.setAdapter(movieAdapterInTheatre);

        RecyclerView mRecyclerViewPopular = view.findViewById(R.id.recycler_view_popular);
        LinearLayoutManager linearLayoutManagerPopular = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        movieAdapterPopular = new MovieAdapter(movieListPopular, 3, mChangeFragmentListener);
        mRecyclerViewPopular.setLayoutManager(linearLayoutManagerPopular);
        mRecyclerViewPopular.setNestedScrollingEnabled(true);
        mRecyclerViewPopular.setHasFixedSize(true);
        mRecyclerViewPopular.setItemAnimator(new DefaultItemAnimator());
        mMovieHorizontalAdapter = new MovieHorizontalAdapter(new ArrayList<>(), null, mChangeFragmentListener);
        mRecyclerViewPopular.setAdapter(movieAdapterPopular);

        RecImage = view.findViewById(R.id.movie_cover);
        RecTitle = view.findViewById(R.id.movie_name);
        RecDesc = view.findViewById(R.id.movie_desc);

//        LikeRec = (ToggleButton) view.findViewById(R.id.like);
//        LikeRec.setVisibility(view.GONE);


        loadPopularMovie();
        loadInTheatreMovie();

    }

    private void loadInTheatreMovie() {
        MovieInTheatreQueryTask movieInTheatreAsync = new MovieInTheatreQueryTask(Constants.IN_THEATRE_URL);
        movieInTheatreAsync.execute(Constants.IN_THEATRE_URL);
    }

    private void loadPopularMovie() {
        MoviePopularQueryTask moviePopularAsync = new MoviePopularQueryTask(Constants.POPULAR_URL);
        moviePopularAsync.execute(Constants.POPULAR_URL);
    }


    @SuppressLint("StaticFieldLeak")
    private class MoviePopularQueryTask extends AsyncTask<String, Movie, Void> {
        private final String mQueryUrl;

        public MoviePopularQueryTask(String queryURL) {

            mQueryUrl = queryURL;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            URL url = Networking.buildUrl(mQueryUrl);
            String coverImage, movieName, movieNameOriginal, movieYear, movieOverview;
            String jsonResult = "";


            long movieId;
            try {
                jsonResult = Networking.getJson(url);
                JSONObject json = new JSONObject(jsonResult);
                JSONArray resultArray = json.getJSONArray("results");

                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject movie = resultArray.getJSONObject(i);
                    coverImage = movie.getString("poster_path");
                    movieName = movie.getString("title");
                    movieNameOriginal = movie.getString("original_title");
                    movieYear = "Год: " + movie.getString("release_date").split("-")[0];
                    movieId = movie.getLong("id");
                    movieOverview = movie.getString("overview");

                    Movie movieQueried = new Movie(coverImage, movieName, movieNameOriginal, movieYear, movieId, movieOverview);
                    publishProgress(movieQueried);
                }

                int RecMovieID = (random.nextInt(resultArray.length() - 10) + 10);

                JSONObject movie = resultArray.getJSONObject(RecMovieID);
                coverImageRec = movie.getString("poster_path");
                movieNameRec = movie.getString("title");
                movieOverviewRec = movie.getString("overview");


            } catch (IOException | JSONException e) {
                e.printStackTrace();

            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            mMovieHorizontalAdapter.add(values[0], false);
            movieListPopular.add(values[0]);
            movieAdapterPopular.notifyDataSetChanged();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RecTitle.setText(movieNameRec);
            RecDesc.setText(movieOverviewRec);
            Picasso.get()
                    // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                    .load(Constants.COVER_W780_URL + coverImageRec)
                    .resize(getContext().getResources().getDimensionPixelSize(R.dimen.imageview_width),
                            getContext().getResources().getDimensionPixelSize(R.dimen.imageview_height))
                    .centerCrop()
                    .into(RecImage);

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MovieInTheatreQueryTask extends AsyncTask<String, Movie, Void> {
        private final String mQueryUrl;

        public MovieInTheatreQueryTask(String queryURL) {

            mQueryUrl = queryURL;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            URL url = Networking.buildUrl(mQueryUrl);
            String coverImage, movieName, movieNameOriginal, movieYear, movieOverview;
            String jsonResult = "";
            long movieId;
            try {
                jsonResult = Networking.getJson(url);
                JSONObject json = new JSONObject(jsonResult);
                JSONArray resultArray = json.getJSONArray("results");

                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject movie = resultArray.getJSONObject(i);
                    coverImage = movie.getString("poster_path");
                    movieName = movie.getString("title");
                    movieNameOriginal = movie.getString("original_title");
                    movieYear = "Год: " + movie.getString("release_date").split("-")[0];
                    movieId = movie.getLong("id");
                    movieOverview = movie.getString("overview");

                    Movie movieQueried = new Movie(coverImage, movieName, movieNameOriginal, movieYear, movieId, movieOverview);

                    publishProgress(movieQueried);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            mMovieHorizontalAdapter.add(values[0], false);
            movieListInTheatre.add(values[0]);
            movieAdapterInTheatre.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }

    private final OnChangeFragmentListener mChangeFragmentListener = fragment -> Utils.replaceFragment(
            getActivity().getSupportFragmentManager(),
            fragment
    );
}