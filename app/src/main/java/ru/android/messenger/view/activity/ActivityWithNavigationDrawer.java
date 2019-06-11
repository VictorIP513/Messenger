package ru.android.messenger.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.android.messenger.R;
import ru.android.messenger.presenter.NavigationDrawerPresenter;
import ru.android.messenger.presenter.implementation.NavigationDrawerPresenterImplementation;
import ru.android.messenger.view.interfaces.NavigationDrawerView;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class ActivityWithNavigationDrawer extends ActivityWithAlerts
        implements NavigationDrawerView {

    private NavigationDrawerPresenter navigationDrawerPresenter;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView navigationDrawerImageViewProfile;
    private TextView navigationDrawerTextViewLogin;
    private TextView navigationDrawerTextViewName;
    private TextView navigationDrawerTextViewEmail;
    private Toolbar toolbar;

    private Class activityToStart;
    private boolean createdNavigationDrawerToolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (createdNavigationDrawerToolbar && item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setProfileImageToNavigationDrawer(Bitmap bitmap) {
        navigationDrawerImageViewProfile = findViewById(R.id.navigation_drawer_image_view_profile);
        navigationDrawerImageViewProfile.setImageBitmap(bitmap);
    }

    @Override
    public void setUserDataToNavigationDrawer(String firstName, String surname,
                                              String login, String email) {
        String name = firstName + " " + surname;
        navigationDrawerTextViewLogin.setText(login);
        navigationDrawerTextViewEmail.setText(email);
        navigationDrawerTextViewName.setText(name);
    }

    protected void init(int layoutResourceId) {
        setContentView(layoutResourceId);

        navigationDrawerPresenter =
                new NavigationDrawerPresenterImplementation(this);

        findViewsInNavigationDrawer();
        setNavigationItemSelectedListener();
        addDrawerLayoutListener();
        configureToolbar();
    }

    protected void configureToolbar() {
        ViewUtils.createActionBarWithNavigationDrawerButtonForActivity(this, toolbar);
        createdNavigationDrawerToolbar = true;
    }

    private void setNavigationItemSelectedListener() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
                            case R.id.navigation_drawer_dialogs:
                                activityToStart = DialogListActivity.class;
                                break;
                            case R.id.navigation_drawer_friends:
                                activityToStart = FriendListActivity.class;
                                break;
                            case R.id.navigation_drawer_settings:
                                activityToStart = SettingsActivity.class;
                                break;
                            case R.id.navigation_drawer_logout:
                                navigationDrawerPresenter.logout();
                                activityToStart = LoginActivity.class;
                                break;
                            default:
                                break;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    private void addDrawerLayoutListener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
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
                findDrawerLayoutViews();
                navigationDrawerPresenter.fillUserInformationToNavigationDrawer();
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                //unused method
            }

            @Override
            public void onDrawerStateChanged(int i) {
                //unused method
            }
        });
    }

    private void findViewsInNavigationDrawer() {
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
    }

    private void findDrawerLayoutViews() {
        navigationDrawerImageViewProfile = findViewById(R.id.navigation_drawer_image_view_profile);
        navigationDrawerTextViewLogin = findViewById(R.id.navigation_drawer_text_view_login);
        navigationDrawerTextViewName = findViewById(R.id.navigation_drawer_text_view_name);
        navigationDrawerTextViewEmail = findViewById(R.id.navigation_drawer_text_view_email);
    }
}
