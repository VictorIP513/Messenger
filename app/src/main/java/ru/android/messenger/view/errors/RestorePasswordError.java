package ru.android.messenger.view.errors;

import ru.android.messenger.R;

public enum RestorePasswordError {

    /* validation errors */
    PASSWORDS_DO_NOT_MATCH(R.string.restore_password_activity_error_passwords_do_not_match),
    INCORRECT_LOGIN_LENGTH(R.string.restore_password_activity_error_incorrect_login_length),
    INCORRECT_PASSWORD_LENGTH(R.string.restore_password_activity_error_incorrect_password_length),

    /* errors from response */
    USER_NOT_FOUND(R.string.restore_password_activity_error_user_not_found);

    private int resourceId;

    RestorePasswordError(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
