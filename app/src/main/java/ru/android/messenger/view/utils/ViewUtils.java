package ru.android.messenger.view.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.stfalcon.chatkit.commons.models.IUser;

import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.model.utils.FirebaseUtils;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.view.activity.LoginActivity;
import ru.android.messenger.view.activity.UserInfoActivity;

public class ViewUtils {

    private ViewUtils() {

    }

    public static Bitmap getDefaultProfileImage(Context context) {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.profile_thumbnail);
    }

    public static void logout(Context context) {
        FirebaseUtils.unsubscribeFromReceivingMessages(context);
        PreferenceManager.clearPreferencesWithoutLastLogin(context);
        FileUtils.deletePhotoFile(context);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void clearErrorInTextInputLayoutOnChangeText(
            final TextInputLayout textInputLayout, final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // unused method
            }

            @Override
            public void afterTextChanged(Editable s) {
                // unused method
            }

            @Override
            public void onTextChanged(CharSequence str, int start, int before, int count) {
                textInputLayout.setError(null);
            }
        });
    }

    public static Intent getUserInfoIntent(Context context, String firstName,
                                           String surname, String login, Bitmap photo) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("user_first_name", firstName);
        intent.putExtra("user_surname", surname);
        intent.putExtra("user_login", login);
        intent.putExtra("user_photo", ImageHelper.getByteArrayFromBitmap(photo));
        return intent;
    }

    public static Intent getUserInfoIntent(Context context, IUser user, Bitmap photo) {
        String[] userName = user.getName().split(" ");
        String firstName = userName[0];
        String surname = userName[1];
        String login = user.getId();
        return getUserInfoIntent(context, firstName, surname, login, photo);
    }

    public static void createActionBarWithBackButtonForActivity(
            AppCompatActivity activity, Toolbar toolbar, String title) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = Objects.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(title);
    }

    public static void createActionBarWithNavigationDrawerButtonForActivity(
            AppCompatActivity activity, Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = Objects.requireNonNull(activity.getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.button_menu);
    }
}
