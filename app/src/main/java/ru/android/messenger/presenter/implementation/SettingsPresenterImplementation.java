package ru.android.messenger.presenter.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import ru.android.messenger.model.ImageHelper;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.UploadPhotoCallback;
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

    }

    @Override
    public void uploadPhoto(Bitmap bitmap) {
        try {
            File photoFile = ImageHelper.writeBitmapToFile(bitmap, settingsView.getContext());
            String authenticationToken =
                    PreferenceManager.getAuthenticationTokenFromSharedPreferences(
                            settingsView.getSharedPreferences());
            settingsView.showWaitAlertDialog();

            repository.uploadPhoto(Model.createFileToSend(
                    photoFile, PHOTO_PART_NAME), authenticationToken)
                    .enqueue(new UploadPhotoCallback(settingsView, bitmap));
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
