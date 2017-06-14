package com.jsz.peini.ui.activity.setting;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.ui.activity.login.LoginActivity;
import com.jsz.peini.ui.view.password.newlook.GestureContentView;
import com.jsz.peini.ui.view.password.newlook.GestureDrawline;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.dmoral.toasty.Toasty;

/**
 * Created by th on 2017/1/16.
 */
public class ResetPasswordActivity extends BaseNotSlideActivity {

    @InjectView(R.id.image_tip)
    ImageView mImageTip;
    @InjectView(R.id.text_tip)
    TextView mTextTip;
    @InjectView(R.id.gesture_tip_layout)
    LinearLayout mGestureTipLayout;
    @InjectView(R.id.gesture_container)
    FrameLayout mGestureContainer;

    public GestureContentView mGestureContentView;
    @InjectView(R.id.iv_toolbar_image)
    ImageView mIvToolbarImage;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    public ResetPasswordActivity mActivity;
    public boolean mEquals = true;
    public int mIndex = 5;

    @Override
    public int initLayoutId() {
        return R.layout.activity_resetpassword;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("重置手势密码");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        mGestureContentView = new GestureContentView(mActivity, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText("最少链接4个点, 请重新输入");
                    mGestureContentView.clearDrawlineState(200L);
                    return;
                }
                if (mEquals) {
                    String lock = (String) SpUtils.get(mActivity, "lock", "");
                    if (!inputCode.equals(lock)) {
                        mIndex--;
                        mTextTip.setText("原始密码输入错误");
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        mGestureContentView.clearDrawlineState(500L);
                        if (mIndex == 0) {
                            dialog();
                        }
                        return;
                    }
                    mEquals = false;
                    mTextTip.setText("请输入新手势密码");
                    mGestureContentView.clearDrawlineState(0L);
                } else {
                    if (mIsFirstInput) {
                        mFirstPassword = inputCode;
                        mGestureContentView.clearDrawlineState(0L);
                        mTextTip.setText("请再次输入手势密码");
                    } else {
                        if (inputCode.equals(mFirstPassword)) {
                            Toasty.normal(mActivity, "设置成功!").show();
                            mGestureContentView.clearDrawlineState(0L);
                            SpUtils.put(mActivity, "lock", mFirstPassword);
                            mActivity.finish();
                        } else {
                            mTextTip.setText("与上一次绘制不一致，请重新绘制");
                            // 左右移动动画
                            Animation shakeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.shake);
                            mTextTip.startAnimation(shakeAnimation);
                            // 保持绘制的线，1.5秒后清除
                            mGestureContentView.clearDrawlineState(500L);
                        }
                    }
                    mIsFirstInput = false;
                }
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("请重新登录");
        builder.setTitle("手势密码已失效");
        builder.setCancelable(false);
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SpUtils.remove(mActivity, "lock");
                SpUtils.remove(mActivity, "mUserToken");
                SpUtils.remove(mActivity, "imageHead");
                SpUtils.remove(mActivity, Conversion.CANCELTASKNUMBER);
                SpUtils.remove(mActivity, Conversion.CANCELPHONENUMBER);
                SpUtils.remove(mActivity, Conversion.STORE_MANAGE_ID);
                SpUtils.remove(mActivity, Conversion.RANKING);
                //关闭推送
                PushManager.getInstance().turnOffPush(mActivity.getApplicationContext());
                PushManager.getInstance().stopService(mActivity.getApplicationContext());
                //注销环信
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                    }
                });
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

}
