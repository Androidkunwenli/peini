package com.jsz.peini.ui.activity.setting;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by th on 2016/12/10.
 */
public class GesturePasswordActivity extends BaseActivity {
    @InjectView(R.id.cb_switch)
    CheckBox mCbSwitch;
    @InjectView(R.id.ll_gesture_setting)
    LinearLayout mGestureSetting;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;

    private boolean mNoNull;
    private GesturePasswordActivity mActivity;
    /*存储的密码*/
    public String mLock;

    @Override
    public int initLayoutId() {
        return R.layout.activity_gesturepassword;
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

    }

    @Override
    public void initData() {
        LogUtil.d("手势密码管理界面可见");
        /*是否有密码*/
        mNoNull = StringUtils.isNoNull(mLock);
        isShowLView(mNoNull);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLock = (String) SpUtils.get(mActivity, "lock", "");
          /*是否有密码*/
        mNoNull = StringUtils.isNoNull(mLock);
        isShowLView(mNoNull);
    }

    /**
     * 是否显示后两条布局
     * case R.id.start_service:
     * Intent startIntent = new Intent(this, MyService.class);
     * startService(startIntent);
     * break;
     * case R.id.stop_service:
     * Intent stopIntent = new Intent(this, MyService.class);
     * stopService(stopIntent);
     */
    private void isShowLView(boolean noNull) {
        mCbSwitch.setChecked(noNull);
        mGestureSetting.setVisibility(noNull ? View.VISIBLE : View.GONE);
    }

    @OnClick({R.id.ll_gesture_lock, R.id.ll_password_reset, R.id.ll_gesture_get_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_gesture_lock:
                if (mNoNull) {
                    Intent intent = new Intent(mActivity, SetPassword.class);
                    intent.putExtra("lock", mLock);
                    intent.putExtra("b", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, SetPassword.class);
                    intent.putExtra("lock", "");
                    intent.putExtra("b", false);
                    startActivity(intent);
                }
                break;
            case R.id.ll_password_reset:
                LogUtil.d("重置手势密码");
                startActivity(new Intent(mActivity, ResetPasswordActivity.class));
                break;
            case R.id.ll_gesture_get_back:
                LogUtil.d("找回手势密码");
                startActivity(new Intent(mActivity, UpdataPasswordActivity.class));
                break;
        }
    }
}
