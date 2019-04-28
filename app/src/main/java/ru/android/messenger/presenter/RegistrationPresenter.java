package ru.android.messenger.presenter;

public interface RegistrationPresenter {

    void registrationButtonClicked(String firstName, String surname, String email, String login,
                                   String password, String passwordConfirm);
}
