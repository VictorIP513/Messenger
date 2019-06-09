package ru.android.messenger.view.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import ru.android.messenger.R;
import ru.android.messenger.view.activity.DialogActivity;

public class Notifications {

    private static NotificationChannels notificationChannels;

    private Notifications() {

    }

    public static void showNewMessageNotification(Context context, String title,
                                                  String content, Bitmap image, String userLogin) {
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
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createMessageContentIntent(context, userLogin))
                .setAutoCancel(true);

        Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private static PendingIntent createMessageContentIntent(Context context, String userLogin) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("user_login", userLogin);
        return PendingIntent.getActivity(context,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
