package ru.android.messenger.model.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.R;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class UserUtils {

    private static Repository repository = Model.getRepository();
    private static List<UserFromView> userFromViewList = new ArrayList<>();

    private UserUtils() {

    }

    public static <T extends ViewWithUsersRecyclerView> void convertAndSetUsersToView(
            final List<User> users, final T view) {
        userFromViewList.clear();
        for (final User user : users) {
            repository.getUserPhoto(user.getLogin())
                    .enqueue(new DefaultCallback<ResponseBody, ViewWithUsersRecyclerView>(view) {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            super.onResponse(call, response);
                            File photo = null;
                            if (response.isSuccessful()) {
                                photo = Objects.requireNonNull(FileUtils.getFileFromResponseBody(
                                        response.body(), view.getContext()));
                            }
                            Context context = view.getContext();
                            UserFromView userFromView = createUserFromView(user, photo, context);
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

    private static UserFromView createUserFromView(
            User user, @Nullable File photo, Context context) {
        UserFromView userFromView = new UserFromView();
        userFromView.setFirstName(user.getFirstName());
        userFromView.setSurname(user.getSurname());
        userFromView.setLogin(user.getLogin());

        if (photo != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(photo.toString());
            userFromView.setUserPhoto(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.profile_thumbnail);
            userFromView.setUserPhoto(bitmap);
        }
        return userFromView;
    }
}
