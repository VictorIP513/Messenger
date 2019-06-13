package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.response.LoginResponse;
import ru.android.messenger.model.utils.DataValidator;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.model.utils.FirebaseUtils;
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
    public void buttonLoginClicked(final String login, String password) {
        boolean isValidInputData = validateInputData(login, password);
        if (isValidInputData) {
            loginView.showWaitAlertDialog();
            repository.login(login, password)
                    .enqueue(new DefaultCallback<LoginResponse, LoginView>(loginView) {
                        @Override
                        public void onResponse(@NonNull Call<LoginResponse> call,
                                               @NonNull Response<LoginResponse> response) {
                            super.onResponse(call, response);
                            processLoginResponse(response, login);
                        }
                    });
        }
    }

    @Override
    public void autoLogin() {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(loginView.getContext());
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

    @Override
    public void fillLastLogin() {
        String login = PreferenceManager.getLastLogin(loginView.getContext());
        loginView.setLogin(login);
    }

    private void processCheckAuthenticationTokenResponse(Response<Boolean> response) {
        if (response.isSuccessful()) {
            boolean isCorrectAuthenticationToken = Objects.requireNonNull(response.body());
            if (isCorrectAuthenticationToken) {
                loadUserDataFromServer();
                loginView.changeToMainActivity();
            }
        }
    }

    private void processLoginResponse(Response<LoginResponse> response, String login) {
        if (response.isSuccessful()) {
            LoginResponse loginResponse = Objects.requireNonNull(response.body());
            String authenticationToken = loginResponse.getAuthenticationToken();
            PreferenceManager.setAuthenticationToken(loginView.getContext(), authenticationToken);
            PreferenceManager.setLogin(loginView.getContext(), login);
            PreferenceManager.setLastLogin(loginView.getContext(), login);
            FirebaseUtils.subscribeToReceiveMessages(loginView.getContext());
            loadUserDataFromServer();
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

    private boolean validateInputData(String login, String password) {
        boolean isValidData = true;
        if (DataValidator.isInorrectLoginLength(login)) {
            loginView.setLoginError(LoginError.INCORRECT_LOGIN_LENGTH);
            isValidData = false;
        }
        if (DataValidator.isIncorrectPassword(password)) {
            loginView.setLoginError(LoginError.INCORRECT_PASSWORD);
            isValidData = false;
        }
        return isValidData;
    }

    private void loadUserDataFromServer() {
        loadUserData();
        loadUserPhoto();
    }

    private void loadUserData() {
        String login = PreferenceManager.getLogin(loginView.getContext());
        repository.getUser(login).enqueue(new CallbackWithoutAlerts<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = Objects.requireNonNull(response.body());
                    PreferenceManager.setUser(loginView.getContext(), user);
                }
            }
        });
    }

    private void loadUserPhoto() {
        String login = PreferenceManager.getLogin(loginView.getContext());
        repository.getUserPhoto(login).enqueue(new CallbackWithoutAlerts<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    File photo = Objects.requireNonNull(FileUtils.getFileFromResponseBody(
                            response.body(), loginView.getContext()));
                    FileUtils.saveUserPhotoToInternalStorage(photo, loginView.getContext());
                }
            }
        });
    }
}
