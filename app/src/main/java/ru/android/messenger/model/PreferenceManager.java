package ru.android.messenger.model;

import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String AUTHENTICATION_TOKEN_KEY = "authenticationToken";

    private PreferenceManager() {

    }

    public static void setAuthenticationTokenToSharedPreferences(
            SharedPreferences sharedPreferences, String authenticationToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTHENTICATION_TOKEN_KEY, authenticationToken);
        editor.commit();
    }

    public static String getAuthenticationTokenFromSharedPreferences(SharedPreferences preferences) {
        return preferences.getString(AUTHENTICATION_TOKEN_KEY, null);
    }

    public static void clearAllPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}
