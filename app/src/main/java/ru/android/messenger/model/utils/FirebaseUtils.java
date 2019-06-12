package ru.android.messenger.model.utils;

import android.content.Context;

import com.google.firebase.messaging.FirebaseMessaging;

import ru.android.messenger.model.PreferenceManager;

public class FirebaseUtils {

    private FirebaseUtils() {

    }

    public static void subscribeToReceiveMessages(Context context) {
        String authenticationToken = PreferenceManager.getAuthenticationToken(context);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic(authenticationToken);
    }

    public static void unsubscribeFromReceivingMessages(Context context) {
        String authenticationToken = PreferenceManager.getAuthenticationToken(context);
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.unsubscribeFromTopic(authenticationToken);
    }
}
