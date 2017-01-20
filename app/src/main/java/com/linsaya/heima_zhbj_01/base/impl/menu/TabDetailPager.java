package com.linsaya.heima_zhbj_01.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.base.BaseMenuDetailPager;
import com.linsaya.heima_zhbj_01.base.BasePager;
import com.linsaya.heima_zhbj_01.domain.NewsMenu;
import com.linsaya.heima_zhbj_01.domain.NewsTabBean;
import com.linsaya.heima_zhbj_01.utils.CacheUtils;
import com.linsaya.heima_zhbj_01.utils.ConstantsValue;
import com.linsaya.heima_zhbj_01.view.TopNewsViewPager;
import com.viewpagerindicator.CirclePageIndicator;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


import java.util.List;

/**
 * 左侧菜单内选项的标签页详情页
 * Created by Administrator on 2017/1/19.
 */

public class TabDetailPager extends BaseMenuDetailPager {


    private NewsMenu.NewsTabData mNewsTabData;
    private TopNewsViewPager vp_paper_tab_detail;
    private List<NewsTabBean.NewsTop> mTopnews;
    private TextView tv_title;
    private String requestUrl;
    private CirclePageIndicator indicator;
    private ListView lv_news_detail;
    private List<NewsTabBean.NewsData> mNews;
    private ListViewAdapter adapter;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mNewsTabData = newsTabData;

    }


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        View headerView = View.inflate(mActivity, R.layout.listview_item_header, null);
        vp_paper_tab_detail = (TopNewsViewPager) headerView.findViewById(R.id.vp_paper_tab_detail);
        tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        lv_news_detail = (ListView) view.findViewById(R.id.lv_news_detail);
        //给listview设置头布局，使整个界面都能滑动
        lv_news_detail.addHeaderView(headerView);
        return view;
    }

    //需要在此方法中赋值
    @Override
    public void initData() {
        //    textView.setText(mNewsTabData.title);
        //先获取缓存
        String cache = CacheUtils.getCache(requestUrl, mActivity);
        //判断缓存不为空时，加载数据
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        //缓存为空时，访问网络加载数据
        getDataFromServer();
        //
    }

    private void getDataFromServer() {
        String mUrl = mNewsTabData.url;
        requestUrl = ConstantsValue.GLOBAL_SERVER_URL + mUrl;
        System.out.println("requestUrl:" + requestUrl);
        RequestParams params = new RequestParams(requestUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                processData(result);
                System.out.println(result);
                CacheUtils.setCache(requestUrl, result, mActivity);
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

    //解析数据包
    private void processData(String json) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(json, NewsTabBean.class);
        mTopnews = newsTabBean.data.topnews;
        mNews = newsTabBean.data.news;
        System.out.println("长度为：" + mTopnews.size());
        if (mTopnews != null) {
            vp_paper_tab_detail.setAdapter(new MyAdapter());
        }
        indicator.setSnap(true);
        indicator.setViewPager(vp_paper_tab_detail);
        //此处需要给页面指示器设置监听事件
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //设置图片下部标题
                NewsTabBean.NewsTop newsTop = mTopnews.get(position);
                tv_title.setText(newsTop.title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //初始化标题设置
        //初始化页面指示器布局
        tv_title.setText(mTopnews.get(0).title);
        indicator.onPageSelected(0);

        //初始化adapter，赋值liseview
        adapter = new ListViewAdapter();
        lv_news_detail.setAdapter(adapter);
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mTopnews.size();
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
            ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置默认图片
            view.setImageResource(R.drawable.pic_item_list_default);
            //加载图片的Url
            String url = mTopnews.get(position).topimage;
            //使用xutils设置图片
            x.image().bind(view, url);

            container.addView(view);
            return view;
        }
    }

    class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNews.size();
        }

        @Override
        public Object getItem(int i) {
            return mNews.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (view == null) {
                viewHolder = new ViewHolder();
                view = View.inflate(mActivity, R.layout.listview_news_item, null);
                viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_date);
                viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            NewsTabBean.NewsData data = mNews.get(i);
            viewHolder.tv_date.setText(data.pubdate);
            viewHolder.tv_title.setText(data.title);
            x.image().bind(viewHolder.iv_icon, data.listimage);
            return view;
        }
    }

    static class ViewHolder {
        public TextView tv_title;
        public TextView tv_date;
        public ImageView iv_icon;
    }

}
