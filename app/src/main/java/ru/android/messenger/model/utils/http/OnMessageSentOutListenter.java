package ru.android.messenger.model.utils.http;

import ru.android.messenger.model.dto.Message;

public interface OnMessageSentOutListenter {

    void onMessageSentOut(Message message);
}
