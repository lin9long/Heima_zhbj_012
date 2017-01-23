package com.linsaya.heima_zhbj_01.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 自定义三级缓存
 * Created by Administrator on 2017/1/23.
 */

public class MyBitmapUtils {

    private final NetCacheUtils mNetCacheUtils;
    private final LocalCacheUtils mLocalCacheUtils;

    public MyBitmapUtils() {
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils);

    }


    /**
     * 从网络加载，从本地加载，从内存加载三级缓存
     *
     * @param iv_photo
     * @param photoUrl
     */
    public void display(ImageView iv_photo, String photoUrl) {

        //获取本地缓存数据
        Bitmap localCache = mLocalCacheUtils.getLocalCache(photoUrl);
        //如果本地缓存数据存在，直接加载数据
        if (localCache!=null){
            iv_photo.setImageBitmap(localCache);
            System.out.println("读取本地缓存啦");
            return;
        }
        mNetCacheUtils.getBitmapFromNet(iv_photo, photoUrl);
        System.out.println("从网络加载数据啦");
    }
}
