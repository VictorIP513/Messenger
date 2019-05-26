package ru.android.messenger.model.dto.chat;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Date;

public class ChatMessage implements IMessage {

    private String id;
    private String text;
    private Date createdAt;
    private ChatUser user;

    public ChatMessage(String id, String text, Date createdAt, ChatUser user) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public IUser getUser() {
        return user;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }
}
