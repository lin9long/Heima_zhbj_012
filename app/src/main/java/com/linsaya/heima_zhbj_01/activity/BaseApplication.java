package com.linsaya.heima_zhbj_01.activity;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/1/17.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        //x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
