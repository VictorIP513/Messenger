package ru.android.messenger.view.interfaces;

import ru.android.messenger.model.api.FriendStatus;

public interface UserInfoView extends ViewWithAlerts {

    void setFriendStatus(FriendStatus friendStatus);
}
