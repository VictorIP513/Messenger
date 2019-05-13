package ru.android.messenger.view.interfaces;

import ru.android.messenger.view.errors.RegistrationError;

public interface RegistrationView extends ViewWithAlerts {

    void setRegistrationError(RegistrationError registrationError);

    void showSuccessRegistrationAlert();

    void backToLoginActivity();
}
