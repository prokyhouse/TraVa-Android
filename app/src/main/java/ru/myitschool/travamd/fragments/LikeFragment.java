package ru.myitschool.travamd.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.adapters.MovieHorizontalAdapter;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Database;
import ru.myitschool.travamd.utils.Networking;
import ru.myitschool.travamd.utils.Utils;

public class LikeFragment extends Fragment {
    private MovieHorizontalAdapter mMovieHorizontalAdapter;
    private ProgressBar mProgressBar;
    private TextView textViewInfo;

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
        mMovieHorizontalAdapter = new MovieHorizontalAdapter(null, null, mChangeFragmentListener);
        recyclerView.setAdapter(mMovieHorizontalAdapter);
        textViewInfo = (TextView) view.findViewById(R.id.textViewInfo);

        //Загрузка данных.
        loadMovie();

    }

    //Загрузка страницы
    private void loadMovie() {
        //Получение всех ID из БД избранных фильмов.
        long[] movies = Database.getAllMovie(getContext());

        //Загрузка данных для каждого фильма.
        for (int i = 0; i < movies.length; i++) {
            new MovieQueryTask().execute(Constants.BASE_URL + movies[i]);
        }
        //Если в списке нет фильмов.
        if (movies.length == 0) {
            textViewInfo.setVisibility(textViewInfo.VISIBLE);
            textViewInfo.setText("Ваш список пуст.");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMovieHorizontalAdapter = null;
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

                coverImage = json.getString("poster_path");
                movieName = json.getString("title");
                movieNameOriginal = json.getString("original_title");
                movieYear = "Год: " + json.getString("release_date").split("-")[0];
                movieId = json.getLong("id");
                movieOverview = json.getString("overview");

                Movie movieQueried = new Movie(coverImage, movieName, movieNameOriginal, movieYear, movieId, movieOverview);
                publishProgress(movieQueried);


            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {
            mMovieHorizontalAdapter.add(values[0], Database.isMovieExist(getContext(), values[0].getMovieId()));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgressBar();
            textViewInfo.setVisibility(textViewInfo.GONE);
        }
    }

    private OnChangeFragmentListener mChangeFragmentListener = fragment -> Utils.replaceFragment(
            getActivity().getSupportFragmentManager(),
            fragment
    );
}
