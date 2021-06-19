package ru.myitschool.travamd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;

import ru.myitschool.travamd.R;

//Фрагмент, содержащий базовые компоненты, такие как AppbarLayout, CollapsingToolbarLayout и CoordinatorLayout.
public class AboutFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about_app,
                container,
                false);

        AppBarLayout appbarLayout = v.findViewById(R.id.materialup_appbar);
        mProfileImage = v.findViewById(R.id.materialup_profile_image);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        return v;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }


}

