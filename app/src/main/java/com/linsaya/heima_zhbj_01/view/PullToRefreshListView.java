package com.linsaya.heima_zhbj_01.view;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linsaya.heima_zhbj_01.R;

import java.util.Date;

/**
 * Created by Administrator on 2017/1/21.
 */

public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private final static int STATE_PULL_TO_REFRESH = 1;
    private final static int STATE_REFRESH_TO_REFRESH = 2;
    private final static int STATE_REFRESHING = 3;
    private int mState = -1;
    private int mHeaderHeight;
    private View mHeader;
    private TextView tv_time;
    private ImageView iv_arrow;
    private ProgressBar pb_refreshing;
    private int startY = -1;
    private int endY;
    private int disY;
    private TextView tv_state;
    private RotateAnimation animUp;
    private RotateAnimation animDown;
    private View mFooter;
    private int mFooterHight;
    private boolean isLoadMore;

    public PullToRefreshListView(Context context) {
        super(context);
        init();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeaderView();
        initAnim();
        getCurrentTime();
        initFooterView();
    }

    private void initHeaderView() {
        mHeader = View.inflate(getContext(), R.layout.listview_pull_to_refresh, null);
        tv_time = (TextView) mHeader.findViewById(R.id.tv_time);
        tv_state = (TextView) mHeader.findViewById(R.id.tv_state);
        iv_arrow = (ImageView) mHeader.findViewById(R.id.iv_arrow);
        pb_refreshing = (ProgressBar) mHeader.findViewById(R.id.pb_refreshing);
        this.addHeaderView(mHeader);
        mHeader.measure(0, 0);
        mHeaderHeight = mHeader.getMeasuredHeight();
        mHeader.setPadding(0, -mHeaderHeight, 0, 0);
    }

    private void initFooterView() {
        mFooter = View.inflate(getContext(), R.layout.listview_pull_to_refresh_footer, null);
        addFooterView(mFooter);
        mFooter.measure(0, 0);
        mFooterHight = mFooter.getMeasuredHeight();
        mFooter.setPadding(0, -mHeaderHeight, 0, 0);
        setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {
                    startY = (int) ev.getY();
                }
                if (mState == STATE_REFRESHING) {
                    //如果当前状态为刷新中，不再执行以下代码
                    break;
                }
                endY = (int) ev.getY();
                disY = endY - startY;
                int firstVisiblePosition = getFirstVisiblePosition();
                //判断在当前位置往下滑动时，才触发padding自动调整的过程
                if (disY > 0 && firstVisiblePosition == 0) {
                    int paddingTop = disY - mHeaderHeight;
                    mHeader.setPadding(0, paddingTop, 0, 0);
                    //当前控件已经完全显示，状态切换为释放刷新
                    if (paddingTop > 0 && mState != STATE_REFRESH_TO_REFRESH) {
                        mState = STATE_REFRESH_TO_REFRESH;
                        updataHeader();
                        //当前控件未完全显示，状态切换为下拉刷新
                    } else if (paddingTop < 0 && mState != STATE_PULL_TO_REFRESH) {
                        mState = STATE_PULL_TO_REFRESH;
                        updataHeader();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                //此处需要给startY初始化数据，否则会影响后续判断
                startY = -1;
                if (mState == STATE_REFRESH_TO_REFRESH) {
                    mState = STATE_REFRESHING;
                    mHeader.setPadding(0, 0, 0, 0);
                    updataHeader();
                } else if (mState == STATE_PULL_TO_REFRESH) {
                    mHeader.setPadding(0, -mHeaderHeight, 0, 0);
                    updataHeader();
                }

                break;
        }


        return super.onTouchEvent(ev);
    }

    private void updataHeader() {
        switch (mState) {
            case STATE_PULL_TO_REFRESH:
                tv_state.setText("下拉刷新");
                iv_arrow.setVisibility(VISIBLE);
                pb_refreshing.setVisibility(INVISIBLE);
                iv_arrow.startAnimation(animDown);
                break;
            case STATE_REFRESH_TO_REFRESH:
                tv_state.setText("释放刷新");
                iv_arrow.setVisibility(VISIBLE);
                pb_refreshing.setVisibility(INVISIBLE);
                iv_arrow.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tv_state.setText("正在刷新");
                iv_arrow.clearAnimation();
                iv_arrow.setVisibility(INVISIBLE);
                pb_refreshing.setVisibility(VISIBLE);
                if (mListener != null) {
                    mListener.Refreshing();
                }
                break;
        }
    }

    private void initAnim() {
        animUp = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(500);
        animUp.setFillAfter(true);

        animDown = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(500);
        animDown.setFillAfter(true);
    }

    public OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }


    public interface OnRefreshListener {
        public void Refreshing();

        public void isLoadMoer();
    }

    public void onRefreshComplete() {
        if (!isLoadMore) {
            mHeader.setPadding(0, -mHeaderHeight, 0, 0);
            mState = STATE_PULL_TO_REFRESH;
            iv_arrow.setVisibility(VISIBLE);
            pb_refreshing.setVisibility(INVISIBLE);
        } else {
            mFooter.setPadding(0, -mFooterHight, 0, 0);
            isLoadMore = false;
        }
    }

    public void getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());
        tv_time.setText(time);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当前滑动状态为空闲时
        if (scrollState == SCROLL_STATE_IDLE) {
            int lastPosition = getLastVisiblePosition();
            int count = getCount();
            //当前位置为最后一个条目，并且系统没有加载更多数据的时候
            if (lastPosition == count - 1 && !isLoadMore) {
                System.out.println("加载更多中....");
                mFooter.setPadding(0, 0, 0, 0);
                isLoadMore = true;
                //设置当前显示条目为最底部
                setSelection(count);
                //通知主线程加载更多，回调方法
                if (mListener != null) {
                    mListener.isLoadMoer();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
