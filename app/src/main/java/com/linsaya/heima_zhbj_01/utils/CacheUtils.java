package com.linsaya.heima_zhbj_01.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/1/18.
 */

public class CacheUtils {
    /**
     * 存储缓存数据方法
     *
     * @param url  存储数据的key
     * @param json 存储数据内容
     * @param ctx
     */
    public static void setCache(String url, String json, Context ctx) {
        PrefUtils.putString(ctx, url, json);
    }

    /**
     * 获取缓存数据方法
     *
     * @param url
     * @param ctx
     * @return
     */
    public static String getCache(String url, Context ctx) {
        return PrefUtils.getString(ctx, url, null);
    }
}
