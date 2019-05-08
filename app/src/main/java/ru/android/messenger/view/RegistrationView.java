package ru.android.messenger.view;

public interface RegistrationView {

    void setRegistrationError(RegistrationError registrationError);

    void showWaitAlertDialog();

    void cancelWaitAlertDialog();

    void showConnectionErrorAlertDialog();

    void showSuccessRegistrationAlert();

    void backToLoginActivity();
}
