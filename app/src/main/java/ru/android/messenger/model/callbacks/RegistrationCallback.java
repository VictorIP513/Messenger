package ru.android.messenger.model.callbacks;

import android.support.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.api.RegistrationResponse;
import ru.android.messenger.view.RegistrationError;
import ru.android.messenger.view.RegistrationView;

public class RegistrationCallback implements Callback<RegistrationResponse> {

    private RegistrationView registrationView;

    public RegistrationCallback(RegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    @Override
    public void onResponse(@NonNull Call<RegistrationResponse> call,
                           @NonNull Response<RegistrationResponse> response) {
        if (response.isSuccessful()) {

        } else {
            RegistrationResponse registrationResponse = ApiUtils.getJsonFromResponseBody(
                    Objects.requireNonNull(response.errorBody()), RegistrationResponse.class);
            if (registrationResponse == RegistrationResponse.LOGIN_IS_EXISTS) {
                registrationView.setRegistrationError(RegistrationError.LOGIN_IS_EXISTS);
            }
            if (registrationResponse == RegistrationResponse.EMAIL_IS_EXISTS) {
                registrationView.setRegistrationError(RegistrationError.EMAIL_IS_EXISTS);
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull Throwable t) {

    }
}
