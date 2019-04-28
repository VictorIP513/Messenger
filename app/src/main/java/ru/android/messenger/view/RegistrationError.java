package ru.android.messenger.view;

import ru.android.messenger.R;

public enum RegistrationError {

    PASSWORDS_DO_NOT_MATCH(R.string.registration_activity_error_passwords_do_not_match),
    INCORRECT_EMAIL(R.string.registration_activity_error_incorrect_email),
    INCORRECT_LOGIN_LENGTH(R.string.registration_activity_error_incorrect_login_length),
    INCORRECT_PASSWORD_LENGTH(R.string.registration_activity_error_incorrect_password_length),
    INCORRECT_FIRST_NAME_LENGTH(R.string.registration_activity_error_incorrect_first_name_length),
    INCORRECT_SURNAME_LENGTH(R.string.registration_activity_error_incorrect_surname_length);

    private int resourceId;

    RegistrationError(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
