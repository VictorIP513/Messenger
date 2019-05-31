package ru.android.messenger.view.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

public class NotificationChannels extends ContextWrapper {

    private static final String MESSENGER_NOTIFICATION_CHANNEL_ID = "MessengerChannel";
    private static final String MESSENGER_NOTIFICATION_CHANNEL_NAME = "Messenger";

    public NotificationChannels(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel androidChannel = new NotificationChannel(
                    MESSENGER_NOTIFICATION_CHANNEL_ID,
                    MESSENGER_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);

            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.BLUE);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(androidChannel);
        }
    }

    public static String getMessengerNotificationChannelId() {
        return MESSENGER_NOTIFICATION_CHANNEL_ID;
    }
}
