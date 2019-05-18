package ru.android.messenger.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.presenter.UsersSearchPresenter;
import ru.android.messenger.presenter.implementation.UsersSearchPresenterImplementation;
import ru.android.messenger.view.adapters.UsersSearchRecyclerViewAdapter;
import ru.android.messenger.view.interfaces.UsersSearchView;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class UsersSearchActivity extends ActivityWithAlerts implements UsersSearchView {

    private UsersSearchRecyclerViewAdapter adapter;
    private UsersSearchPresenter usersSearchPresenter;

    private List<UserFromView> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_search);

        usersSearchPresenter = new UsersSearchPresenterImplementation(this);

        usersSearchPresenter.fillUsersList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                adapter.getUsersFilter().filter(text);
                return false;
            }
        });
        return true;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setUsersList(List<UserFromView> users) {
        this.users = users;
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_users_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new UsersSearchRecyclerViewAdapter(users);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
