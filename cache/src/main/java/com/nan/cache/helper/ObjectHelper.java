package com.nan.cache.helper;

import com.nan.cache.annotation.Cache;
import com.nan.cache.annotation.Strategy;
import com.nan.cache.error.CacheException;

import java.lang.reflect.Method;

public final class ObjectHelper {

    private ObjectHelper() {
        throw CacheException.of("No instances!");
    }

    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw CacheException.of(message);
        } else {
            return object;
        }
    }

    public static void checkMethodLegitimacy(Method method) {
        String methodName = method.getName();

        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        ObjectHelper.requireNonNull(cacheAnnotation, "cache api method " + methodName + " should have @Cache annotation");

        if (cacheAnnotation.action() == Cache.GET) {
            Strategy strategyAnnotation = method.getAnnotation(Strategy.class);
            ObjectHelper.requireNonNull(strategyAnnotation, "cache api method " + methodName + " should have @Strategy annotation");
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw CacheException.of("cache api method " + methodName + " should have only one parameter");
        }
    }

}
