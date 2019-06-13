package ru.android.messenger.model.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.model.utils.http.HttpUtils;
import ru.android.messenger.model.utils.http.OnPhotoLoadedListener;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class UserUtils {

    private static List<UserFromView> userFromViewList = new ArrayList<>();

    private UserUtils() {

    }

    public static String getUserPhotoUrl(String login) {
        return Model.getServerUrl() + "/api/getUserPhoto/" + login;
    }

    public static <T extends ViewWithUsersRecyclerView> void convertAndSetUsersToView(
            final List<User> users, final T view) {
        Context context = view.getContext();
        userFromViewList.clear();
        for (final User user : users) {
            HttpUtils.getUserPhotoAndExecuteAction(user.getLogin(), context,
                    new OnPhotoLoadedListener() {
                        @Override
                        public void onPhotoLoaded(Bitmap photo) {
                            UserFromView userFromView = createUserFromView(user, photo);
                            userFromViewList.add(userFromView);
                            if (userFromViewList.size() == users.size()) {
                                sortUsersFromFirstNameAndSurname();
                                view.setUsersList(userFromViewList);
                            }
                        }
                    });
        }
    }

    public static void deleteCurrentUserFromUserList(List<User> users, Context context) {
        String currentUserLogin = PreferenceManager.getLogin(context);
        for (User user : users) {
            if (user.getLogin().equals(currentUserLogin)) {
                users.remove(user);
                break;
            }
        }
    }

    private static void sortUsersFromFirstNameAndSurname() {
        Collections.sort(userFromViewList, new Comparator<UserFromView>() {
            @Override
            public int compare(UserFromView firstUser, UserFromView secondUser) {
                String firstName = firstUser.getFirstName() + " " + firstUser.getSurname();
                String secondName = secondUser.getFirstName() + " " + secondUser.getSurname();
                return firstName.compareToIgnoreCase(secondName);
            }
        });
    }

    private static UserFromView createUserFromView(User user, Bitmap userPhoto) {
        UserFromView userFromView = new UserFromView();
        userFromView.setFirstName(user.getFirstName());
        userFromView.setSurname(user.getSurname());
        userFromView.setLogin(user.getLogin());
        userFromView.setUserPhoto(userPhoto);
        return userFromView;
    }
}
