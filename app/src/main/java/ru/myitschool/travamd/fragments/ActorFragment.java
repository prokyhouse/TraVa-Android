package ru.myitschool.travamd.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Networking;


public class ActorFragment extends Fragment {

    ImageView Poster;
    TextView Name, Birtday, PlaceOfBirth, Popularity;
    CollapsingToolbarLayout NameHeader;

    private String id = "";
    private String poster = "";
    //В случае отсутствия данных об Актере, будет выведена соответствующая информация.
    private String name = Constants.NOT_FOUND;
    private String birthday = Constants.NOT_FOUND;
    private String place_of_birth = Constants.NOT_FOUND;
    private String popularity = Constants.NOT_FOUND;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_actor,
                container,
                false);

        Name = v.findViewById(R.id.actor_name_l);
        Birtday = v.findViewById(R.id.actor_birtday);
        PlaceOfBirth = v.findViewById(R.id.actor_place_of_birth);
        Popularity = v.findViewById(R.id.actor_popularity);
        NameHeader = v.findViewById(R.id.collapsing);
        Poster = v.findViewById(R.id.actor_cover_l);
        //Получение данных из предыдущего фрагмента.
        Bundle bundle = getArguments();

        if (bundle != null) {
            id = bundle.getString("ID");
            name = bundle.getString("Name");
            poster = bundle.getString("ProfilePath");
            Log.d("ID", id);
        }
        Name.setText(name);
        NameHeader.setTitle(name);

        Picasso.get()
                .load(Constants.COVER_W780_URL + poster)
                .into(Poster);

        //Получение дополнительных данных, помимо имени и фотографии Актера.
        ActorQueryTask actorAsync = new ActorQueryTask();
        actorAsync.execute("" + Constants.ACTOR_URL + id);

        return v;
    }


    @SuppressLint("StaticFieldLeak")
    public class ActorQueryTask extends AsyncTask<String, Movie, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... params) {

            String queryUrl = params[0];
            URL url = Networking.buildUrl(queryUrl);
            String jsonResult;

            try {

                jsonResult = Networking.getJson(url);
                JSONObject json = new JSONObject(jsonResult);
                birthday = json.getString("birthday");
                place_of_birth = json.getString("place_of_birth");
                popularity = json.getString("popularity");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Movie... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            Birtday.setText(birthday);
            PlaceOfBirth.setText(place_of_birth);
            Popularity.setText(popularity);


        }
    }
}
