package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.android.messenger.R;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FriendListActivity extends ActivityWithNavigationDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_friend_list);
    }

    public void actionButtonAddFriendClick(View view) {
        Intent intent = new Intent(this, UsersSearchActivity.class);
        startActivity(intent);
    }
}
