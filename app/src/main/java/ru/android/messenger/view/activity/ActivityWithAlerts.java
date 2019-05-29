package ru.android.messenger.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import cn.pedant.SweetAlert.SweetAlertDialog;
import ru.android.messenger.R;
import ru.android.messenger.view.interfaces.ViewWithAlerts;

public abstract class ActivityWithAlerts extends AppCompatActivity implements ViewWithAlerts {

    private static final int PROGRESS_BAR_COLOR = Color.parseColor("#A5DC86");

    private SweetAlertDialog waitAlertDialog;

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showWaitAlertDialog() {
        waitAlertDialog = createWaitAlertDialog();
        waitAlertDialog.show();
    }

    @Override
    public void cancelWaitAlertDialog() {
        waitAlertDialog.cancel();
    }

    @Override
    public void showConnectionErrorAlertDialog() {
        SweetAlertDialog alertDialog = createConnectionErrorAlertDialog();
        alertDialog.show();
    }

    protected SweetAlertDialog createErrorAlertDialog(String titleText, String contentText) {
        return new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(titleText)
                .setContentText(contentText);
    }

    private SweetAlertDialog createWaitAlertDialog() {
        SweetAlertDialog alertDialog =
                new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.getProgressHelper().setBarColor(PROGRESS_BAR_COLOR);
        alertDialog.setTitleText(getString(R.string.alert_dialog_wait_title));
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    private SweetAlertDialog createConnectionErrorAlertDialog() {
        return createErrorAlertDialog(getString(R.string.alert_dialog_connection_failed_title),
                getString(R.string.alert_dialog_connection_failed_content));
    }
}
