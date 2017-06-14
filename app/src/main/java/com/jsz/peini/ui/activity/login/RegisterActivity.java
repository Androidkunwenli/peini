package com.jsz.peini.ui.activity.login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.login.LoginSuccess;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.ui.activity.GuidanceActivity;
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.time.TimeUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 注册界面
 * Created by huizhe.ju on 2017/4/27.
 */
public class RegisterActivity extends BaseNotSlideActivity implements NonGestureLockInterface {

    private static final String EXTRA_THIRD_TOKEN_FLAG = "extra_third_token_flag";
    private static final String EXTRA_PLATFORM_FLAG = "extra_platform_flag";

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mTvLogin;
    @InjectView(R.id.et_phone)
    EditText mEtPhone;
    @InjectView(R.id.btn_code)
    Button mBtnCode;
    @InjectView(R.id.et_code)
    EditText mEtCode;
    @InjectView(R.id.et_password)
    EditText mEtPassword;
    @InjectView(R.id.et_inviter_id)
    EditText mEtInviterId;
    @InjectView(R.id.btn_register)
    Button mBtnRegister;
    @InjectView(R.id.btn_remove_phone)
    Button mBtnRemovePhone;
    @InjectView(R.id.btn_remove_code)
    Button mBtnRemoveCode;
    @InjectView(R.id.btn_remove_password)
    Button mBtnRemovePassword;
    @InjectView(R.id.cb_show_password)
    CheckBox mCbShowPassword;

    private Activity mContext;

    private String mPhone;
    private String mPassword;

    /**
     * 三方传递过来的Token
     */
    private String mThirdToken;
    /**
     * 1、QQ；2、微信；3、新浪微博
     */
    private int mPlatform;

