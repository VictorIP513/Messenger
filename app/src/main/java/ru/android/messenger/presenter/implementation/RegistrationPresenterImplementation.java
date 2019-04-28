package ru.android.messenger.presenter.implementation;

import android.util.Patterns;

import ru.android.messenger.presenter.RegistrationPresenter;
import ru.android.messenger.view.RegistrationError;
import ru.android.messenger.view.RegistrationView;

public class RegistrationPresenterImplementation implements RegistrationPresenter {

    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 30;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final int MIN_FIRST_NAME_LENGTH = 1;
    private static final int MAX_FIRST_NAME_LENGTH = 50;
    private static final int MIN_SURNAME_LENGTH = 1;
    private static final int MAX_SURNAME_LENGTH = 50;

    private RegistrationView registrationView;

    public RegistrationPresenterImplementation(RegistrationView registrationView) {
        this.registrationView = registrationView;
    }

    @Override
    public void registrationButtonClicked(String firstName, String surname, String email,
                                          String login, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            registrationView.setRegistrationError(RegistrationError.PASSWORDS_DO_NOT_MATCH);
        }
        validateIncorrectTextLength(firstName, surname, login, password);
        validateEmail(email);
    }

    private void validateIncorrectTextLength(String firstName, String surname, String login, String password) {
        if (firstName.length() < MIN_FIRST_NAME_LENGTH || firstName.length() > MAX_FIRST_NAME_LENGTH) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_FIRST_NAME_LENGTH);
        }
        if (surname.length() < MIN_SURNAME_LENGTH || surname.length() > MAX_SURNAME_LENGTH) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_SURNAME_LENGTH);
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_PASSWORD_LENGTH);
        }
        if (login.length() < MIN_LOGIN_LENGTH || login.length() > MAX_LOGIN_LENGTH) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_LOGIN_LENGTH);
        }
    }

    private void validateEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_EMAIL);
        }
    }
}
