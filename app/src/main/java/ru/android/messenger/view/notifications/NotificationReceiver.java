package ru.android.messenger.view.notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.android.messenger.model.utils.http.HttpUtils;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String ADD_FRIEND_MESSAGE = "addFriend";
    private static final String CANCEL_ADD_FRIEND_MESSAGE = "cancelAddFriend";

    private static final int NEW_FRIEND_REQUEST_NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        if (message.equals(ADD_FRIEND_MESSAGE)) {
            String userLogin = intent.getStringExtra("user_login");
            HttpUtils.acceptFriendRequest(userLogin, context);
        }
        if (message.equals(CANCEL_ADD_FRIEND_MESSAGE)) {
            String userLogin = intent.getStringExtra("user_login");
            HttpUtils.cancelFriendRequest(userLogin, context);
        }

        cancelNotification(context);
    }

    private void cancelNotification(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NEW_FRIEND_REQUEST_NOTIFICATION_ID);
    }
}
