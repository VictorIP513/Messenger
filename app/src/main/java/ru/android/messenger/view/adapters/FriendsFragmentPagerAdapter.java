package ru.android.messenger.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ru.android.messenger.R;
import ru.android.messenger.view.fragments.FriendsFragment;
import ru.android.messenger.view.fragments.IncomingRequestsFragment;
import ru.android.messenger.view.fragments.OutgoingRequestsFragment;

public class FriendsFragmentPagerAdapter extends FragmentPagerAdapter {

    private static final int COUNT_FRAGMENTS = 3;

    private Context context;

    public FriendsFragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FriendsFragment();
            case 1:
                return new IncomingRequestsFragment();
            case 2:
                return new OutgoingRequestsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return COUNT_FRAGMENTS;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.friend_list_activity_friends_fragment_title);
            case 1:
                return context.getString(
                        R.string.friend_list_activity_incoming_requests_fragment_title);
            case 2:
                return context.getString(
                        R.string.friend_list_activity_outgoing_requests_fragment_title);
            default:
                return null;
        }
    }
}
