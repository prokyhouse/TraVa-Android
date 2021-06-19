package ru.myitschool.travamd.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import ru.myitschool.travamd.R;
import ru.myitschool.travamd.fragments.AboutFragment;
import ru.myitschool.travamd.fragments.LikeFragment;
import ru.myitschool.travamd.fragments.MainFragment;
import ru.myitschool.travamd.fragments.PopularFragment;
import ru.myitschool.travamd.fragments.SeriesFragment;
import ru.myitschool.travamd.fragments.TopFragment;
import ru.myitschool.travamd.fragments.UpcomingFragment;
import ru.myitschool.travamd.utils.Constants;

/**
 * Created by Kirill Prokofyev on 19.06.21.
 */

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, new MainFragment())
                .commit();

        setupNavBar();

    }

    private void setupNavBar(){

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        toolbar =  findViewById(R.id.toolbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment fragment = null;
                        switch (item.getItemId()) {
                            case R.id.main:
                                fragment = new MainFragment();
                                toolbar.setSubtitle("");
                                break;
                            case R.id.popular:
                                fragment = new PopularFragment();
                                toolbar.setSubtitle("Популярное");
                                break;
                            case R.id.movie:
                                fragment = new UpcomingFragment();
                                toolbar.setSubtitle("Скоро");
                                break;
                            case R.id.top:
                                fragment = new UpcomingFragment();
                                toolbar.setSubtitle("Лучшие фильмы");
                                break;
                            case R.id.serials:
                                fragment = new SeriesFragment();
                                toolbar.setSubtitle("Сериалы");
                                break;
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frame_container, fragment)
                                .commit();

                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
        toolbar.setSubtitle("");
    }
}
