package ru.android.messenger.view.utils;

import android.app.Activity;
import android.content.Context;

import ru.android.messenger.model.utils.OnlineNotifier;
import ru.android.messenger.model.utils.http.HttpUtils;
import ru.android.messenger.model.utils.http.OnAuthenticationTokenCheckedListener;
import ru.android.messenger.utils.Logger;

public class ActiveActivitiesTracker {

    private static int activeActivities = 0;

    private ActiveActivitiesTracker() {

    }

    public static void activityStarted(Activity activity) {
        if (activeActivities == 0) {
            Logger.debug("Application resumed");
            checkAuthenticationToken(activity);
            OnlineNotifier.start(activity);
        }
        ++activeActivities;
    }

    public static void activityStopped() {
        --activeActivities;
        if (activeActivities == 0) {
            Logger.debug("Application paused");
            OnlineNotifier.stop();
        }
    }

    private static void checkAuthenticationToken(final Context context) {
        HttpUtils.checkAuthenticationToken(context, new OnAuthenticationTokenCheckedListener() {
            @Override
            public void onAuthenticationTokenChecked(boolean isCorrectAuthenticationToken) {
                if (!isCorrectAuthenticationToken) {
                    ViewUtils.logout(context);
                }
            }
        });
    }
}
