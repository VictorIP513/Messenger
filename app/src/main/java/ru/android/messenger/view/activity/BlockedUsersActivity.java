package ru.android.messenger.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.presenter.BlockedUsersPresenter;
import ru.android.messenger.presenter.implementation.BlockedUsersPresenterImplementation;
import ru.android.messenger.view.interfaces.ViewWithAlerts;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;
import ru.android.messenger.view.utils.ViewUtils;
import ru.android.messenger.view.utils.recyclerview.RecyclerViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlockedUsersActivity extends ActivityWithNavigationDrawer
        implements ViewWithAlerts, ViewWithUsersRecyclerView {

    private BlockedUsersPresenter blockedUsersPresenter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_blocked_users);

        blockedUsersPresenter = new BlockedUsersPresenterImplementation(this);

        findViews();
        blockedUsersPresenter.fillBlockedUsersList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        blockedUsersPresenter.fillBlockedUsersList();
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
        String actionBarTitle = getString(R.string.blocked_users_activity_action_bar_title);
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
