package ru.android.messenger.view.utils.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.util.List;
import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.model.utils.ImageHelper;
import ru.android.messenger.view.activity.UserInfoActivity;
import ru.android.messenger.view.adapters.UsersSearchRecyclerViewAdapter;

public class RecyclerViewUtils {

    private RecyclerViewUtils() {

    }

    public static void configureRecyclerViewForUsers(
            RecyclerView recyclerView, final Context context, final List<UserFromView> users) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        RecyclerView.Adapter adapter = new UsersSearchRecyclerViewAdapter(users);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                context, recyclerView, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                openUserInfo(users.get(position), context);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                openUserInfo(users.get(position), context);
            }
        }));
    }

    public static void configureUsersSearchMenu(Menu menu,
                                                Context context, final RecyclerView recyclerView) {
        MenuInflater menuInflater = ((Activity) context).getMenuInflater();
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
                UsersSearchRecyclerViewAdapter adapter =
                        (UsersSearchRecyclerViewAdapter) recyclerView.getAdapter();
                Objects.requireNonNull(adapter).getUsersFilter().filter(text);
                return false;
            }
        });
    }

    private static void openUserInfo(UserFromView user, Context context) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        Bitmap userPhoto = user.getUserPhoto();
        intent.putExtra("user_first_name", user.getFirstName());
        intent.putExtra("user_surname", user.getSurname());
        intent.putExtra("user_login", user.getLogin());
        intent.putExtra("user_photo", ImageHelper.getByteArrayFromBitmap(userPhoto));
        context.startActivity(intent);
    }
}
