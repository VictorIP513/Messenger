package ru.android.messenger.model.callbacks;

import android.support.annotation.NonNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.messenger.view.interfaces.LoginView;

public class CheckAuthenticationTokenCallback implements Callback<Boolean> {

    private LoginView loginView;

    public CheckAuthenticationTokenCallback(LoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
        loginView.cancelWaitAlertDialog();
        if (response.isSuccessful()) {
            boolean isCorrectAuthenticationToken = Objects.requireNonNull(response.body());
            if (isCorrectAuthenticationToken) {
                loginView.changeToMainActivity();
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
        loginView.cancelWaitAlertDialog();
        loginView.showConnectionErrorAlertDialog();
    }
}
