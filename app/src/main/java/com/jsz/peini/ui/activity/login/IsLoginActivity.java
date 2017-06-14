package com.jsz.peini.ui.activity.login;

import android.content.Intent;
import android.view.View;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.utils.CacheActivity;

import butterknife.OnClick;

public class IsLoginActivity extends BaseNotSlideActivity implements NonGestureLockInterface {


    @Override
    public int initLayoutId() {
        return R.layout.activity_is_login;
    }

    public void initView() {
        /**存储activity*/
        if (!CacheActivity.activityList.contains(IsLoginActivity.this)) {
            CacheActivity.addActivity(IsLoginActivity.this);
        }
    }

    @Override
    public void showAllVisual(boolean b) {
        super.showAllVisual(true);
    }

    @OnClick({R.id.btn_register, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register://注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login://登录
                startActivity(new Intent(this, LoginActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean isGestureLock() {
        return false;
    }
}
