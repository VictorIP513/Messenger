package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.Dialog;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.model.utils.ChatUtils;
import ru.android.messenger.presenter.DialogPresenter;
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
        //dialogView.showWaitAlertDialog();
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.createDialog(login, lastMessageText, authenticationToken)
                .enqueue(new DefaultCallback<Dialog, DialogView>(dialogView) {
                    @Override
                    public void onResponse(@NonNull Call<Dialog> call,
                                           @NonNull Response<Dialog> response) {
                        super.onResponse(call, response);
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
        //dialogView.showWaitAlertDialog();
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.sendMessage(dialogId, message, authenticationToken)
                .enqueue(new DefaultCallback<Message, DialogView>(dialogView) {
                    @Override
                    public void onResponse(@NonNull Call<Message> call,
                                           @NonNull Response<Message> response) {
                        super.onResponse(call, response);
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
        //dialogView.showWaitAlertDialog();
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogView.getContext());
        repository.getDialog(login, authenticationToken)
                .enqueue(new DefaultCallback<Dialog, DialogView>(dialogView) {
                    @Override
                    public void onResponse(@NonNull Call<Dialog> call,
                                           @NonNull Response<Dialog> response) {
                        super.onResponse(call, response);
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

    private void sortMessagesByTime(List<ChatMessage> messages) {
        Collections.sort(messages, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                return o2.getCreatedAt().compareTo(o1.getCreatedAt());
            }
        });
    }
}