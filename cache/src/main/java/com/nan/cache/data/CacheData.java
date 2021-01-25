package com.nan.cache.data;

public final class CacheData<T> {

    T data;

    public CacheData() {
    }

    public CacheData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
