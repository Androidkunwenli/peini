package com.jsz.peini.ui.activity.login.password;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.model.login.updataPassword;
import com.jsz.peini.presenter.login.LoginService;
import com.jsz.peini.ui.activity.login.LoginActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ZzUtils;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangxu  on 2016/11/21.
 */
public class RemountActivity extends BaseNotSlideActivity implements View.OnClickListener {
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    private RelativeLayout show_password_layout;
    private RelativeLayout show_password_again_layout;
    private Button password_show;
    private Button password_disappear;
    private Button password_clean;
    private Button password_again_show;
    private Button password_again_disappear;
    private Button password_again_clean;
    private EditText password_edt;
    private EditText password_again_edt;
    private Button confirm_btn;
    Toast toast;
    private Intent intent1;
    private String phone;
    private TextView phone_number_txt;
    private RemountActivity mActivity;


    @Override
    public int initLayoutId() {
        return R.layout.remount_activity;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("找回密码");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Init();
        intent1 = getIntent();
        phone = intent1.getStringExtra("phone");
        phone_number_txt.setText("正在为" + ZzUtils.phone(this.phone) + "重设密码");

    }

    private void Init() {
        show_password_layout = (RelativeLayout) findViewById(R.id.show_password_layout);
        show_password_again_layout = (RelativeLayout) findViewById(R.id.show_password_again_layout);
        password_again_edt = (EditText) findViewById(R.id.password_again_edt);
        password_edt = (EditText) findViewById(R.id.password_edt);
        password_show = (Button) findViewById(R.id.password_show);
        password_disappear = (Button) findViewById(R.id.password_disappear);
        password_clean = (Button) findViewById(R.id.password_clean);
        password_again_show = (Button) findViewById(R.id.password_again_show);
        password_again_disappear = (Button) findViewById(R.id.password_again_disappear);
        password_again_clean = (Button) findViewById(R.id.password_again_clean);
        confirm_btn = (Button) findViewById(R.id.confirm_btn);
        phone_number_txt = (TextView) findViewById(R.id.phone_number_txt);
        confirm_btn.setOnClickListener(this);
        password_show.setOnClickListener(this);
        password_disappear.setOnClickListener(this);
        password_clean.setOnClickListener(this);
        password_again_show.setOnClickListener(this);
        password_again_disappear.setOnClickListener(this);
        password_again_clean.setOnClickListener(this);
        password_edt.addTextChangedListener(textWatcher);
        password_again_edt.addTextChangedListener(textWatcher);
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
            if (password_edt.length() > 0) {
                password_clean.setVisibility(View.VISIBLE);
                show_password_layout.setVisibility(View.VISIBLE);
            } else {
                password_clean.setVisibility(View.GONE);
                show_password_layout.setVisibility(View.GONE);
            }
            if (password_again_edt.length() > 0) {
                password_again_clean.setVisibility(View.VISIBLE);
                show_password_again_layout.setVisibility(View.VISIBLE);
            } else {
                password_again_clean.setVisibility(View.GONE);
                show_password_again_layout.setVisibility(View.GONE);
            }
            if (password_edt.length() >= 6 && password_again_edt.length() >= 6) {
                confirm_btn.setBackgroundResource(R.drawable.checkbutton);
                confirm_btn.setEnabled(true);
            } else {
                confirm_btn.setBackgroundResource(R.drawable.nocheckbutton);
                confirm_btn.setEnabled(false);
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_disappear:
                password_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password_show.setVisibility(View.VISIBLE);
                password_disappear.setVisibility(View.GONE);
                break;
            case R.id.password_show:
                password_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_show.setVisibility(View.GONE);
                password_disappear.setVisibility(View.VISIBLE);
                break;
            case R.id.password_clean:
                password_edt.setText("");
                password_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_show.setVisibility(View.GONE);
                password_disappear.setVisibility(View.VISIBLE);
                break;
            case R.id.password_again_show:
                password_again_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_again_show.setVisibility(View.GONE);
                password_again_disappear.setVisibility(View.VISIBLE);
                break;
            case R.id.password_again_disappear:
                password_again_edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password_again_show.setVisibility(View.VISIBLE);
                password_again_disappear.setVisibility(View.GONE);
                break;
            case R.id.password_again_clean:
                password_again_edt.setText("");
                password_again_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_again_show.setVisibility(View.GONE);
                password_again_disappear.setVisibility(View.VISIBLE);
                break;
            case R.id.confirm_btn:
                String s = password_edt.getText().toString();
                String s1 = password_again_edt.getText().toString();
                if (!s.equals(s1) && !password_again_edt.getText().toString().equals(password_edt.getText().toString())) {
                    toast = Toast.makeText(getApplicationContext(), "您两次密码输入不一致", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                } else if (password_edt.getText().toString().length() < 6 && password_again_edt.getText().toString().length() < 6) {
                    toast = Toast.makeText(getApplicationContext(), "请输入6-23位密码", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                VerificationSms(s1);
                break;
            default:
                break;
        }
    }

    /*修改密码接口*/
    private void VerificationSms(String s1) {
        RetrofitUtil.createHttpsService(LoginService.class).updatePassword(phone, MD5Utils.encode(s1))
                .enqueue(new Callback<updataPassword>() {
                    @Override
                    public void onResponse(Call<updataPassword> call, Response<updataPassword> response) {
                        if (response.isSuccessful()) {
                            updataPassword body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                LogUtil.i("获取修改密码", "成功,跳转登录界面");

                                Intent intent = new Intent(mActivity, LoginActivity.class);
                                startActivity(intent);
                                mActivity.finish();
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }
                        } else {
                            Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<updataPassword> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }
}
