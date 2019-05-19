package ru.android.messenger.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.android.messenger.R;

public class UserInfoActivity extends AppCompatActivity {

    private static final String LOGIN_PREFIX = "@";

    private ImageView imageViewProfile;
    private TextView textViewName;
    private TextView textViewLogin;
    private TextView textViewFriendStatus;
    private Button buttonFriendAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        findViews();
        setUserDataFromIntent();
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

        String login = LOGIN_PREFIX + intent.getStringExtra("user_login");
        textViewLogin.setText(login);

        byte[] userPhoto = intent.getByteArrayExtra("user_photo");
        Bitmap bitmap = BitmapFactory.decodeByteArray(userPhoto, 0, userPhoto.length);
        imageViewProfile.setImageBitmap(bitmap);
    }
}
