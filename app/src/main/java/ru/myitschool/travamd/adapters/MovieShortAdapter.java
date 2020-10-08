package ru.myitschool.travamd.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.fragments.DescriptionFragment;
import ru.myitschool.travamd.models.Movie;
import ru.myitschool.travamd.utils.Constants;

public class MovieShortAdapter extends RecyclerView.Adapter<MovieShortAdapter.MovieViewHolder> {
    private Context mContext;
    private List<Movie> mData;

    public MovieShortAdapter(Context context, List<Movie> data) {
        mContext=context;
        mData = data;
    }

    //Здесь мы передаём View, которая будет отображаться как элемент списка
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie, parent, false);
        return new MovieViewHolder(convertView);
    }

    //Задаём ей значения
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mData.get(position);
        //Заполнение элементов + обработка
        holder.name.setText(movie.getMovieName());
        holder.originalName.setText(movie.getMovieNameOriginal());
        holder.year.setText(movie.getMovieYear());
        Picasso.with(mContext)
                // "movie.getCover_url()" содержит только название картинки формата "Название.jpg".
                .load(Constants.COVER_W780_URL + movie.getCover_url())
                .resize(mContext.getResources().getDimensionPixelSize(R.dimen.imageview_width),
                        mContext.getResources().getDimensionPixelSize(R.dimen.imageview_height))
                .centerCrop()
                .into(holder.image);
        holder.card.setOnClickListener(v -> {
            DescriptionFragment fragment = DescriptionFragment.newInstance(
                    movie.getMovieId(), movie.getCover_url(),
                    movie.getMovieName(), movie.getMovieNameOriginal(),
                    movie.getMovieOverview(),movie.getMovieYear());

            ((Activity)mContext).getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void add(Movie movie) {
        mData.add(movie);
        notifyItemInserted(getItemCount());
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, originalName, year;
        View card;

        MovieViewHolder(View itemView) {
            super(itemView);
            //Нахождение объектов по ID. Никакой логики.
            card=itemView;
            image = (ImageView) itemView.findViewById(R.id.movie_cover);
            name = (TextView) itemView.findViewById(R.id.movie_name);
            originalName = (TextView) itemView.findViewById(R.id.movie_name_original);
            year = (TextView) itemView.findViewById(R.id.movie_year);
        }
    }
}
