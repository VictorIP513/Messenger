package ru.android.messenger.model.callbacks;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.android.messenger.view.interfaces.SettingsView;

public class UploadPhotoCallback implements Callback<Void> {

    private SettingsView settingsView;
    private Bitmap photo;

    public UploadPhotoCallback(SettingsView settingsView, Bitmap photo) {
        this.settingsView = settingsView;
        this.photo = photo;
    }

    @Override
    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
        settingsView.cancelWaitAlertDialog();
        if (response.isSuccessful()) {
            settingsView.setProfileImage(photo);
        }
    }

    @Override
    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
        settingsView.cancelWaitAlertDialog();
        settingsView.showConnectionErrorAlertDialog();
    }
}
