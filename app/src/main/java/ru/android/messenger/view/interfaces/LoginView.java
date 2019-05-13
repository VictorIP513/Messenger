package ru.android.messenger.view.interfaces;

import android.content.SharedPreferences;

import ru.android.messenger.view.errors.LoginError;

public interface LoginView extends ViewWithAlerts {

    void setLoginError(LoginError loginError);

    void setInvalidLoginOrPasswordError();

    void setAccountNotConfirmedError();

    SharedPreferences getSharedPreferences();
}