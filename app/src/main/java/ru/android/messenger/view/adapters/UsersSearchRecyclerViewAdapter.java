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
import ru.android.messenger.model.UserFromView;

public class UsersSearchRecyclerViewAdapter
        extends RecyclerView.Adapter<UsersSearchRecyclerViewAdapter.UsersSearchViewHolder> {

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
    public void onBindViewHolder(@NonNull UsersSearchViewHolder usersSearchViewHolder, int position) {
        UserFromView user = users.get(position);
        String firstNameAndSurname = user.getFirstName() + user.getSurname();

        usersSearchViewHolder.imageViewUserPhoto.setImageBitmap(user.getUserPhoto());
        usersSearchViewHolder.textViewLogin.setText(user.getLogin());
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
                List<UserFromView> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(allUsers);
                } else {
                    // TODO filter
                }
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

    class UsersSearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewUserPhoto;
        private TextView textViewLogin;
        private TextView textViewName;

        UsersSearchViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewUserPhoto = itemView.findViewById(R.id.image_view_user_photo);
            textViewLogin = itemView.findViewById(R.id.text_view_login);
            textViewName = itemView.findViewById(R.id.text_view_name);
        }
    }
}
