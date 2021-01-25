package com.nan.cache.strategy;

import com.nan.cache.strategy.base.BaseStrategy;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class PutStrategy extends BaseStrategy {

    public PutStrategy(Type returnType, String key, long expireTime, TimeUnit timeUnit) {
        super(returnType, key, expireTime, timeUnit);
    }

    @Override
    public boolean isCacheValid() {
        return false;
    }
}
