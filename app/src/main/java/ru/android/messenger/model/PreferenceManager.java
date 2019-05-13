package ru.android.messenger.model;

import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String AUTHENTICATION_TOKEN_KEY = "authenticationToken";

    private PreferenceManager() {

    }

    public static void setAuthenticationTokenToSharedPreferences(
            SharedPreferences.Editor editor, String authenticationToken) {
        editor.putString(AUTHENTICATION_TOKEN_KEY, authenticationToken);
    }

    public static String getAuthenticationTokenFromSharedPreferences(SharedPreferences preferences) {
        return preferences.getString(AUTHENTICATION_TOKEN_KEY, null);
    }
}
