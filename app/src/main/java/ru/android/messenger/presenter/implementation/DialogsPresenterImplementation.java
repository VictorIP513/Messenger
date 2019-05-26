package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.Dialog;
import ru.android.messenger.model.dto.chat.ChatDialog;
import ru.android.messenger.model.utils.ChatUtils;
import ru.android.messenger.presenter.DialogsPresenter;
import ru.android.messenger.view.interfaces.DialogsView;

public class DialogsPresenterImplementation implements DialogsPresenter {

    private DialogsView dialogsView;
    private Repository repository;

    public DialogsPresenterImplementation(DialogsView dialogsView) {
        this.dialogsView = dialogsView;
        repository = Model.getRepository();
    }

    @Override
    public void fillDialogsList() {
        //dialogView.showWaitAlertDialog();
        String authenticationToken =
                PreferenceManager.getAuthenticationToken(dialogsView.getContext());
        repository.getAllDialogs(authenticationToken)
                .enqueue(new DefaultCallback<List<Dialog>, DialogsView>(dialogsView) {
                    @Override
                    public void onResponse(@NonNull Call<List<Dialog>> call,
                                           @NonNull Response<List<Dialog>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            List<Dialog> dialogs = Objects.requireNonNull(response.body());
                            List<ChatDialog> chatDialogs = new ArrayList<>(dialogs.size());
                            for (Dialog dialog : dialogs) {
                                chatDialogs.add(ChatUtils.convertDialogToChatDialog(dialog));
                            }
                            dialogsView.setDialogList(chatDialogs);
                        }
                    }
                });
    }
}
