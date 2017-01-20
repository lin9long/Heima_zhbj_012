package com.linsaya.heima_zhbj_01.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.activity.MainActivity;
import com.linsaya.heima_zhbj_01.base.BasePager;
import com.linsaya.heima_zhbj_01.base.impl.GovAffairsPager;
import com.linsaya.heima_zhbj_01.base.impl.HomePager;
import com.linsaya.heima_zhbj_01.base.impl.NewsCenterPager;
import com.linsaya.heima_zhbj_01.base.impl.SettingPager;
import com.linsaya.heima_zhbj_01.base.impl.SmartServicePager;
import com.linsaya.heima_zhbj_01.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager vp_content;
    private List<BasePager> mPageList;
    private RadioGroup rgGroup;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        vp_content = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        rgGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
        mPageList = new ArrayList<>();
        //添加5个标签页
        mPageList.add(new HomePager(mActivity));
        mPageList.add(new NewsCenterPager(mActivity));
        mPageList.add(new SmartServicePager(mActivity));
        mPageList.add(new GovAffairsPager(mActivity));
        mPageList.add(new SettingPager(mActivity));
        //设置adapter
        vp_content.setAdapter(new MyAdapter());
        //响应RadioButton的点击事件
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0,false);//是否具有滑动动画
                        break;
                    case R.id.rb_newscenter:
                        vp_content.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smartservice:
                        vp_content.setCurrentItem(2,false);
                        break;
                    case R.id.rb_govaffairs:
                        vp_content.setCurrentItem(3,false);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4,false);
                        break;
                    default:
                        break;
                }
            }
        });
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //当前页面被选中时才调用加载数据的方法
                BasePager basePager = mPageList.get(position);
                basePager.initData();

                //主页面及设置页面不显示侧边栏
                if (position == 0 || position == mPageList.size() - 1) {
                    setSlidingMenuEnable(false);
                } else {
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPageList.get(0).initData();
    }

    /**
     * 设置SlidingMenu的隐藏与显示
     *
     * @param enable
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPageList.size();
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
            BasePager basePager = mPageList.get(position);
            View view = basePager.mRootView;
            //  basePager.initData(); 因为ViewPager默认添加当前页及后一页的数据，不能再此处初始化数据，需要在用户选中界面在初始化数据
            container.addView(view);
            return view;
        }
    }
    public NewsCenterPager getNewscenterPager() {
        NewsCenterPager basePager = (NewsCenterPager) mPageList.get(1);
        return basePager;
    }
}
