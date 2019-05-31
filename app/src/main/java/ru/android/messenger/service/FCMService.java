package ru.android.messenger.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.R;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.api.ApiUtils;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.view.notifications.Notifications;

public class FCMService extends FirebaseMessagingService {

    private static final Repository repository = Model.getRepository();
    private static final String MESSAGE_DATA_TYPE = "message";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            if (data.get(MESSAGE_DATA_TYPE) != null) {

                String json = data.get(MESSAGE_DATA_TYPE);
                Message message = ApiUtils.getObjectFromJson(json, Message.class);
                sendMessageNotification(message);
            }
        }
    }

    private void sendMessageNotification(final Message message) {
        User sender = message.getUser();
        repository.getUserPhoto(sender.getLogin())
                .enqueue(new CallbackWithoutAlerts<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call,
                                           @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            File photo = Objects.requireNonNull(FileUtils.getFileFromResponseBody(
                                    response.body(), getApplicationContext()));
                            Bitmap bitmap = ImageHelper.getBitmapFromFile(photo);
                            createNotification(message, bitmap);
                        } else {
                            Bitmap bitmap = BitmapFactory.decodeResource(
                                    getResources(), R.drawable.profile_thumbnail);
                            createNotification(message, bitmap);
                        }
                    }
                });
    }

    private void createNotification(Message message, Bitmap bitmap) {
        User sender = message.getUser();
        String title = sender.getFirstName() + " " + sender.getSurname();
        String content = message.getText();
        Date date = message.getDate();
        Notifications.showNewMessageNotification(
                getApplicationContext(), title, content, bitmap, date);
    }
}
