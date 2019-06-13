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
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.view.activity.DialogActivity;
import ru.android.messenger.view.utils.ViewUtils;

public class Notifications {

    private static final String ADD_FRIEND_MESSAGE = "addFriend";
    private static final String CANCEL_ADD_FRIEND_MESSAGE = "cancelAddFriend";

    private static final int NEW_MESSAGE_NOTIFICATION_ID = 0;
    private static final int NEW_FRIEND_REQUEST_NOTIFICATION_ID = 1;
    private static final int ACCEPT_FRIEND_REQUEST_NOTIFICATION_ID = 2;

    private static final int ADD_FRIEND_REQUEST_CODE = 0;
    private static final int CANCEL_ADD_FRIEND_REQUEST_CODE = 1;

    private Notifications() {

    }

    public static void showNewMessageNotification(Context context, Message message, Bitmap image) {
        String notificationImageText = context.getString(R.string.notification_image_body);

        User sender = message.getUser();
        String userLogin = message.getUser().getLogin();
        String title = sender.getFirstName() + " " + sender.getSurname();
        String content = message.isPhoto() ? notificationImageText : message.getText();

        NotificationCompat.Builder builder =
                createDefaultNotificationBuilder(context, title, content, image);
        builder.setContentIntent(createMessageContentIntent(context, userLogin));
        buildNotificationAndSendToUser(builder, context, NEW_MESSAGE_NOTIFICATION_ID);
    }

    public static void showNewFriendNotification(Context context, User user, Bitmap image) {
        String title = context.getString(R.string.notification_new_friend_title);
        String userName = user.getFirstName() + " " + user.getSurname();
        String content = userName + " " + context.getString(R.string.notification_new_friend_body);
        String firstName = user.getFirstName();
        String surname = user.getSurname();
        String userLogin = user.getLogin();

        String buttonAddText = context.getString(R.string.notification_button_add_text);
        String buttonCancelText = context.getString(R.string.notification_button_cancel_text);

        NotificationCompat.Builder builder =
                createDefaultNotificationBuilder(context, title, content, image);
        builder.addAction(R.drawable.button_done, buttonAddText,
                createAddFriendActionIntent(context, userLogin,
                        ADD_FRIEND_MESSAGE, ADD_FRIEND_REQUEST_CODE));
        builder.addAction(R.drawable.button_cancel, buttonCancelText,
                createAddFriendActionIntent(context, userLogin,
                        CANCEL_ADD_FRIEND_MESSAGE, CANCEL_ADD_FRIEND_REQUEST_CODE));
        builder.setContentIntent(createUserInfoContentIntent(
                context, firstName, surname, userLogin, image));
        buildNotificationAndSendToUser(builder, context, NEW_FRIEND_REQUEST_NOTIFICATION_ID);
    }

    public static void showAcceptFriendRequestNotification(Context context, User user, Bitmap image) {
        String title = context.getString(R.string.notification_accept_friend_request_title);
        String userName = user.getFirstName() + " " + user.getSurname();
        String content = userName + " " +
                context.getString(R.string.notification_accept_friend_request_body);
        String firstName = user.getFirstName();
        String surname = user.getSurname();
        String userLogin = user.getLogin();

        NotificationCompat.Builder builder =
                createDefaultNotificationBuilder(context, title, content, image);
        builder.setContentIntent(createUserInfoContentIntent(
                context, firstName, surname, userLogin, image));
        buildNotificationAndSendToUser(builder, context, ACCEPT_FRIEND_REQUEST_NOTIFICATION_ID);
    }

    private static NotificationCompat.Builder createDefaultNotificationBuilder(
            Context context, String title, String content, Bitmap image) {
        return new NotificationCompat.Builder(
                context, NotificationChannels.getMessengerNotificationChannelId())
                .setSmallIcon(R.drawable.logo_small)
                .setLargeIcon(image)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(Calendar.getInstance().getTimeInMillis())
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content));
    }

    private static void buildNotificationAndSendToUser(
            NotificationCompat.Builder builder, Context context, int notificationId) {
        Notification notification = builder.build();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

    private static PendingIntent createAddFriendActionIntent(
            Context context, String userLogin, String message, int requestCode) {
        Intent addFriendIntent = new Intent(context, NotificationReceiver.class);
        addFriendIntent.putExtra("message", message);
        addFriendIntent.putExtra("user_login", userLogin);
        return PendingIntent.getBroadcast(context,
                requestCode, addFriendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent createUserInfoContentIntent(
            Context context, String firstName, String surname, String login, Bitmap photo) {
        Intent intent = ViewUtils.getUserInfoIntent(context, firstName, surname, login, photo);
        return PendingIntent.getActivity(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent createMessageContentIntent(Context context, String userLogin) {
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("user_login", userLogin);
        return PendingIntent.getActivity(context,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
