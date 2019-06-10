package ru.android.messenger.service;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ru.android.messenger.MessengerApplication;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.model.utils.ChatUtils;
import ru.android.messenger.model.utils.http.HttpUtils;
import ru.android.messenger.model.utils.http.OnPhotoLoadedListener;
import ru.android.messenger.view.activity.DialogActivity;
import ru.android.messenger.view.activity.DialogListActivity;
import ru.android.messenger.view.notifications.Notifications;

public class FCMService extends FirebaseMessagingService {

    private static final String MESSAGE_DATA_TYPE = "message";
    private static final String NEW_FRIEND_TYPE = "new_friend";
    private static final String ACCEPT_FRIEND_REQUEST_TYPE = "accept_friend_request";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        MessengerApplication application = (MessengerApplication) getApplication();
        Activity activity = application.getCurrentActivity();

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            if (data.get(MESSAGE_DATA_TYPE) != null) {
                String json = data.get(MESSAGE_DATA_TYPE);
                Message message = ApiUtils.getObjectFromJson(json, Message.class);
                processMessage(message, activity);
            }
            if (data.get(NEW_FRIEND_TYPE) != null) {
                String json = data.get(NEW_FRIEND_TYPE);
                User user = ApiUtils.getObjectFromJson(json, User.class);
                sendNewFriendNotification(user);
            }
            if (data.get(ACCEPT_FRIEND_REQUEST_TYPE) != null) {
                String json = data.get(ACCEPT_FRIEND_REQUEST_TYPE);
                User user = ApiUtils.getObjectFromJson(json, User.class);
                sendAcceptFriendRequestNotification(user);
            }
        }
    }

    private void processMessage(Message message, Activity activity) {
        if (activity instanceof DialogActivity) {
            setNewMessageToDialog((DialogActivity) activity, message);
        } else {
            if (activity instanceof DialogListActivity) {
                ((DialogListActivity) activity).updateDialogs();
            }
            if (isEnabledNotifications()) {
                sendMessageNotification(message);
            }
        }
    }

    private boolean isEnabledNotifications() {
        return PreferenceManager.isEnabledNotifications(this);
    }

    private void setNewMessageToDialog(DialogActivity activity, Message message) {
        String userLogin = activity.getUserLogin();
        if (message.getUser().getLogin().equals(userLogin)) {
            ChatMessage chatMessage = ChatUtils.convertMessageToChatMessage(message);
            activity.setNewMessage(chatMessage);
        }
    }

    private void sendAcceptFriendRequestNotification(final User user) {
        HttpUtils.getUserPhotoAndExecuteAction(user.getLogin(), this,
                new OnPhotoLoadedListener() {
                    @Override
                    public void onPhotoLoaded(Bitmap photo) {
                        Notifications.showAcceptFriendRequestNotification(
                                FCMService.this, user, photo);
                    }
                });
    }

    private void sendNewFriendNotification(final User user) {
        HttpUtils.getUserPhotoAndExecuteAction(user.getLogin(), this,
                new OnPhotoLoadedListener() {
                    @Override
                    public void onPhotoLoaded(Bitmap photo) {
                        Notifications.showNewFriendNotification(
                                FCMService.this, user, photo);
                    }
                });
    }

    private void sendMessageNotification(final Message message) {
        User sender = message.getUser();
        HttpUtils.getUserPhotoAndExecuteAction(sender.getLogin(), this,
                new OnPhotoLoadedListener() {
                    @Override
                    public void onPhotoLoaded(Bitmap photo) {
                        Notifications.showNewMessageNotification(
                                FCMService.this, message, photo);
                    }
                });
    }
}
