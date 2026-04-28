package com.example.mad_project;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Handles the shared BottomNavigationView setup used across
 * HomeActivity, Progress, and EditProfileActivity.
 *
 * WHY THIS EXISTS:
 * The same 12-line bottom nav block was copy-pasted in 3 Activities.
 * Any new tab or navigation change had to be replicated manually.
 *
 * HOW TO USE:
 *   BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
 *   NavigationHelper.setup(this, bottomNav, R.id.nav_roadmaps);
 *
 * HOW TO ADD A NEW TAB:
 *   1. Add the item to res/menu/bottom_nav_menu.xml
 *   2. Add a new else-if block inside setup() below
 *   3. All Activities using NavigationHelper get the new tab automatically
 */
public final class NavigationHelper {

    private NavigationHelper() {} // Utility class — no instantiation

    /**
     * Configures the BottomNavigationView and sets the active tab.
     *
     * @param activity      The host Activity (used for launching Intents)
     * @param bottomNav     The BottomNavigationView to configure
     * @param currentItemId The menu item ID that represents the current screen
     *                      (e.g., R.id.nav_roadmaps)
     */
    public static void setup(AppCompatActivity activity,
                             BottomNavigationView bottomNav,
                             int currentItemId) {

        bottomNav.setSelectedItemId(currentItemId);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_roadmaps) {
                if (currentItemId != R.id.nav_roadmaps) {
                    activity.startActivity(new Intent(activity, HomeActivity.class));
                    activity.finish();
                }
                return true;

            } else if (id == R.id.nav_progress) {
                if (currentItemId != R.id.nav_progress) {
                    activity.startActivity(new Intent(activity, Progress.class));
                    activity.finish();
                }
                return true;

            } else if (id == R.id.nav_profile) {
                if (currentItemId != R.id.nav_profile) {
                    activity.startActivity(new Intent(activity, EditProfileActivity.class));
                    activity.finish();
                }
                return true;
            }

            return false;
        });
    }
}
