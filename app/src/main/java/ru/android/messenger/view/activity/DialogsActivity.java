package ru.android.messenger.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.chat.ChatDialog;
import ru.android.messenger.presenter.DialogsPresenter;
import ru.android.messenger.presenter.implementation.DialogsPresenterImplementation;
import ru.android.messenger.view.interfaces.DialogsView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DialogsActivity extends ActivityWithNavigationDrawer implements DialogsView {

    private DialogsPresenter dialogsPresenter;

    private ImageLoader imageLoader;
    private DialogsListAdapter<ChatDialog> dialogsAdapter;

    private List<ChatDialog> dialogs;

    private DialogsList dialogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_dialogs);

        dialogsPresenter = new DialogsPresenterImplementation(this);

        findViews();
        createImageLoader();
        dialogsPresenter.fillDialogsList();
    }

    @Override
    public void setDialogList(List<ChatDialog> dialogs) {
        this.dialogs = dialogs;
        initAdapter();
    }

    private void findViews() {
        dialogsList = findViewById(R.id.dialogsList);
    }

    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsAdapter.setItems(dialogs);
        dialogsAdapter.setOnDialogClickListener(new DialogsListAdapter.OnDialogClickListener<ChatDialog>() {
            @Override
            public void onDialogClick(ChatDialog dialog) {

            }
        });
        dialogsAdapter.setOnDialogLongClickListener(new DialogsListAdapter.OnDialogLongClickListener<ChatDialog>() {
            @Override
            public void onDialogLongClick(ChatDialog dialog) {

            }
        });
        dialogsList.setAdapter(dialogsAdapter);
    }

    private void createImageLoader() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, @Nullable String url, @Nullable Object payload) {
                Picasso.with(DialogsActivity.this).load(url).into(imageView);
            }
        };
    }
}
