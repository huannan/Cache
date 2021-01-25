package com.nan.cache.strategy.base;

import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.nan.cache.Cache;
import com.nan.cache.constance.GlobalConstance;
import com.nan.cache.data.CacheData;
import com.nan.cache.helper.JsonUtil;
import com.nan.cache.helper.CacheMMKV;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public abstract class BaseStrategy {

    private static final String SAVE_TIME_KEY_SUFFIX = GlobalConstance.DIVISION_UNDERLINE + "time";
    private Type returnType;
    private String key;
    private long expireTime;
    private TimeUnit timeUnit;

    public BaseStrategy(Type returnType, String key, long expireTime, TimeUnit timeUnit) {
        this.returnType = returnType;
        this.key = key;
        this.expireTime = expireTime;
        this.timeUnit = timeUnit;
    }

    public abstract boolean isCacheValid();

    protected long getSaveTime() {
        return getMMKV().get(key + SAVE_TIME_KEY_SUFFIX, 0L);
    }

    protected void updateSaveTime() {
        getMMKV().put(key + SAVE_TIME_KEY_SUFFIX, System.currentTimeMillis());
    }

    protected long getTimeMillis() {
        return timeUnit.toMillis(expireTime);
    }

    public <T> void putCache(T data) {
        CacheMMKV mmkv = getMMKV();
        CacheData<T> dataWrapper = new CacheData<>(data);
        String cacheJson = JsonUtil.toJson(dataWrapper);
        mmkv.put(key, cacheJson);
        updateSaveTime();
    }

    public <T> T getCache() {
        T cache = null;
        String cacheJson = getMMKV().get(key, "");
        if (!TextUtils.isEmpty(cacheJson)) {
            CacheData<T> data = JsonUtil.parse(cacheJson, new TypeReference<CacheData<T>>(returnType) {
            });
            if (null != data && null != data.getData()) {
                cache = data.getData();
            }
        }
        return cache;
    }

    public CacheMMKV getMMKV() {
        return CacheMMKV.getInstance(Cache.getContext());
    }

    public Type getReturnType() {
        return returnType;
    }

    public void setReturnType(Type returnType) {
        this.returnType = returnType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }
}
