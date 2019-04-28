package ru.android.messenger.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ru.android.messenger.R;

public class ConnectionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
    }

    public void buttonConnectClicked(View view) {
        // TODO connect to server
    }
}
