package com.nan.cache.helper;

import com.nan.cache.error.CacheException;

import java.util.Calendar;

public final class TimeHelper {

    private TimeHelper() {
        throw CacheException.of("No instances!");
    }

    public static boolean isSameHour(long time1, long time2) {
        Calendar calendar1 = getCalendar(time1);
        Calendar calendar2 = getCalendar(time2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                && calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY);
    }

    public static boolean isSameDay(long time1, long time2) {
        Calendar calendar1 = getCalendar(time1);
        Calendar calendar2 = getCalendar(time2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameWeek(long time1, long time2) {
        Calendar calendar1 = getCalendar(time1);
        Calendar calendar2 = getCalendar(time2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.WEEK_OF_YEAR) == calendar2.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isSameMonth(long time1, long time2) {
        Calendar calendar1 = getCalendar(time1);
        Calendar calendar2 = getCalendar(time2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH);
    }

    public static boolean isSameYear(long time1, long time2) {
        Calendar calendar1 = getCalendar(time1);
        Calendar calendar2 = getCalendar(time2);
        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }

    public static Calendar getCalendar(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

}
