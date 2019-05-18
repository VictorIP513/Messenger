package ru.android.messenger.view.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

public interface NavigationDrawerView {

    Context getContext();

    void setProfileImageToNavigationDrawer(Bitmap bitmap);

    void setUserDataToNavigationDrawer(String firstName, String surname, String login, String email);
}
