package com.jsz.peini.ui.activity.login;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.model.login.LoginSuccess;
import com.jsz.peini.model.login.SanLoginSuccess;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.ui.activity.GuidanceActivity;
import com.jsz.peini.ui.activity.SetIpActivity;
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.ui.activity.login.password.FindPasswordActivity;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseNotSlideActivity {

    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.btn_remove_phone)
    Button btnRemovePhone;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.cb_show_password)
    CheckBox cbShowPassword;
    @InjectView(R.id.btn_remove_password)
    Button btnRemovePassword;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.cb_remember)
    CheckBox cbRemember;

    private LoginActivity mActivity;
    private UMShareAPI mShareAPI;

    private String mPhone;
    private String mPassword;

    /**
     * 三方传递过来的Token
     */
    private String mThirdToken;
    /**
     * 1、QQ；2、微信；3、新浪微博
     */
    private int mPlatform = -1;

    public boolean mB;

    @Override
    public int initLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        mActivity = LoginActivity.this;
        mShareAPI = UMShareAPI.get(this);
        if (!CacheActivity.activityList.contains(LoginActivity.this)) {
            CacheActivity.addActivity(LoginActivity.this);
        }

        //默认不显示
        cbShowPassword.setVisibility(View.GONE);
        btnRemovePassword.setVisibility(View.GONE);
        btnRemovePhone.setVisibility(View.GONE);

        mB = (boolean) SpUtils.get(mActivity, "rememberpassword", true);
        if (mB) {
            String username = (String) SpUtils.get(mActivity, "phone", "");
            String password = (String) SpUtils.get(mActivity, "password", "");
            if (StringUtils.isNoNull(username)) {
                etPhone.setText(username);
                btnRemovePhone.setVisibility(View.VISIBLE);
            } else {
                btnRemovePhone.setVisibility(View.GONE);
            }
            if (StringUtils.isNoNull(password)) {
                etPassword.setText(password);
                cbShowPassword.setVisibility(View.VISIBLE);
                btnRemovePassword.setVisibility(View.VISIBLE);
            } else {
                cbShowPassword.setVisibility(View.GONE);
                btnRemovePassword.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(username) && username.length() == 11
                    && !TextUtils.isEmpty(password) && password.length() >= 6) {
                btnLogin.setEnabled(true);
            } else {
                btnLogin.setEnabled(false);
            }
        } else {
            cbShowPassword.setVisibility(View.GONE);
            btnRemovePassword.setVisibility(View.GONE);
            btnRemovePhone.setVisibility(View.GONE);
        }
        cbRemember.setChecked(mB);

        cbShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SpUtils.put(mActivity, "rememberpassword", b);
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                boolean phoneIsEmpty = editable.length() > 0;
                if (phoneIsEmpty) {
                    btnRemovePhone.setVisibility(View.VISIBLE);
                    if (editable.length() == 11) {

                        String passwordStr = etPassword.getText().toString().trim();//密码
                        if (!TextUtils.isEmpty(passwordStr) && passwordStr.length() >= 6) {
                            if (!btnLogin.isEnabled()) {
                                btnLogin.setEnabled(true);
                            }
                        } else {
                            if (btnLogin.isEnabled()) {
                                btnLogin.setEnabled(false);
                            }
                        }
//                        }
                    } else {
                        if (btnLogin.isEnabled()) {
                            btnLogin.setEnabled(false);
                        }
                    }
                } else {
                    btnRemovePhone.setVisibility(View.GONE);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //设置密码的 是否显示
                boolean pswIsEmpty = etPassword.length() > 0;
                btnRemovePassword.setVisibility(pswIsEmpty ? View.VISIBLE : View.GONE);
                cbShowPassword.setVisibility(pswIsEmpty ? View.VISIBLE : View.GONE);

                String username = etPhone.getText().toString().trim();//用户名
                String password = etPassword.getText().toString().trim();//密码
                if (!TextUtils.isEmpty(username) && username.length() == 11
                        && !TextUtils.isEmpty(password) && password.length() >= 6) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
    }

    @OnClick({R.id.login_toolbar, R.id.btn_login, R.id.weixin, R.id.qq, R.id.weibo, R.id.tv_register, R.id.tv_forget_password,
            R.id.btn_remove_phone, R.id.btn_remove_password, R.id.btn_setIp})
    public void onClick(View view) {
        KeyBoardUtils.hideKeyBoard(mActivity, etPassword);
        switch (view.getId()) {
            case R.id.login_toolbar:
                finish();
                break;
            case R.id.btn_login:
                mPhone = etPhone.getText().toString().trim();
                mPassword = etPassword.getText().toString().trim();
                initLogin();
                btnLogin.setClickable(false);
                KeyBoardUtils.hideKeyBoard(mActivity, etPassword);
                break;
            case R.id.weixin://微信授权
//                doOauthVerify(SHARE_MEDIA.WEIXIN);
                if (!PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "请安装微信后重试!").show();
                    return;
                }
                mDialog.show();
                mShareAPI.getPlatformInfo(mActivity, SHARE_MEDIA.WEIXIN, getGetInfoListener());
                break;
            case R.id.qq://qq授权
                if (!PeiNiUtils.isQQClientAvailable(mActivity)) {
                    Toasty.normal(mActivity, "请安装QQ后重试!").show();
                    return;
                }
//                doOauthVerify(SHARE_MEDIA.QQ);
                mDialog.show();
                mShareAPI.getPlatformInfo(mActivity, SHARE_MEDIA.QQ, getGetInfoListener());
                break;
            case R.id.weibo://微博授权
                if (!PeiNiUtils.isWeiboInstalled(mActivity)) {
                    Toasty.normal(mActivity, "请安装微博后重试!").show();
                    return;
                }
//                doOauthVerify(SHARE_MEDIA.SINA);
                mDialog.show();
                mShareAPI.getPlatformInfo(mActivity, SHARE_MEDIA.SINA, getGetInfoListener());
                break;
            case R.id.tv_register://注册
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.tv_forget_password://忘记密码
                startActivity(new Intent(this, FindPasswordActivity.class));
                break;
            case R.id.btn_remove_phone:
                etPhone.setText("");
                btnRemovePhone.setVisibility(View.GONE);
                break;
            case R.id.btn_remove_password:
                etPassword.setText("");
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                cbShowPassword.setChecked(false);
                break;

            case R.id.btn_setIp: //设置IP
                startActivity(new Intent(this, SetIpActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @NonNull
    private UMAuthListener getGetInfoListener() {
        return new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                mDialog.show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                if (platform.equals(SHARE_MEDIA.QQ)) {
                    mThirdToken = data.get("openid");
                    if (TextUtils.isEmpty(mThirdToken)) {
                        mThirdToken = data.get("uid");
                    }
                    mPlatform = 1;
                } else if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                    mThirdToken = data.get("openid");
                    if (TextUtils.isEmpty(mThirdToken)) {
                        mThirdToken = data.get("uid");
                    }
                    mPlatform = 2;
                } else if (platform.equals(SHARE_MEDIA.SINA)) {
                    mThirdToken = data.get("id");
                    if (TextUtils.isEmpty(mThirdToken)) {
                        mThirdToken = data.get("uid");
                    }
                    mPlatform = 3;
                }
//                doOauthVerify(platform);
                thirdLogin();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Toasty.normal(mActivity, "授权失败").show();
                mDialog.dismiss();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Toasty.normal(mActivity, "授权取消").show();
                mDialog.dismiss();
            }
        };
    }

