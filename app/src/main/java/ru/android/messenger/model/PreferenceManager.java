package ru.android.messenger.model;

import android.content.Context;
import android.content.SharedPreferences;

import ru.android.messenger.model.dto.User;

public class PreferenceManager {

    private static final String AUTHENTICATION_TOKEN_KEY = "authenticationToken";
    private static final String LAST_LOGIN_KEY = "lastLogin";
    private static final String LOGIN_KEY = "login";
    private static final String EMAIL_KEY = "email";
    private static final String USER_FIRST_NAME_KEY = "userFirstName";
    private static final String USER_SURNAME_KEY = "userSurname";
    private static final String ENABLED_NOTIFICATIONS_KEY = "enabledNotifications";

    private static final String GLOBAL_SHARED_PREFERENCES_FILE = "application_preferences";

    private PreferenceManager() {

    }

    public static void setAuthenticationToken(Context context, String authenticationToken) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTHENTICATION_TOKEN_KEY, authenticationToken);
        editor.apply();
    }

    public static String getAuthenticationToken(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(AUTHENTICATION_TOKEN_KEY, null);
    }

    public static void setLastLogin(Context context, String lastLogin) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LAST_LOGIN_KEY, lastLogin);
        editor.apply();
    }

    public static String getLastLogin(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(LAST_LOGIN_KEY, null);
    }

    public static void setLogin(Context context, String login) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, login);
        editor.apply();
    }

    public static String getLogin(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(LOGIN_KEY, null);
    }

    public static void setUser(Context context, User user) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, user.getLogin());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(USER_FIRST_NAME_KEY, user.getFirstName());
        editor.putString(USER_SURNAME_KEY, user.getSurname());
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        User user = new User();
        user.setLogin(sharedPreferences.getString(LOGIN_KEY, null));
        user.setEmail(sharedPreferences.getString(EMAIL_KEY, null));
        user.setFirstName(sharedPreferences.getString(USER_FIRST_NAME_KEY, null));
        user.setSurname(sharedPreferences.getString(USER_SURNAME_KEY, null));
        return user;
    }

    public static void setEnabledNotifications(Context context, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ENABLED_NOTIFICATIONS_KEY, value);
        editor.apply();
    }

    public static boolean isEnabledNotifications(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(ENABLED_NOTIFICATIONS_KEY, true);
    }

    public static void clearPreferencesWithoutLastLogin(Context context) {
        String lastLogin = getLastLogin(context);
        clearAllPreferences(context);
        setLastLogin(context, lastLogin);
    }

    public static void clearAllPreferences(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(GLOBAL_SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
    }
}
