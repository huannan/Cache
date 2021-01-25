package com.nan.cache.cacheapi;

import com.nan.cache.annotation.Cache;
import com.nan.cache.annotation.Strategy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public interface IVcCache {

    /**
     * 异步获取缓存
     */
    @Cache(action = Cache.GET, key = "cache_async")
    @Strategy(strategy = Strategy.TIMELY, timeUnit = TimeUnit.DAYS)
    Observable<List<String>> getCacheAsync(Observable<List<String>> source);

    /**
     * 同步获取缓存
     */
    @Cache(action = Cache.GET, key = "cache_sync")
    @Strategy(strategy = Strategy.EXPIRE, expireTime = 90L, timeUnit = TimeUnit.DAYS)
    List<String> getCacheSync(Callable<List<String>> source);

    /**
     * 插入缓存示例
     */
    @Cache(action = Cache.PUT, key = "cache_sync")
    void setCache(List<String> source);

}
