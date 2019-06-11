package ru.android.messenger.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.presenter.DialogPresenter;
import ru.android.messenger.presenter.implementation.DialogPresenterImplementation;
import ru.android.messenger.view.interfaces.DialogView;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DialogActivity extends ActivityWithNavigationDrawer implements DialogView {

    private DialogPresenter dialogPresenter;

    private ImageLoader imageLoader;
    private MessagesListAdapter<ChatMessage> messagesAdapter;
    private MessagesList messagesList;
    private MessageInput messageInput;

    private String userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_dialog);

        dialogPresenter = new DialogPresenterImplementation(this);

        findViews();
        setDataFromIntent();
        createImageLoader();
        initMessagesAdapter();
        initMessageInput();
        dialogPresenter.fillDialog(userLogin);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewUtils.createActionBarWithBackButtonForActivity(this, toolbar, "");
    }

    @Override
    public void setNewMessage(final ChatMessage message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messagesAdapter.addToStart(message, true);
            }
        });
    }

    @Override
    public void setMessageList(List<ChatMessage> messageList) {
        messagesAdapter.addToEnd(messageList, false);
    }

    @Override
    public String getUserLogin() {
        return userLogin;
    }

    private void findViews() {
        messagesList = findViewById(R.id.messages_list);
        messageInput = findViewById(R.id.message_input);
    }

    private void setDataFromIntent() {
        userLogin = getIntent().getStringExtra("user_login");
    }

    private void createImageLoader() {
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url, Object payload) {
                Picasso.with(DialogActivity.this)
                        .load(url)
                        .error(R.drawable.profile_thumbnail)
                        .into(imageView);
            }
        };
    }

    private void initMessagesAdapter() {
        messagesAdapter = new MessagesListAdapter<>(
                PreferenceManager.getLogin(this), imageLoader);
        messagesList.setAdapter(messagesAdapter);
    }

    private void initMessageInput() {
        messageInput.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                if (messagesAdapter.isEmpty()) {
                    dialogPresenter.createDialog(userLogin, input.toString());
                } else {
                    dialogPresenter.sendMessage(input.toString());
                }
                return true;
            }
        });
    }
}
