package ru.android.messenger.view;

import ru.android.messenger.R;

public enum RegistrationError {

    /* validation errors */
    PASSWORDS_DO_NOT_MATCH(R.string.registration_activity_error_passwords_do_not_match),
    INCORRECT_EMAIL(R.string.registration_activity_error_incorrect_email),
    INCORRECT_LOGIN_LENGTH(R.string.registration_activity_error_incorrect_login_length),
    INCORRECT_PASSWORD_LENGTH(R.string.registration_activity_error_incorrect_password_length),
    INCORRECT_FIRST_NAME_LENGTH(R.string.registration_activity_error_incorrect_first_name_length),
    INCORRECT_SURNAME_LENGTH(R.string.registration_activity_error_incorrect_surname_length),

    /* errors from response */
    LOGIN_IS_EXISTS(R.string.registration_activity_error_login_is_exists),
    EMAIL_IS_EXISTS(R.string.registration_activity_error_email_is_exists);


    private int resourceId;

    RegistrationError(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceId() {
        return resourceId;
    }
}
