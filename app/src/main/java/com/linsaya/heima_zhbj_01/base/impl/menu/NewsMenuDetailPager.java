package com.linsaya.heima_zhbj_01.base.impl.menu;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.activity.MainActivity;
import com.linsaya.heima_zhbj_01.base.BaseMenuDetailPager;
import com.linsaya.heima_zhbj_01.domain.NewsMenu;
import com.viewpagerindicator.TabPageIndicator;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements ViewPager.OnPageChangeListener {
    //数据包集合
    private List<NewsMenu.NewsTabData> mChildren;
    //页面集合
    private List<TabDetailPager> mPager;
    private ViewPager vp_news_menu_detail;
    private TabPageIndicator indicator;
    private ImageButton ib_next;

    //从
    public NewsMenuDetailPager(Activity activity, List<NewsMenu.NewsTabData> children) {
        super(activity);
        mChildren = children;
    }

    //新建一个布局，添加到标签页视图中，里面包含一个viewpager
    @Override
    public View initView() {

//            TextView textView = new TextView(mActivity);
//            textView.setText("菜单详情页：新闻");
//            textView.setTextColor(Color.RED);
//            textView.setTextSize(30);
//            textView.setGravity(Gravity.CENTER);
//            return textView;

        View view = View.inflate(mActivity,R.layout.pager_news_menu_detail,null);
        vp_news_menu_detail = (ViewPager) view.findViewById(R.id.vp_news_menu_detail);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        ib_next = (ImageButton) view.findViewById(R.id.ib_next);

        return view;
    }

    @Override
    public void initData() {
        mPager = new ArrayList<>();
        for (int i = 0; i < mChildren.size(); i++) {
            //在构造方法内穿入解析数据，给构造方法使用
            TabDetailPager pager = new TabDetailPager(mActivity, mChildren.get(i));
            mPager.add(pager);
        }
        vp_news_menu_detail.setAdapter(new NewsMenuDetailAdapter());
        //indicator自定义控件此方法需放在setAdapter后执行
        indicator.setViewPager(vp_news_menu_detail);
        //必须给指示器设置监听事件，不能给viewpager设置，否则指示器执行事件监听
        indicator.setOnPageChangeListener(this);
        //点击标签页右边箭头，跳转到下一个页面
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = vp_news_menu_detail.getCurrentItem();
                currentItem++;
                vp_news_menu_detail.setCurrentItem(currentItem);
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //  System.out.println("当前页面为：" + position);
        //当前页面为第一个页面时，才允许打开侧边栏
        if (position == 0) {
            setSlidingMenuEnable(true);
        } else {
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //自定义adapter
    class NewsMenuDetailAdapter extends PagerAdapter {

        //给indicator自定义控件设置标题栏
        @Override
        public CharSequence getPageTitle(int position) {
            return mChildren.get(position).title;
        }

        @Override
        public int getCount() {
            return mPager.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = mPager.get(position);
            View view = tabDetailPager.mRootView;
            container.addView(view);
            tabDetailPager.initData();
            return view;
        }
    }

    /**
     * 设置SlidingMenu的隐藏与显示
     *
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


}
