package ru.android.messenger.view.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

public interface SettingsView extends ViewWithAlerts {

    Context getContext();

    void setErrorWritingBitmapToFile();

    void setImageNotFoundError();

    void setProfileImage(Bitmap bitmap);

    void setUserData(String firstName, String surname, String login, String email);
}
