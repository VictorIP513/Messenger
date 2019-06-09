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
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.Dialog;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.chat.ChatDialog;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.model.utils.ChatUtils;
import ru.android.messenger.model.utils.UserUtils;
import ru.android.messenger.presenter.DialogListPresenter;
import ru.android.messenger.view.interfaces.DialogsView;

public class DialogListPresenterImplementation implements DialogListPresenter {

    private DialogsView dialogsView;
    private Repository repository;

    public DialogListPresenterImplementation(DialogsView dialogsView) {
        this.dialogsView = dialogsView;
        repository = Model.getRepository();
    }

    @Override
    public void fillDialogsList() {
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogsView.getContext());
        repository.getAllDialogs(authenticationToken)
                .enqueue(new CallbackWithoutAlerts<List<Dialog>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Dialog>> call,
                                           @NonNull Response<List<Dialog>> response) {
                        if (response.isSuccessful()) {
                            List<Dialog> dialogs = Objects.requireNonNull(response.body());
                            List<ChatDialog> chatDialogs = getDialogsToView(dialogs);
                            dialogsView.setDialogList(chatDialogs);
                        }
                    }
                });
    }

    private List<ChatDialog> getDialogsToView(List<Dialog> dialogs) {
        String currentUserLogin = PreferenceManager.getLogin(dialogsView.getContext());

        List<ChatDialog> chatDialogs = new ArrayList<>(dialogs.size());
        for (Dialog dialog : dialogs) {
            List<User> users = dialog.getUsers();
            for (User user : users) {
                if (!user.getLogin().equals(currentUserLogin)) {
                    String dialogName = user.getFirstName() + " " + user.getSurname();
                    dialog.setDialogName(dialogName);
                    dialog.setDialogPhoto(UserUtils.getUserPhotoUrl(user.getLogin()));
                    ChatDialog chatDialog = ChatUtils.convertDialogToChatDialog(dialog);
                    chatDialog.setUserLogin(user.getLogin());
                    chatDialogs.add(chatDialog);
                    break;
                }
            }
        }
        sortDialogsByTime(chatDialogs);
        return chatDialogs;
    }

    private void sortDialogsByTime(List<ChatDialog> chatDialogs) {
        Collections.sort(chatDialogs, new Comparator<ChatDialog>() {
            @Override
            public int compare(ChatDialog o1, ChatDialog o2) {
                ChatMessage firstLastMessage = o1.getLastMessage();
                ChatMessage secondLastMessage = o2.getLastMessage();
                return secondLastMessage.getCreatedAt().compareTo(firstLastMessage.getCreatedAt());
            }
        });
    }
}
