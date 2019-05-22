package ru.android.messenger.presenter;

import android.graphics.Bitmap;

public interface UsersRecyclerViewPresenter {

    void fillUsersList();

    byte[] getByteArrayFromBitmap(Bitmap bitmap);
}
