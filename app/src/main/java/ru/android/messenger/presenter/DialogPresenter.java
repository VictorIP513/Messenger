package ru.android.messenger.presenter;

public interface DialogPresenter {

    void createDialog(String login, String lastMessageText);

    void sendMessage(String message);

    void fillDialog(String login);

    void fillUserInformation(String login);

    void fillBlockedInformation(String login);
}
