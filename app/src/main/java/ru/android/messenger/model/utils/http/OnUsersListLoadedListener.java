package ru.android.messenger.model.utils.http;

import java.util.List;

import ru.android.messenger.model.dto.User;

public interface OnUsersListLoadedListener {

    void onUsersListLoaded(List<User> usersList);
}
