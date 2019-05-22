package ru.android.messenger.view.activity;

import android.os.Bundle;

import ru.android.messenger.R;
import ru.android.messenger.view.interfaces.UsersSearchView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class UsersSearchActivity extends ActivityWithUsersRecyclerView implements UsersSearchView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_users_search);

        usersRecyclerViewPresenter.fillUsersList();
    }
}
