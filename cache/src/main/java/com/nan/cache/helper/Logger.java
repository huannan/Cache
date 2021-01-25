package com.nan.cache.helper;

import android.content.Context;
import android.util.Log;

import com.nan.cache.constance.MMKVKey;
import com.nan.cache.error.CacheException;
import com.tencent.mmkv.BuildConfig;

import java.util.Locale;

public final class Logger {

    private static final String TAG = "Logger";
    private static boolean sLoggable = false;

    private Logger() {
        throw CacheException.of("No instances!");
    }

    private static boolean isLoggable() {
        return BuildConfig.DEBUG || sLoggable;
    }

    public static void init(Context context) {
        boolean enable = CacheMMKV.getInstance(context).get(MMKVKey.KEY_LOGGABLE, false);
        sLoggable = enable;
    }

    public static void toggleLog(Context context, boolean enable) {
        sLoggable = enable;
        CacheMMKV.getInstance(context).put(MMKVKey.KEY_LOGGABLE, sLoggable);
        if (sLoggable) {
            d(TAG, "log enable");
        }
    }

    public static void i(String tag, String fmt, Object... args) {
        if (isLoggable()) {
            Log.i(tag, format(fmt, args));
        }
    }

    public static void d(String tag, String fmt, Object... args) {
        if (isLoggable()) {
            Log.d(tag, format(fmt, args));
        }
    }

    public static void w(String tag, String fmt, Object... args) {
        if (isLoggable()) {
            Log.w(tag, format(fmt, args));
        }
    }

    public static void e(String tag, String fmt, Object... args) {
        if (isLoggable()) {
            Log.e(tag, format(fmt, args));
        }
    }

    public static void e(String tag, Throwable t, String fmt, Object... args) {
        if (isLoggable()) {
            Log.e(tag, format(fmt, args), t);
        }
    }

    private static String format(String fmt, Object... args) {
        if (args.length == 0) {
            return fmt;
        } else {
            return String.format(Locale.getDefault(), fmt, args);
        }
    }

}
