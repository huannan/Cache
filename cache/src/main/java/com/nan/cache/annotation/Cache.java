package com.nan.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cache {

    // 获取缓存
    int GET = 0;
    // 放置、清除缓存
    int PUT = 1;

    int action();

    String key();

}
