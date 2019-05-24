package ru.android.messenger.presenter;

public interface UserInfoPresenter {

    void fillUserFriendStatus(String login);

    void addToFriend(String login);

    void deleteFromFriend(String login);

    void acceptFriendRequest(String login);
}
