package ru.android.messenger.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.presenter.SettingsPresenter;
import ru.android.messenger.presenter.implementation.SettingsPresenterImplementation;
import ru.android.messenger.view.interfaces.SettingsView;

public class SettingsActivity extends ActivityWithAlerts implements SettingsView {

    private static final String GLOBAL_SHARED_PREFERENCES_FILE = "application_preferences";
    private static final int UPLOAD_PHOTO_REQUEST_CODE = 0;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final String INTENT_IMAGE_TYPE = "image/*";

    private SettingsPresenter settingsPresenter;

    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewLogin;
    private TextView textViewEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPresenter = new SettingsPresenterImplementation(this);

        findViews();
        fillUserInformation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            getPhotoFromDevice(data);
        }
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            getPhotoFromCamera(data);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(GLOBAL_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void setErrorWritingBitmapToFile() {
        Toast errorToast = Toast.makeText(this,
                getText(R.string.settings_activity_error_writing_bitmap_to_file), Toast.LENGTH_LONG);
        errorToast.show();
    }

    @Override
    public void setImageNotFoundError() {
        Toast errorToast = Toast.makeText(this,
                getText(R.string.settings_activity_error_file_not_found), Toast.LENGTH_LONG);
        errorToast.show();
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        imageViewProfile.setImageBitmap(bitmap);
    }

    @Override
    public void setUserData(String firstName, String surname, String login, String email) {
        String name = firstName + " " + surname;
        textViewName.setText(name);
        textViewLogin.setText(login);
        textViewEmail.setText(email);
    }

    public void imageViewProfileClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.settings_activity_alert_set_profile_image_title))
                .setItems(new String[]{
                        getString(R.string.settings_activity_alert_set_profile_image_upload_photo),
                        getString(R.string.settings_activity_alert_set_profile_image_take_photo)
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            uploadPhoto();
                        } else {
                            takePhoto();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void linearLayoutClearCacheClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.settings_activity_alert_clear_cache_title))
                .setMessage(getString(R.string.settings_activity_alert_clear_cache_message))
                .setPositiveButton(getString(R.string.settings_activity_alert_clear_cache_yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteCacheAndBackToLoginActivity();
                            }
                        })
                .setNegativeButton(
                        getString(R.string.settings_activity_alert_clear_cache_no), null)
                .show();
    }

    private void deleteCacheAndBackToLoginActivity() {
        settingsPresenter.deleteCache();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void findViews() {
        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewName = findViewById(R.id.text_view_name);
        textViewLogin = findViewById(R.id.text_view_login);
        textViewEmail = findViewById(R.id.text_view_email);
    }

    private void uploadPhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType(INTENT_IMAGE_TYPE);
        startActivityForResult(photoPickerIntent, UPLOAD_PHOTO_REQUEST_CODE);
    }

    private void takePhoto() {
        PackageManager packageManager = getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast errorToast = Toast.makeText(this,
                    getText(R.string.settings_activity_error_file_not_found), Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    private void getPhotoFromDevice(Intent data) {
        Bitmap imageBitmap = settingsPresenter.getBitmapFromUri(data.getData());
        if (imageBitmap != null) {
            settingsPresenter.uploadPhoto(imageBitmap);
        }
    }

    private void getPhotoFromCamera(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
        settingsPresenter.uploadPhoto(imageBitmap);
    }

    private void fillUserInformation() {
        settingsPresenter.fillUserInformation();
    }
}
