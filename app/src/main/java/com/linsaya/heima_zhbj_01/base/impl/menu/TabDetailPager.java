package com.linsaya.heima_zhbj_01.base.impl.menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.activity.NewsDetailActivity;
import com.linsaya.heima_zhbj_01.base.BaseMenuDetailPager;
import com.linsaya.heima_zhbj_01.domain.NewsMenu;
import com.linsaya.heima_zhbj_01.domain.NewsTabBean;
import com.linsaya.heima_zhbj_01.utils.CacheUtils;
import com.linsaya.heima_zhbj_01.utils.ConstantsValue;
import com.linsaya.heima_zhbj_01.utils.PrefUtils;
import com.linsaya.heima_zhbj_01.view.PullToRefreshListView;
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
    private PullToRefreshListView lv_news_detail;
    private List<NewsTabBean.NewsData> mListNews;
    private ListViewAdapter mNewsListAdapter;
    private String mMoreUrl;
    private Handler mHandler;

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
        lv_news_detail = (PullToRefreshListView) view.findViewById(R.id.lv_news_detail);
        //给listview设置头布局，使整个界面都能滑动
        lv_news_detail.addHeaderView(headerView);
        lv_news_detail.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void Refreshing() {
                getDataFromServer();
                System.out.println("下拉刷新更新啦。。。。。。。。。");
            }

            @Override
            public void isLoadMoer() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                    //加载更多脚布局收回
                    lv_news_detail.onRefreshComplete();
                } else {
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    //加载更多脚布局收回
                    lv_news_detail.onRefreshComplete();
                }
            }
        });
        //处理新闻详情页的点击事件
        lv_news_detail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取头布局的个数
                int headerViewsCount = lv_news_detail.getHeaderViewsCount();
                //当前位置需要减去头布局的个数
                int currentPosition = position - headerViewsCount;
                System.out.println("当前点击条目为：" + currentPosition);
                //获取当前点击条目的id
                String readIds = PrefUtils.getString(mActivity, ConstantsValue.READ_ID, "");
                //此处要传入当前位置的id值
                int newsId = mListNews.get(currentPosition).id;
                //需要判断当前id值不存在于PrefUtils内，不再才能往里面添加，避免重复
                if (!readIds.contains(newsId + "")) {
                    //将新闻id累加后，以","作为分隔符，放到PrefUtils内
                    readIds = readIds + newsId + ",";
                    PrefUtils.putString(mActivity, ConstantsValue.READ_ID, readIds);
                }
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setTextColor(Color.GRAY);
                //开启一个新的activity，打开webview显示网页
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                //将网页链接传入intent内
                intent.putExtra("url", mListNews.get(position).url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    private void getMoreDataFromServer() {

        RequestParams params = new RequestParams(mMoreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                processData(result, true);
                //调用listview的方法，获取数据成功后，收回头布局并更新时间
//                    lv_news_detail.onRefreshComplete();
//                    lv_news_detail.getCurrentTime();
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

    //需要在此方法中赋值
    @Override
    public void initData() {
        //    textView.setText(mNewsTabData.title);
        //先获取缓存
        String cache = CacheUtils.getCache(requestUrl, mActivity);
        //判断缓存不为空时，加载数据
        if (!TextUtils.isEmpty(cache)) {
            processData(cache, false);
        }
        //缓存为空时，访问网络加载数据
        getDataFromServer();
        //
    }

    private void getDataFromServer() {
        String mUrl = mNewsTabData.url;
        requestUrl = ConstantsValue.GLOBAL_SERVER_URL + mUrl;
        //System.out.println("requestUrl:" + requestUrl);
        RequestParams params = new RequestParams(requestUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                processData(result, false);
                //System.out.println(result);
                CacheUtils.setCache(requestUrl, result, mActivity);
                //调用listview的方法，获取数据成功后，收回头布局并更新时间
                lv_news_detail.onRefreshComplete();
                lv_news_detail.getCurrentTime();
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

    //解析数据包，boolean变量判断是否加载更多
    private void processData(String json, boolean isLoadMore) {
        Gson gson = new Gson();
        NewsTabBean newsTabBean = gson.fromJson(json, NewsTabBean.class);

        //此处获取加载更多数据的url
        final String more = newsTabBean.data.more;
        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = ConstantsValue.GLOBAL_SERVER_URL + more;
        } else {
            //如果该字段为空，则加载更多url也置为空
            mMoreUrl = null;
        }

        //如果不需要加载更多时，走原来的逻辑
        if (!isLoadMore) {
            mTopnews = newsTabBean.data.topnews;
            mListNews = newsTabBean.data.news;
            // System.out.println("长度为：" + mTopnews.size());
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
            mNewsListAdapter = new ListViewAdapter();
            lv_news_detail.setAdapter(mNewsListAdapter);
        } else {
            //如果需要加载更多时，走新逻辑的逻辑，Gson解析完数据后，往原有的mListNews集合内添加新数据
            // ，再通知adapter刷新界面，加载所有数据
            List<NewsTabBean.NewsData> news = newsTabBean.data.news;
            mListNews.addAll(news);
            mNewsListAdapter.notifyDataSetChanged();
        }
        //实现topnews的viewpager自动轮播
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    int currentItem = vp_paper_tab_detail.getCurrentItem();
                    currentItem++;
                    if (currentItem > mTopnews.size() - 1) {
                        currentItem = 0;
                    }
                    vp_paper_tab_detail.setCurrentItem(currentItem);
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                }
            };
        }
        //只发送一次msg的数据，如果在开始设置则会发送多次
        mHandler.sendEmptyMessageDelayed(0, 3000);
        //重写触摸监听事件，改善用户体验
        vp_paper_tab_detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //当用户按下时，取消所有的msg发送
                        mHandler.removeCallbacksAndMessages(null);
                        System.out.println("ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_UP:
                        //当用户抬手时，恢复消息发送
                        mHandler.sendEmptyMessageDelayed(0, 3000);
                        System.out.println("ACTION_UP");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //当用户按下时，没有在原地抬手，而是触摸listview或是滑动到布局的其他地方，执行此方法，不再执行ACTION_UP
                        mHandler.sendEmptyMessageDelayed(0, 3000);
                        System.out.println("ACTION_CANCEL");
                        break;
                }
                return false;
            }
        });
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
            return mListNews.size();
        }

        @Override
        public Object getItem(int i) {
            return mListNews.get(i);
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
            NewsTabBean.NewsData data = mListNews.get(i);
            viewHolder.tv_date.setText(data.pubdate);
            viewHolder.tv_title.setText(data.title);
            //判断如果当前标签页已经被选中，能在PrefUtils读取到id值，将标题颜色置为灰色
            String readIds = PrefUtils.getString(mActivity, ConstantsValue.READ_ID, "");
            if (readIds.contains(data.id + "")) {
                viewHolder.tv_title.setTextColor(Color.GRAY);
            } else {
                viewHolder.tv_title.setTextColor(Color.BLACK);
            }

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
