package ru.android.messenger.view.interfaces;

import ru.android.messenger.view.errors.RestorePasswordError;

public interface RestorePasswordView extends ViewWithAlerts {

    void setRestorePasswordError(RestorePasswordError restorePasswordError);

    void showSuccessChangePasswordAlert();
}
