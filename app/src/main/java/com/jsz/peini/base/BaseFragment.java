package com.jsz.peini.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.utils.SpUtils;

import butterknife.ButterKnife;

/**
 * Created by kunwe on 2016/10/14.
 */

public abstract class BaseFragment extends Fragment {
    public Activity mActivity;//当做Context去使用
    public View mRootView;//fragment的根布局
    public Context mContext;
    /**
     * 用户mUserToken
     */
    public String mUserToken;
    /**
     * 用户mId
     */
    public String muId;
    /**
     * 头像
     */
    public String mImageHead;
    /*名字*/
    public String mNickname;
    /**
     * 用户的性别
     */
    public String mSex;
    /**
     * 手机号
     */
    public String mPhone;
    /**
     * 精度
     */
    public String mXpoint;
    /**
     * 维度
     */
    public String mYpoint;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();//获取fragment所依赖的activity的对象
        mContext = PeiNiApp.context;
        mUserToken = (String) SpUtils.get(mContext, "mUserToken", "");

        muId = (String) SpUtils.get(mContext, "id", "");

        mNickname = (String) SpUtils.get(mContext, "nickname", "");

        mImageHead = (String) SpUtils.get(mContext, "imageHead", "");

        mSex = (String) SpUtils.get(mContext, "sex", "");

        mPhone = (String) SpUtils.get(mContext, "phone", "");

        mXpoint = (String) SpUtils.get(mContext, "xpoint", "38.048684");

        mYpoint = (String) SpUtils.get(mContext, "ypoint", "114.520828");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = initViews();
        ButterKnife.inject(this, mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListentr();
    }

    public void initListentr() {
    }

    //初始化数据, 子类可以不实现
    public void initData() {
    }

    //初始化布局, 必须由子类来实现
    public abstract View initViews();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }
}
