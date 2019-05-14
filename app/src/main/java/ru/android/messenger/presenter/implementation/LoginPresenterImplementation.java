package ru.android.messenger.presenter.implementation;

import android.content.SharedPreferences;

import ru.android.messenger.model.DataValidator;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CheckAuthenticationTokenCallback;
import ru.android.messenger.model.callbacks.LoginCallback;
import ru.android.messenger.presenter.LoginPresenter;
import ru.android.messenger.view.errors.LoginError;
import ru.android.messenger.view.interfaces.LoginView;

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
        SharedPreferences sharedPreferences = loginView.getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        PreferenceManager.setAuthenticationTokenToSharedPreferences(editor, authenticationToken);
    }

    @Override
    public void autoLogin() {
        SharedPreferences sharedPreferences = loginView.getSharedPreferences();
        String authenticationToken =
                PreferenceManager.getAuthenticationTokenFromSharedPreferences(sharedPreferences);
        if (authenticationToken != null) {
            loginView.showWaitAlertDialog();
            repository.checkAuthenticationToken(authenticationToken)
                    .enqueue(new CheckAuthenticationTokenCallback(loginView));
        }
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
