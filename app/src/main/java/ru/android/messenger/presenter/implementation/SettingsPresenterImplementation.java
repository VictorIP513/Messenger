package ru.android.messenger.presenter.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.presenter.SettingsPresenter;
import ru.android.messenger.utils.Logger;
import ru.android.messenger.view.interfaces.SettingsView;

public class SettingsPresenterImplementation implements SettingsPresenter {

    private static final String PHOTO_PART_NAME = "photo";

    private SettingsView settingsView;
    private Repository repository;

    public SettingsPresenterImplementation(SettingsView settingsView) {
        this.settingsView = settingsView;
        repository = Model.getRepository();
    }

    @Override
    public void fillUserInformation() {
        Context context = settingsView.getContext();
        String pathToPhoto = FileUtils.getPathToPhoto(context);
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPhoto);
        settingsView.setProfileImage(bitmap);

        User user = PreferenceManager.getUser(context);
        settingsView.setUserData(user.getFirstName(), user.getSurname(),
                user.getLogin(), user.getEmail());
    }

    @Override
    public void uploadPhoto(final Bitmap bitmap) {
        final Context context = settingsView.getContext();
        final File photoFile = ImageHelper.writeBitmapToFile(bitmap, context);
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(settingsView.getContext());

        repository.uploadPhoto(Model.createFileToSend(
                photoFile, PHOTO_PART_NAME), authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call,
                                           @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            FileUtils.saveUserPhotoToInternalStorage(photoFile, context);
                            settingsView.setProfileImage(bitmap);
                        }
                    }
                });
    }

    @Override
    public void deleteCache() {
        Context context = settingsView.getContext();
        FileUtils.deleteCache(context);
        PreferenceManager.clearAllPreferences(context);
    }

    @Override
    public Bitmap getBitmapFromUri(Uri imageUri) {
        Context context = settingsView.getContext();
        try {
            return ImageHelper.getBitmapFromUriAndContext(imageUri, context);
        } catch (FileNotFoundException e) {
            Logger.error("Image not found", e);
            settingsView.setImageNotFoundError();
        }
        return null;
    }

    @Override
    public String getServerAddress() {
        return Model.getServerIp();
    }

    @Override
    public void enableNotifications(boolean value) {
        PreferenceManager.setEnabledNotifications(settingsView.getContext(), value);
    }

    @Override
    public boolean isEnabledNotifications() {
        Context context = settingsView.getContext();
        return PreferenceManager.isEnabledNotifications(context);
    }
}
