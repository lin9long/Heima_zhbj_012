package com.linsaya.heima_zhbj_01.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 自定义三级缓存
 * Created by Administrator on 2017/1/23.
 */

public class MyBitmapUtils {

    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public MyBitmapUtils() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }


    /**
     * 从网络加载，从本地加载，从内存加载三级缓存
     *
     * @param iv_photo
     * @param photoUrl
     */
    public void display(ImageView iv_photo, String photoUrl) {

        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(photoUrl);
        if (bitmap != null) {
            iv_photo.setImageBitmap(bitmap);
            System.out.println("发现内存缓存啦");
            return;
        }
        //获取本地缓存数据
        bitmap = mLocalCacheUtils.getLocalCache(photoUrl);
        //如果本地缓存数据存在，直接加载数据
        if (bitmap != null) {
            iv_photo.setImageBitmap(bitmap);
            //当不从网络获取图片时，讲本地图片写入内存缓存
            mMemoryCacheUtils.setMemoryCache(photoUrl, bitmap);
            System.out.println("读取本地缓存啦");
            return;
        }
        mNetCacheUtils.getBitmapFromNet(iv_photo, photoUrl);
        System.out.println("从网络加载数据啦");
    }
}
