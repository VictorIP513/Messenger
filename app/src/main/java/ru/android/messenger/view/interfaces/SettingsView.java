package ru.android.messenger.view.interfaces;

import android.graphics.Bitmap;

public interface SettingsView extends ViewWithAlerts {

    void setErrorWritingBitmapToFile();

    void setImageNotFoundError();

    void setProfileImage(Bitmap bitmap);

    void setUserData(String firstName, String surname, String login, String email);
}
