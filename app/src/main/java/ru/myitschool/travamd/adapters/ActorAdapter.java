package ru.myitschool.travamd.adapters;

import android.os.Bundle;
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
import ru.myitschool.travamd.fragments.ActorFragment;
import ru.myitschool.travamd.models.Actor;
import ru.myitschool.travamd.utils.Constants;

/**
 * Created by Kirill Prokofyev on 01.06.17.
 */

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.AdapterViewHolder> {
    private final OnChangeFragmentListener mChangeFragmentListener;
    private final ArrayList<Actor> actorList;

    public ActorAdapter(ArrayList<Actor> actorList, OnChangeFragmentListener changeFragmentListener) {
        this.actorList = actorList;
        mChangeFragmentListener = changeFragmentListener;
    }

    @NotNull
    @Override
    public ActorAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_actor, parent, false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorAdapter.AdapterViewHolder holder, int position) {
        holder.bind(actorList.get(position));
    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private final ImageView cover_image;
        private final TextView actorName;
        private final CardView actorCard;

        public AdapterViewHolder(View view) {
            super(view);

            //Элементы карточки Актера.
            actorCard = view.findViewById(R.id.actor_card);
            cover_image = view.findViewById(R.id.actor_cover);
            actorName = view.findViewById(R.id.actor_name);
        }

        public void bind(final Actor actor) {
            //Вывод имени Актера и его фотографии.
            actorName.setText(actor.getActorName());

            int width = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_width);
            int height = itemView.getResources().getDimensionPixelSize(R.dimen.imageview_height);

            Picasso.get()
                    .load(Constants.COVER_W780_URL + actor.getActorProfilePath())
                    .resize(width, height)
                    .centerCrop()
                    .into(cover_image);

            //Обработка нажатия на карточку Актера.
            actorCard.setOnClickListener(v -> {
                //Подготовка к переходу в фрагмент ActorFragment.
                ActorFragment rFragment = new ActorFragment();

                //Передача имеющихся данных между фрагментами для сохранения трафика.
                Bundle bundle = new Bundle();
                bundle.putString("ID", actor.getActorID());
                bundle.putString("Name", actor.getActorName());
                bundle.putString("ProfilePath", actor.getActorProfilePath());

                rFragment.setArguments(bundle);

                //Переход в ActorFragment.
                mChangeFragmentListener.onChange(rFragment);
            });
        }
    }
}
