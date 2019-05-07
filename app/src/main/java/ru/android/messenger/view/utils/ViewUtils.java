package ru.android.messenger.view.utils;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;

public class ViewUtils {

    private ViewUtils() {

    }

    public static SweetAlertDialog createWaitAlertDialog(Context context) {
        SweetAlertDialog waitAlertDialog =
                new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        waitAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        waitAlertDialog.setTitleText(context.getString(R.string.alert_dialog_wait_title));
        waitAlertDialog.setCancelable(false);
        return waitAlertDialog;
    }

    public static SweetAlertDialog createConnectionErrorAlertDialog(Context context) {
        return new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.alert_dialog_connection_failed_title))
                .setContentText(context.getString(R.string.alert_dialog_connection_failed_content));
    }
}
