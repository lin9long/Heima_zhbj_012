package com.linsaya.heima_zhbj_01.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/1/20.
 */

public class TopNewsViewPager extends ViewPager {

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public TopNewsViewPager(Context context) {
        super(context);
    }

    public TopNewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) ev.getX();
                endY = (int) ev.getY();

                int offsetX = endX - startX;
                int offsetY = endY - startY;

                //左右滑动
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    int currentItem = getCurrentItem();
                    //向右滑动
                    if (offsetX > 0) {
                        //当前为第一个页面，父控件拦截事件（弹出侧边栏）
                        if (currentItem == 0) {
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        //向左滑动，获取adapter的总个数
                        int count = getAdapter().getCount();
                        //当前为第最后一个页面，父控件拦截事件（切换到下一个集合）
                        if (currentItem==count-1){
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    }


        }else{
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        break;
    }

    return super.dispatchTouchEvent(ev);

}
}
