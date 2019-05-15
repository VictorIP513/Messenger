package ru.android.messenger.utils;

import android.util.Log;

public class Logger {

    private static final String TAG = "Messenger";

    private Logger() {

    }

    public static void error(String message, Throwable t) {
        Log.e(TAG, message, t);
    }

    public static void warning(String message) {
        Log.w(TAG, message);
    }
}
