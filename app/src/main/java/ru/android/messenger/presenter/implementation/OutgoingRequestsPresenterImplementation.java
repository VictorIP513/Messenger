package ru.android.messenger.presenter.implementation;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;
import ru.android.messenger.model.Model;
import ru.android.messenger.model.PreferenceManager;
import ru.android.messenger.model.Repository;
import ru.android.messenger.model.callbacks.DefaultCallback;
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
        view.showWaitAlertDialog();
        String authenticationToken = PreferenceManager.getAuthenticationToken(view.getContext());
        repository.getOutgoingRequests(authenticationToken)
                .enqueue(new DefaultCallback<List<User>, ViewWithUsersRecyclerView>(view) {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call,
                                           @NonNull Response<List<User>> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            List<User> users = Objects.requireNonNull(response.body());
                            UserUtils.convertAndSetUsersToView(users, view);
                        }
                    }
                });
    }
}
