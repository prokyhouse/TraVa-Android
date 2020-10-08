package ru.myitschool.travamd.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.adapters.ActorAdapter;
import ru.myitschool.travamd.adapters.Movie_adapter;
import ru.myitschool.travamd.models.Actor;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Networking;

public class DescriptionFragment extends Fragment {


    ImageView movieImagePoster, movieImageBackPoster;
    TextView movieOriginalTitle, movieTitle, movieReleaseDate,
             movieOverview, movieGenres, movieAverage, movieRating;
    CardView cardOverview;

    RecyclerView.LayoutManager layoutManagerActor,layoutManagerRecommendation;
    private RecyclerView recyclerViewActor, recyclerViewRecommendation;
    private ProgressBar progressBarActor,progressBarRecommendation;
    private Movie_adapter mMovieAdapter;
    private ActorAdapter actorAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private ArrayList<Actor> actorList = new ArrayList<>();


    long movieID = 0;

    String jsonResult = "";
    String coverPath = "";
    String actorProfilePath = "";
    String actorID = "";
    //По умолчанию данные содержат сведения об ошибки.
    String name = Constants.NOT_FOUND;
    String nameOriginal = Constants.NOT_FOUND;
    String year = Constants.NOT_FOUND;
    String overview = Constants.NOT_FOUND;
    String actor_Name = Constants.NOT_FOUND;
    String actorCharacter = Constants.NOT_FOUND;

    public static DescriptionFragment newInstance(long movieId,String url,String name,String originalName,String overview, String year) {
        DescriptionFragment fragment = new DescriptionFragment();

        //Передача имеющихся данных между фрагментами для сохранения трафика
        Bundle bundle = new Bundle();
        bundle.putLong("id", movieId);
        bundle.putString("cover_path", url);
        bundle.putString("name", name);
        bundle.putString("nameOriginal", originalName);
        bundle.putString("overview", overview);
        bundle.putString("year", year);

        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_description, container, false);

        int CardType = 3;

        Bundle bundle = getArguments();

        if (bundle != null) {
            movieID = bundle.getLong("id");
            coverPath = bundle.getString("cover_path");
            name = bundle.getString("name");
            nameOriginal = bundle.getString("nameOriginal");
            year = bundle.getString("year");
            overview = bundle.getString("overview");
        }

        //Основные поля фрагмента.
        movieImagePoster = (ImageView) v.findViewById(R.id.image_movie_detail_poster);
        movieImageBackPoster = (ImageView) v.findViewById(R.id.backdrop_image);
        movieOriginalTitle = (TextView) v.findViewById(R.id.text_movie_original_title);
        movieTitle = (TextView) v.findViewById(R.id.text_movie_title);
        movieReleaseDate = (TextView) v.findViewById(R.id.text_movie_release_date);
        movieOverview = (TextView) v.findViewById(R.id.text_movie_overview);
        movieGenres = (TextView) v.findViewById(R.id.text_movie_genre);
        movieAverage = (TextView) v.findViewById(R.id.text_movie_smth);

        progressBarActor = (ProgressBar) v.findViewById(R.id.progress_bar_actor);
        progressBarRecommendation = (ProgressBar) v.findViewById(R.id.progress_bar_recommendation);

        movieRating = (TextView) v.findViewById(R.id.text_movie_rating);

        cardOverview = (CardView) v.findViewById(R.id.card_movie_overview);

        recyclerViewActor = (RecyclerView) v.findViewById(R.id.recycler_view_actor);
        recyclerViewRecommendation = (RecyclerView) v.findViewById(R.id.recycler_view_recommendation);

        actorAdapter = new ActorAdapter(actorList, getActivity());
        mMovieAdapter = new Movie_adapter(getActivity(),movieList, CardType);

        recyclerViewActor.setHasFixedSize(true);
        recyclerViewRecommendation.setHasFixedSize(true);

