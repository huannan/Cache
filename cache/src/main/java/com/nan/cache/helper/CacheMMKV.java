package com.nan.cache.helper;

import android.content.Context;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.nan.cache.constance.MMKVKey;
import com.tencent.mmkv.MMKV;

import java.util.Map;
import java.util.Set;

/**
 * 为解决SharedPreferences带来的线上ANR、Crash、性能、Crash导致数据丢失等问题，引入更可靠、效率更高的腾讯MMKV代替SharedPreferences
 * 本类为MMKV的封装类，防止代码入侵
 */
public final class CacheMMKV {

    public static final String TAG = "CacheMMKV";
    private static volatile CacheMMKV sInstance;
    private static final String ID_DEFAULT = "id_default";
    private static final String ID_ACCOUNT = "id_account";
    private static final String ID_COLLECT = "id_collect";

    private CacheMMKV() {

    }

    public static CacheMMKV getInstance(Context context) {
        if (null == sInstance) {
            synchronized (CacheMMKV.class) {
                if (null == sInstance) {
                    sInstance = new CacheMMKV();
                    sInstance.initialize(context);
                }
            }
        }
        return sInstance;
    }

    private void initialize(Context context) {
        MMKV.initialize(context);
    }

    public MMKV getDefaultMMKV() {
        return MMKV.mmkvWithID(ID_DEFAULT, MMKV.SINGLE_PROCESS_MODE);
    }

    public MMKV getAccountMMKV() {
        return MMKV.mmkvWithID(ID_ACCOUNT, MMKV.SINGLE_PROCESS_MODE);
    }

    public MMKV getCollectMMKV() {
        return MMKV.mmkvWithID(ID_COLLECT, MMKV.SINGLE_PROCESS_MODE);
    }

    public <T> void put(@MMKVKey @NonNull String key, @NonNull T value) {
        put(getDefaultMMKV(), key, value);
    }

    public <T> void put(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull T value) {
        if (value instanceof String || value instanceof Integer || value instanceof Boolean ||
                value instanceof Float || value instanceof Long || value instanceof Double) {
            mmkv.encode(key, String.valueOf(value));
        } else {
            mmkv.encode(key, JsonUtil.toJson(value));
        }
    }

    public void put(@MMKVKey @NonNull String key, @NonNull byte[] value) {
        put(getDefaultMMKV(), key, value);
    }

    public void put(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull byte[] value) {
        mmkv.encode(key, value);
    }

    public void put(@MMKVKey @NonNull String key, @NonNull Set<String> value) {
        put(getDefaultMMKV(), key, value);
    }

    public void put(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull Set<String> value) {
        mmkv.encode(key, value);
    }

    public void put(@MMKVKey @NonNull String key, @NonNull Parcelable value) {
        put(getDefaultMMKV(), key, value);
    }

    public void put(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull Parcelable value) {
        mmkv.encode(key, value);
    }

    public <T> T get(@MMKVKey @NonNull String key, @NonNull T defaultValue) {
        return get(getDefaultMMKV(), key, defaultValue);
    }

    public <T> T get(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull T defaultValue) {
        String value = mmkv.decodeString(key, String.valueOf(defaultValue));
        if (defaultValue instanceof String) {
            return (T) value;
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(value);
        } else if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(value);
        } else if (defaultValue instanceof Float) {
            return (T) Float.valueOf(value);
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(value);
        } else if (defaultValue instanceof Double) {
            return (T) Double.valueOf(value);
        } else {
            return (T) JsonUtil.parse(value, defaultValue.getClass());
        }
    }

    public byte[] get(@MMKVKey @NonNull String key, @NonNull byte[] defaultValue) {
        return get(getDefaultMMKV(), key, defaultValue);
    }

    public byte[] get(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull byte[] defaultValue) {
        return mmkv.decodeBytes(key, defaultValue);
    }

    public Set<String> get(@MMKVKey @NonNull String key, @NonNull Set<String> defaultValue) {
        return get(getDefaultMMKV(), key, defaultValue);
    }

    public Set<String> get(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull Set<String> defaultValue) {
        return mmkv.decodeStringSet(key, defaultValue);
    }

    public Parcelable get(@MMKVKey @NonNull String key, @NonNull Parcelable defaultValue) {
        return get(getDefaultMMKV(), key, defaultValue);
    }

    public Parcelable get(MMKV mmkv, @MMKVKey @NonNull String key, @NonNull Parcelable defaultValue) {
        return mmkv.decodeParcelable(key, defaultValue.getClass());
    }

    public void remove(@MMKVKey @NonNull String key) {
        remove(getDefaultMMKV(), key);
    }

    public void remove(MMKV mmkv, @MMKVKey @NonNull String key) {
        mmkv.remove(key);
    }

    public void clear() {
        clear(getDefaultMMKV());
    }

    public void clear(MMKV mmkv) {
        mmkv.clearAll();
    }

    public boolean contains(@MMKVKey @NonNull String key) {
        return contains(getDefaultMMKV(), key);
    }

    public boolean contains(MMKV mmkv, @MMKVKey @NonNull String key) {
        return mmkv.contains(key);
    }

    public Map<String, ?> getAll() {
        return getAll(getDefaultMMKV());
    }

    public Map<String, ?> getAll(MMKV mmkv) {
        return mmkv.getAll();
    }

}
