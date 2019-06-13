package ru.android.messenger.model.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static final int USER_ONLINE_DELAY_SECONDS = 180;

    private DateUtils() {

    }

    @SuppressWarnings("SimpleDateFormat")
    public static String[] getFormattedDateAndTime(Date lastOnlineUTCDate) {
        Date convertedToLocalDate = convertUTCToLocalDate(lastOnlineUTCDate);
        String date = new SimpleDateFormat("dd.MM.yyyy").format(convertedToLocalDate);
        String time = new SimpleDateFormat("HH:mm").format(convertedToLocalDate);
        return new String[]{date, time};
    }

    public static boolean isOnline(Date date) {
        Date currentDateOnDevice = new Date();
        Date userLastOnlineDate = convertUTCToLocalDate(date);

        long currentTimeOnDevice = currentDateOnDevice.getTime();
        long userLastOnlineTime = userLastOnlineDate.getTime();
        long userOnlineDelayMillis = TimeUnit.SECONDS.toMillis(USER_ONLINE_DELAY_SECONDS);

        return currentTimeOnDevice - userLastOnlineTime < userOnlineDelayMillis;
    }

    static Date convertUTCToLocalDate(Date date) {
        long time = date.getTime();
        TimeZone currentTimeZone = TimeZone.getDefault();
        int offset = currentTimeZone.getRawOffset();
        return new Date(time + offset);
    }
}
