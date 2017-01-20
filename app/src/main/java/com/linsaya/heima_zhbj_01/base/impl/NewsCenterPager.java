package com.linsaya.heima_zhbj_01.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;


import com.google.gson.Gson;
import com.linsaya.heima_zhbj_01.activity.MainActivity;
import com.linsaya.heima_zhbj_01.base.BaseMenuDetailPager;
import com.linsaya.heima_zhbj_01.base.BasePager;
import com.linsaya.heima_zhbj_01.base.impl.menu.InteractMenuDetailPager;
import com.linsaya.heima_zhbj_01.base.impl.menu.NewsMenuDetailPager;
import com.linsaya.heima_zhbj_01.base.impl.menu.PhotosMenuDetailPager;
import com.linsaya.heima_zhbj_01.base.impl.menu.TopicMenuDetailPager;
import com.linsaya.heima_zhbj_01.domain.NewsMenu;
import com.linsaya.heima_zhbj_01.fragment.LeftMenuFragment;
import com.linsaya.heima_zhbj_01.utils.CacheUtils;
import com.linsaya.heima_zhbj_01.utils.ConstantsValue;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/17.
 */

public class NewsCenterPager extends BasePager {
    private NewsMenu mData;
    private List<BaseMenuDetailPager> pages;


    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        System.out.println("3初始化啦。。。");
        TextView textView = new TextView(mActivity);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);
        tvTitle.setText("新闻中心");
        ibMenu.setVisibility(View.VISIBLE);

        //请求数据前先访问缓存，如果缓存存在则先加载后，再从网络获取最新数据，保证用户体验
        //此处不用else判断是因为缓存是有生命周期，动态变化的，不能因为存在缓存而不从网络加载数据
        String cache = CacheUtils.getCache(ConstantsValue.SERVER_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
          //  System.out.println("发现缓存啦....");
            processData(cache);
        }
            getDataFromServer();

    }

    private void getDataFromServer() {
        RequestParams params = new RequestParams(ConstantsValue.SERVER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                processData(result);
                CacheUtils.setCache(ConstantsValue.SERVER_URL,result,mActivity);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String result) {
        Gson gson = new Gson();
        mData = gson.fromJson(result, NewsMenu.class);
        //System.out.println("解析结果为："+mData);
        //获取主activity的对象
        MainActivity mainUi = (MainActivity) mActivity;
        //通过MainActivivty获取leftFragment
        LeftMenuFragment leftFragment = mainUi.getLeftFragment();
        //在leftFragment方法内新建一个方法，给新闻页调用传递数据
        leftFragment.setMenuData(mData.data);

        //添加菜单详情页数组
        pages = new ArrayList<>();
        //需要在构造方法内传入参数，使菜单详情页能获取网络数据
        pages.add(new NewsMenuDetailPager(mActivity,mData.data.get(0).children));
        pages.add(new TopicMenuDetailPager(mActivity));
        pages.add(new PhotosMenuDetailPager(mActivity));
        pages.add(new InteractMenuDetailPager(mActivity));
        //初始化布局
        setCurrentDetailPager(0);

    }

    public void setCurrentDetailPager(int position) {
        //获取当前应该显示的界面
        BaseMenuDetailPager baseMenuDetailPager = pages.get(position);
        //先清空原布局内部内容
        flContent.removeAllViews();
        //添加布局到容器内
        flContent.addView(baseMenuDetailPager.mRootView);
        baseMenuDetailPager.initData();
        tvTitle.setText(mData.data.get(position).title);

    }
}
