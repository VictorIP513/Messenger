package ru.android.messenger.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.presenter.UsersSearchPresenter;
import ru.android.messenger.presenter.implementation.UsersSearchPresenterImplementation;
import ru.android.messenger.view.interfaces.UsersSearchView;
import ru.android.messenger.view.utils.recyclerview.RecyclerViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class UsersSearchActivity extends ActivityWithNavigationDrawer implements UsersSearchView {

    private UsersSearchPresenter usersSearchPresenter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_users_search);

        usersSearchPresenter = new UsersSearchPresenterImplementation(this);

        findViews();
        usersSearchPresenter.fillUsersList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        RecyclerViewUtils.configureUsersSearchMenu(menu, this, recyclerView);
        return true;
    }

    @Override
    public void setUsersList(List<UserFromView> users) {
        RecyclerViewUtils.configureRecyclerViewForUsers(recyclerView, this, users);
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recycler_view_users_list);
    }
}
