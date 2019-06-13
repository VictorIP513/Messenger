package ru.android.messenger.model.utils.http;

import java.util.List;

import ru.android.messenger.model.dto.Dialog;

public interface OnDialogsListLoadedListener {

    void onDialogListLoadedListener(List<Dialog> dialogList);
}
