package ru.myitschool.travamd.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;


public class Movie_adapter extends RecyclerView.Adapter<Movie_adapter.AdapterViewHolder> {
    private Context context;
    private int CardType;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private View view;

    public Movie_adapter(Context context, ArrayList<Movie> movieList, int CardType) {
        this.movieList = movieList;
        this.CardType = CardType;
        this.context=context;
    }

    @Override
    public AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (CardType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal_preview_main, parent, false);
                break;
        }

        AdapterViewHolder viewHolder = new AdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Movie_adapter.AdapterViewHolder holder, int position) {
        final Movie movie = movieList.get(position);

        //Разный тип карточек имеет разный тип полей, содержащий различную информацию.
        switch (CardType) {
            case 1:
                holder.movieName.setText(movie.getMovieName());
                holder.movieDesc.setText(movie.getMovieOverview());
                break;
            case 2:
                holder.movieName.setText(movie.getMovieName());
                holder.movieNameOriginal.setText(movie.getMovieNameOriginal());
                holder.movieYear.setText(movie.getMovieYear());
                break;
            case 3:
                holder.movieName.setText(movie.getMovieName());
                break;
        }
        Picasso.with(context)
                // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                .load(Constants.COVER_W780_URL + movie.getCover_url())
                .resize(context.getResources().getDimensionPixelSize(R.dimen.imageview_width),
                        context.getResources().getDimensionPixelSize(R.dimen.imageview_height))
                .centerCrop()
                .into(holder.cover_image);

        holder.movieCard.setOnClickListener(v -> {


            DescriptionFragment fragment = DescriptionFragment.newInstance(
                    movie.getMovieId(), movie.getCover_url(),
                    movie.getMovieName(), movie.getMovieNameOriginal(),
                    movie.getMovieOverview(),movie.getMovieYear());

            ((Activity)context).getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();

        });

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView cover_image;
        TextView movieNameOriginal, movieDesc, movieName, movieYear;
        CardView movieCard;

        public AdapterViewHolder(View view) {
            super(view);

            //Аналогично, разный тип карточек содержит внутри разные поля информации.
            cover_image = (ImageView) view.findViewById(R.id.movie_cover);
            movieCard = (CardView) view.findViewById(R.id.movie_card);

            switch (CardType) {
                case 1:
                    movieName = (TextView) view.findViewById(R.id.movie_name);
                    movieDesc = (TextView) view.findViewById(R.id.movie_desc);
                    break;
                case 2:
                    movieName = (TextView) view.findViewById(R.id.movie_name);
                    movieNameOriginal = (TextView) view.findViewById(R.id.movie_name_original);
                    movieYear = (TextView) view.findViewById(R.id.movie_year);
                    break;
                case 3:
                    movieName = (TextView) view.findViewById(R.id.movie_name);
            }
        }
    }

}

