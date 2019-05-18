package ru.android.messenger.presenter.implementation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.R;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.presenter.UsersSearchPresenter;
import ru.android.messenger.view.interfaces.UsersSearchView;

public class UsersSearchPresenterImplementation implements UsersSearchPresenter {

    private UsersSearchView usersSearchView;
    private Repository repository;

    private List<UserFromView> userFromViewList;

    public UsersSearchPresenterImplementation(UsersSearchView usersSearchView) {
        this.usersSearchView = usersSearchView;
        repository = Model.getRepository();

        userFromViewList = new ArrayList<>();
    }

    @Override
    public void fillUsersList() {
        usersSearchView.showWaitAlertDialog();
        repository.getAllUsers()
                .enqueue(new DefaultCallback<List<User>, UsersSearchView>(usersSearchView) {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call,
                                           @NonNull Response<List<User>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            List<User> users = Objects.requireNonNull(response.body());
                            userFromViewList.clear();
                            convertUsersToUsersFromView(users);
                        }
                    }
                });
    }

    private void convertUsersToUsersFromView(final List<User> users) {
        for (final User user : users) {
            repository.getUserPhoto(user.getLogin())
                    .enqueue(new DefaultCallback<ResponseBody, UsersSearchView>(usersSearchView) {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call,
                                               @NonNull Response<ResponseBody> response) {
                            super.onResponse(call, response);
                            File photo = null;
                            if (response.isSuccessful()) {
                                photo = Objects.requireNonNull(FileUtils.getFileFromResponseBody(
                                        response.body(), usersSearchView.getContext()));
                            }
                            UserFromView userFromView = createUserFromView(user, photo);
                            userFromViewList.add(userFromView);
                            if (userFromViewList.size() == users.size()) {
                                usersSearchView.setUsersList(userFromViewList);
                            }
                        }
                    });
        }
    }

    private UserFromView createUserFromView(User user, @Nullable File photo) {
        UserFromView userFromView = new UserFromView();
        userFromView.setFirstName(user.getFirstName());
        userFromView.setSurname(user.getSurname());
        userFromView.setLogin(user.getLogin());

        if (photo != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(photo.toString());
            userFromView.setUserPhoto(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(
                    usersSearchView.getContext().getResources(), R.drawable.profile_thumbnail);
            userFromView.setUserPhoto(bitmap);
        }
        return userFromView;
    }
}
