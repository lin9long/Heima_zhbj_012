package com.linsaya.heima_zhbj_01.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/1/16.
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment {


    public Activity mActivity;

    //因为Fragment依赖于Activity存在，且生命周期开始于Activity的onCreate方法之后，可以在方法内直接调用获取Activity
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    //创建一个抽象方法，由子类实现并创建一个View，返回到fragment中,初始化布局
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView();
        return view;
    }

    //创建一个抽象方法，由子类实现并初始化数据，fragment所以来的activity的oncreate方法执行结束
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public abstract View initView();

    public abstract void initData();
}
