package ru.android.messenger.view.utils.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface RecyclerViewOnClickListener {

    void onItemClick(RecyclerView recyclerView, int position);

    void onLongItemClick(RecyclerView recyclerView, int position);
}
