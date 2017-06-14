package com.jsz.peini.ui.activity.setting;

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
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/12/6.
 */
public class LoginPasswordActivity extends BaseActivity {
    @InjectView(R.id.login_new_password)
    EditText loginNewPassword;
    @InjectView(R.id.login_news_password)
    EditText loginNewsPassword;
    @InjectView(R.id.ok)
    Button ok;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    private String loginNewPasswordString;
    private String loginNewsPasswordString;
    public String mLoginNewmPassword;
    public LoginPasswordActivity mActivity;

    @Override
    public int initLayoutId() {
        return R.layout.activity_loginpassword;
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
        mTitle.setText("修改登录密码");
    }

    @Override
    public void initData() {
        mPassword.addTextChangedListener(getWatcher());
        loginNewPassword.addTextChangedListener(getWatcher());
        loginNewsPassword.addTextChangedListener(getWatcher());
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
                if (mPassword.length() >= 6 && loginNewPassword.length() >= 6 && loginNewsPassword.length() >= 6) {
                    ok.setBackgroundResource(R.drawable.checkbutton);
                } else {
                    ok.setBackgroundResource(R.drawable.nocheckbutton);
                }
            }
        };
    }


    @OnClick({R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                mLoginNewmPassword = mPassword.getText().toString().trim();
                loginNewPasswordString = loginNewPassword.getText().toString().trim();
                loginNewsPasswordString = loginNewsPassword.getText().toString().trim();
                String password = (String) SpUtils.get(mActivity, "password", "");
                if (TextUtils.isEmpty(password)) {
                    Toasty.normal(mActivity, "密码为空!").show();
                    finish();
                    return;
                }
                if (!password.equals(mLoginNewmPassword)) {
                    Toasty.normal(mActivity, "原始密码输入错误").show();
                    return;
                }
                if (!loginNewPasswordString.equals(loginNewsPasswordString)) {
                    Toasty.normal(mActivity, "密码不一致!").show();
                    return;
                }
                if (loginNewPasswordString.length() < 6 || loginNewsPasswordString.length() < 6) {
//                    请您输入6-23位字符！
                    Toasty.normal(mActivity, "请您输入6-23位字符!").show();
                    return;
                }
                initUpdataPassword(mLoginNewmPassword, loginNewsPasswordString);
                break;
        }
    }

    /*修改密码*/
    private void initUpdataPassword(String loginNewmPassword, final String loginNewsPasswordString) {
        RetrofitUtil.createHttpsService(SettingService.class).resetPass(mUserToken, MD5Utils.encode(loginNewmPassword), MD5Utils.encode(loginNewsPasswordString)).enqueue(new Callback<SuccessfulBean>() {
            @Override
            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful()) {
                    SuccessfulBean body = response.body();
                    if (body.getResultCode() == 1) {
                        LogUtil.d(body.toString());
                        SpUtils.put(mActivity, "password", loginNewsPasswordString);
                        Toasty.success(mActivity, body.getResultDesc()).show();
                        finish();
                    } else if (body.getResultCode() == 9) {
                        LogUtil.d(body.toString());
                    } else {
                        LogUtil.d(body.toString());
                        Toasty.normal(mActivity, body.getResultDesc()).show();
                    }

                } else {
                    LogUtil.d("修改密码失败");
                    Toasty.normal(mActivity, "修改密码失败").show();
                }

            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
            }
        });
    }
}
