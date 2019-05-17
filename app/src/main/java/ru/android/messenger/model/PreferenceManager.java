package ru.android.messenger.model;

import android.content.SharedPreferences;

import ru.android.messenger.model.dto.User;

public class PreferenceManager {

    private static final String AUTHENTICATION_TOKEN_KEY = "authenticationToken";
    private static final String LOGIN_KEY = "login";
    private static final String EMAIL_KEY = "email";
    private static final String USER_FIRST_NAME_KEY = "userFirstName";
    private static final String USER_SURNAME_KEY = "userSurname";

    private PreferenceManager() {

    }

    public static void setAuthenticationTokenToSharedPreferences(
            SharedPreferences sharedPreferences, String authenticationToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTHENTICATION_TOKEN_KEY, authenticationToken);
        editor.apply();
    }

    public static String getAuthenticationTokenFromSharedPreferences(SharedPreferences preferences) {
        return preferences.getString(AUTHENTICATION_TOKEN_KEY, null);
    }

    public static void setLoginToSharedPreferences(
            SharedPreferences sharedPreferences, String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, login);
        editor.apply();
    }

    public static String getLoginFromSharedPreferences(SharedPreferences preferences) {
        return preferences.getString(LOGIN_KEY, null);
    }

    public static void setUserToSharedPreferences(SharedPreferences sharedPreferences, User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, user.getLogin());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(USER_FIRST_NAME_KEY, user.getFirstName());
        editor.putString(USER_SURNAME_KEY, user.getSurname());
        editor.apply();
    }

    public static User getUserToSharedPreferences(SharedPreferences preferences) {
        User user = new User();
        user.setLogin(preferences.getString(LOGIN_KEY, null));
        user.setEmail(preferences.getString(EMAIL_KEY, null));
        user.setFirstName(preferences.getString(USER_FIRST_NAME_KEY, null));
        user.setSurname(preferences.getString(USER_SURNAME_KEY, null));
        return user;
    }

    public static void clearAllPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}
