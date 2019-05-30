package ru.android.messenger.model.utils;

import android.util.Patterns;

public class DataValidator {

    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 30;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 100;
    private static final int MIN_FIRST_NAME_LENGTH = 1;
    private static final int MAX_FIRST_NAME_LENGTH = 50;
    private static final int MIN_SURNAME_LENGTH = 1;
    private static final int MAX_SURNAME_LENGTH = 50;

    private DataValidator() {

    }

    public static boolean isCorrectEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isCorrectPassword(String password) {
        boolean isCorrectLength =
                password.length() > MIN_PASSWORD_LENGTH && password.length() < MAX_PASSWORD_LENGTH;
        boolean isContainsDigit = password.matches(".*\\d.*");
        boolean isContainsLowercaseLetter = false;
        for (char ch : password.toCharArray()) {
            if (Character.isLowerCase(ch)) {
                isContainsLowercaseLetter = true;
            }
        }
        return isCorrectLength && isContainsDigit && isContainsLowercaseLetter;
    }

    public static boolean isCorrectFirstNameLength(String firstName) {
        return firstName.length() > MIN_FIRST_NAME_LENGTH &&
                firstName.length() < MAX_FIRST_NAME_LENGTH;
    }

    public static boolean isCorrectSurnameLength(String surname) {
        return surname.length() > MIN_SURNAME_LENGTH && surname.length() < MAX_SURNAME_LENGTH;
    }

    public static boolean isCorrectLoginLength(String login) {
        return login.length() > MIN_LOGIN_LENGTH && login.length() < MAX_LOGIN_LENGTH;
    }
}
