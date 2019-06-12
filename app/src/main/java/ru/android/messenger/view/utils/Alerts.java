package ru.android.messenger.view.utils;

import android.app.Activity;
import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;

public class Alerts {

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
