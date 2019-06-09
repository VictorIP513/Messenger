package ru.android.messenger.model.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.android.messenger.model.dto.Dialog;
import ru.android.messenger.model.dto.Message;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.chat.ChatDialog;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.model.dto.chat.ChatUser;

public class ChatUtils {

    private ChatUtils() {

    }

    public static ChatMessage convertMessageToChatMessage(Message message) {
        String id = String.valueOf(message.getId());
        String text = message.getText();
        Date createdAt = DateUtils.convertUTCToLocalDate(message.getDate());
        ChatUser chatUser = convertUserToChatUser(message.getUser());
        return new ChatMessage(id, text, createdAt, chatUser);
    }

    public static ChatDialog convertDialogToChatDialog(Dialog dialog) {
        String id = String.valueOf(dialog.getId());
        String name = dialog.getDialogName();
        String photo = dialog.getDialogPhoto();
        List<ChatUser> chatUsers = new ArrayList<>(dialog.getUsers().size());
        for (User user : dialog.getUsers()) {
            chatUsers.add(convertUserToChatUser(user));
        }
        ChatMessage lastMessage = convertMessageToChatMessage(dialog.getLastMessage());
        return new ChatDialog(id, name, photo, chatUsers, lastMessage, 0);
    }

    private static ChatUser convertUserToChatUser(User user) {
        String id = user.getLogin();
        String name = user.getFirstName() + " " + user.getSurname();
        String avatar = UserUtils.getUserPhotoUrl(user.getLogin());
        return new ChatUser(id, name, avatar);
    }
}
