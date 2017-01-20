package com.linsaya.heima_zhbj_01.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.activity.MainActivity;
import com.linsaya.heima_zhbj_01.base.impl.NewsCenterPager;
import com.linsaya.heima_zhbj_01.domain.NewsMenu;

import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */

public class LeftMenuFragment extends BaseFragment {

    private ListView lv_menudata;
    private List<NewsMenu.NewsMenuData> mNewsMenuData;
    private MyAdapter myAdapter;
    private int mCurrentPos;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_layout, null);
        lv_menudata = (ListView) view.findViewById(R.id.lv_menudata);
        return view;
    }

    @Override
    public void initData() {

    }

    //给侧边栏设置数据
    public void setMenuData(List<NewsMenu.NewsMenuData> data) {
        mCurrentPos = 0;
        mNewsMenuData = data;

        myAdapter = new MyAdapter();
        lv_menudata.setAdapter(myAdapter);
        System.out.println("Fragment内的数据为：" + mNewsMenuData.size());
        System.out.println("数组1的title内数据为：" + mNewsMenuData.get(0).title);

        //在此处给mCurrentPos赋值
        lv_menudata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;

                myAdapter.notifyDataSetChanged();
                //点击相应项目后，打开关闭左边栏
                toogle();
                //更新设置相应项目对应主布局详情页
                setMenuDetailPager(position);

            }
        });

    }

    private void setMenuDetailPager(int position) {
        //1.获取MainActivity对象
        MainActivity mainUi = (MainActivity) mActivity;
        //2.通过获取主Fragment，获得其主容器
        ContentFragment contentFragment = mainUi.getContentFragment();
        //3.获取主布局内的newscenterPager页面
        NewsCenterPager newscenterPager = contentFragment.getNewscenterPager();
        //4.在newscenterPager内根据选择位置设置容器内容
        newscenterPager.setCurrentDetailPager(position);

    }

    //控制slidingmenu的开关，当前状态为打开时，切换为关；当前状态为关闭时，切换为开
    private void toogle() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.listview_left_item, null);
            TextView tv_menu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(position);
            tv_menu.setText(item.title);
            if (position == mCurrentPos) {
                tv_menu.setEnabled(true);
            } else {
                tv_menu.setEnabled(false);
            }
            return view;
        }
    }

}
