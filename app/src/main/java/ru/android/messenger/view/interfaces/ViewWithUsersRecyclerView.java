package ru.android.messenger.view.interfaces;

import android.content.Context;

import java.util.List;

import ru.android.messenger.model.dto.UserFromView;

public interface ViewWithUsersRecyclerView {

    Context getContext();

    void setUsersList(List<UserFromView> users);
}
