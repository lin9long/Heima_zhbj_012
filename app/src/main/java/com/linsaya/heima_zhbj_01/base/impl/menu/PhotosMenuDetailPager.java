package com.linsaya.heima_zhbj_01.base.impl.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linsaya.heima_zhbj_01.R;
import com.linsaya.heima_zhbj_01.base.BaseMenuDetailPager;
import com.linsaya.heima_zhbj_01.domain.PhotoBean;
import com.linsaya.heima_zhbj_01.utils.CacheUtils;
import com.linsaya.heima_zhbj_01.utils.ConstantsValue;
import com.linsaya.heima_zhbj_01.utils.MyBitmapUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager implements View.OnClickListener {

    private ListView lv_photo_detail;
    private GridView gv_photo_detail;
    private String mPhotoUrl;
    private PhotoBean mPhotoBean;
    private List<PhotoBean.PhotoNews> mNewsList;
    private PhotoAdapter mAdapter;
    private ImageButton ib_grid_list;

    public PhotosMenuDetailPager(Activity activity, ImageButton ib_photo_list) {
        super(activity);
        ib_grid_list = ib_photo_list;
        ib_photo_list.setOnClickListener(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photo_menu_detail, null);
        lv_photo_detail = (ListView) view.findViewById(R.id.lv_photo_detail);
        gv_photo_detail = (GridView) view.findViewById(R.id.gv_photo_detail);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mPhotoUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            procerrData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        mPhotoUrl = ConstantsValue.GLOBAL_SERVER_URL + ConstantsValue.PHOTO_URL;
        RequestParams params = new RequestParams(mPhotoUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                procerrData(result);
                CacheUtils.setCache(mPhotoUrl, result, mActivity);
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

    private void procerrData(String result) {
        Gson gson = new Gson();
        mPhotoBean = gson.fromJson(result, PhotoBean.class);
        mNewsList = mPhotoBean.data.news;
        mAdapter = new PhotoAdapter();
        lv_photo_detail.setAdapter(mAdapter);
        gv_photo_detail.setAdapter(mAdapter);
    }

    private boolean isListView = true;

    @Override
    public void onClick(View v) {
        if (isListView) {
            lv_photo_detail.setVisibility(View.GONE);
            gv_photo_detail.setVisibility(View.VISIBLE);
            ib_grid_list.setImageResource(R.drawable.icon_pic_grid_type);
            isListView = false;
        } else {
            lv_photo_detail.setVisibility(View.VISIBLE);
            gv_photo_detail.setVisibility(View.GONE);
            ib_grid_list.setImageResource(R.drawable.icon_pic_list_type);
            isListView = true;
        }
    }

    class PhotoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Viewholder viewholder = null;
            if (convertView == null) {
                viewholder = new Viewholder();
                convertView = View.inflate(mActivity, R.layout.listview_photo_detail_item, null);
                viewholder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
                viewholder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(viewholder);
            } else {
                viewholder = (Viewholder) convertView.getTag();
            }
            String title = mNewsList.get(position).title;
            viewholder.tv_title.setText(title);
            String photoUrl = mNewsList.get(position).listimage;
            //自定义三级缓存工具
            MyBitmapUtils mBitmapUtils = new MyBitmapUtils();
            mBitmapUtils.display(viewholder.iv_photo,photoUrl);
            //x.image().bind(viewholder.iv_photo, photoUrl);
            return convertView;
        }
    }

    class Viewholder {
        public ImageView iv_photo;
        public TextView tv_title;
    }


}
