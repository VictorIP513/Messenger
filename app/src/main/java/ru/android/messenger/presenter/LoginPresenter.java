package ru.android.messenger.presenter;

public interface LoginPresenter {

    void buttonLoginClicked(String login, String password);

    void saveAuthenticationToken(String authenticationToken);
}
