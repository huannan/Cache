package com.nan.cache.proxy;

import com.nan.cache.annotation.Cache;
import com.nan.cache.annotation.Strategy;
import com.nan.cache.error.CacheException;
import com.nan.cache.helper.ObjectHelper;
import com.nan.cache.strategy.ExpireStrategy;
import com.nan.cache.strategy.PutStrategy;
import com.nan.cache.strategy.TimelyStrategy;
import com.nan.cache.strategy.base.BaseStrategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

public class CacheProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ObjectHelper.checkMethodLegitimacy(method);
        String methodName = method.getName();
        Object result = null;

        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        assert cacheAnnotation != null;
        switch (cacheAnnotation.action()) {
            case Cache.GET:
                result = invokeGetMethod(method, args);
                break;
            case Cache.PUT:
                invokePutMethod(method, args);
                break;
            default:
                throw CacheException.of("cache api method " + methodName + "have unsupported action");
        }
        return result;
    }


    private Object invokeGetMethod(Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?> parameterType = method.getParameterTypes()[0];
        Class<?> returnType = method.getReturnType();
        Type returnGenericType = method.getGenericReturnType();
        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        Strategy strategyAnnotation = method.getAnnotation(Strategy.class);
        assert strategyAnnotation != null;
        assert cacheAnnotation != null;
        int strategyType = strategyAnnotation.strategy();
        long expireTime = strategyAnnotation.expireTime();
        TimeUnit timeUnit = strategyAnnotation.timeUnit();
        String key = cacheAnnotation.key();

        Object result;
        Object source = args[0];
        BaseStrategy strategy;
        if (parameterType == Callable.class) {
            switch (strategyType) {
                case Strategy.EXPIRE:
                    strategy = new ExpireStrategy(returnGenericType, key, expireTime, timeUnit);
                    result = processSync(strategy, (Callable<?>) source);
                    break;
                case Strategy.TIMELY:
                    strategy = new TimelyStrategy(returnGenericType, key, expireTime, timeUnit);
                    result = processSync(strategy, (Callable<?>) source);
                    break;
                default:
                    throw CacheException.of("cache api method " + methodName + " have unsupported strategy");
            }
        } else if (parameterType == Observable.class && returnType == Observable.class) {
            returnGenericType = ((ParameterizedType) returnGenericType).getActualTypeArguments()[0];
            switch (strategyType) {
                case Strategy.EXPIRE:
                    strategy = new ExpireStrategy(returnGenericType, key, expireTime, timeUnit);
                    result = processAsync(strategy, (Observable<?>) source);
                    break;
                case Strategy.TIMELY:
                    strategy = new TimelyStrategy(returnGenericType, key, expireTime, timeUnit);
                    result = processAsync(strategy, (Observable<?>) source);
                    break;
                default:
                    throw CacheException.of("cache api method " + methodName + " have unsupported strategy");
            }
        } else {
            throw CacheException.of("cache api method " + methodName + " parameterType or returnType error");
        }

        return result;
    }

    private void invokePutMethod(Method method, Object[] args) {
        Cache cacheAnnotation = method.getAnnotation(Cache.class);
        assert cacheAnnotation != null;
        String key = cacheAnnotation.key();
        BaseStrategy strategy = new PutStrategy(null, key, 0L, null);
        strategy.putCache(args[0]);
    }

    private <T> T processSync(BaseStrategy strategy, Callable<T> source) throws Exception {
        T result = null;
        if (strategy.isCacheValid()) {
            T cache = strategy.getCache();
            if (null != cache) {
                result = cache;
            }
        }
        if (null == result) {
            result = source.call();
            strategy.putCache(result);
        }
        return result;
    }

    private <T> Observable<T> processAsync(final BaseStrategy strategy, Observable<T> sourceObservable) {
        return Observable
                .create(new ObservableOnSubscribe<T>() {
                    @Override
                    public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                        if (strategy.isCacheValid()) {
                            T cache = strategy.getCache();
                            if (null != cache) {
                                emitter.onNext(cache);
                                emitter.onComplete();
                            } else {
                                emitter.onError(new Exception("get cache error"));
                            }
                        } else {
                            emitter.onError(new Exception("cache invalid and get cache from source"));
                        }
                    }
                })
                .onErrorResumeNext(
                        sourceObservable.doOnNext(new Consumer<T>() {
                            @Override
                            public void accept(T source) throws Exception {
                                strategy.putCache(source);
                            }
                        })
                );
    }

}
