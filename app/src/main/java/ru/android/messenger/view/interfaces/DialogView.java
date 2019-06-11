package ru.android.messenger.view.interfaces;

import android.graphics.Bitmap;

import java.util.List;

import ru.android.messenger.model.dto.chat.ChatMessage;

public interface DialogView extends ViewWithAlerts {

    void setNewMessage(ChatMessage message);

    void setMessageList(List<ChatMessage> messageList);

    String getUserLogin();

    void setDialogUserName(String dialogUserName);

    void setDialogPhoto(Bitmap dialogPhoto);
}

