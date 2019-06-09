package ru.android.messenger;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.android.messenger.utils.Logger;

public class MessengerApplication extends Application
        implements Application.ActivityLifecycleCallbacks {

    private List<Activity> activities;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);

        activities = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.debug(String.format("Created %s activity", activity.getLocalClassName()));
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.debug(String.format("Started %s activity", activity.getLocalClassName()));
        activities.add(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.debug(String.format("Resumed %s activity", activity.getLocalClassName()));
        activities.add(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.debug(String.format("Paused %s activity", activity.getLocalClassName()));
        activities.remove(activity);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.debug(String.format("Stopped %s activity", activity.getLocalClassName()));
        activities.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.debug(String.format("SaveInstanceState %s activity", activity.getLocalClassName()));
        activities.remove(activity);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.debug(String.format("Destroyed %s activity", activity.getLocalClassName()));
        activities.remove(activity);
    }

    @Nullable
    public Activity getCurrentActivity() {
        return activities.isEmpty() ? null : activities.get(0);
    }
}
