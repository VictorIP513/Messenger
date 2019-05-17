package ru.android.messenger.presenter.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.ImageHelper;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.FileUtils;
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
        String pathToPhoto = FileUtils.getPathToPhoto(settingsView.getContext());
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPhoto);
        settingsView.setProfileImage(bitmap);

        SharedPreferences sharedPreferences = settingsView.getSharedPreferences();
        User user = PreferenceManager.getUserToSharedPreferences(sharedPreferences);
        settingsView.setUserData(user.getFirstName(), user.getSurname(),
                user.getLogin(), user.getEmail());
    }

    @Override
    public void uploadPhoto(final Bitmap bitmap) {
        try {
            final File photoFile = ImageHelper.writeBitmapToFile(bitmap, settingsView.getContext());
            String authenticationToken =
                    PreferenceManager.getAuthenticationTokenFromSharedPreferences(
                            settingsView.getSharedPreferences());
            settingsView.showWaitAlertDialog();

            repository.uploadPhoto(Model.createFileToSend(
                    photoFile, PHOTO_PART_NAME), authenticationToken)
                    .enqueue(new DefaultCallback<Void, SettingsView>(settingsView) {
                        @Override
                        public void onResponse(@NonNull Call<Void> call,
                                               @NonNull Response<Void> response) {
                            super.onResponse(call, response);
                            if (response.isSuccessful()) {
                                FileUtils.saveUserPhotoToInternalStorage(photoFile,
                                        settingsView.getContext());
                                settingsView.setProfileImage(bitmap);
                            }
                        }
                    });
        } catch (IOException e) {
            Logger.error("Error when writing bitmap to file", e);
            settingsView.setErrorWritingBitmapToFile();
        }
    }

    @Override
    public void deleteCache() {
        Context context = settingsView.getContext();
        FileUtils.deleteCache(context);
        SharedPreferences sharedPreferences = settingsView.getSharedPreferences();
        PreferenceManager.clearAllPreferences(sharedPreferences);
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
}
