package ru.android.messenger.view.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.presenter.UsersRecyclerViewPresenter;
import ru.android.messenger.presenter.implementation.UsersRecyclerViewPresenterImplementation;
import ru.android.messenger.view.adapters.UsersSearchRecyclerViewAdapter;
import ru.android.messenger.view.interfaces.ViewWithUsersRecyclerView;
import ru.android.messenger.view.utils.recyclerview.RecyclerItemClickListener;
import ru.android.messenger.view.utils.recyclerview.RecyclerViewOnClickListener;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class ActivityWithUsersRecyclerView extends ActivityWithAlerts
        implements ViewWithUsersRecyclerView {

    private UsersSearchRecyclerViewAdapter adapter;
    protected UsersRecyclerViewPresenter usersRecyclerViewPresenter;

    private RecyclerView recyclerView;

    private List<UserFromView> users;

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
    public void setUsersList(List<UserFromView> users) {
        this.users = users;
        setupRecyclerView();
    }

    protected void init(int layoutResourceId) {
        setContentView(layoutResourceId);
        usersRecyclerViewPresenter = new UsersRecyclerViewPresenterImplementation(this);

        findViews();
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new UsersSearchRecyclerViewAdapter(users);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openUserInfo(users.get(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                openUserInfo(users.get(position));
            }
        }));
    }

    private void openUserInfo(UserFromView user) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("user_first_name", user.getFirstName());
        intent.putExtra("user_surname", user.getSurname());
        intent.putExtra("user_login", user.getLogin());
        intent.putExtra("user_photo",
                usersRecyclerViewPresenter.getByteArrayFromBitmap(user.getUserPhoto()));
        startActivity(intent);
    }

    private void findViews() {
        recyclerView = findViewById(R.id.recycler_view_users_list);
    }
}
