package ru.android.messenger.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.android.messenger.R;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void textViewRegisterClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}
