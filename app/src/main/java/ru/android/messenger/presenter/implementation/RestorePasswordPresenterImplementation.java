package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.response.RestorePasswordResponse;
import ru.android.messenger.model.utils.DataValidator;
import ru.android.messenger.presenter.RestorePasswordPresenter;
import ru.android.messenger.view.errors.RestorePasswordError;
import ru.android.messenger.view.interfaces.RestorePasswordView;

public class RestorePasswordPresenterImplementation implements RestorePasswordPresenter {

    private RestorePasswordView restorePasswordView;
    private Repository repository;

    public RestorePasswordPresenterImplementation(RestorePasswordView restorePasswordView) {
        this.restorePasswordView = restorePasswordView;
        repository = Model.getRepository();
    }

    @Override
    public void changePassword(String login, String password, String confirmPassword) {
        boolean isValidData = validateInputData(login, password, confirmPassword);
        if (isValidData) {
            restorePasswordView.showWaitAlertDialog();
            repository.restorePassword(login, password)
                    .enqueue(new DefaultCallback<RestorePasswordResponse, RestorePasswordView>(
                            restorePasswordView) {
                        @Override
                        public void onResponse(@NonNull Call<RestorePasswordResponse> call,
                                               @NonNull Response<RestorePasswordResponse> response) {
                            super.onResponse(call, response);
                            processRestorePasswordResponse(response);
                        }
                    });
        }
    }

    private void processRestorePasswordResponse(Response<RestorePasswordResponse> response) {
        if (response.isSuccessful()) {
            restorePasswordView.showSuccessChangePasswordAlert();
        } else {
            RestorePasswordResponse restorePasswordResponse = ApiUtils.getJsonFromResponseBody(
                    Objects.requireNonNull(response.errorBody()), RestorePasswordResponse.class);
            if (restorePasswordResponse == RestorePasswordResponse.USER_NOT_FOUND) {
                restorePasswordView.setRestorePasswordError(RestorePasswordError.USER_NOT_FOUND);
            }
        }
    }

    private boolean validateInputData(String login, String password, String confirmPassword) {
        boolean isValidData = true;
        if (!password.equals(confirmPassword)) {
            restorePasswordView.setRestorePasswordError(RestorePasswordError.PASSWORDS_DO_NOT_MATCH);
            isValidData = false;
        }
        if (DataValidator.isInorrectLoginLength(login)) {
            restorePasswordView.setRestorePasswordError(RestorePasswordError.INCORRECT_LOGIN_LENGTH);
            isValidData = false;
        }
        if (DataValidator.isIncorrectPassword(password)) {
            restorePasswordView.setRestorePasswordError(
                    RestorePasswordError.INCORRECT_PASSWORD);
            isValidData = false;
        }
        return isValidData;
    }
}
