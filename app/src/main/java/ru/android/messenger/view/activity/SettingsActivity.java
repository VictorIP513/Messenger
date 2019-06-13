package ru.android.messenger.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.presenter.SettingsPresenter;
import ru.android.messenger.presenter.implementation.SettingsPresenterImplementation;
import ru.android.messenger.view.interfaces.SettingsView;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class SettingsActivity extends ActivityWithNavigationDrawer implements SettingsView {

    private static final int UPLOAD_PHOTO_REQUEST_CODE = 0;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final String INTENT_IMAGE_TYPE = "image/*";

    private SettingsPresenter settingsPresenter;

    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewLogin;
    private TextView textViewEmail;
    private TextView textViewServerAddress;
    private SwitchCompat switchNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_settings);

        settingsPresenter = new SettingsPresenterImplementation(this);

        findViews();
        configureNotificationsSwitch();
        fillUserInformation();
        fillServerAddress();
        configureActionBar();
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
        imageViewProfile.setImageBitmap(
                bitmap == null ? ViewUtils.getDefaultProfileImage(this) : bitmap);
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

    public void linearLayoutBlockedMembersListClick(View view) {
        Intent intent = new Intent(this, BlockedUsersActivity.class);
        startActivity(intent);
    }

    private void deleteCacheAndBackToLoginActivity() {
        ViewUtils.logout(this);
        settingsPresenter.deleteCache();
    }

    private void findViews() {
        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewName = findViewById(R.id.text_view_name);
        textViewLogin = findViewById(R.id.text_view_login);
        textViewEmail = findViewById(R.id.text_view_email);
        textViewServerAddress = findViewById(R.id.text_view_server_address);
        switchNotifications = findViewById(R.id.switch_notifications);
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

    private void fillServerAddress() {
        String serverAddress = settingsPresenter.getServerAddress();
        String fullText =
                getString(R.string.settings_activity_server_address_text) + " " + serverAddress;
        textViewServerAddress.setText(fullText);
    }

    private void configureNotificationsSwitch() {
        boolean isEnabledNotifications = settingsPresenter.isEnabledNotifications();
        switchNotifications.setChecked(isEnabledNotifications);
        switchNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsPresenter.enableNotifications(isChecked);
            }
        });
    }

    private void configureActionBar() {
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        String actionBarTitle = getString(R.string.settings_activity_action_bar_title);
        actionBar.setTitle(actionBarTitle);
    }
}
