package com.jsz.peini.ui.activity.square;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.login.VerificationSmsBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.ZzUtils;
import com.jsz.peini.utils.time.TimeUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/3.
 */
public class PhoneAuthenticationActivity extends BaseActivity {
    @InjectView(R.id.send1)
    Button mSend1;
    @InjectView(R.id.activity_phone_authentication1)
    LinearLayout mActivityPhoneAuthentication1;
    @InjectView(R.id.edittext_cell_phone_number2)
    EditText mEdittextCellPhoneNumber2;
    @InjectView(R.id.send_verification_code2)
    Button mSendVerificationCode2;
    @InjectView(R.id.edittext_verification_code2)
    EditText mEdittextVerificationCode2;
    @InjectView(R.id.send2)
    Button mSend2;
    @InjectView(R.id.activity_phone_authentication2)
    LinearLayout mActivityPhoneAuthentication2;
    @InjectView(R.id.edittext_cell_phone_number3)
    EditText mEdittextCellPhoneNumber3;
    @InjectView(R.id.send_verification_code3)
    Button mSendVerificationCode3;
    @InjectView(R.id.edittext_verification_code3)
    EditText mEdittextVerificationCode3;
    @InjectView(R.id.send3)
    Button mSend3;
    @InjectView(R.id.activity_phone_authentication3)
    LinearLayout mActivityPhoneAuthentication3;
    @InjectView(R.id.phone_text)
    TextView mPhoneText;
    @InjectView(R.id.sendfind_text)
    TextView mSendfindText;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    private PhoneAuthenticationActivity mActivity;
    /**
     * 原手机号
     */
    private String mPhoneNumber2;
    /**
     * 新手机号
     */
    private String mPhoneNumber3;
    private String mCode2;
    private String mCode3;

