package ru.android.messenger.view.errors;

import ru.android.messenger.R;

public enum LoginError {

    INCORRECT_LOGIN_LENGTH(R.string.login_activity_error_incorrect_login_length),
    INCORRECT_PASSWORD(R.string.login_activity_error_incorrect_password);

    private int resourceId;

    LoginError(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
