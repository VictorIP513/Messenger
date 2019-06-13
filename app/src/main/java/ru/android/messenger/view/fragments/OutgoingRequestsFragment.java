package ru.android.messenger.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.presenter.OutgoingRequestsPresenter;
import ru.android.messenger.presenter.implementation.OutgoingRequestsPresenterImplementation;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;
import ru.android.messenger.view.utils.recyclerview.RecyclerViewUtils;

public class OutgoingRequestsFragment extends Fragment implements ViewWithUsersRecyclerView {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_outgoing_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        OutgoingRequestsPresenter outgoingRequestsPresenter =
                new OutgoingRequestsPresenterImplementation(this);

        findViews();
        outgoingRequestsPresenter.fillOutgoingRequestsList();
    }

    @Override
    public void setUsersList(List<UserFromView> users) {
        RecyclerViewUtils.configureRecyclerViewForUsers(recyclerView, getActivity(), users);
    }

    private void findViews() {
        recyclerView =
                Objects.requireNonNull(getView()).findViewById(R.id.recycler_view_outgoing_requests);
    }
}
