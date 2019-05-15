package ru.android.messenger.model.callbacks;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.messenger.presenter.SettingsPresenter;
import ru.android.messenger.view.interfaces.SettingsView;

public class UploadPhotoCallback implements Callback<Void> {

    private SettingsView settingsView;
    private SettingsPresenter settingsPresenter;

    public UploadPhotoCallback(SettingsView settingsView, SettingsPresenter settingsPresenter) {
        this.settingsView = settingsView;
        this.settingsPresenter = settingsPresenter;
    }

    @Override
    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        settingsView.cancelWaitAlertDialog();
    }

    @Override
    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        settingsView.cancelWaitAlertDialog();
        settingsView.showConnectionErrorAlertDialog();
    }
}
