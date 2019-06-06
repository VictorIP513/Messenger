package ru.android.messenger.view.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import ru.android.messenger.R;

public class Notifications {

    private static NotificationChannels notificationChannels;

    private Notifications() {

    }

    public static void showNewMessageNotification(Context context, String title,
                                                  String content, Bitmap image) {
        if (notificationChannels == null) {
            notificationChannels = new NotificationChannels(context);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context, NotificationChannels.getMessengerNotificationChannelId())
                .setSmallIcon(R.drawable.logo_small)
                .setLargeIcon(image)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
