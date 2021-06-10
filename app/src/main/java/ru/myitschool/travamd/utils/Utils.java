package ru.myitschool.travamd.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import ru.myitschool.travamd.R;

public class Utils {
    public static void replaceFragment(final FragmentManager manager, final Fragment fragment) {
        manager
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
