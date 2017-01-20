package com.linsaya.heima_zhbj_01.base;

import android.app.Activity;
import android.view.View;

/**
 * Created by Administrator on 2017/1/18.
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
