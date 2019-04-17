package ru.android.messenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

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
