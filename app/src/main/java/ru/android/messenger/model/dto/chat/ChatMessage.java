package ru.android.messenger.model.dto.chat;

import android.support.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class ChatMessage implements IMessage, MessageContentType.Image {

    private String id;
    private String text;
    private Date createdAt;
    private ChatUser user;
    private Image image;

    public ChatMessage(String id, String text, Date createdAt, ChatUser user, Image image) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.user = user;
        this.image = image;
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

    @Nullable
    @Override
    public String getImageUrl() {
        return image == null ? null : image.url;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public static class Image {

        private String url;

        public Image(String url) {
            this.url = url;
        }
    }
}
