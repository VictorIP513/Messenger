package ru.android.messenger.model.utils;

import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private DateUtils() {

    }

    public static Date convertUTCToLocalDate(Date date) {
        long time = date.getTime();
        TimeZone currentTimeZone = TimeZone.getDefault();
        int offset = currentTimeZone.getRawOffset();
        return new Date(time + offset);
    }
}
