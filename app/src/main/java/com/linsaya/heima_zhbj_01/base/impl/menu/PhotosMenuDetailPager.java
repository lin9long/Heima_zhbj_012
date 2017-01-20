package com.linsaya.heima_zhbj_01.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.linsaya.heima_zhbj_01.base.BaseMenuDetailPager;

/**
 * Created by Administrator on 2017/1/18.
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager {
    public PhotosMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页：组图");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
