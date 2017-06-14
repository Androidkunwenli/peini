package com.jsz.peini.ui.activity.square;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.IDEntityBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdentityAuthenticationActivity extends BaseActivity {

    @InjectView(R.id.et_name)
    EditText mEtName;
    @InjectView(R.id.et_identity_no)
    EditText mEtIdentityNo;
    @InjectView(R.id.btn_confirm)
    Button mBtnConfirm;
    @InjectView(R.id.title)
    TextView mTitle;

    private Activity mContext;

    @Override
    public int initLayoutId() {
        return R.layout.activity_identity_authentication;
    }

    @Override
    public void initView() {
        super.initView();
        mContext = this;
        mTitle.setText("身份认证");
    }

    @Override
    public void initData() {
        super.initData();
        mEtName.addTextChangedListener(mEditViewWatcher);
        mEtIdentityNo.addTextChangedListener(mEditViewWatcher);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.toolbar, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.btn_confirm:
                requestIdentity();
                break;
        }
    }

    private void requestIdentity() {
        String name = mEtName.getText().toString().trim();
        String id = mEtIdentityNo.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toasty.normal(mContext, "请输入姓名!").show();
            return;
        }
        if (TextUtils.isEmpty(id)) {
            Toasty.normal(mContext, "输入身份证号!").show();
            return;
        }
        mDialog.show();
        RetrofitUtil.createService(SquareService.class).isIdcard(mUserToken, name, id).enqueue(new Callback<IDEntityBean>() {
            @Override
            public void onResponse(Call<IDEntityBean> call, Response<IDEntityBean> response) {
                mDialog.dismiss();
                if (response.isSuccessful()) {
                    IDEntityBean body = response.body();
                    if (body.getResultCode() == 1) {
                        LogUtil.d(getLocalClassName(), "身份认证成功---------");
                        Toasty.normal(mContext, response.body().getResultDesc()).show();
                        finish();
                    } else if (body.getResultCode() == 0) {
                        Toasty.normal(mContext, response.body().getResultDesc()).show();
                        LogUtil.d(getLocalClassName(), "身份认证失败");
                    } else if (body.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mContext);
                    } else {
                        Toasty.normal(mContext, Conversion.NETWORKERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<IDEntityBean> call, Throwable t) {
                mDialog.dismiss();
                Toasty.normal(mContext, "身份认证失败, 请稍后重试").show();
                LogUtil.d(getLocalClassName(), "身份认证失败" + t.getMessage());
            }
        });
    }

    private TextWatcher mEditViewWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            mBtnConfirm.setEnabled(verifyName() && verifyIdentityNo());
        }
    };

    private boolean verifyName() {
        String name = mEtName.getText().toString().trim();
        return !TextUtils.isEmpty(name);
    }

    private boolean verifyIdentityNo() {
        String id = mEtIdentityNo.getText().toString().trim();
        return !TextUtils.isEmpty(id);
    }
}
