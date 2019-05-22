package ru.android.messenger.view.interfaces;

import android.content.Context;

public interface ViewWithAlerts {

    Context getContext();

    void showWaitAlertDialog();

    void cancelWaitAlertDialog();

    void showConnectionErrorAlertDialog();
}
