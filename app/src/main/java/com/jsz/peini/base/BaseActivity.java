package com.jsz.peini.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.widget.loading.LoadingDialog;

import butterknife.ButterKnife;

/**
 * Created by kunwe on 2016/10/13.
 * 初始化 activity  删除测试my
 * activity 的基类
 */

public abstract class BaseActivity extends SwipeBackActivity {
    /**
     * 用户mUserToken
     */
    public String mUserToken;
    /**
     * 弹出加载框
     */
    public LoadingDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayoutId());
        ButterKnife.inject(this);
        mDialog = new LoadingDialog(this, R.layout.view_tips_loading, R.style.ActionSheetDialogStyle);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setText("正在加载...");
        initspData();
        initView();
        initData();
        initInternet();
        initListener();
    }

    //初始化数据
    private void initspData() {
        mUserToken = (String) SpUtils.get(this, "mUserToken", "");
    }

    public void initInternet() {
    }

    public abstract int initLayoutId();

    /**
     * 初始化view
     * 1. 如果使用注解工具，不需要重新该方法
     * 2. 如果是findById ,可以重新改方法
     */

    public void initView() {
    }

    /**
     * 根据业务需求，去实现该方法，用于初始化数据， 读取数据库，访问网络
     */
    public void initData() {
    }

    protected void initListener() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void showProgressDialog() {
        mDialog.show();
    }

    protected void hideProgressDialog() {
        mDialog.dismiss();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
