package ru.android.messenger.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.presenter.UsersSearchPresenter;
import ru.android.messenger.presenter.implementation.UsersSearchPresenterImplementation;
import ru.android.messenger.view.interfaces.ViewWithAlerts;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;
import ru.android.messenger.view.utils.ViewUtils;
import ru.android.messenger.view.utils.recyclerview.RecyclerViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class UsersSearchActivity extends ActivityWithNavigationDrawer
        implements ViewWithAlerts, ViewWithUsersRecyclerView {

    private UsersSearchPresenter usersSearchPresenter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_users_search);

        usersSearchPresenter = new UsersSearchPresenterImplementation(
                this, this);

        findViews();
        usersSearchPresenter.fillUsersList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        RecyclerViewUtils.configureUsersSearchMenu(menu, this, recyclerView);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        String actionBarTitle = getString(R.string.users_search_activity_action_bar_title);
        ViewUtils.createActionBarWithBackButtonForActivity(this, toolbar, actionBarTitle);
    }

    @Override
    public void setUsersList(List<UserFromView> users) {
        RecyclerViewUtils.configureRecyclerViewForUsers(recyclerView, this, users);
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recycler_view_users_list);
    }
}
