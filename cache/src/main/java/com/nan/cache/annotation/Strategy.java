package com.nan.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Strategy {

    // 以时间间隔计算过期
    int EXPIRE = 0;
    // 以时间单位计算过期，例如timeUnit=TimeUnit.DAYS表示当天有效
    int TIMELY = 1;

    /**
     * @return 过期策略
     */
    int strategy();

    /**
     * @return 过期时间，当过期策略为EXPIRE生效
     */
    long expireTime() default 0L;

    /**
     * @return 时间单位
     */
    TimeUnit timeUnit();

}
