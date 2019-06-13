package ru.android.messenger.view.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;

public class Alerts {

    private static final int UPLOAD_PHOTO_REQUEST_CODE = 0;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private static final String INTENT_IMAGE_TYPE = "image/*";

    private Alerts() {

    }

    public static void sendLoggedInANewDeviceNotification(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SweetAlertDialog alertDialog = createLoggedInANewDeviceAlert(activity);
                alertDialog.show();
            }
        });
    }

    public static void showUploadPhotoAlertDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.alert_dialog_set_profile_image_title))
                .setItems(new String[]{
                        activity.getString(R.string.alert_dialog_set_profile_image_upload_photo),
                        activity.getString(R.string.alert_dialog_set_profile_image_take_photo)
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            uploadPhoto(activity);
                        } else {
                            takePhoto(activity);
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void uploadPhoto(Activity activity) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType(INTENT_IMAGE_TYPE);
        activity.startActivityForResult(photoPickerIntent, UPLOAD_PHOTO_REQUEST_CODE);
    }

    private static void takePhoto(Activity activity) {
        PackageManager packageManager = activity.getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast errorToast = Toast.makeText(activity,
                    activity.getText(R.string.alert_dialog_error_file_not_found),
                    Toast.LENGTH_LONG);
            errorToast.show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
    }

    private static SweetAlertDialog createLoggedInANewDeviceAlert(final Context context) {
        return new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(context.getString(R.string.alert_dialog_logged_in_on_new_device_title))
                .setContentText(context.getString(
                        R.string.alert_dialog_logged_in_on_new_device_content))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ViewUtils.logout(context);
                    }
                });
    }
}
