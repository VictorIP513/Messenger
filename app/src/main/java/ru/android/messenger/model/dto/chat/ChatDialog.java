package ru.android.messenger.model.dto.chat;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.List;

public class ChatDialog implements IDialog<ChatMessage> {

    private String id;
    private String dialogPhoto;
    private String dialogName;
    private List<ChatUser> users;
    private ChatMessage lastMessage;
    private int unreadCount;
    private String userLogin;

    public ChatDialog(String id, String name, String photo, List<ChatUser> users,
                      ChatMessage lastMessage, int unreadCount) {
        this.id = id;
        this.dialogName = name;
        this.dialogPhoto = photo;
        this.users = users;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDialogPhoto() {
        return dialogPhoto;
    }

    @Override
    public String getDialogName() {
        return dialogName;
    }

    @Override
    public List<? extends IUser> getUsers() {
        return users;
    }

    @Override
    public ChatMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(ChatMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public int getUnreadCount() {
        return unreadCount;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
}
