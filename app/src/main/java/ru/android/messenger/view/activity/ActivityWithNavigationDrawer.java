package ru.android.messenger.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.android.messenger.R;
import ru.android.messenger.presenter.NavigationDrawerPresenter;
import ru.android.messenger.presenter.implementation.NavigationDrawerPresenterImplementation;
import ru.android.messenger.view.interfaces.NavigationDrawerView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class ActivityWithNavigationDrawer extends ActivityWithAlerts
        implements NavigationDrawerView {

    private NavigationDrawerPresenter navigationDrawerPresenter;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView imageViewProfile;
    private TextView textViewLogin;
    private TextView textViewName;
    private TextView textViewEmail;

    private Class activityToStart;

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setProfileImageToNavigationDrawer(Bitmap bitmap) {
        imageViewProfile = findViewById(R.id.image_view_profile);
        imageViewProfile.setImageBitmap(bitmap);
    }

    @Override
    public void setUserDataToNavigationDrawer(String firstName, String surname,
                                              String login, String email) {
        String name = firstName + " " + surname;
        textViewLogin.setText(login);
        textViewEmail.setText(email);
        textViewName.setText(name);
    }

    protected void init(int layoutResourceId) {
        setContentView(layoutResourceId);

        navigationDrawerPresenter =
                new NavigationDrawerPresenterImplementation(this);

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
            public void onDrawerOpened(@NonNull View view) {
                findDrawerLayoutViews();
                navigationDrawerPresenter.fillUserInformationToNavigationDrawer();
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

            @Override
            public void onDrawerSlide(@NonNull View view, float v) {
                //unused method
            }

            @Override
            public void onDrawerStateChanged(int i) {
                //unused method
            }
        });
    }

    private void findViews() {
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
    }

    private void findDrawerLayoutViews() {
        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewLogin = findViewById(R.id.text_view_login);
        textViewName = findViewById(R.id.text_view_name);
        textViewEmail = findViewById(R.id.text_view_email);
    }
}
