package ru.android.messenger.view.interfaces;

import java.util.List;

import ru.android.messenger.model.dto.UserFromView;

public interface ViewWithUsersRecyclerView extends ViewWithAlerts {

    void setUsersList(List<UserFromView> users);
}
