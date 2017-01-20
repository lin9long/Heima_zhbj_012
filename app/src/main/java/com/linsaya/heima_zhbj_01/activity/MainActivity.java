package com.linsaya.heima_zhbj_01.activity;

import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.fragment.ContentFragment;
import com.linsaya.heima_zhbj_01.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {
    private String LEFT_MENU = "left_menu";
    private String CONTENT = "content";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //添加侧边栏到布局内
        setBehindContentView(R.layout.left_menu);
        //获取侧边栏的对象，调用内部的方法
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setBehindOffset(350);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        initFragment();
    }

    public void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fl_left_menu, new LeftMenuFragment(), LEFT_MENU);
        transaction.add(R.id.fl_content, new ContentFragment(), CONTENT);
        transaction.commit();
    }
    //给子方法获取左边栏fragment
    public LeftMenuFragment getLeftFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fragmentManager.findFragmentByTag(LEFT_MENU);
        return leftMenuFragment;
    }
    //给子方法获取主内容fragment
    public ContentFragment getContentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContentFragment contentFragment = (ContentFragment) fragmentManager.findFragmentByTag(CONTENT);
        return contentFragment;
    }
}
