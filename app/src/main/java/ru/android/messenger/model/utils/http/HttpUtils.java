package ru.android.messenger.model.utils.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.R;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.utils.Logger;

public class HttpUtils {

    private static final String IMAGE_PART_NAME = "image";

    private static Repository repository = Model.getRepository();

    private HttpUtils() {

    }

    public static void getUserPhotoAndExecuteAction(String login, final Context context,
                                                    final OnPhotoLoadedListener listener) {
        repository.getUserPhoto(login).enqueue(new CallbackWithoutAlerts<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                Bitmap bitmap;
                if (response.isSuccessful()) {
                    File photo = Objects.requireNonNull(FileUtils.getFileFromResponseBody(
                            response.body(), context));
                    bitmap = ImageHelper.getBitmapFromFile(photo);
                } else {
                    bitmap = BitmapFactory.decodeResource(
                            context.getResources(), R.drawable.profile_thumbnail);
                }
                listener.onPhotoLoaded(bitmap);
            }
        });
    }

    public static void getUserAndExecuteAction(String login, final Context context,
                                               final OnUserLoadedListener listener) {
        repository.getUser(login).enqueue(new CallbackWithoutAlerts<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    listener.onUserLoaded(user);
                }
            }
        });
    }

    public static void cancelFriendRequest(String login, final Context context) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(context);
        String cancelRequestErrorText = context.getString(R.string.toast_cancel_add_friend_error);
        repository.deleteFromFriend(login, authenticationToken)
                .enqueue(createFriendCallback(cancelRequestErrorText, context));
    }

    public static void acceptFriendRequest(String login, Context context) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(context);
        String acceptRequestErrorText = context.getString(R.string.toast_add_friend_error);
        repository.acceptFriendRequest(login, authenticationToken)
                .enqueue(createFriendCallback(acceptRequestErrorText, context));
    }

    public static void checkAuthenticationToken(
            Context context, final OnAuthenticationTokenCheckedListener listener) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(context);
        repository.checkAuthenticationToken(authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call,
                                           @NonNull Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            boolean isCorrectAuthenticationToken =
                                    Objects.requireNonNull(response.body());
                            listener.onAuthenticationTokenChecked(isCorrectAuthenticationToken);
                        }
                    }
                });
    }

    public static void sendUserIsOnlineNotification(Context context) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(context);
        repository.userIsOnline(authenticationToken).enqueue(new CallbackWithoutAlerts<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                // unused response
            }
        });
    }

    public static void getLastOnlineDate(String login, final OnDateLoadedListener listener) {
        repository.getLastOnlineDate(login).enqueue(new CallbackWithoutAlerts<Date>() {
            @Override
            public void onResponse(@NonNull Call<Date> call, @NonNull Response<Date> response) {
                if (response.isSuccessful()) {
                    Date date = response.body();
                    listener.onDateLoaded(date);
                }
            }
        });
    }

    public static void sendImage(int dialogId, Bitmap image, Context context,
                                 final OnMessageSentOutListenter listener) {
        try {
            String authenticationToken =
                    PreferenceManager.getAuthenticationToken(context);
            File imageFile = ImageHelper.writeBitmapToFile(image, context);
            MultipartBody.Part file = Model.createFileToSend(imageFile, IMAGE_PART_NAME);
            repository.sendImage(dialogId, file, authenticationToken)
                    .enqueue(new CallbackWithoutAlerts<Message>() {
                        @Override
                        public void onResponse(@NonNull Call<Message> call,
                                               @NonNull Response<Message> response) {
                            if (response.isSuccessful()) {
                                Message message = response.body();
                                listener.onMessageSentOut(message);
                            }
                        }
                    });
        } catch (IOException e) {
            Logger.error("Error when writing bitmap to file", e);
        }
    }

    private static CallbackWithoutAlerts<Void> createFriendCallback(
            final String errorText, final Context context) {
        return new CallbackWithoutAlerts<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, errorText, Toast.LENGTH_SHORT).show();
            }
        };
    }
}