        layoutManagerActor = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerRecommendation = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewActor.setLayoutManager(layoutManagerActor);
        recyclerViewRecommendation.setLayoutManager(layoutManagerRecommendation);

        recyclerViewRecommendation.setAdapter(mMovieAdapter);
        recyclerViewActor.setAdapter(actorAdapter);


        //Получение детальных данных о выбранном фильме.
        DescriptionFragment.MovieQueryTask movieAsync = new DescriptionFragment.MovieQueryTask();
        //Получение данных об актерах.
        DescriptionFragment.ActorQueryTask actorAsync = new DescriptionFragment.ActorQueryTask();
        //Получение данных рекомендуемых фильмов.
        DescriptionFragment.MovieRecQueryTask movieRecAsync = new DescriptionFragment.MovieRecQueryTask();

        //Все необходимые URL находятся в классе Constants.
        movieAsync.execute(Constants.BASE_URL + movieID);
        actorAsync.execute(Constants.BASE_URL + movieID + "/credits");
        movieRecAsync.execute(Constants.BASE_URL + movieID + "/recommendations");

        //Заполнение полей.
        Glide.with(this)
                .load(Constants.COVER_W780_URL + coverPath)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(movieImagePoster);


        movieTitle.setText(name);
        movieOriginalTitle.setText(nameOriginal);

        String releaseDate = String.format(year);
        movieReleaseDate.setText(releaseDate);

        //Если поле не содержит информации, оно скрывается.
        if (overview.equals("")) {
            cardOverview.setVisibility(v.GONE);
        } else {
            movieOverview.setText(overview);
        }

        return v;
    }


    public class MovieQueryTask extends AsyncTask<String, Movie, Void> {

        String backdrop_path, vote_average;
        JSONArray genresArray;
        String genres = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            String queryUrl = params[0];
            URL url = Networking.buildUrl(queryUrl);
            String jsonResult = "";

            try {
                jsonResult = Networking.getJson(url);
                JSONObject json = new JSONObject(jsonResult);
                backdrop_path = json.getString("backdrop_path");
                vote_average = json.getString("vote_average");
                genresArray = json.getJSONArray("genres");

                    for (int i = 0; i < genresArray.length(); i++) {
                        genres = genres + genresArray.getJSONObject(i).getString("name") + "\n";
                    }

            } catch (IOException e) {
                e.printStackTrace();
                genres = Constants.NOT_FOUND;
                vote_average = Constants.NOT_RATED;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            //Если в JSON не содержалось информации о задней картинке, она заменяется постером.
            try {
                if (backdrop_path.equals("null")) {
                    backdrop_path = coverPath;
                }

                Picasso.with(getContext())
                        .load(Constants.COVER_W780_URL + backdrop_path)
                        .error(R.drawable.cover_back)
                        .into(movieImageBackPoster);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Заполнение полей.
            movieGenres.setText(genres);
            movieAverage.setText(vote_average);
            movieRating.setText(vote_average);

        }

    }

    public class ActorQueryTask extends AsyncTask<String, Actor, Void> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            String queryUrl = params[0];
            URL url = Networking.buildUrl(queryUrl);


            try {

                jsonResult = Networking.getJson(url);


                JSONObject json = new JSONObject(jsonResult);
                JSONArray items = json.getJSONArray("cast");


                for (int i = 0; i < items.length() - 1; i++) {
                    actor_Name = items.getJSONObject(i).getString("name");
                    actorProfilePath = items.getJSONObject(i).getString("profile_path");
                    actorCharacter = items.getJSONObject(i).getString("character");
                    actorID = items.getJSONObject(i).getString("id");

                    Actor actorQueried = new Actor(actorID, actor_Name, actorCharacter, actorProfilePath);
                    publishProgress(actorQueried);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Actor... values) {

            actorList.add(values[0]);
            recyclerViewActor.getAdapter().notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }

    }

    public class MovieRecQueryTask extends AsyncTask<String, Movie, Void> {

        @Override
        protected void onPreExecute() {

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

            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
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

        }
    }

}