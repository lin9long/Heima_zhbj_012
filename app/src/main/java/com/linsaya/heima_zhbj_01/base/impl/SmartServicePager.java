package com.linsaya.heima_zhbj_01.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.linsaya.heima_zhbj_01.base.BasePager;


/**
 * Created by Administrator on 2017/1/17.
 */

public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("4初始化啦。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("智慧服务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        tvTitle.setText("智慧服务");
        ibMenu.setVisibility(View.VISIBLE);
    }
}
