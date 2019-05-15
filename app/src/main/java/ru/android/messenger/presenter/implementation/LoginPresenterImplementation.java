package ru.android.messenger.presenter.implementation;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.api.LoginResponse;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.utils.DataValidator;
import ru.android.messenger.presenter.LoginPresenter;
import ru.android.messenger.view.errors.LoginError;
import ru.android.messenger.view.interfaces.LoginView;

public class LoginPresenterImplementation implements LoginPresenter {

    private LoginView loginView;
    private Repository repository;

    public LoginPresenterImplementation(LoginView loginView) {
        this.loginView = loginView;
        repository = Model.getRepository();
    }

    @Override
    public void buttonLoginClicked(String login, String password) {
        boolean isValidInputData = validateInputData(login, password);
        if (isValidInputData) {
            loginView.showWaitAlertDialog();
            repository.login(login, password)
                    .enqueue(new DefaultCallback<LoginResponse, LoginView>(loginView) {
                        @Override
                        public void onResponse(@NonNull Call<LoginResponse> call,
                                               @NonNull Response<LoginResponse> response) {
                            super.onResponse(call, response);
                            processLoginResponse(response);
                        }
                    });
        }
    }

    @Override
    public void autoLogin() {
        SharedPreferences sharedPreferences = loginView.getSharedPreferences();
        String authenticationToken =
                PreferenceManager.getAuthenticationTokenFromSharedPreferences(sharedPreferences);
        if (authenticationToken != null) {
            loginView.showWaitAlertDialog();
            repository.checkAuthenticationToken(authenticationToken)
                    .enqueue(new DefaultCallback<Boolean, LoginView>(loginView) {
                        @Override
                        public void onResponse(@NonNull Call<Boolean> call,
                                               @NonNull Response<Boolean> response) {
                            super.onResponse(call, response);
                            processCheckAuthenticationTokenResponse(response);
                        }
                    });
        }
    }

    private void processCheckAuthenticationTokenResponse(Response<Boolean> response) {
        if (response.isSuccessful()) {
            boolean isCorrectAuthenticationToken = Objects.requireNonNull(response.body());
            if (isCorrectAuthenticationToken) {
                loginView.changeToMainActivity();
            }
        }
    }

    private void processLoginResponse(Response<LoginResponse> response) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = Objects.requireNonNull(response.body());
            saveAuthenticationToken(loginResponse.getAuthenticationToken());
            loginView.changeToMainActivity();
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

    private void saveAuthenticationToken(String authenticationToken) {
        SharedPreferences sharedPreferences = loginView.getSharedPreferences();
        PreferenceManager.setAuthenticationTokenToSharedPreferences(
                sharedPreferences, authenticationToken);
    }

    private boolean validateInputData(String login, String password) {
        boolean isValidData = true;
        if (!DataValidator.isCorrectLoginLength(login)) {
            loginView.setLoginError(LoginError.INCORRECT_LOGIN_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectPasswordLength(password)) {
            loginView.setLoginError(LoginError.INCORRECT_PASSWORD_LENGTH);
            isValidData = false;
        }
        return isValidData;
    }
}
