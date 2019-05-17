package ru.android.messenger.view.activity;

import android.os.Bundle;

import ru.android.messenger.R;

public class DialogsActivity extends ActivityWithNavigationDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_dialogs);
    }
}
