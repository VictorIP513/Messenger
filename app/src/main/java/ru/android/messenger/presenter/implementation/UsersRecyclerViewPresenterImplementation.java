package ru.android.messenger.presenter.implementation;

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
import ru.android.messenger.model.utils.FileUtils;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.presenter.UsersRecyclerViewPresenter;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class UsersRecyclerViewPresenterImplementation implements UsersRecyclerViewPresenter {

    private ViewWithUsersRecyclerView view;
    private Repository repository;

    private List<UserFromView> userFromViewList;

    public UsersRecyclerViewPresenterImplementation(ViewWithUsersRecyclerView view) {
        this.view = view;
        repository = Model.getRepository();

        userFromViewList = new ArrayList<>();
    }

    @Override
    public void fillUsersList() {
        view.showWaitAlertDialog();
        repository.getAllUsers()
                .enqueue(new DefaultCallback<List<User>, ViewWithUsersRecyclerView>(view) {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call,
                                           @NonNull Response<List<User>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            List<User> users = Objects.requireNonNull(response.body());
                            deleteCurrentUserFromUserList(users);
                            userFromViewList.clear();
                            convertUsersToUsersFromView(users);
                        }
                    }
                });
    }

    @Override
    public byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        return ImageHelper.getByteArrayFromBitmap(bitmap);
    }

    private void convertUsersToUsersFromView(final List<User> users) {
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

    private void sortUsersFromFirstNameAndSurname() {
        Collections.sort(userFromViewList, new Comparator<UserFromView>() {
            @Override
            public int compare(UserFromView firstUser, UserFromView secondUser) {
                String firstName = firstUser.getFirstName() + " " + firstUser.getSurname();
                String secondName = secondUser.getFirstName() + " " + secondUser.getSurname();
                return firstName.compareToIgnoreCase(secondName);
            }
        });
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
                    view.getContext().getResources(), R.drawable.profile_thumbnail);
            userFromView.setUserPhoto(bitmap);
        }
        return userFromView;
    }

    private void deleteCurrentUserFromUserList(List<User> users) {
        String currentUserLogin = PreferenceManager.getLogin(view.getContext());
        for (User user : users) {
            if (user.getLogin().equals(currentUserLogin)) {
                users.remove(user);
                break;
            }
        }
    }
}
