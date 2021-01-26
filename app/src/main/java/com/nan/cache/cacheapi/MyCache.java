package com.nan.cache.cacheapi;

import com.nan.cache.Cache;

public final class MyCache {

    private IMyCache mCache;

    private MyCache() {
        mCache = Cache.create(IMyCache.class);
    }

    private static final class Holder {
        static final MyCache INSTANCE = new MyCache();
    }

    private static MyCache getInstance() {
        return Holder.INSTANCE;
    }

    public static IMyCache get() {
        return getInstance().mCache;
    }

}
