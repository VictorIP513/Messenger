package ru.android.messenger.model.callbacks;

import android.support.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.api.LoginResponse;
import ru.android.messenger.presenter.LoginPresenter;
import ru.android.messenger.view.LoginView;

public class LoginCallback implements Callback<LoginResponse> {

    private LoginView loginView;
    private LoginPresenter loginPresenter;

    public LoginCallback(LoginView loginView, LoginPresenter loginPresenter) {
        this.loginView = loginView;
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void onResponse(@NonNull Call<LoginResponse> call,
                           @NonNull Response<LoginResponse> response) {
        loginView.cancelWaitAlertDialog();
        if (response.isSuccessful()) {
            LoginResponse loginResponse = Objects.requireNonNull(response.body());
            loginPresenter.saveAuthenticationToken(loginResponse.getAuthenticationToken());
        } else {
            LoginResponse loginResponse = ApiUtils.getJsonFromResponseBody(
                    Objects.requireNonNull(response.errorBody()), LoginResponse.class);
            if (loginResponse.getStatus() == LoginResponse.Status.INVALID_LOGIN_OR_PASSWORD) {
                loginView.setInvalidLoginOrPasswordError();
            }
            if (loginResponse.getStatus() == LoginResponse.Status.ACCOUNT_NOT_CONFIRMED) {
                loginView.setAccountNotConfirmedError();
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
        loginView.cancelWaitAlertDialog();
        loginView.showConnectionErrorAlertDialog();
    }
}
