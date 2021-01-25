package com.nan.cache;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.nan.cache.error.CacheException;
import com.nan.cache.helper.Logger;
import com.nan.cache.proxy.CacheProxy;

import java.lang.reflect.Proxy;

public final class Cache {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private static boolean sIsDebug;

    private Cache() {
        throw CacheException.of("No instances!");
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> cache) {
        CacheProxy proxy = new CacheProxy();
        return (T) Proxy.newProxyInstance(cache.getClassLoader(), new Class<?>[]{cache}, proxy);
    }

    public static void init(CacheConfig config) {
        sContext = config.context;
        sIsDebug = config.isDebug;
        Logger.init(sContext);
        Logger.toggleLog(sContext, sIsDebug);
    }

    public static Context getContext() {
        return sContext;
    }

    public static final class CacheConfig {
        Context context;
        boolean isDebug;

        public CacheConfig(Application application) {
            this.context = application.getApplicationContext();
            isDebug = false;
        }

        public CacheConfig setContext(Application application) {
            this.context = application.getApplicationContext();
            return this;
        }

        public CacheConfig setDebug(boolean debug) {
            isDebug = debug;
            return this;
        }
    }

}