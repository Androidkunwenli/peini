package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.square.VerifyDataBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/21.
 */
public class AccountActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.peini_id)
    EditText mPeiniId;
    @InjectView(R.id.next_btn)
    Button mNextBtn;
    private String mId;
    private String mNumbar;
    private AccountActivity mActivity;
    private Intent mIntent;
    private VerifyDataBean mDataBean;

    @Override
    public int initLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("指定账户");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initListener() {
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = mPeiniId.getText().toString().trim();
                if (TextUtils.isEmpty(mId)) {
                    Toasty.normal(mActivity, "请输入用户ID或手机号").show();
                    return;
                }
                initNetWork();
            }
        });
    }

    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .payGoldSureUserInfo(mUserToken, "", mId, "")
                .enqueue(new RetrofitCallback<VerifyDataBean>() {
                    @Override
                    public void onSuccess(Call<VerifyDataBean> call, Response<VerifyDataBean> response) {
                        if (response.isSuccessful()) {
                            mDataBean = response.body();
                            if (mDataBean.getResultCode() == 1) {
                                setJumpIntent();
                            } else if (mDataBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (mDataBean.getResultCode() == 0) {
                                Toasty.normal(mActivity, mDataBean.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyDataBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void setJumpIntent() {
        mIntent = new Intent(mActivity, VerifyDataActivity.class);
        mIntent.putExtra("otherId", mId);
        mIntent.putExtra("mDataBean", mDataBean);
        startActivity(mIntent);
        finish();
    }
}
