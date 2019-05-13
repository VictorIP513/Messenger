package ru.android.messenger.presenter.implementation;

import ru.android.messenger.model.DataValidator;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.User;
import ru.android.messenger.model.callbacks.RegistrationCallback;
import ru.android.messenger.presenter.RegistrationPresenter;
import ru.android.messenger.view.interfaces.RegistrationView;
import ru.android.messenger.view.errors.RegistrationError;

public class RegistrationPresenterImplementation implements RegistrationPresenter {

    private RegistrationView registrationView;
    private Repository repository;

    public RegistrationPresenterImplementation(RegistrationView registrationView) {
        this.registrationView = registrationView;
        repository = Model.getRepository();
    }

    @Override
    public void registrationButtonClicked(String firstName, String surname, String email,
                                          String login, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            registrationView.setRegistrationError(RegistrationError.PASSWORDS_DO_NOT_MATCH);
        }

        boolean isValidInputData = validateInputData(login, password, email, firstName, surname);
        if (isValidInputData) {
            User user = new User(login, password, email, firstName, surname);
            registrationView.showWaitAlertDialog();
            repository.registerUser(user).enqueue(new RegistrationCallback(registrationView));
        }
    }

    private boolean validateInputData(String login, String password, String email,
                                      String firstName, String surname) {
        boolean isValidData = true;
        if (!DataValidator.isCorrectLoginLength(login)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_LOGIN_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectPasswordLength(password)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_PASSWORD_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectEmail(email)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_EMAIL);
            isValidData = false;
        }
        if (!DataValidator.isCorrectFirstNameLength(firstName)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_FIRST_NAME_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectSurnameLength(surname)) {
            registrationView.setRegistrationError(RegistrationError.INCORRECT_SURNAME_LENGTH);
            isValidData = false;
        }
        return isValidData;
    }
}
