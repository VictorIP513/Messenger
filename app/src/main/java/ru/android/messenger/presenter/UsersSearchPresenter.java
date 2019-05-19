package ru.android.messenger.presenter;

import android.graphics.Bitmap;

public interface UsersSearchPresenter {

    void fillUsersList();

    byte[] getBitmapFromByteArray(Bitmap bitmap);
}
