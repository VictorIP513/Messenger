package ru.android.messenger.presenter.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.presenter.NavigationDrawerPresenter;
import ru.android.messenger.view.interfaces.NavigationDrawerView;

public class NavigationDrawerPresenterImplementation implements NavigationDrawerPresenter {

    private NavigationDrawerView navigationDrawerView;

    public NavigationDrawerPresenterImplementation(NavigationDrawerView navigationDrawerView) {
        this.navigationDrawerView = navigationDrawerView;
    }

    @Override
    public void fillUserInformationToNavigationDrawer() {
        Context context = navigationDrawerView.getContext();
        String pathToPhoto = FileUtils.getPathToPhoto(context);
        Bitmap bitmap = BitmapFactory.decodeFile(pathToPhoto);
        navigationDrawerView.setProfileImageToNavigationDrawer(bitmap);

        User user = PreferenceManager.getUser(context);
        navigationDrawerView.setUserDataToNavigationDrawer(user.getFirstName(), user.getSurname(),
                user.getLogin(), user.getEmail());
    }
}
