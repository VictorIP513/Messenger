package ru.android.messenger.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.presenter.SettingsPresenter;
import ru.android.messenger.presenter.implementation.SettingsPresenterImplementation;
import ru.android.messenger.utils.Logger;
import ru.android.messenger.view.interfaces.SettingsView;

public class SettingsActivity extends AppCompatActivity implements SettingsView {

    private static final int UPLOAD_PHOTO_REQUEST_CODE = 0;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final String INTENT_IMAGE_TYPE = "image/*";

    private SettingsPresenter settingsPresenter;

    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPresenter = new SettingsPresenterImplementation(this);

        findViews();
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

    private void findViews() {
        imageViewProfile = findViewById(R.id.image_view_profile);
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
        try {
            Uri imageUri = data.getData();
            InputStream imageStream = getContentResolver().openInputStream(
                    Objects.requireNonNull(imageUri));
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageViewProfile.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            Logger.error("Image not found", e);
            Toast errorToast = Toast.makeText(this,
                    getText(R.string.settings_activity_error_file_not_found), Toast.LENGTH_LONG);
            errorToast.show();
        }
    }

    private void getPhotoFromCamera(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
        imageViewProfile.setImageBitmap(imageBitmap);
    }
}
