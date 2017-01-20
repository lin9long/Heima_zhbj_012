package com.linsaya.heima_zhbj_01.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linsaya.heima_zhbj_01.activity.MainActivity;
import com.linsaya.heima_zhbj_01.base.BasePager;

/**
 * Created by Administrator on 2017/1/17.
 */

public class HomePager extends BasePager {
    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //给帧布局填充内容
        System.out.println("2初始化啦。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        //修改页面标题
        tvTitle.setText("首页");
        //首页不显示菜单按键
        ibMenu.setVisibility(View.INVISIBLE);
        //主页面不需要生成侧边栏，在初始化界面时禁用
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu menu = mainUi.getSlidingMenu();
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }
}
