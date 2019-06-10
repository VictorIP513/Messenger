package ru.android.messenger.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import ru.android.messenger.R;
import ru.android.messenger.view.adapters.FriendsFragmentPagerAdapter;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FriendListActivity extends ActivityWithNavigationDrawer {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(R.layout.activity_friend_list);

        findViews();
        createFragmentPagerAdapter();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        createFragmentPagerAdapter();
    }

    public void actionButtonAddFriendClick(View view) {
        Intent intent = new Intent(this, UsersSearchActivity.class);
        startActivity(intent);
    }

    private void findViews() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
    }

    private void createFragmentPagerAdapter() {
        FriendsFragmentPagerAdapter adapter =
                new FriendsFragmentPagerAdapter(getSupportFragmentManager(), this);
        if (viewPager.getAdapter() != null) {
            int currentItem = viewPager.getCurrentItem();
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(currentItem);
        } else {
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }
}
