package ru.android.messenger.view.activity;

import android.os.Bundle;

import ru.android.messenger.R;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FriendListActivity extends ActivityWithNavigationDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_friend_list);
    }
}
