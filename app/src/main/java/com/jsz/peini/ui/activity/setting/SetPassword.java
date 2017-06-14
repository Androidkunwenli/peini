package com.jsz.peini.ui.activity.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.login.LoginActivity;
import com.jsz.peini.ui.view.password.newlook.GestureContentView;
import com.jsz.peini.ui.view.password.newlook.GestureDrawline;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


/**
 * Created by th on 2016/12/10.
 */
public class SetPassword extends BaseNotSlideActivity {
    /*头像*/
    @InjectView(R.id.image_tip)
    CircleImageView mImageTip;
    /*文字*/
    @InjectView(R.id.text_tip)
    TextView mTextTip;
    /*头像布局*/
    @InjectView(R.id.gesture_tip_layout)
    LinearLayout mGestureTipLayout;
    /*手势密码*/
    @InjectView(R.id.gesture_container)
    FrameLayout mGestureContainer;

    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;

    private SetPassword mActivity;
    private GestureContentView mGestureContentView;

    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    /**
     * 是否校验密码
     */
    public boolean mIsVerify;
    /**
     * 密码信息
     */
    public String mLock;
    /**
     * 可输入次数
     */
    public int mIndex = 5;

    @Override
    public int initLayoutId() {
        return R.layout.activity_setpassword;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("手势密码");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mIsVerify = getIntent().getBooleanExtra("b", false);
        mLock = getIntent().getStringExtra("lock");
        LogUtil.d("收到传递过来的参数了" + mIsVerify + "--" + mLock);

        if (mIsVerify) {
            //请输入手势密码
            mTextTip.setText("请输入手势密码");
        } else {
            mTextTip.setText("请设置手势密码");
        }


        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic+SpUtils.getImageHead(mActivity),mImageTip,SpUtils.getSex(mActivity));
    }

    @Override
    public void initData() {
        mGestureContentView = new GestureContentView(mActivity, mIsVerify, mLock, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText("最少链接4个点, 请重新输入");
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    mGestureContentView.clearDrawlineState(0L);
                    mTextTip.setText("再次设置手势密码");
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        Toasty.normal(mActivity, "设置成功！").show();
                        mGestureContentView.clearDrawlineState(0L);
                        SpUtils.put(mActivity, "lock", mFirstPassword);
                        mActivity.finish();
                    } else {
                        mTextTip.setText("您两次设置手势密码不一致！");
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(500L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {
                Toasty.normal(mActivity, "取消手势密码").show();
                SpUtils.put(mActivity, "lock", "");
                mGestureContentView.clearDrawlineState(0L);
                finish();
            }

            @Override
            public void checkedFail() {
                mIndex--;
                mTextTip.setText("密码错了,还可以输入" + mIndex + "次");
                // 左右移动动画
                Animation shakeAnimation = AnimationUtils.loadAnimation(mActivity, R.anim.shake);
                mTextTip.startAnimation(shakeAnimation);
                mGestureContentView.clearDrawlineState(500L);
                if (mIndex == 0) {
                    dialog();
                }
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
        return !(TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4);
    }

}
