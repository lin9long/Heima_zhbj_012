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

public class GovAffairsPager extends BasePager {
    public GovAffairsPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("1初始化啦。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("政务");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        tvTitle.setText("政务");
        ibMenu.setVisibility(View.VISIBLE);
    }
}
