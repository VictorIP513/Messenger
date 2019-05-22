package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.api.FriendStatus;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.presenter.UserInfoPresenter;
import ru.android.messenger.view.interfaces.UserInfoView;

public class UserInfoPresenterImplementation implements UserInfoPresenter {

    private UserInfoView userInfoView;
    private Repository repository;

    public UserInfoPresenterImplementation(UserInfoView userInfoView) {
        this.userInfoView = userInfoView;
        repository = Model.getRepository();
    }

    @Override
    public void fillUserFriendStatus(String login) {
        userInfoView.showWaitAlertDialog();
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(userInfoView.getContext());
        repository.getFriendStatus(login, authenticationToken)
                .enqueue(new DefaultCallback<FriendStatus, UserInfoView>(userInfoView) {
                    @Override
                    public void onResponse(@NonNull Call<FriendStatus> call,
                                           @NonNull Response<FriendStatus> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            userInfoView.setFriendStatus(response.body());
                        }
                    }
                });
    }

    @Override
    public void addToFriend(String login) {
        userInfoView.showWaitAlertDialog();
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(userInfoView.getContext());
        repository.addToFriend(login, authenticationToken)
                .enqueue(new DefaultCallback<Void, UserInfoView>(userInfoView) {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            userInfoView.setFriendStatus(FriendStatus.FRIEND_REQUEST_HAS_BEEN_SENT);
                        }
                    }
                });
    }
}
