package ru.android.messenger.model.dto;

import java.util.List;

@SuppressWarnings("unused")
public class Dialog {

    private int id;
    private String dialogPhoto;
    private String dialogName;
    private Message lastMessage;
    private List<User> users;
    private List<Message> messages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDialogPhoto() {
        return dialogPhoto;
    }

    public void setDialogPhoto(String dialogPhoto) {
        this.dialogPhoto = dialogPhoto;
    }

    public String getDialogName() {
        return dialogName;
    }

    public void setDialogName(String dialogName) {
        this.dialogName = dialogName;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
