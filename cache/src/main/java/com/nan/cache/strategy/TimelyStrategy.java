package com.nan.cache.strategy;

import com.nan.cache.error.CacheException;
import com.nan.cache.helper.TimeHelper;
import com.nan.cache.strategy.base.BaseStrategy;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class TimelyStrategy extends BaseStrategy {

    public TimelyStrategy(Type returnType, String key, long expireTime, TimeUnit timeUnit) {
        super(returnType, key, expireTime, timeUnit);
    }

    @Override
    public boolean isCacheValid() {
        boolean isCacheValid;
        long saveTime = getSaveTime();
        long nowTime = System.currentTimeMillis();
        TimeUnit timeUnit = getTimeUnit();
        switch (timeUnit) {
            case MINUTES:
                isCacheValid = TimeHelper.isSameMinute(saveTime, nowTime);
                break;
            case HOURS:
                isCacheValid = TimeHelper.isSameHour(saveTime, nowTime);
                break;
            case DAYS:
                isCacheValid = TimeHelper.isSameDay(saveTime, nowTime);
                break;
            default:
                throw CacheException.of("not support " + timeUnit.name() + " timely strategy");
        }
        return isCacheValid;
    }
}
