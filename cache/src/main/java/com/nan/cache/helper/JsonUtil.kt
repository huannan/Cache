/**
 * Json解析工具类，该类中需要对有可能出现的异常进行处理后抛出最终处理结果
 */
@file:JvmName("JsonUtil")

package com.nan.cache.helper

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.nan.cache.helper.Logger.e

private const val TAG = "JsonUtil"

fun <T> parse(json: String?, typeRef: TypeReference<T>): T? {
    if (!TextUtils.isEmpty(json)) {
        val jsonTrim = json!!.trim()
        if (isValid(jsonTrim)) {
            return try {
                JSON.parseObject(jsonTrim, typeRef)
            } catch (e: Exception) {
                e(TAG, "parseToResult Exception:${e.message} from JsonString:$json")
                null
            }
        }
    }
    return null
}

fun <T> parse(json: String?, clazz: Class<T>): T? {
    if (!TextUtils.isEmpty(json)) {
        val jsonTrim = json!!.trim()
        if (isValid(jsonTrim)) {
            return try {
                JSON.parseObject(jsonTrim, clazz)
            } catch (e: Exception) {
                e(TAG, "parseToClass Exception:${e.message} from JsonString:$json")
                null
            }
        }
    }
    return null
}

fun <T> parseArray(json: String?, clazz: Class<T>): List<T>? {
    if (!TextUtils.isEmpty(json)) {
        val jsonTrim = json!!.trim()
        if (isValid(jsonTrim)) {
            return try {
                JSON.parseArray(jsonTrim, clazz)
            } catch (e: Exception) {
                e(TAG, "parseArray Exception:${e.message} from JsonString:$json")
                null
            }
        }
    }
    return null
}

fun toJson(any: Any?): String {
    if (null == any) {
        return ""
    }
    return try {
        JSON.toJSONString(any)
    } catch (e: Exception) {
        e(TAG, "toJSONString Exception:${e.message} from JsonString:$any")
        ""
    }
}

/**
 * 简单地对JSON合法性进行检查
 */
fun isValid(json: String): Boolean {
    return if ((json.startsWith("{") && json.endsWith("}")) ||
            (json.startsWith("[") && json.endsWith("]"))) {
        true
    } else {
        e(TAG, "json string :$json is invalid")
        false
    }
}