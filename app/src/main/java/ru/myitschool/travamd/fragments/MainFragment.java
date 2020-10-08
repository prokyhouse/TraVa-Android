package ru.myitschool.travamd.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import ru.myitschool.travamd.R;

import ru.myitschool.travamd.adapters.Movie_adapter;
import ru.myitschool.travamd.adapters.MovieHorizontalAdapter;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Networking;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerViewInTheatre, mRecyclerViewPopular;
    private MovieHorizontalAdapter mMovieHorizontalAdapter;
    private Movie_adapter movieAdapterInTheatre,movieAdapterPopular;
    private ArrayList<Movie> movieListPopular = new ArrayList<>();
    private ArrayList<Movie> movieListInTheatre = new ArrayList<>();
    private ImageView RecImage;
    private TextView RecTitle, RecDesc;

    ToggleButton LikePop,LikeTheatre,LikeRec;
    final Random random = new Random();
    String coverImageRec = "";
    String movieNameRec = "";
    String movieOverviewRec = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerViewInTheatre = (RecyclerView) view.findViewById(R.id.recycler_view_in_theatre);
        LinearLayoutManager  linearLayoutManagerInTheatre = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewInTheatre.setLayoutManager(linearLayoutManagerInTheatre);
        movieAdapterInTheatre = new Movie_adapter(getActivity(), movieListInTheatre, 3);
        mRecyclerViewInTheatre.setNestedScrollingEnabled(true);
        mRecyclerViewInTheatre.setHasFixedSize(true);
        mRecyclerViewInTheatre.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewInTheatre.setAdapter(movieAdapterInTheatre);

        mRecyclerViewPopular = (RecyclerView) view.findViewById(R.id.recycler_view_popular);
        LinearLayoutManager linearLayoutManagerPopular = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        movieAdapterPopular = new Movie_adapter(getActivity(), movieListPopular, 3);
        mRecyclerViewPopular.setLayoutManager(linearLayoutManagerPopular);
        mRecyclerViewPopular.setNestedScrollingEnabled(true);
        mRecyclerViewPopular.setHasFixedSize(true);
        mRecyclerViewPopular.setItemAnimator(new DefaultItemAnimator());
        mMovieHorizontalAdapter = new MovieHorizontalAdapter(getContext(),new ArrayList<>(),null);
        mRecyclerViewPopular.setAdapter(movieAdapterPopular);

        RecImage = (ImageView) view.findViewById(R.id.movie_cover);
        RecTitle = (TextView) view.findViewById(R.id.movie_name);
        RecDesc = (TextView) view.findViewById(R.id.movie_desc);

        LikeRec = (ToggleButton) view.findViewById(R.id.like);
        LikeRec.setVisibility(view.GONE);


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


    private class MoviePopularQueryTask extends AsyncTask<String, Movie, Void> {
        private String mQueryUrl;
        public MoviePopularQueryTask(String queryURL){

            mQueryUrl=queryURL;
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

                int RecMovieID = (random.nextInt(resultArray.length()-10) + 10);

                JSONObject movie = resultArray.getJSONObject(RecMovieID);
                coverImageRec = movie.getString("poster_path");
                movieNameRec = movie.getString("title");
                movieOverviewRec = movie.getString("overview");



            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            mMovieHorizontalAdapter.add(values[0],false);
            movieListPopular.add(values[0]);
            movieAdapterPopular.notifyDataSetChanged();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            RecTitle.setText(movieNameRec);
            RecDesc.setText(movieOverviewRec);
            Picasso.with(getContext())
                    // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                    .load(Constants.COVER_W780_URL + coverImageRec)
                    .resize(getContext().getResources().getDimensionPixelSize(R.dimen.imageview_width),
                            getContext().getResources().getDimensionPixelSize(R.dimen.imageview_height))
                    .centerCrop()
                    .into(RecImage);

        }
    }

    private class MovieInTheatreQueryTask extends AsyncTask<String, Movie, Void> {
        private String mQueryUrl;
        public MovieInTheatreQueryTask(String queryURL){

            mQueryUrl=queryURL;
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

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            mMovieHorizontalAdapter.add(values[0],false);
            movieListInTheatre.add(values[0]);
            movieAdapterInTheatre.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }
}