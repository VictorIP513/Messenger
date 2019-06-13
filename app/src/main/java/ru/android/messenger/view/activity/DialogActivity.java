package ru.android.messenger.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.List;
import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.dto.chat.ChatMessage;
import ru.android.messenger.presenter.DialogPresenter;
import ru.android.messenger.presenter.implementation.DialogPresenterImplementation;
import ru.android.messenger.view.interfaces.DialogView;
import ru.android.messenger.view.utils.Alerts;
import ru.android.messenger.view.utils.ViewUtils;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DialogActivity extends ActivityWithNavigationDrawer implements DialogView {

    private static final int UPLOAD_PHOTO_REQUEST_CODE = 0;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;

    private DialogPresenter dialogPresenter;
    private MessagesListAdapter<ChatMessage> messagesAdapter;
    private ImageLoader imageLoader;

    private MessagesList messagesList;
    private MessageInput messageInput;
    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewBlockedInformation;
    private TextView textViewLastOnlineDate;

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
        fillUserInformation();
        dialogPresenter.fillDialog(userLogin);
        dialogPresenter.fillBlockedInformation(userLogin);
        dialogPresenter.fillLastOnlineDate(userLogin);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPLOAD_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            getImageFromDevice(data);
        }
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            getImageFromCamera(data);
        }
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

    @Override
    public void setDialogUserName(String dialogUserName) {
        textViewName.setText(dialogUserName);
    }

    @Override
    public void setDialogPhoto(Bitmap dialogPhoto) {
        imageViewProfile.setImageBitmap(dialogPhoto);
    }

    @Override
    public void setBlockedInformation() {
        messageInput.setVisibility(View.INVISIBLE);
        textViewBlockedInformation.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUserIsOnline() {
        String onlineText = getString(R.string.dialog_activity_online_text);
        textViewLastOnlineDate.setText(onlineText);
    }

    @Override
    public void setLastOnlineDate(String date, String time) {
        String lastSeenText = getString(R.string.dialog_activity_last_seen_text);
        String atText = getString(R.string.dialog_activity_at_text);
        String resultText = String.format("%s %s %s %s", lastSeenText, date, atText, time);

        textViewLastOnlineDate.setText(resultText);
    }

    @Override
    public void setImageNotFoundError() {
        Toast errorToast = Toast.makeText(this,
                getText(R.string.alert_dialog_error_file_not_found), Toast.LENGTH_LONG);
        errorToast.show();
    }

    @Override
    public void setErrorWritingBitmapToFile() {
        Toast errorToast = Toast.makeText(this,
                getText(R.string.settings_activity_error_writing_bitmap_to_file), Toast.LENGTH_LONG);
        errorToast.show();
    }

    private void findViews() {
        messagesList = findViewById(R.id.messages_list);
        messageInput = findViewById(R.id.message_input);
        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewName = findViewById(R.id.text_view_name);
        textViewBlockedInformation = findViewById(R.id.text_view_blocked_information);
        textViewLastOnlineDate = findViewById(R.id.text_view_last_online_date);
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
        MessageHolders holdersConfig = new MessageHolders()
                .setIncomingTextLayout(R.layout.item_incoming_text_message)
                .setOutcomingTextLayout(R.layout.item_outcoming_text_message);

        messagesAdapter = new MessagesListAdapter<>(
                PreferenceManager.getLogin(this), holdersConfig, imageLoader);
        registerViewClickListener(messagesAdapter);
        messagesList.setAdapter(messagesAdapter);
    }

    private void registerViewClickListener(MessagesListAdapter<ChatMessage> messagesAdapter) {
        messagesAdapter.registerViewClickListener(R.id.messageUserAvatar,
                new MessagesListAdapter.OnMessageViewClickListener<ChatMessage>() {
                    @Override
                    public void onMessageViewClick(View view, ChatMessage message) {
                        ImageView imageView = (ImageView) view;
                        Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        IUser user = message.getUser();
                        Intent intent = ViewUtils.getUserInfoIntent(
                                DialogActivity.this, user, image);
                        startActivity(intent);
                    }
                });
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
        messageInput.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                Alerts.showUploadPhotoAlertDialog(DialogActivity.this);
            }
        });
    }

    private void fillUserInformation() {
        dialogPresenter.fillUserInformation(userLogin);
    }

    private void getImageFromDevice(Intent data) {
        Bitmap imageBitmap = dialogPresenter.getBitmapFromUri(data.getData());
        if (imageBitmap != null) {
            dialogPresenter.sendImage(imageBitmap);
        }
    }

    private void getImageFromCamera(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
        dialogPresenter.sendImage(imageBitmap);
    }
}
