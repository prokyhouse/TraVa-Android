package ru.myitschool.travamd.activities;

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


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setupDrawer();
        // setupToolbar();

       /* getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_container, new MainFragment())
                .commit();*/

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, new MainFragment())
                .commit();

        toolbar =  findViewById(R.id.toolbar);



        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
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

    //Установка начальных параметров бокового меню
  /*  private void setupDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setCheckedItem(R.id.main);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.isChecked()) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            Fragment fragment;
            //Выборка элементов
            switch (item.getItemId()) {
                case R.id.main:
                    fragment = new MainFragment();
                    break;
                case R.id.favorite:
                    fragment = new LikeFragment();
                    break;
                case R.id.popular:
                    fragment = new PopularFragment();
                    break;
                case R.id.movie:
                    fragment = new UpcomingFragment();
                    break;
                case R.id.top:
                    fragment = new TopFragment();
                    break;
                case R.id.serials:
                    fragment = new SeriesFragment();
                    break;
                default:
                    fragment = new AboutFragment();
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            item.setChecked(true);
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
*/
    //Установка начальных параметров Toolbar
   /* private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }*/

    @Override
    public void onBackPressed() {
       /* if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else */
            super.onBackPressed();
        toolbar.setSubtitle("");
    }
}
