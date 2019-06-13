package ru.android.messenger.presenter.implementation;

import android.content.Context;

import java.util.List;

import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.UserUtils;
import ru.android.messenger.model.utils.http.HttpUtils;
import ru.android.messenger.model.utils.http.OnUsersListLoadedListener;
import ru.android.messenger.presenter.BlockedUsersPresenter;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class BlockedUsersPresenterImplementation implements BlockedUsersPresenter {

    private ViewWithUsersRecyclerView viewWithUsersRecyclerView;

    public BlockedUsersPresenterImplementation(ViewWithUsersRecyclerView viewWithUsersRecyclerView) {
        this.viewWithUsersRecyclerView = viewWithUsersRecyclerView;
    }

    @Override
    public void fillBlockedUsersList() {
        final Context context = viewWithUsersRecyclerView.getContext();
        HttpUtils.getAllBlockedUsers(context, new OnUsersListLoadedListener() {
            @Override
            public void onUsersListLoaded(List<User> usersList) {
                UserUtils.deleteCurrentUserFromUserList(usersList, context);
                UserUtils.convertAndSetUsersToView(usersList, viewWithUsersRecyclerView);
            }
        });
    }
}
