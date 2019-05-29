package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.CallbackWithoutAlerts;
import ru.android.messenger.model.dto.User;
import ru.android.messenger.model.utils.UserUtils;
import ru.android.messenger.presenter.OutgoingRequestsPresenter;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;

public class OutgoingRequestsPresenterImplementation implements OutgoingRequestsPresenter {

    private ViewWithUsersRecyclerView view;
    private Repository repository;

    public OutgoingRequestsPresenterImplementation(ViewWithUsersRecyclerView view) {
        this.view = view;
        repository = Model.getRepository();
    }

    @Override
    public void fillOutgoingRequestsList() {
        String authenticationToken = PreferenceManager.getAuthenticationToken(view.getContext());
        repository.getOutgoingRequests(authenticationToken)
                .enqueue(new CallbackWithoutAlerts<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call,
                                           @NonNull Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            List<User> users = Objects.requireNonNull(response.body());
                            UserUtils.convertAndSetUsersToView(users, view);
                        }
                    }
                });
    }
}
