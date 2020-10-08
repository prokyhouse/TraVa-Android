package ru.myitschool.travamd.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import ru.myitschool.travamd.fragments.ActorFragment;
import ru.myitschool.travamd.models.Actor;
import ru.myitschool.travamd.utils.Constants;

/**
 * Created by kirillprokofev on 01.06.17.
 */

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.AdapterViewHolder> {

    private ArrayList<Actor> actorList = new ArrayList<Actor>();
    private Context context;
    private View view;

    public ActorAdapter(ArrayList<Actor> actorList, Context context) {
        this.actorList = actorList;
        this.context = context;

    }

    @Override
    public ActorAdapter.AdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_actor, parent, false);
        ActorAdapter.AdapterViewHolder viewHolder = new ActorAdapter.AdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActorAdapter.AdapterViewHolder holder, int position) {

        final Actor actor = actorList.get(position);

        //Вывод имени Актера и его фотографии.
        holder.actorName.setText(actor.getActorName());

        Picasso.with(context)
                .load(Constants.COVER_W780_URL + actor.getActorProfilePath())
                .resize(context.getResources().getDimensionPixelSize(R.dimen.imageview_width),
                        context.getResources().getDimensionPixelSize(R.dimen.imageview_height))
                .centerCrop()
                .into(holder.cover_image);

        //Обработка нажатия на карточку Актера.
        holder.actorCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Подготовка к переходу в фрагмент ActorFragment.
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                ActorFragment rFragment = new ActorFragment();

                //Передача имеющихся данных между фрагментами для сохранения трафика.
                Bundle bundle = new Bundle();
                bundle.putString("ID", actor.getActorID());
                bundle.putString("Name", actor.getActorName());
                bundle.putString("ProfilePath", actor.getActorProfilePath());

                rFragment.setArguments(bundle);

                //Переход в ActorFragment.
                activity.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_container, rFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return actorList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView cover_image;
        TextView actorName;
        CardView actorCard;

        public AdapterViewHolder(View view) {
            super(view);

            //Элементы карточки Актера.
            actorCard = (CardView) view.findViewById(R.id.actor_card);
            cover_image = (ImageView) view.findViewById(R.id.actor_cover);
            actorName = (TextView) view.findViewById(R.id.actor_name);

            context = view.getContext();
        }
    }
}
