package ru.android.messenger.presenter.implementation;

import ru.android.messenger.model.DataValidator;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.LoginCallback;
import ru.android.messenger.presenter.LoginPresenter;
import ru.android.messenger.view.LoginView;
import ru.android.messenger.view.errors.LoginError;

public class LoginPresenterImplementation implements LoginPresenter {

    private LoginView loginView;
    private Repository repository;

    public LoginPresenterImplementation(LoginView loginView) {
        this.loginView = loginView;
        repository = Model.getRepository();
    }

    @Override
    public void buttonLoginClicked(String login, String password) {
        boolean isValidInputData = validateInputData(login, password);
        if (isValidInputData) {
            loginView.showWaitAlertDialog();
            repository.login(login, password)
                    .enqueue(new LoginCallback(loginView, this));
        }
    }

    @Override
    public void saveAuthenticationToken(String authenticationToken) {

    }

    private boolean validateInputData(String login, String password) {
        boolean isValidData = true;
        if (!DataValidator.isCorrectLoginLength(login)) {
            loginView.setLoginError(LoginError.INCORRECT_LOGIN_LENGTH);
            isValidData = false;
        }
        if (!DataValidator.isCorrectPasswordLength(password)) {
            loginView.setLoginError(LoginError.INCORRECT_PASSWORD_LENGTH);
            isValidData = false;
        }
        return isValidData;
    }
}
