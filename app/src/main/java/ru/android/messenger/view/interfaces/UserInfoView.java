package ru.android.messenger.view.interfaces;

import android.content.Context;

import ru.android.messenger.model.api.FriendStatus;

public interface UserInfoView extends ViewWithAlerts {

    Context getContext();

    void setFriendStatus(FriendStatus friendStatus);
}