    /**
     * @param context    context
     * @param thirdToken 三方传递过来的 mThirdToken
     * @param platform   三方的平台 1、QQ；2、微信；3、新浪微博
     */
    public static void actionShow(Context context, String thirdToken, int platform) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(EXTRA_THIRD_TOKEN_FLAG, thirdToken);
        intent.putExtra(EXTRA_PLATFORM_FLAG, platform);
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        mTitle.setText("注册");
        mTvLogin.setText("登录");
        mThirdToken = getIntent().getStringExtra(EXTRA_THIRD_TOKEN_FLAG);
        mPlatform = getIntent().getIntExtra(EXTRA_PLATFORM_FLAG, -1);
    }

    @Override
    public void initData() {
        mContext = this;
        mEtPhone.addTextChangedListener(mEtPhoneTextWatcher);
        mEtCode.addTextChangedListener(mEtCodeTextWatcher);
        mEtPassword.addTextChangedListener(mEtPasswordTextWatcher);
        mBtnRemovePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPhone.setText("");
            }
        });
        mBtnRemoveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtCode.setText("");
            }
        });
        mBtnRemovePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtPassword.setText("");
            }
        });

        mCbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private TextWatcher mEtPhoneTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mBtnRegister.setEnabled(verifyPhoneNumber() && verifySmsCode() && verifyPassword());
            if (editable.length() != 0) {
                mBtnRemovePhone.setVisibility(View.VISIBLE);
            } else {
                mBtnRemovePhone.setVisibility(View.INVISIBLE);
            }
        }
    };
    private TextWatcher mEtCodeTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mBtnRegister.setEnabled(verifyPhoneNumber() && verifySmsCode() && verifyPassword());
            if (editable.length() != 0) {
                mBtnRemoveCode.setVisibility(View.VISIBLE);
            } else {
                mBtnRemoveCode.setVisibility(View.GONE);
            }
        }
    };
    private TextWatcher mEtPasswordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mBtnRegister.setEnabled(verifyPhoneNumber() && verifySmsCode() && verifyPassword());
            if (editable.length() != 0) {
                mBtnRemovePassword.setVisibility(View.VISIBLE);
                mCbShowPassword.setVisibility(View.VISIBLE);
            } else {
                mBtnRemovePassword.setVisibility(View.GONE);
                mCbShowPassword.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public boolean isGestureLock() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.toolbar, R.id.right_button, R.id.btn_code, R.id.btn_register, R.id.tv_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.right_button:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_code:
                requestSmsCode();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_service:
                startActivity(new Intent(this, ServiceActivity.class));
                break;
        }
    }

    private boolean verifyPhoneNumber() {
        String phone = mEtPhone.getText().toString().trim();
        return !TextUtils.isEmpty(phone) && phone.length() == 11;
    }

    private boolean verifySmsCode() {
        String smsCode = mEtCode.getText().toString().trim();
        return !TextUtils.isEmpty(smsCode) && smsCode.matches("\\d{6}");
    }

    private boolean verifyPassword() {
        String pwd = mEtPassword.getText().toString().trim();
        return !TextUtils.isEmpty(pwd) && pwd.length() >= 6 && pwd.length() <= 23;
    }

    private void requestSmsCode() {
        String phone = mEtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toasty.normal(mContext, "请输入您的手机号!").show();
            return;
        } else if (phone.length() != 11) {
            Toasty.normal(mContext, "您输入的手机号不正确!").show();
            return;
        }
        requestSmsCode(phone);
    }

    private void requestSmsCode(String phone) {
        mBtnCode.setEnabled(false);
        RetrofitUtil.createHttpsService(LoginService.class).smsSendRegister(phone).
                enqueue(new Callback<GainSmsBean>() {
                    @Override
                    public void onResponse(Call<GainSmsBean> call, Response<GainSmsBean> response) {
                        if (response.isSuccessful()) {
                            GainSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                TimeUtils.TimeCount(mBtnCode, 60000, 1000);
                                Toasty.success(mContext, response.body().getResultDesc()).show();
                                mEtCode.setText(response.body().getSmsCode());
                            } else {
                                mBtnCode.setEnabled(true);
                                Toasty.normal(mContext, response.body().getResultDesc()).show();
                            }
                        } else {
                            mBtnCode.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<GainSmsBean> call, Throwable t) {
                        mBtnCode.setEnabled(true);
                        Toasty.normal(mContext, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void register() {
        mPhone = mEtPhone.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        String smsCode = mEtCode.getText().toString().trim();
        String inviterId = mEtInviterId.getText().toString().trim();

        if (!verifyPhoneNumber()) {
            Toasty.normal(mContext, "请您输入手机号!").show();
        } else if (!verifySmsCode()) {
            Toasty.normal(mContext, "请您输入验证码!").show();
        } else if (!verifyPassword()) {
            Toasty.normal(mContext, "请您输入密码!").show();
        } else {
            requestRegister(mPhone, mPassword, smsCode, inviterId);
            mBtnRegister.setClickable(false);
        }
    }

    /**
     * 请求注册
     *
     * @param userPhone    手机号
     * @param userPassword 密码
     * @param smsCode      验证码
     * @param inviterId    邀请人id (选填)
     */
    private void requestRegister(String userPhone, String userPassword, String smsCode, String inviterId) {
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        RetrofitUtil.createHttpsService(LoginService.class)
                .register(userPhone, MD5Utils.encode(userPassword), smsCode, Conversion.getRandomappA(),
                        PeiNiApp.getUniquePsuedoID(), PeiNiApp.getChannelNo(), "1", inviterId, mPlatform, mThirdToken)
                .enqueue(new Callback<LoginSuccess>() {
                    @Override
                    public void onResponse(Call<LoginSuccess> call, Response<LoginSuccess> response) {
                        if (response.isSuccessful()) {
                            LoginSuccess body = response.body();
                            SpUtils.putServerB(mContext, body.getServerB());
                            if (body.getResultCode() == 1) {
                                Toasty.normal(mContext, "注册成功!").show();
                                registerSuccess(body);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mContext, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mContext, Conversion.NETWORKERROR).show();
                            }

                        }
                        mDialog.dismiss();
                        mBtnRegister.setClickable(true);
                    }

                    @Override
                    public void onFailure(Call<LoginSuccess> call, Throwable t) {
                        mDialog.dismiss();
                        mBtnRegister.setClickable(true);
                        Toasty.normal(mContext, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void registerSuccess(LoginSuccess loginSuccess) {
//        SpUtils.put(mContext, "phone", mPhone);
//        SpUtils.put(mContext, "password", mPassword);
        SpUtils.put(mContext, "mUserToken", loginSuccess.getUserToken());
        SpUtils.put(mContext, "id", loginSuccess.getUserInfo().getId() + "");
        SpUtils.put(mContext, "userLoginId", loginSuccess.getUserInfo().getUserLoginId() + "");
        SpUtils.put(mContext, "phone", loginSuccess.getUserInfo().getUserPhone() + "");
        SpUtils.put(mContext, "addUserInfo", loginSuccess.getAddUserInfo() + "");

        boolean guidedFlag = SpUtils.isInstalledRightNow(mContext);
        if (!guidedFlag) {
            Intent intent = new Intent(mContext, HomeActivity.class);
            startActivity(intent);
            mContext.finish();
        } else {
            Intent intent = new Intent(mContext, GuidanceActivity.class);
            startActivity(intent);
            mContext.finish();
        }
    }
}

