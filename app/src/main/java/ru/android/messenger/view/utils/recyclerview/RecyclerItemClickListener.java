package ru.android.messenger.view.utils.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private RecyclerViewOnClickListener listener;
    private GestureDetector gestureDetector;

    public RecyclerItemClickListener(Context context,
                                     final RecyclerView recyclerView,
                                     final RecyclerViewOnClickListener listener) {
        this.listener = listener;
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (child != null && listener != null) {
                            listener.onLongItemClick(child,
                                    recyclerView.getChildAdapterPosition(child));
                        }
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView,
                                         @NonNull MotionEvent motionEvent) {
        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (childView != null && listener != null && gestureDetector.onTouchEvent(motionEvent)) {
            listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent motionEvent) {
        //unused method
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        //unused method
    }
}
