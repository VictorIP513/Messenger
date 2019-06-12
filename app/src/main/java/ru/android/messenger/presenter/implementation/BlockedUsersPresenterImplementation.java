package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.UserUtils;
import ru.android.messenger.presenter.BlockedUsersPresenter;
import ru.android.messenger.view.interfaces.ViewWithAlerts;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class BlockedUsersPresenterImplementation implements BlockedUsersPresenter {

    private ViewWithAlerts viewWithAlerts;
    private ViewWithUsersRecyclerView viewWithUsersRecyclerView;
    private Repository repository;

    public BlockedUsersPresenterImplementation(ViewWithAlerts viewWithAlerts,
                                               ViewWithUsersRecyclerView viewWithUsersRecyclerView) {
        this.viewWithAlerts = viewWithAlerts;
        this.viewWithUsersRecyclerView = viewWithUsersRecyclerView;
        repository = Model.getRepository();
    }

    @Override
    public void fillBlockedUsersList() {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(viewWithUsersRecyclerView.getContext());
        viewWithAlerts.showWaitAlertDialog();
        repository.getAllBlockedUsers(authenticationToken)
                .enqueue(new DefaultCallback<List<User>, ViewWithAlerts>(viewWithAlerts) {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call,
                                           @NonNull Response<List<User>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            List<User> users = Objects.requireNonNull(response.body());
                            UserUtils.deleteCurrentUserFromUserList(users,
                                    viewWithUsersRecyclerView.getContext());
                            UserUtils.convertAndSetUsersToView(users, viewWithUsersRecyclerView);
                        }
                    }
                });
    }
}
