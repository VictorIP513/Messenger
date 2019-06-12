package ru.android.messenger.model.utils;

import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import ru.android.messenger.model.utils.http.HttpUtils;

public class OnlineNotifier {

    private static final int DELAY_SECONDS = 60;

    private static Timer timer;

    private OnlineNotifier() {

    }

    public static void start(final Context context) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HttpUtils.sendUserIsOnlineNotification(context);
            }
        }, 0, TimeUnit.SECONDS.toMillis(DELAY_SECONDS));
    }

    public static void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
