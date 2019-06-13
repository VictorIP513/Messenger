package ru.android.messenger.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

public interface DialogPresenter {

    void createDialog(String login, String lastMessageText);

    void sendMessage(String message);

    void fillDialog(String login);

    void fillUserInformation(String login);

    void fillBlockedInformation(String login);

    void fillLastOnlineDate(String login);

    Bitmap getBitmapFromUri(Uri data);

    void sendImage(Bitmap image);
}
