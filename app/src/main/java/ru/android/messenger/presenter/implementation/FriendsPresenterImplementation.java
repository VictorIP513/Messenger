package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.UserUtils;
import ru.android.messenger.presenter.FriendsPresenter;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class FriendsPresenterImplementation implements FriendsPresenter {

    private ViewWithUsersRecyclerView view;
    private Repository repository;

    public FriendsPresenterImplementation(ViewWithUsersRecyclerView view) {
        this.view = view;
        repository = Model.getRepository();
    }

    @Override
    public void fillFriendList() {
        String authenticationToken = PreferenceManager.getAuthenticationToken(view.getContext());
        repository.getFriends(authenticationToken).enqueue(new CallbackWithoutAlerts<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call,
                                   @NonNull Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = Objects.requireNonNull(response.body());
                    UserUtils.convertAndSetUsersToView(users, view);
                }
            }
        });
    }
}
