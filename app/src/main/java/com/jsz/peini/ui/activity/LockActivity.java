package com.jsz.peini.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
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

/**
 * Created by th on 2017/1/16.
 */
public class LockActivity extends BaseNotSlideActivity implements NonGestureLockInterface {
    @InjectView(R.id.image_tip)
    CircleImageView mImageTip;
    @InjectView(R.id.text_tip)
    TextView mTextTip;
    @InjectView(R.id.gesture_tip_layout)
    LinearLayout mGestureTipLayout;
    @InjectView(R.id.gesture_container)
    FrameLayout mGestureContainer;
    public GestureContentView mGestureContentView;
    public LockActivity mActivity;
    public String mLock;
    public int mIndex = 5;

    @Override
    public int initLayoutId() {
        return R.layout.activity_lock;
    }

    @Override
    public void initView() {
        mActivity = this;
        mLock = (String) SpUtils.get(mActivity, "lock", "");

        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + SpUtils.getImageHead(mActivity), mImageTip, SpUtils.getSex(mActivity));
    }

    @Override
    public void initData() {
        mGestureContentView = new GestureContentView(mActivity, true, mLock, new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {

            }

            @Override
            public void checkedSuccess() {
                finish();
            }

            @Override
            public void checkedFail() {
                mIndex--;
                mTextTip.setText("密码错了,还可以输入" + mIndex + "次");
                if (mIndex == 0) {
                    dialog();
                }
                mGestureContentView.clearDrawlineState(100L);
            }
        });
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
                //注销别名
                boolean unBindAlias = PushManager.getInstance().unBindAlias(mActivity, SpUtils.getPhone(mActivity), false);
                if (unBindAlias) {
                    LogUtil.d("解绑定别名成功了-----");
                } else {
                    LogUtil.d("解绑定别名失败了-----");
                }
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

    @Override
    public boolean isGestureLock() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PeiNiApp.setIsGestureLocking(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PeiNiApp.setIsGestureLocking(false);
    }

    @Override
    public void onBackPressed() {
    }
}
