package ru.myitschool.travamd.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.adapters.MovieAdapter;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Networking;
import ru.myitschool.travamd.utils.Utils;

public class UpcomingFragment extends Fragment {

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    int page = 1;
    RecyclerView.LayoutManager layoutManager;
    int cardNumber;
    private MovieAdapter mMovieAdapter;
    private final ArrayList<Movie> movieList = new ArrayList<>();
    private ProgressBar progressBar;
    private boolean loading = true;

    //Фрагмент содержит код, аналогичный предыдущим.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scroll, container, false);
        cardNumber = 2;

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        mMovieAdapter = new MovieAdapter(movieList, cardNumber, mChangeFragmentListener);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), cardNumber);


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mMovieAdapter);

        progressBar = v.findViewById(R.id.progress_bar);

        MovieQueryTask movieAsync = new MovieQueryTask();
        movieAsync.execute(Constants.UPCOMING_URL);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            page++;
                            MovieQueryTask movieAsync = new MovieQueryTask();
                            movieAsync.execute(Constants.UPCOMING_URL, String.valueOf(page));

                            showProgressBar();
                        }
                    }
                }
            }
        });
        return v;
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @SuppressLint("StaticFieldLeak")
    public class MovieQueryTask extends AsyncTask<String, Movie, Void> {

        @Override
        protected void onPreExecute() {
            showProgressBar();
        }

        @Override
        protected Void doInBackground(String... params) {

            String queryUrl = params[0];
            String page = null;
            if (params.length > 1) {
                page = params[1];
            }

            URL url = Networking.buildUrl(queryUrl, page);
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            movieList.add(values[0]);
            mMovieAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgressBar();
            loading = true;
        }
    }

    private final OnChangeFragmentListener mChangeFragmentListener = fragment -> Utils.replaceFragment(
            getActivity().getSupportFragmentManager(),
            fragment
    );
}
