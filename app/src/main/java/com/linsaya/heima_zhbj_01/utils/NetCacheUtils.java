package com.linsaya.heima_zhbj_01.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2017/1/23.
 */

public class NetCacheUtils {


    private ImageView mImageView;
    private String mUrl;
    private LocalCacheUtils mLocalCacheUtils;

    public NetCacheUtils(LocalCacheUtils LocalCacheUtils) {
        mLocalCacheUtils = LocalCacheUtils;
    }

    public void getBitmapFromNet(ImageView iv_photo, String photoUrl) {
        mImageView = iv_photo;
        mUrl = photoUrl;

        new BitmapTask().execute(iv_photo, photoUrl);
    }

    //第一个泛型为传入参数类型，此处定义为object意思为传入对象，可以为不同的类型
    //第二个泛型为onProgressUpdate内的参数类型
    //第三个泛型为doInBackground返回值及onPostExecute传入参数

    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {


        @Override
        protected Bitmap doInBackground(Object... params) {
            mImageView = (ImageView) params[0];
            mUrl = (String) params[1];
            Bitmap bitmap = null;

            try {
                bitmap = downLoadBitmap(mUrl);
                mLocalCacheUtils.setLocahCache(mUrl,bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //异步任务执行前参数处理
        @Override
        protected void onPreExecute() {
            mImageView.setTag(mUrl);
            super.onPreExecute();

        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //先进行非空判断后，再复制
            if (bitmap != null) {
                String tag = (String) mImageView.getTag();
                if (tag.equals(mUrl)) {//因为listview存在复用机制
                    // 判断当前图片是否为当前所加载url的图片，如果是，说明图片正确
                    mImageView.setImageBitmap(bitmap);
                }
            }

        }

        private Bitmap downLoadBitmap(String url) throws MalformedURLException {
            URL mUrl = new URL(url);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) mUrl.openConnection();
                connection.setReadTimeout(5000);
                connection.setConnectTimeout(2000);
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    InputStream is = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }
    }
}
