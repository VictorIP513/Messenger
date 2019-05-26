package ru.android.messenger.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.response.FriendStatus;
import ru.android.messenger.presenter.UserInfoPresenter;
import ru.android.messenger.presenter.implementation.UserInfoPresenterImplementation;
import ru.android.messenger.view.interfaces.UserInfoView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class UserInfoActivity extends ActivityWithAlerts implements UserInfoView {

    private static final String LOGIN_PREFIX = "@";
    private static final int BUTTON_FRIEND_ADD_COLOR = 0xFF03A9F4;
    private static final int BUTTON_FRIEND_DELETE_COLOR = 0xFFFF2B2B;
    private static final int BUTTON_FRIEND_CANCEL_COLOR = 0xB2C2BCBC;
    private static final int BUTTON_FRIEND_ACCEPT_COLOR = 0xFFFF2B2B;
    private static final int BUTTON_FRIEND_ADD_TEXT_COLOR = 0xFFFFFFFF;
    private static final int BUTTON_FRIEND_DELETE_TEXT_COLOR = 0xFFFFFFFF;
    private static final int BUTTON_FRIEND_CANCEL_TEXT_COLOR = 0xFF000000;
    private static final int BUTTON_FRIEND_ACCEPT_TEXT_COLOR = 0xFFFFFFFF;

    private UserInfoPresenter userInfoPresenter;
    private FriendStatus friendStatus;
    private String userLogin;

    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewLogin;
    private TextView textViewFriendStatus;
    private Button buttonFriendAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userInfoPresenter = new UserInfoPresenterImplementation(this);

        findViews();
        setUserDataFromIntent();
        userInfoPresenter.fillUserFriendStatus(userLogin);
    }

    @Override
    public void setFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
        switch (friendStatus) {
            case USER_IS_NOT_FRIEND:
                textViewFriendStatus.setText(
                        getText(R.string.user_info_friend_status_user_is_not_friend));
                buttonFriendAdd.setText(getText(R.string.user_info_button_friend_add));
                buttonFriendAdd.setBackgroundColor(BUTTON_FRIEND_ADD_COLOR);
                buttonFriendAdd.setTextColor(BUTTON_FRIEND_ADD_TEXT_COLOR);
                break;
            case USER_IS_FRIEND:
                textViewFriendStatus.setText(
                        getText(R.string.user_info_friend_status_user_is_friend));
                buttonFriendAdd.setText(getText(R.string.user_info_button_friend_delete));
                buttonFriendAdd.setBackgroundColor(BUTTON_FRIEND_DELETE_COLOR);
                buttonFriendAdd.setTextColor(BUTTON_FRIEND_DELETE_TEXT_COLOR);
                break;
            case FRIEND_REQUEST_HAS_BEEN_SENT:
                textViewFriendStatus.setText(
                        getText(R.string.user_info_friend_status_friend_request_has_been_sent));
                buttonFriendAdd.setText(getText(R.string.user_info_button_friend_cancel));
                buttonFriendAdd.setBackgroundColor(BUTTON_FRIEND_CANCEL_COLOR);
                buttonFriendAdd.setTextColor(BUTTON_FRIEND_CANCEL_TEXT_COLOR);
                break;
            case INCOMING_FRIEND_REQUEST:
                textViewFriendStatus.setText(
                        getText(R.string.user_info_friend_status_friend_incoming_friend_request));
                buttonFriendAdd.setText(getText(R.string.user_info_button_friend_accept));
                buttonFriendAdd.setBackgroundColor(BUTTON_FRIEND_ACCEPT_COLOR);
                buttonFriendAdd.setTextColor(BUTTON_FRIEND_ACCEPT_TEXT_COLOR);
                break;
            default:
                break;
        }
    }

    public void buttonFriendAddClick(View view) {
        if (friendStatus == null) {
            showConnectionErrorAlertDialog();
        } else {
            switch (friendStatus) {
                case USER_IS_NOT_FRIEND:
                    userInfoPresenter.addToFriend(userLogin);
                    break;
                case USER_IS_FRIEND:
                case FRIEND_REQUEST_HAS_BEEN_SENT:
                    userInfoPresenter.deleteFromFriend(userLogin);
                    break;
                case INCOMING_FRIEND_REQUEST:
                    userInfoPresenter.acceptFriendRequest(userLogin);
                    break;
                default:
                    break;
            }
        }
    }

    public void linearLayoutGotoDialogClick(View view) {
        Intent intent = new Intent(this, DialogActivity.class);
        intent.putExtra("user_login", userLogin);
        startActivity(intent);
    }

    private void findViews() {
        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewName = findViewById(R.id.text_view_name);
        textViewLogin = findViewById(R.id.text_view_login);
        textViewFriendStatus = findViewById(R.id.text_view_friend_status);
        buttonFriendAdd = findViewById(R.id.button_friend_add);
    }

    private void setUserDataFromIntent() {
        Intent intent = getIntent();

        String firstName = intent.getStringExtra("user_first_name");
        String surname = intent.getStringExtra("user_surname");
        String name = firstName + " " + surname;
        textViewName.setText(name);

        userLogin = intent.getStringExtra("user_login");
        String login = LOGIN_PREFIX + userLogin;
        textViewLogin.setText(login);

        byte[] userPhoto = intent.getByteArrayExtra("user_photo");
        Bitmap bitmap = BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length);
        imageViewProfile.setImageBitmap(bitmap);
    }
}
