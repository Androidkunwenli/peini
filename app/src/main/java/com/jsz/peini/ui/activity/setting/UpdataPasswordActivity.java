package com.jsz.peini.ui.activity.setting;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.login.VerificationSmsBean;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.utils.Conversion;
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
 * Created by th on 2017/1/16.
 */
public class UpdataPasswordActivity extends BaseActivity {


    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.phone_text)
    TextView mPhoneText;
    @InjectView(R.id.phone_eit)
    EditText mPhoneEit;
    @InjectView(R.id.pay_show_sms)
    Button mPayShowSms;
    @InjectView(R.id.ok)
    Button mOk;
    public UpdataPasswordActivity mActivity;

    @Override
    public int initLayoutId() {
        return R.layout.activity_updata_password;
    }

    @Override
    public void initView() {
        mActivity = this;
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText("找回手势密码");
        if (StringUtils.isNoNull(SpUtils.getPhone(mActivity))) {
            mPhoneText.setText("正在为" + "" + ZzUtils.phone(SpUtils.getPhone(mActivity)) + "找回手势密码");
        }
        mPhoneEit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mOk.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick({R.id.pay_show_sms, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_show_sms:
                if (!StringUtils.isNoNull(SpUtils.getPhone(mActivity))) {
                    return;
                }
                initNetWorkSms();
                break;
            case R.id.ok:
                String sms = mPhoneEit.getText().toString().trim();
                if (!StringUtils.isNoNull(sms)) {
                    Toasty.normal(mActivity, "验证码为空").show();
                    return;
                }
                initNetWork(sms);
                break;

        }
    }

    private void initNetWork(String sms) {
        RetrofitUtil.createHttpsService(LoginService.class).checkSmsCode(SpUtils.getPhone(mActivity), sms).enqueue(new Callback<VerificationSmsBean>() {
            @Override
            public void onResponse(Call<VerificationSmsBean> call, Response<VerificationSmsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 1) {
                        startActivity(new Intent(mActivity, SetPassword.class));
                        finish();
                    } else {
                        Toasty.normal(mActivity, "您输入的手机验证码不正确！").show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VerificationSmsBean> call, Throwable t) {
            }
        });
    }


    private void initNetWorkSms() {
        mPayShowSms.setEnabled(false);
        RetrofitUtil.createHttpsService(LoginService.class).smsSendFind(SpUtils.getPhone(mActivity)).enqueue(new Callback<GainSmsBean>() {
            @Override
            public void onResponse(Call<GainSmsBean> call, Response<GainSmsBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 1) {
                        TimeUtils.TimeCount(mPayShowSms, 60000, 1000);
                        mPhoneEit.setText(response.body().getSmsCode());
                    } else {
                        mPayShowSms.setEnabled(true);
                        Toasty.normal(mActivity, response.body().getResultDesc()).show();
                    }
                } else {
                    mPayShowSms.setEnabled(true);
                    Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
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
