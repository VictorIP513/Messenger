package ru.android.messenger.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import ru.android.messenger.R;

public abstract class ActivityWithNavigationDrawer extends ActivityWithAlerts {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private Class activityToStart;

    protected void init(int layoutResourceId) {
        setContentView(layoutResourceId);

        findViews();
        setNavigationItemSelectedListener();
        addDrawerLayoutListener();
    }

    private void setNavigationItemSelectedListener() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id == R.id.navigation_drawer_settings) {
                            activityToStart = SettingsActivity.class;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    private void addDrawerLayoutListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                //unused method
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                //unused method
            }

            @Override
            public void onDrawerStateChanged(int i) {
                //unused method
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                if (activityToStart != null) {
                    Intent intent = new Intent(
                            ActivityWithNavigationDrawer.this, activityToStart);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    activityToStart = null;
                }
            }
        });
    }

    private void findViews() {
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
    }
}
