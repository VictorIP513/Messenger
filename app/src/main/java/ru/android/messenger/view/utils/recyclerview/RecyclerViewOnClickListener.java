package ru.android.messenger.view.utils.recyclerview;

import android.view.View;

public interface RecyclerViewOnClickListener {

    void onItemClick(View view, int position);

    void onLongItemClick(View view, int position);
}
