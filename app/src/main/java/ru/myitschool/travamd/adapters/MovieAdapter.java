package ru.myitschool.travamd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.AdapterViewHolder> {
    private final OnChangeFragmentListener mChangeFragmentListener;
    private final int mCardType;
    private final ArrayList<Movie> movieList;
    private View view;

    public MovieAdapter(ArrayList<Movie> movieList, int CardType, OnChangeFragmentListener changeFragmentListener) {
        this.movieList = movieList;
        this.mCardType = CardType;
        mChangeFragmentListener = changeFragmentListener;
    }

    @NotNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        switch (mCardType) {
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

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.AdapterViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover_image;
        private TextView movieNameOriginal, movieDesc, movieName, movieYear;
        private final CardView movieCard;

        public AdapterViewHolder(View view) {
            super(view);

            //Аналогично, разный тип карточек содержит внутри разные поля информации.
            cover_image = view.findViewById(R.id.movie_cover);
            movieCard = view.findViewById(R.id.movie_card);

            switch (mCardType) {
                case 1:
                    movieName = view.findViewById(R.id.movie_name);
                    movieDesc = view.findViewById(R.id.movie_desc);
                    break;
                case 2:
                    movieName = view.findViewById(R.id.movie_name);
                    movieNameOriginal = view.findViewById(R.id.movie_name_original);
                    movieYear = view.findViewById(R.id.movie_year);
                    break;
                case 3:
                    movieName = view.findViewById(R.id.movie_name);
            }
        }

        public void bind(final Movie movie) {
            //Разный тип карточек имеет разный тип полей, содержащий различную информацию.
            switch (mCardType) {
                case 1:
                    movieName.setText(movie.getMovieName());
                    movieDesc.setText(movie.getMovieOverview());
                    break;
                case 2:
                    movieName.setText(movie.getMovieName());
                    movieNameOriginal.setText(movie.getMovieNameOriginal());
                    movieYear.setText(movie.getMovieYear());
                    break;
                case 3:
                    movieName.setText(movie.getMovieName());
                    break;
            }

            int width = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_width);
            int height = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_height);

            Picasso.get()
                    // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                    .load(Constants.COVER_W780_URL + movie.getCover_url())
                    .resize(width, height)
                    .centerCrop()
                    .into(cover_image);

            movieCard.setOnClickListener(v -> {
                DescriptionFragment fragment = DescriptionFragment.newInstance(
                        movie.getMovieId(), movie.getCover_url(),
                        movie.getMovieName(), movie.getMovieNameOriginal(),
                        movie.getMovieOverview(),movie.getMovieYear());

                mChangeFragmentListener.onChange(fragment);
            });
        }
    }

}

