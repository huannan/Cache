package com.nan.cache.cacheapi;

import com.nan.cache.annotation.Cache;
import com.nan.cache.annotation.Strategy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public interface IMyCache {

    /**
     * RxJava获取缓存示例
     */
    @Cache(action = Cache.GET, key = "cache_key1")
    @Strategy(strategy = Strategy.TIMELY, timeUnit = TimeUnit.DAYS)
    Observable<List<String>> getCacheRxJava(Observable<List<String>> source);

    /**
     * 普通方式获取缓存示例
     */
    @Cache(action = Cache.GET, key = "cache_key2")
    @Strategy(strategy = Strategy.EXPIRE, expireTime = 10L, timeUnit = TimeUnit.SECONDS)
    List<String> getCacheNormal(Callable<List<String>> source);

    /**
     * 放置缓存示例
     */
    @Cache(action = Cache.PUT, key = "cache_kye1")
    void setCache(List<String> source);

}
