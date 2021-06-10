package ru.myitschool.travamd.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Database;

public class MovieHorizontalAdapter extends RecyclerView.Adapter<MovieHorizontalAdapter.MovieViewHolder> {
    private final OnChangeFragmentListener mChangeFragmentListener;
    private final List<Movie> mData;
    final List<Boolean> mLike;

    public MovieHorizontalAdapter(List<Movie> data, List<Boolean> likes, OnChangeFragmentListener changeFragmentListener) {
        mChangeFragmentListener = changeFragmentListener;
        if (data == null || likes == null) {
            mData = new ArrayList<>();
            mLike = new ArrayList<>();
        } else {
            mData = data;
            mLike = likes;
        }
    }

    //Здесь мы передаём View, которая будет отображаться как элемент списка
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal, parent, false);
        return new MovieViewHolder(convertView);
    }
    //Задаём ей значения
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Метод позволяет добавлять из фрагментов/активностей новые данные (дозагрузка)
    public void add(Movie movie, boolean isFavorite) {
        mData.add(movie);
        mLike.add(isFavorite);
        notifyItemInserted(getItemCount());
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final ToggleButton like;
        private final TextView name, description;
        private final View card;

        MovieViewHolder(View itemView) {
            super(itemView);
            //Нахождение объектов по ID. Никакой логики.
            card = itemView;
            image = (ImageView) itemView.findViewById(R.id.movie_cover);
            like = (ToggleButton) itemView.findViewById(R.id.like);
            name = (TextView) itemView.findViewById(R.id.movie_name);
            description = (TextView) itemView.findViewById(R.id.movie_desc);
        }

        public void bind(Movie movie, int position) {
            //Заполнение элементов + обработка
            name.setText(movie.getMovieName());
            description.setText(movie.getMovieOverview());

            int width = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_width);
            int height = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_height);

            Picasso.get()
                    // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                    .load(Constants.COVER_W780_URL + movie.getCover_url())
                    .resize(width, height)
                    .centerCrop()
                    .into(image);
            //Обработка нажатия на карточку
            card.setOnClickListener(v -> {
                DescriptionFragment fragment = DescriptionFragment.newInstance(
                        movie.getMovieId(), movie.getCover_url(),
                        movie.getMovieName(), movie.getMovieNameOriginal(),
                        movie.getMovieOverview(), movie.getMovieYear());

                mChangeFragmentListener.onChange(fragment);
            });
            like.setChecked(mLike.get(position));
            //Обработка нажатия отметки "нравится"
            like.setOnClickListener(v -> {
                ToggleButton button = (ToggleButton) v;
                if (button.isChecked()) {
                    Database.addMovie(v.getContext(), movie.getMovieId());
                    mLike.set(getAdapterPosition(),true);
                } else {Database.removeMovie(v.getContext(), movie.getMovieId());
                    mLike.set(getAdapterPosition(),false);
                }
            });

        }
    }
}
