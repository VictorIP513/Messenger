package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;
import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.chat.ChatDialog;
import ru.android.messenger.presenter.DialogListPresenter;
import ru.android.messenger.presenter.implementation.DialogListPresenterImplementation;
import ru.android.messenger.view.interfaces.DialogsView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DialogListActivity extends ActivityWithNavigationDrawer implements DialogsView {

    private DialogListPresenter dialogListPresenter;

    private ImageLoader imageLoader;
    private DialogsListAdapter<ChatDialog> dialogsAdapter;

    private List<ChatDialog> dialogs;

    private DialogsList dialogsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_dialog_list);

        dialogListPresenter = new DialogListPresenterImplementation(this);

        findViews();
        createImageLoader();
        configureActionBar();
        dialogListPresenter.fillDialogsList();
    }

    @Override
    public void setDialogList(List<ChatDialog> dialogs) {
        this.dialogs = dialogs;
        initAdapter();
    }

    public void updateDialogs() {
        dialogListPresenter.fillDialogsList();
    }

    private void findViews() {
        dialogsList = findViewById(R.id.dialogs_list);
    }

    private void initAdapter() {
        dialogsAdapter = new DialogsListAdapter<>(R.layout.item_dialog, imageLoader);
        dialogsAdapter.setItems(dialogs);
        dialogsAdapter.setOnDialogClickListener(
                new DialogsListAdapter.OnDialogClickListener<ChatDialog>() {
                    @Override
                    public void onDialogClick(ChatDialog dialog) {
                        openDialog(dialog.getUserLogin());
                    }
                });
        dialogsAdapter.setOnDialogLongClickListener(
                new DialogsListAdapter.OnDialogLongClickListener<ChatDialog>() {
                    @Override
                    public void onDialogLongClick(ChatDialog dialog) {
                        openDialog(dialog.getUserLogin());
                    }
                });
        dialogsList.setAdapter(dialogsAdapter);
    }

    private void openDialog(String userLogin) {
        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("user_login", userLogin);
        startActivity(intent);
    }

    private void createImageLoader() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView,
                                  @Nullable String url, @Nullable Object payload) {
                Picasso.with(DialogListActivity.this)
                        .load(url)
                        .error(R.drawable.profile_thumbnail)
                        .into(imageView);
            }
        };
    }

    private void configureActionBar() {
        ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());
        String actionBarTitle = getString(R.string.dialogs_list_activity_action_bar_title);
        actionBar.setTitle(actionBarTitle);
    }
}
