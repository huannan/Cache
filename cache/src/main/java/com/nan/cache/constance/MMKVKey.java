package com.nan.cache.constance;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        MMKVKey.KEY_LOGGABLE
})
@Retention(RetentionPolicy.SOURCE)
public @interface MMKVKey {
    String KEY_LOGGABLE = "key_loggable";
}
