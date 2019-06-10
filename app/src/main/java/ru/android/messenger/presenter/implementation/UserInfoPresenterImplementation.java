package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.response.FriendStatus;
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
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(userInfoView.getContext());
        repository.getFriendStatus(login, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<FriendStatus>() {
                    @Override
                    public void onResponse(@NonNull Call<FriendStatus> call,
                                           @NonNull Response<FriendStatus> response) {
                        if (response.isSuccessful()) {
                            userInfoView.setFriendStatus(response.body());
                        }
                    }
                });
    }

    @Override
    public void addToFriend(String login) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(userInfoView.getContext());
        repository.addToFriend(login, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            userInfoView.setFriendStatus(FriendStatus.FRIEND_REQUEST_HAS_BEEN_SENT);
                        }
                    }
                });
    }

    @Override
    public void deleteFromFriend(String login) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(userInfoView.getContext());
        repository.deleteFromFriend(login, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            userInfoView.setFriendStatus(FriendStatus.USER_IS_NOT_FRIEND);
                        }
                    }
                });
    }

    @Override
    public void acceptFriendRequest(String login) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(userInfoView.getContext());
        repository.acceptFriendRequest(login, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            userInfoView.setFriendStatus(FriendStatus.USER_IS_FRIEND);
                        }
                    }
                });
    }
}
