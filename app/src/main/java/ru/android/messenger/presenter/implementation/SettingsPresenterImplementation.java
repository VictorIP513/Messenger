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
import java.util.Objects;

import okhttp3.ResponseBody;
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
        fillUserData();
        fillPhoto();
    }

    @Override
    public void uploadPhoto(final Bitmap bitmap) {
        try {
            File photoFile = ImageHelper.writeBitmapToFile(bitmap, settingsView.getContext());
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

    private void fillUserData() {
        SharedPreferences sharedPreferences = settingsView.getSharedPreferences();
        String login = PreferenceManager.getLoginFromSharedPreferences(sharedPreferences);
        settingsView.showWaitAlertDialog();
        repository.getUser(login).enqueue(new DefaultCallback<User, SettingsView>(settingsView) {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    User user = Objects.requireNonNull(response.body());
                    settingsView.setUserData(user.getFirstName(), user.getSurname(),
                            user.getLogin(), user.getEmail());
                }
            }
        });
    }

    private void fillPhoto() {
        SharedPreferences sharedPreferences = settingsView.getSharedPreferences();
        String login = PreferenceManager.getLoginFromSharedPreferences(sharedPreferences);
        repository.getUserPhoto(login)
                .enqueue(new DefaultCallback<ResponseBody, SettingsView>(settingsView) {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            File file = Objects.requireNonNull(FileUtils.getFileFromResponseBody(
                                    response.body(), settingsView.getContext()));
                            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
                            settingsView.setProfileImage(bitmap);
                        }
                    }
                });
    }
}
