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

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("5初始化啦。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("设置");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        tvTitle.setText("设置");
        //设置页不显示菜单按键
        ibMenu.setVisibility(View.INVISIBLE);
    }
}
