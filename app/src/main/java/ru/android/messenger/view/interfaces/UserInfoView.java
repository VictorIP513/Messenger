package ru.android.messenger.view.interfaces;

import ru.android.messenger.model.dto.response.FriendStatus;

public interface UserInfoView extends ViewWithAlerts {

    void setFriendStatus(FriendStatus friendStatus);
}