    @Override
    public int initLayoutId() {
        return R.layout.activity_phone_authentication;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        mTitle.setText("手机认证");
        if (!TextUtils.isEmpty(SpUtils.getPhone(mActivity))) {
            mPhoneText.setText("您的账号已与" + "" + ZzUtils.phone(SpUtils.getPhone(mActivity)) + "绑定");
            mSendfindText.setText("您正在为" + "" + ZzUtils.phone(SpUtils.getPhone(mActivity)) + "更换绑定手机");
        }
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEdittextCellPhoneNumber2.addTextChangedListener(textWatcher);
        mEdittextCellPhoneNumber3.addTextChangedListener(textWatcher);
        mEdittextVerificationCode2.addTextChangedListener(textWatcher);
        mEdittextVerificationCode3.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mPhoneNumber2 = mEdittextCellPhoneNumber2.getText().toString().trim();
            mCode2 = mEdittextVerificationCode2.getText().toString().trim();
            if (mPhoneNumber2.length() != 11 || mCode2.length() != 6) {
                mSend2.setEnabled(false);
                mSend2.setBackgroundResource(R.drawable.nocheckbutton);
            } else {
                mSend2.setEnabled(true);
                mSend2.setBackgroundResource(R.drawable.checkbutton);

            }
            mPhoneNumber3 = mEdittextCellPhoneNumber3.getText().toString().trim();
            mCode3 = mEdittextVerificationCode3.getText().toString().trim();
            if (mPhoneNumber3.length() != 11 || mCode3.length() != 6) {
                mSend3.setEnabled(false);
                mSend3.setBackgroundResource(R.drawable.nocheckbutton);
            } else {
                mSend3.setEnabled(true);
                mSend3.setBackgroundResource(R.drawable.checkbutton);

            }
        }
    };

    @Override
    public void initData() {
        super.initData();
    }

    @OnClick({R.id.send1, R.id.send2, R.id.send3, R.id.send_verification_code2, R.id.send_verification_code3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send1:
                mTitle.setText("更换绑定手机");
                mActivityPhoneAuthentication1.setVisibility(View.GONE);
                mActivityPhoneAuthentication2.setVisibility(View.VISIBLE);
                mActivityPhoneAuthentication3.setVisibility(View.GONE);
                break;
            case R.id.send2:
                mTitle.setText("更换绑定手机");
                mPhoneNumber2 = mEdittextCellPhoneNumber2.getText().toString().trim();
                mCode2 = mEdittextVerificationCode2.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNumber2)) {
                    Toasty.normal(mActivity, "请您先输入原手机号!").show();
                    LogUtil.d(getLocalClassName(), "请您输入手机号!");
                    return;
                } else if (TextUtils.isEmpty(mCode2)) {
                    Toasty.normal(mActivity, "请您输入手机验证码!").show();
                    return;
                } else if (TextUtils.isEmpty(mPhoneNumber2) || mPhoneNumber2.length() != 11) {
                    Toasty.normal(mActivity, "您输入的手机号不正确!").show();
                    return;
                }
                RetrofitUtil.createHttpsService(LoginService.class).checkSmsCode(mPhoneNumber2, mCode2).enqueue(new Callback<VerificationSmsBean>() {
                    @Override
                    public void onResponse(Call<VerificationSmsBean> call, Response<VerificationSmsBean> response) {
                        if (response.isSuccessful()) {
                            VerificationSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                mActivityPhoneAuthentication1.setVisibility(View.GONE);
                                mActivityPhoneAuthentication2.setVisibility(View.GONE);
                                mActivityPhoneAuthentication3.setVisibility(View.VISIBLE);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VerificationSmsBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();

                    }
                });
                break;
            case R.id.send3:
                mTitle.setText("更换绑定手机");
                mPhoneNumber3 = mEdittextCellPhoneNumber3.getText().toString().trim();
                mCode3 = mEdittextVerificationCode3.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNumber3)) {
                    Toasty.normal(mActivity, "请您先输入新手机号!").show();
                    return;
                } else if (TextUtils.isEmpty(mCode3)) {
                    Toasty.normal(mActivity, "请您输入手机验证码!").show();
                    return;
                } else if (TextUtils.isEmpty(mPhoneNumber3) || mPhoneNumber3.length() != 11) {
                    Toasty.normal(mActivity, "您输入的手机号不正确!").show();
                    return;
                }
                RetrofitUtil.createHttpsService(LoginService.class).checkSmsCode(mPhoneNumber3, mCode3).enqueue(new Callback<VerificationSmsBean>() {
                    @Override
                    public void onResponse(Call<VerificationSmsBean> call, Response<VerificationSmsBean> response) {
                        if (response.isSuccessful()) {
                            final VerificationSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, body.getResultDesc() + "正在更换绑定!").show();
                                mDialog.show();
                                RetrofitUtil.createHttpsService(LoginService.class).updateLoginName(SpUtils.getUserToken(mActivity), mPhoneNumber3).enqueue(new RetrofitCallback<SuccessfulBean>() {
                                    @Override
                                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                                        mDialog.dismiss();
                                        if (response.isSuccessful()) {
                                            SuccessfulBean successfulBean = response.body();
                                            if (successfulBean.getResultCode() == 1) {
                                                SpUtils.put(mActivity, "phone", mPhoneNumber3);
                                                Toasty.success(mActivity, body.getResultDesc()).show();
                                                finish();
                                            } else if (successfulBean.getResultCode() == 0) {
                                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                            } else {
                                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                        mDialog.dismiss();
                                    }
                                });
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<VerificationSmsBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
                break;
            case R.id.send_verification_code2: //获取验证码 原手机号
                mPhoneNumber2 = mEdittextCellPhoneNumber2.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNumber2)) {
                    Toasty.normal(mActivity, "请您先输入原手机号!").show();
                    LogUtil.d(getLocalClassName(), "请您输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNumber2) || mPhoneNumber2.length() != 11) {
                    Toasty.normal(mActivity, "您输入的手机号不正确!").show();
                    LogUtil.d(getLocalClassName(), "请输入正确的手机号-");
                    return;
                }
                if (!mPhoneNumber2.equals(SpUtils.getPhone(mActivity))) {
                    Toasty.normal(mActivity, "您输入的手机号不正确!").show();
                    return;
                }
                LogUtil.d(getLocalClassName(), "点击了倒计时的按钮--------2");
                mSendVerificationCode2.setEnabled(false);
                RetrofitUtil.createHttpsService(LoginService.class).smsSendFind(mPhoneNumber2).enqueue(new Callback<GainSmsBean>() {
                    @Override
                    public void onResponse(Call<GainSmsBean> call, Response<GainSmsBean> response) {
                        if (response.isSuccessful()) {
                            GainSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, body.getResultDesc()).show();
                                TimeUtils.TimeCount(mSendVerificationCode2, 60000, 1000);
                                mEdittextVerificationCode2.setText(response.body().getSmsCode());
                            } else if (body.getResultCode() == 0) {
                                mSendVerificationCode2.setEnabled(true);
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                mSendVerificationCode2.setEnabled(true);
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        } else {
                            mSendVerificationCode2.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<GainSmsBean> call, Throwable t) {
                        mSendVerificationCode2.setEnabled(true);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
                break;
            case R.id.send_verification_code3: //获取验证码 新手机号
                mPhoneNumber3 = mEdittextCellPhoneNumber3.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNumber3)) {
                    Toasty.normal(mActivity, "请您先输入新手机号!").show();
                    LogUtil.d(getLocalClassName(), "请您输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNumber3) || mPhoneNumber3.length() != 11) {
                    Toasty.normal(mActivity, "您输入的手机号不正确!").show();
                    return;
                }
                mSendVerificationCode3.setEnabled(false);
                RetrofitUtil.createHttpsService(LoginService.class).smsSendRegister(mPhoneNumber3).enqueue(new Callback<GainSmsBean>() {
                    @Override
                    public void onResponse(Call<GainSmsBean> call, Response<GainSmsBean> response) {
                        if (response.isSuccessful()) {
                            GainSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, body.getResultDesc()).show();
                                TimeUtils.TimeCount(mSendVerificationCode3, 60000, 1000);
                                mEdittextVerificationCode3.setText(response.body().getSmsCode());
                            } else if (body.getResultCode() == 0) {
                                mSendVerificationCode3.setEnabled(true);
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                mSendVerificationCode3.setEnabled(true);
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                            LogUtil.d(getLocalClassName(), mPhoneNumber2 + "发送验证码成功" + response.body().toString());
                        } else {
                            mSendVerificationCode3.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<GainSmsBean> call, Throwable t) {
                        mSendVerificationCode3.setEnabled(true);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
                break;
        }
    }
}

