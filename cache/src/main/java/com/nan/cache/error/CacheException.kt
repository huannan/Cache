package com.nan.cache.error

class CacheException private constructor(message: String) : RuntimeException(message) {
    companion object {
        @JvmStatic
        fun of(message: String) = CacheException(message)
    }
}