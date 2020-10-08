package ru.myitschool.travamd.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;
import ru.myitschool.travamd.utils.Database;

public class MovieHorizontalPreviewAdapter extends RecyclerView.Adapter<MovieHorizontalPreviewAdapter.MovieViewHolder>  {
    private Context mContext;
    private List<Movie> mData;
    private List<Boolean> mLike;

    public MovieHorizontalPreviewAdapter(Context context, List<Movie> data, List<Boolean> likes) {
        mContext = context;
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
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_horizontal_preview, parent, false);
        return new MovieViewHolder(convertView);
    }

    //Задаём ей значения
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mData.get(position);
        //Заполнение элементов + обработка
        holder.name.setText(movie.getMovieName());
        Picasso.with(mContext)
                // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                .load(Constants.COVER_W780_URL + movie.getCover_url())
                .resize(mContext.getResources().getDimensionPixelSize(R.dimen.imageview_width),
                        mContext.getResources().getDimensionPixelSize(R.dimen.imageview_height))
                .centerCrop()
                .into(holder.image);
        //Обработка нажатия на карточку
        holder.card.setOnClickListener(v -> {
            DescriptionFragment fragment = DescriptionFragment.newInstance(
                    movie.getMovieId(), movie.getCover_url(),
                    movie.getMovieName(), movie.getMovieNameOriginal(),
                    movie.getMovieOverview(), movie.getMovieYear());

            ((Activity) mContext).getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        holder.like.setChecked(mLike.get(position));
        //Обработка нажатия отметки "нравится"
        holder.like.setOnClickListener(v -> {
            ToggleButton button = (ToggleButton) v;
            if (button.isChecked()) {
                Database.addMovie(v.getContext(), movie.getMovieId());
                mLike.set(holder.getAdapterPosition(),true);
            } else {Database.removeMovie(v.getContext(), movie.getMovieId());
                mLike.set(holder.getAdapterPosition(),false);
            }
        });
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

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ToggleButton like;
        TextView name;
        View card;

        MovieViewHolder(View itemView) {
            super(itemView);
            //Нахождение объектов по ID. Никакой логики.
            card = itemView;
            image = (ImageView) itemView.findViewById(R.id.movie_cover);
            like = (ToggleButton) itemView.findViewById(R.id.like);
            name = (TextView) itemView.findViewById(R.id.movie_name);
        }
    }
}