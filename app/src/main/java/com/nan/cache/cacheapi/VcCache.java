package com.nan.cache.cacheapi;

import com.nan.cache.Cache;

public final class VcCache {

    private IVcCache mCache;

    private VcCache() {
        mCache = Cache.create(IVcCache.class);
    }

    private static final class Holder {
        static final VcCache INSTANCE = new VcCache();
    }

    private static VcCache getInstance() {
        return Holder.INSTANCE;
    }

    public static IVcCache get() {
        return getInstance().mCache;
    }

}
