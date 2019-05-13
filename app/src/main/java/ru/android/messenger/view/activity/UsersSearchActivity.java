package ru.android.messenger.view.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.UserFromView;
import ru.android.messenger.view.adapters.UsersSearchRecyclerViewAdapter;
import ru.android.messenger.view.interfaces.UsersSearchView;

public class UsersSearchActivity extends AppCompatActivity implements UsersSearchView {

    private UsersSearchRecyclerViewAdapter adapter;

    private List<UserFromView> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_search);

        fillUsers();
        setupRecyclerView();
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

    private void fillUsers() {
        users = new ArrayList<>();
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login1", "firstName1", "surname1"));
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login2", "firstName2", "surname2"));
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login3", "firstName3", "surname3"));
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login4", "firstName4", "surname4"));
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login5", "firstName5", "surname5"));
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login6", "firstName6", "surname6"));
        users.add(new UserFromView(Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.logo)), "login7", "firstName7", "surname7"));
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_users_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new UsersSearchRecyclerViewAdapter(users);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
