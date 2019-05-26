package ru.android.messenger.view.interfaces;

import java.util.List;

import ru.android.messenger.model.dto.chat.ChatDialog;

public interface DialogsView extends ViewWithAlerts {

    void setDialogList(List<ChatDialog> dialogs);
}