//    private void doOauthVerify(SHARE_MEDIA share_media) {
//        mShareAPI.doOauthVerify(mActivity, share_media, getDoOauthListener());
//    }
//
//    @NonNull
//    private UMAuthListener getDoOauthListener() {
//        return new UMAuthListener() {
//            @Override
//            public void onStart(SHARE_MEDIA share_media) {
//                mDialog.show();
//            }
//
//            @Override
//            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//                thirdLogin();
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                Toasty.normal(mActivity, "授权失败").show();
//                mDialog.dismiss();
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA share_media, int i) {
//                Toasty.normal(mActivity, "授权取消").show();
//                mDialog.dismiss();
//            }
//        };
//    }

//    private void sanImage(final String value) {
//        if (value != null) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    FutureTarget<File> future = Glide.with(mActivity)
//                            .load(value)
//                            .downloadOnly(100, 100);
//                    try {
//                        File cacheFile = future.get();
//                        SpUtils.put(mActivity, "sanImage", cacheFile.getPath());
//                    } catch (Exception e) {
//                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
//                    }
//                }
//            }).start();
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void initLogin() {
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        RetrofitUtil.createHttpsService(LoginService.class)
                .userLogin(mPhone, MD5Utils.encode(mPassword), Conversion.getRandomappA())
                .enqueue(new Callback<LoginSuccess>() {
                    @Override
                    public void onResponse(Call<LoginSuccess> call, Response<LoginSuccess> response) {
                        if (response.isSuccessful()) {
                            LoginSuccess body = response.body();
                            SpUtils.putServerB(mActivity, body.getServerB());
                            if (body.getResultCode() == 1) {
                                loginSuccessSaveInfoJump(body);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                            mDialog.dismiss();
                            btnLogin.setClickable(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginSuccess> call, Throwable t) {
                        btnLogin.setClickable(true);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        mDialog.dismiss();
                    }
                });
    }

    private void loginSuccessSaveInfoJump(LoginSuccess response) {

        SpUtils.put(mActivity, "phone", mPhone);
        SpUtils.put(mActivity, "password", mPassword);

        if (response.getAddUserInfo() == 1) {
            SpUtils.put(mActivity, "nickname", response.getUserInfo().getNickname() + "");
            SpUtils.put(mActivity, "imageHead", response.getUserInfo().getImageHead() + "");
            SpUtils.put(mActivity, "sex", response.getUserInfo().getSex() + "");
        }
        SpUtils.put(mActivity, "id", response.getUserInfo().getId() + "");
        SpUtils.put(mActivity, "mUserToken", response.getUserToken() + "");
        SpUtils.put(mActivity, "userLoginId", response.getUserInfo().getUserLoginId() + "");
        SpUtils.put(mActivity, "phone", response.getUserInfo().getUserPhone() + "");
        SpUtils.put(mActivity, "addUserInfo", response.getAddUserInfo() + "");

        boolean guidedFlag = SpUtils.isInstalledRightNow(mActivity);
        if (!guidedFlag) {
            Intent intent = new Intent(mActivity, HomeActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        } else {
            Intent intent = new Intent(mActivity, GuidanceActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }

    /*三方登录*/
    private void thirdLogin() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        if (null == mThirdToken) {
            mDialog.dismiss();
            Toasty.normal(mActivity, "获取accessToken失败").show();
            return;
        }
        RetrofitUtil.createHttpsService(LoginService.class)
                .userLoginByThird(mThirdToken, Conversion.getRandomappA())
                .enqueue(new Callback<SanLoginSuccess>() {
                             @Override
                             public void onResponse(Call<SanLoginSuccess> call, Response<SanLoginSuccess> response) {
                                 mDialog.dismiss();
                                 if (response.isSuccessful()) {
                                     SanLoginSuccess body = response.body();
                                     SpUtils.putServerB(mActivity, body.getServerB());
                                     if (body.getResultCode() == 1) {
                                         thirdResult(body);
                                     } else if (body.getResultCode() == 0) {
                                         Toasty.normal(mActivity, body.getResultDesc()).show();
                                     } else {
                                         Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<SanLoginSuccess> call, Throwable t) {
                                 Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                 mDialog.dismiss();
                             }
                         }
                );
    }

    private void thirdResult(SanLoginSuccess loginSuccess) {
        if ("1".equals(loginSuccess.getIsExist())) {
            SpUtils.remove(mActivity, "password");
            thirdLoginSuccess(loginSuccess);
        } else {
            RegisterActivity.actionShow(mActivity, mThirdToken, mPlatform);
        }
    }

    private void thirdLoginSuccess(SanLoginSuccess loginSuccess) {
        LogUtil.d("三方登录返回的信息" + loginSuccess.toString());
        if (loginSuccess.getAddUserInfo() == 1) {
            SpUtils.put(mActivity, "nickname", loginSuccess.getUserInfo().getNickname() + "");
            SpUtils.put(mActivity, "imageHead", loginSuccess.getUserInfo().getImageHead() + "");
            SpUtils.put(mActivity, "sex", loginSuccess.getUserInfo().getSex() + "");
        }
        SpUtils.put(mActivity, "mUserToken", loginSuccess.getUserToken() + "");
        SpUtils.put(mActivity, "id", loginSuccess.getUserInfo().getId() + "");
        SpUtils.put(mActivity, "userLoginId", loginSuccess.getUserInfo().getUserLoginId() + "");
        SpUtils.put(mActivity, "phone", loginSuccess.getUserInfo().getPhone() + "");
        SpUtils.put(mActivity, "addUserInfo", loginSuccess.getAddUserInfo() + "");

        boolean guidedFlag = SpUtils.isInstalledRightNow(mActivity);
        if (!guidedFlag) {
            Intent intent = new Intent(mActivity, HomeActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        } else {
            Intent intent = new Intent(mActivity, GuidanceActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();
        }
    }
}
