package com.linsaya.heima_zhbj_01.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/1/23.
 */

public class MemoryCacheUtils {

    //使用hashmap传入键值对，将数据保存在内存内
    public HashMap<String, Bitmap> mMap = new HashMap<String, Bitmap>();
    //public LruCache<String,Bitmap> lruCache = new LruCache<>(100);

    public void setMemoryCache(String url, Bitmap bitmap) {
        mMap.put(url,bitmap);
        //lruCache.put(url,bitmap);

    }

    public Bitmap getMemoryCache(String url) {
        return mMap.get(url);
        //return lruCache.get(url);
    }
}
