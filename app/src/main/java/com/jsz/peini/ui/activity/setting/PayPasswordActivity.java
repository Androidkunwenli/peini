package com.jsz.peini.ui.activity.setting;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.ZzUtils;
import com.jsz.peini.utils.time.TimeUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/12/6.
 */
public class PayPasswordActivity extends BaseActivity {
    @InjectView(R.id.phone_text)
    TextView mPhoneText;
    @InjectView(R.id.phone_eit)
    EditText mPhoneEit;
    @InjectView(R.id.pay_show_sms)
    Button mPayShowSms;
    @InjectView(R.id.imageView5)
    ImageView mImageView5;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.nextpassword)
    EditText mNextpassword;
    @InjectView(R.id.ok)
    Button mOk;
    public PayPasswordActivity mActivity;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;

    @Override
    public int initLayoutId() {
        return R.layout.activity_paypassword;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("设置支付密码");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        String phone = SpUtils.getPhone(mActivity);
        if (TextUtils.isEmpty(phone)) {
            Toasty.normal(mActivity, "系统异常,请重新登录!").show();
            finish();
        }
        if (StringUtils.isNoNull(phone)) {
            mPhoneText.setText("正在为" + "" + ZzUtils.phone(SpUtils.getPhone(mActivity) + "设置支付密码"));
        }
        mPassword.addTextChangedListener(getWatcher());
        mNextpassword.addTextChangedListener(getWatcher());
    }

    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = mPassword.getText().toString().length();
                int length1 = mNextpassword.getText().toString().length();
                int length2 = mPhoneEit.getText().toString().trim().length();
                if (length >= 6 && length1 >= 6 && length2 >= 6) {
                    mOk.setBackgroundResource(R.drawable.button_red);
                }
            }
        };
    }

    @OnClick({R.id.pay_show_sms, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_show_sms:
                if (StringUtils.isNull(SpUtils.getPhone(mActivity))) {
                    Toasty.normal(mActivity, "手机号为空!").show();
                    return;
                }
                initNetWorkSms();
                break;
            case R.id.ok:
                String password = mPassword.getText().toString().trim();
                String nextPassword = mNextpassword.getText().toString().trim();
                String sms = mPhoneEit.getText().toString().trim();
                if (!StringUtils.isNoNull(sms)) {
                    Toasty.normal(mActivity, "验证码为空!").show();
                    return;
                }
                if (!StringUtils.isNoNull(password)) {
                    Toasty.normal(mActivity, "请输入支付密码!").show();
                    return;
                }
                if (!StringUtils.isNoNull(nextPassword)) {
                    Toasty.normal(mActivity, "请再次输入支付密码!").show();
                    return;
                }
                if (!password.equals(nextPassword)) {
                    Toasty.normal(mActivity, "两次输入密码不一致!").show();
                    return;
                }
                initNetWork(nextPassword, sms);
                break;
        }
    }

    private void initNetWork(String nextPassword, String sms) {
        RetrofitUtil.createHttpsService(SettingService.class).updatePayPassword(MD5Utils.encode(nextPassword), mUserToken, sms).enqueue(new Callback<SuccessfulBean>() {
            @Override
            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful()) {
                    SuccessfulBean body = response.body();
                    if (body.getResultCode() == 1) {
                        Toasty.success(mActivity, body.getResultDesc()).show();
                        SpUtils.put(mActivity, "PayPassWord", true);
                        finish();
                    } else if (body.getResultCode() == 0) {
                        LogUtil.d("修改失败" + response.body().toString());
                        Toasty.success(mActivity, response.body().getResultDesc()).show();
                    } else {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
            }
        });
    }

    private void initNetWorkSms() {
        mPayShowSms.setEnabled(false);
        RetrofitUtil.createHttpsService(SettingService.class)
                .smsSendPay(mUserToken)
                .enqueue(new Callback<GainSmsBean>() {
                    @Override
                    public void onResponse(Call<GainSmsBean> call, Response<GainSmsBean> response) {
                        if (response.isSuccessful()) {
                            GainSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                TimeUtils.TimeCount(mPayShowSms, 60000, 1000);
//                                ToastUtils.showToast(mActivity, "获取验证码成功");
//                                LogUtil.d("修改支付密码界面 获取验证码" + response.body().toString());
                                mPhoneEit.setText(response.body().getSmsCode());
                            } else if (body.getResultCode() == 0) {
                                mPayShowSms.setEnabled(true);
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                mPayShowSms.setEnabled(true);
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        } else {
                            mPayShowSms.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<GainSmsBean> call, Throwable t) {
                        mPayShowSms.setEnabled(true);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }
}
