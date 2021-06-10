package ru.myitschool.travamd.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.adapters.MovieHorizontalAdapter;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Database;
import ru.myitschool.travamd.utils.Networking;
import ru.myitschool.travamd.utils.Utils;


public class SeriesFragment extends Fragment {
    private int mPage = 0;
    private MovieHorizontalAdapter mMovieHorizontalAdapter;
    private ProgressBar mProgressBar;
    private boolean mIsLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scroll, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mMovieHorizontalAdapter = new MovieHorizontalAdapter(null,null, mChangeFragmentListener);
        recyclerView.setAdapter(mMovieHorizontalAdapter);

        //Загрузка данных.
        loadMovie();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0){
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    //Обработка загрузки списка фильмов.
                    if (mIsLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            mIsLoading = false;
                            // Пользователь долистал до последнего элемента списка.
                            loadMovie();
                        }
                    }
                }
            }
        });
    }

    //Загрузка страницы
    private void loadMovie() {
        mPage++;
        new MovieQueryTask().execute(Constants.POPULAR_SERIES_URL, mPage+"");
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private class MovieQueryTask extends AsyncTask<String, Movie, Void> {

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
                    movieName = movie.getString("name");
                    movieNameOriginal = movie.getString("original_name");
                    movieYear = "Год: " + movie.getString("first_air_date").split("-")[0];
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
            mMovieHorizontalAdapter.add(values[0], Database.isMovieExist(getContext(),values[0].getMovieId()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgressBar();
            mIsLoading = true;
        }
    }

    private OnChangeFragmentListener mChangeFragmentListener = fragment -> Utils.replaceFragment(
            getActivity().getSupportFragmentManager(),
            fragment
    );

}
