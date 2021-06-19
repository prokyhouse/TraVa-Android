package ru.myitschool.travamd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;

public class MovieShortAdapter extends RecyclerView.Adapter<MovieShortAdapter.MovieViewHolder> {
    private final OnChangeFragmentListener mChangeFragmentListener;
    private final List<Movie> mData;

    public MovieShortAdapter(List<Movie> data, OnChangeFragmentListener changeFragmentListener) {
        mData = data;
        mChangeFragmentListener = changeFragmentListener;
    }

    //Здесь мы передаём View, которая будет отображаться как элемент списка
    @NotNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return new MovieViewHolder(convertView);
    }

    //Задаём ей значения
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(Movie movie) {
        mData.add(movie);
        notifyItemInserted(getItemCount());
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, originalName, year;
        View card;

        MovieViewHolder(View itemView) {
            super(itemView);
            //Нахождение объектов по ID. Никакой логики.
            card = itemView;
            image = itemView.findViewById(R.id.movie_cover);
            name = itemView.findViewById(R.id.movie_name);
            originalName = itemView.findViewById(R.id.movie_name_original);
            year = itemView.findViewById(R.id.movie_year);
        }

        public void bind(Movie movie) {
            //Заполнение элементов + обработка
            name.setText(movie.getMovieName());
            originalName.setText(movie.getMovieNameOriginal());
            year.setText(movie.getMovieYear());

            int width = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_width);
            int height = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_height);

            Picasso.get()
                    // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                    .load(Constants.COVER_W780_URL + movie.getCover_url())
                    .resize(width, height)
                    .centerCrop()
                    .into(image);

            card.setOnClickListener(v -> {
                DescriptionFragment fragment = DescriptionFragment.newInstance(
                        movie.getMovieId(), movie.getCover_url(),
                        movie.getMovieName(), movie.getMovieNameOriginal(),
                        movie.getMovieOverview(),movie.getMovieYear());

                mChangeFragmentListener.onChange(fragment);
            });

        }
    }
}
