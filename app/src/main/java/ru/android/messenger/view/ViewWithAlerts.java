package ru.android.messenger.view;

public interface ViewWithAlerts {

    void showWaitAlertDialog();

    void cancelWaitAlertDialog();

    void showConnectionErrorAlertDialog();
}
