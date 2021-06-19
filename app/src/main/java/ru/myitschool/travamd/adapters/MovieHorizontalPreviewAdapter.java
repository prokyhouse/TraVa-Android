package ru.myitschool.travamd.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.callbacks.OnChangeFragmentListener;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;

public class MovieHorizontalPreviewAdapter extends RecyclerView.Adapter<MovieHorizontalPreviewAdapter.MovieViewHolder>  {
    private final OnChangeFragmentListener mChangeFragmentListener;
    private final List<Movie> mData;
    private final List<Boolean> mLike;

    public MovieHorizontalPreviewAdapter(List<Movie> data, List<Boolean> likes, OnChangeFragmentListener changeFragmentListener) {
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
    @NotNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal_preview, parent, false);
        return new MovieViewHolder(convertView);
    }

    //Задаём ей значения
    @Override
    public void onBindViewHolder(@NotNull MovieViewHolder holder, int position) {
        Movie movie = mData.get(position);
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
        ImageView image;
       // ToggleButton like;
        TextView name;
        View card;

        MovieViewHolder(View itemView) {
            super(itemView);
            //Нахождение объектов по ID. Никакой логики.
            card = itemView;
            image = itemView.findViewById(R.id.movie_cover);
           // like = (ToggleButton) itemView.findViewById(R.id.like);
            name = itemView.findViewById(R.id.movie_name);
        }

        public void bind(Movie movie) {
            //Заполнение элементов + обработка
            name.setText(movie.getMovieName());

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
         /*   like.setChecked(mLike.get(position));
            //Обработка нажатия отметки "нравится"
            like.setOnClickListener(v -> {
                ToggleButton button = (ToggleButton) v;
                if (button.isChecked()) {
                    Database.addMovie(v.getContext(), movie.getMovieId());
                    mLike.set(getAdapterPosition(),true);
                } else {Database.removeMovie(v.getContext(), movie.getMovieId());
                    mLike.set(getAdapterPosition(),false);
                }
            });*/
        }


    }
}