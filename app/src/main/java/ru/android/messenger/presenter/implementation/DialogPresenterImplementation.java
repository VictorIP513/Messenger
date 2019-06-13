package ru.android.messenger.presenter.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.Dialog;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.model.utils.ChatUtils;
import ru.android.messenger.model.utils.DateUtils;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.model.utils.http.HttpUtils;
import ru.android.messenger.model.utils.http.OnDateLoadedListener;
import ru.android.messenger.model.utils.http.OnMessageSentOutListenter;
import ru.android.messenger.model.utils.http.OnPhotoLoadedListener;
import ru.android.messenger.model.utils.http.OnUserLoadedListener;
import ru.android.messenger.presenter.DialogPresenter;
import ru.android.messenger.utils.Logger;
import ru.android.messenger.view.interfaces.DialogView;

public class DialogPresenterImplementation implements DialogPresenter {

    private DialogView dialogView;
    private Repository repository;

    private int dialogId;

    public DialogPresenterImplementation(DialogView dialogView) {
        this.dialogView = dialogView;
        repository = Model.getRepository();
    }

    @Override
    public void createDialog(String login, String lastMessageText) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.createDialog(login, lastMessageText, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Dialog>() {
                    @Override
                    public void onResponse(@NonNull Call<Dialog> call,
                                           @NonNull Response<Dialog> response) {
                        if (response.isSuccessful()) {
                            Dialog dialog = Objects.requireNonNull(response.body());
                            dialogId = dialog.getId();
                            ChatMessage chatMessage =
                                    ChatUtils.convertMessageToChatMessage(dialog.getLastMessage());
                            dialogView.setNewMessage(chatMessage);
                        }
                    }
                });

    }

    @Override
    public void sendMessage(String message) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.sendMessage(dialogId, message, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Message>() {
                    @Override
                    public void onResponse(@NonNull Call<Message> call,
                                           @NonNull Response<Message> response) {
                        if (response.isSuccessful()) {
                            Message responseMessage = Objects.requireNonNull(response.body());
                            ChatMessage chatMessage =
                                    ChatUtils.convertMessageToChatMessage(responseMessage);
                            dialogView.setNewMessage(chatMessage);
                        }
                    }
                });
    }

    @Override
    public void fillDialog(String login) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.getDialog(login, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Dialog>() {
                    @Override
                    public void onResponse(@NonNull Call<Dialog> call,
                                           @NonNull Response<Dialog> response) {
                        if (response.isSuccessful()) {
                            Dialog dialog = Objects.requireNonNull(response.body());
                            dialogId = dialog.getId();
                            List<ChatMessage> messages = new ArrayList<>();
                            for (Message message : dialog.getMessages()) {
                                messages.add(ChatUtils.convertMessageToChatMessage(message));
                            }
                            sortMessagesByTime(messages);
                            dialogView.setMessageList(messages);
                        }
                    }
                });
    }

    @Override
    public void fillUserInformation(String login) {
        Context context = dialogView.getContext();
        HttpUtils.getUserAndExecuteAction(login, new OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                String userName = user.getFirstName() + " " + user.getSurname();
                dialogView.setDialogUserName(userName);
            }
        });

        HttpUtils.getUserPhotoAndExecuteAction(login, context, new OnPhotoLoadedListener() {
            @Override
            public void onPhotoLoaded(Bitmap photo) {
                dialogView.setDialogPhoto(photo);
            }
        });
    }

    @Override
    public void fillBlockedInformation(String login) {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.getBlockYouStatus(login, authenticationToken)
                .enqueue(new CallbackWithoutAlerts<Boolean>() {
                    @Override
                    public void onResponse(@NonNull Call<Boolean> call,
                                           @NonNull Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            boolean isBlockedUser = Objects.requireNonNull(response.body());
                            if (isBlockedUser) {
                                dialogView.setBlockedInformation();
                            }
                        }
                    }
                });
    }

    @Override
    public void fillLastOnlineDate(String login) {
        HttpUtils.getLastOnlineDate(login, new OnDateLoadedListener() {
            @Override
            public void onDateLoaded(Date date) {
                if (DateUtils.isOnline(date)) {
                    dialogView.setUserIsOnline();
                } else {
                    String[] lastOnlineDate = DateUtils.getFormattedDateAndTime(date);
                    dialogView.setLastOnlineDate(lastOnlineDate[0], lastOnlineDate[1]);
                }
            }
        });
    }

    @Override
    public Bitmap getBitmapFromUri(Uri data) {
        Context context = dialogView.getContext();
        try {
            return ImageHelper.getBitmapFromUriAndContext(data, context);
        } catch (FileNotFoundException e) {
            Logger.error("Image not found", e);
            dialogView.setImageNotFoundError();
        }
        return null;
    }

    @Override
    public void sendImage(Bitmap image) {
        Context context = dialogView.getContext();
        HttpUtils.sendImage(dialogId, image, context, new OnMessageSentOutListenter() {
            @Override
            public void onMessageSentOut(Message message) {
                ChatMessage chatMessage = ChatUtils.convertMessageToChatMessage(message);
                dialogView.setNewMessage(chatMessage);
            }
        });
    }

    private void sortMessagesByTime(List<ChatMessage> messages) {
        Collections.sort(messages, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });
    }
}
