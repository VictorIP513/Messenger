package ru.android.messenger.view.utils.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;

import java.util.List;
import java.util.Objects;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;
import ru.android.messenger.view.adapters.UsersSearchRecyclerViewAdapter;
import ru.android.messenger.view.utils.ViewUtils;

public class RecyclerViewUtils {

    private RecyclerViewUtils() {

    }

    public static void configureRecyclerViewForUsers(
            final RecyclerView recyclerView, final Context context, final List<UserFromView> users) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        RecyclerView.Adapter adapter = new UsersSearchRecyclerViewAdapter(users);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                context, recyclerView, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(RecyclerView view, int position) {
                UserFromView user = RecyclerViewUtils.findUserFromRecyclerView(view, position);
                openUserInfo(user, context);
            }

            @Override
            public void onLongItemClick(RecyclerView view, int position) {
                UserFromView user = RecyclerViewUtils.findUserFromRecyclerView(view, position);
                openUserInfo(user, context);
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
                if (adapter != null) {
                    adapter.getUsersFilter().filter(text);
                }
                return false;
            }
        });
    }

    private static UserFromView findUserFromRecyclerView(RecyclerView recyclerView, int position) {
        UsersSearchRecyclerViewAdapter adapter =
                (UsersSearchRecyclerViewAdapter) recyclerView.getAdapter();
        return Objects.requireNonNull(adapter).getUser(position);
    }

    private static void openUserInfo(UserFromView user, Context context) {
        Intent intent = ViewUtils.getUserInfoIntent(context, user.getFirstName(), user.getSurname(),
                user.getLogin(), user.getUserPhoto());
        context.startActivity(intent);
    }
}
