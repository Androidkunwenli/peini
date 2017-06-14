package com.jsz.peini.ui.activity.login.password;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.login.VerificationSmsBean;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.time.TimeUtils;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangxu on 2016/11/21.
 */
public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.bt_username_clean)
    ImageView mBtUsernameClean;
    private EditText phone_edt;  // 手机号
    private EditText code_edt; // 验证码
    private Button next_btn;
    @InjectView(R.id.find_toolbar)
    LinearLayout find_toolbar;
    Intent intent;
    private Button code_btn;
    private String trim;
    private String trim1;
    private FindPasswordActivity mActivity;


    @Override
    public int initLayoutId() {
        return R.layout.find_password_activity;
    }

    @Override
    public void initView() {
        find_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Init();
    }

    private void Init() {
        mActivity = this;
        phone_edt = (EditText) findViewById(R.id.phone_edt);
        code_edt = (EditText) findViewById(R.id.code_edt);
        next_btn = (Button) findViewById(R.id.next_btn);
        phone_edt.addTextChangedListener(textWatcher);
        code_edt.addTextChangedListener(textWatcher);
        next_btn.setOnClickListener(this);
        code_btn = (Button) findViewById(R.id.code_btn);
        code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = phone_edt.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toasty.normal(mActivity, "请输入您的手机号!").show();
                    return;
                } else if(mobile.length() != 11) {
                    Toasty.normal(mActivity, "您输入的手机号不正确!").show();
                    return;
                }
                gainSms();
            }
        });
        mBtUsernameClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone_edt.setText("");
                mBtUsernameClean.setVisibility(View.GONE);
            }
        });
        mBtUsernameClean.setVisibility(View.GONE);
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
            if (phone_edt.length() > 0) {
                mBtUsernameClean.setVisibility(View.VISIBLE);
            } else {
                mBtUsernameClean.setVisibility(View.GONE);
            }
            if (phone_edt.length() == 11 && code_edt.length() == 6) {
//                next_btn.setBackgroundResource(R.drawable.checkbutton);
                next_btn.setEnabled(true);
            } else {
//                next_btn.setBackgroundResource(R.drawable.nocheckbutton);
                next_btn.setEnabled(false);
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn:
                trim1 = code_edt.getText().toString().trim();
                trim = phone_edt.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    Toasty.normal(mActivity, "请您输入手机号!").show();
                    return;
                }
                if (TextUtils.isEmpty(trim1)) {
                    Toasty.normal(mActivity, "请输入6~23密码!").show();
                    return;
                }
                VerificationSms(trim, trim1);
                break;
            default:
                break;
        }
    }

    private void gainSms() {
        code_btn.setEnabled(false);
        RetrofitUtil.createHttpsService(LoginService.class).smsSendFind(phone_edt.getText().toString().trim())
                .enqueue(new Callback<GainSmsBean>() {
                    @Override
                    public void onResponse(Call<GainSmsBean> call, Response<GainSmsBean> response) {
                        if (response.isSuccessful()) {
                            GainSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, body.getResultDesc()).show();
                                TimeUtils.TimeCount(code_btn, 60000, 1000);
                                code_edt.setText(response.body().getSmsCode());
                            } else if (body.getResultCode() == 0) {
                                code_btn.setEnabled(true);
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                code_btn.setEnabled(true);
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        } else {
                            code_btn.setEnabled(true);
                            Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GainSmsBean> call, Throwable t) {
                        code_btn.setEnabled(true);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /*验证验证码借口*/
    private void VerificationSms(final String trim, final String trim1) {
        RetrofitUtil.createHttpsService(LoginService.class).checkSmsCode(trim, trim1)
                .enqueue(new Callback<VerificationSmsBean>() {
                    @Override
                    public void onResponse(Call<VerificationSmsBean> call, Response<VerificationSmsBean> response) {
                        if (response.isSuccessful()) {
                            VerificationSmsBean body = response.body();
                            if (body.getResultCode() == 1) {
                                intent = new Intent(mActivity, RemountActivity.class);
                                intent.putExtra("phone", trim);
                                startActivity(intent);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        } else {
                            Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<VerificationSmsBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }
}
