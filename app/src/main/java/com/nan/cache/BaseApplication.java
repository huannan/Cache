package com.nan.cache;

import android.app.Application;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Cache.init(new Cache.CacheConfig(this)
                .setDebug(true));
    }
}
