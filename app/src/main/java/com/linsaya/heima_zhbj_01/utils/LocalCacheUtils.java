package com.linsaya.heima_zhbj_01.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/1/23.
 */

public class LocalCacheUtils {
    public final static String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "zhbj74";

    //设置本地缓存
    public void setLocahCache(String url, Bitmap bitmap) {
        //新建一个路径，判断路径不为空
        File dir = new File(PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        //将url作md5编码后，作为本地缓存的文件名
        String urlMd5 = MD5Util.encoder(url);
        //新建本地文件，将bitmap压缩后以流的形式写入到文件
        File cacheFile = new File(dir, urlMd5);
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cacheFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取本地缓存
    public Bitmap getLocalCache(String url) {
        String urlMd5 = MD5Util.encoder(url);
        //获取本地文件，以输入流的形式写入到bitmap，并返回出去
        File cacheFile = new File(PATH, urlMd5);
        if (cacheFile.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
