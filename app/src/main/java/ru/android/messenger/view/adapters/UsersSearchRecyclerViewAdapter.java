package ru.android.messenger.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.android.messenger.R;
import ru.android.messenger.model.dto.UserFromView;

public class UsersSearchRecyclerViewAdapter
        extends RecyclerView.Adapter<UsersSearchRecyclerViewAdapter.UsersSearchViewHolder> {

    private static final String LOGIN_PREFIX = "@";

    private Filter usersFilter;
    private List<UserFromView> users;
    private List<UserFromView> allUsers;

    public UsersSearchRecyclerViewAdapter(List<UserFromView> users) {
        this.users = users;
        allUsers = new ArrayList<>(users);
        usersFilter = createFriendsFilter();
    }

    @NonNull
    @Override
    public UsersSearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.item_user, viewGroup, false);
        return new UsersSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersSearchViewHolder usersSearchViewHolder,
                                 int position) {
        UserFromView user = users.get(position);
        String firstNameAndSurname = user.getFirstName() + " " + user.getSurname();
        String login = LOGIN_PREFIX + user.getLogin();

        usersSearchViewHolder.imageViewUserPhoto.setImageBitmap(user.getUserPhoto());
        usersSearchViewHolder.textViewLogin.setText(login);
        usersSearchViewHolder.textViewName.setText(firstNameAndSurname);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public Filter getUsersFilter() {
        return usersFilter;
    }

    private Filter createFriendsFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<UserFromView> filteredList = getFilteredList(constraint);
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                users.clear();
                users.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    private List<UserFromView> getFilteredList(CharSequence constraint) {
        if (constraint == null || constraint.length() == 0) {
            return new ArrayList<>(allUsers);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            if (String.valueOf(filterPattern.charAt(0)).equals(LOGIN_PREFIX)) {
                filterPattern = filterPattern.substring(1);
                return filterByLogin(filterPattern);
            } else {
                return filterByName(filterPattern);
            }
        }
    }

    private List<UserFromView> filterByLogin(String filterPattern) {
        List<UserFromView> filteredList = new ArrayList<>();
        for (UserFromView userFromView : allUsers) {
            if (userFromView.getLogin().toLowerCase().contains(filterPattern)) {
                filteredList.add(userFromView);
            }
        }
        return filteredList;
    }

    private List<UserFromView> filterByName(String filterPattern) {
        List<UserFromView> filteredList = new ArrayList<>();
        for (UserFromView userFromView : allUsers) {
            if (userFromView.getFirstName().toLowerCase().contains(filterPattern) ||
                    userFromView.getSurname().toLowerCase().contains(filterPattern)) {
                filteredList.add(userFromView);
            }
        }
        return filteredList;
    }

    class UsersSearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewUserPhoto;
        private TextView textViewLogin;
        private TextView textViewName;

        UsersSearchViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserPhoto = itemView.findViewById(R.id.image_view_user_photo);
            textViewLogin = itemView.findViewById(R.id.navigation_drawer_text_view_login);
            textViewName = itemView.findViewById(R.id.navigation_drawer_text_view_name);
        }
    }
}
