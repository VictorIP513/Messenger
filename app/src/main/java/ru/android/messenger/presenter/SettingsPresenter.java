package ru.android.messenger.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

public interface SettingsPresenter {

    void fillUserInformation();

    void uploadPhoto(Bitmap bitmap);

    Bitmap getBitmapFromUri(Uri uri);
}
