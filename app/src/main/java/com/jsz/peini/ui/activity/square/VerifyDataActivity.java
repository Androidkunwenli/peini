package com.jsz.peini.ui.activity.square;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.listener.OnPasswordInputFinish;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.pay.PayJinBiOrderIdStrBean;
import com.jsz.peini.model.square.VerifyDataBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.PayService;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.setting.PayPasswordActivity;
import com.jsz.peini.ui.view.PasswordView;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.ZzUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/21.
 */
public class VerifyDataActivity extends BaseNotSlideActivity {
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.imageHead)
    CircleImageView mImageHead;
    @InjectView(R.id.sex)
    ImageView mSex;
    @InjectView(R.id.headImg)
    ImageView mHeadImg;
    @InjectView(R.id.provinceNamecityName)
    TextView mProvinceNamecityName;
    @InjectView(R.id.userID)
    TextView mUserID;
    @InjectView(R.id.userPhone)
    TextView mUserPhone;
    @InjectView(R.id.relation)
    TextView mRelation;
    @InjectView(R.id.pay_donation)
    Button mPayDonation;
    @InjectView(R.id.verifydata)
    FrameLayout mVerifydata;
    @InjectView(R.id.gold_numbar)
    EditText mGoldNumbar;
    @InjectView(R.id.nickName)
    TextView mNickName;
    @InjectView(R.id.et_gold_context)
    EditText mEtGoldContext;
    @InjectView(R.id.tv_gold_context_index)
    TextView mTvGoldContextIndex;

    /*用户id或者手机号 */
    private String mOther;
    /*他人id*/
    private String mOtherId;
    /*金币数量*/
    private String mNumbar;
    /*上下文*/
    private VerifyDataActivity mActivity;
    /*核实信息数据*/
    private VerifyDataBean mDataBean;
    /*支付返回的数据*/
    private PayJinBiOrderIdStrBean mPayJinBiOrderIdStrBean;
    private String mUserId;
    private String mStrPassword;
    private Popwindou mPopwindou;
    private String mPayId;
    private String intentStringExtra;
    private String mOrderids;
    private String mNumbars;
    private Intent mIntent;
    private PasswordView mPasswordView;
    private VerifyDataBean mMDataBean;
    private String mTaskid;
    private String mUserToken;

    @Override
    public int initLayoutId() {
        return R.layout.activity_verifydata;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("核实信息");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        //他人ID
        mOther = intent.getStringExtra("otherId");
        //userId
        mOtherId = intent.getStringExtra("userId");
        //信息
        mMDataBean = (VerifyDataBean) intent.getParcelableExtra("mDataBean");
        /**
         *   mIntent.putExtra(Conversion.ORDERIDS, orderId);
         mIntent.putExtra(Conversion.NUMBAR, payMoney);
         账单明细跳转过来的数据
         */
        mOrderids = intent.getStringExtra(Conversion.ORDERIDS);
        mNumbars = intent.getStringExtra(Conversion.NUMBAR);

        mTaskid = intent.getStringExtra(Conversion.TASKID);

        mUserToken = SpUtils.getUserToken(mActivity);
    }


    /*输入密码转账*/
    private void showPassword() {
        mPopwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_paythebill));
        View view = UiUtils.inflate(mActivity, R.layout.pop_passwordview);
        mPopwindou.init(view, Gravity.BOTTOM, true);
        mPasswordView = (PasswordView) view.findViewById(R.id.pay_passwordpop);
        mPasswordView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                mStrPassword = mPasswordView.getStrPassword();
                PayJinBiData();
            }
        });
        mPasswordView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePassword(mPasswordView);
                mPopwindou.dismiss();
            }
        });
        view.findViewById(R.id.passwordview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePassword(mPasswordView);
                mPopwindou.dismiss();
            }
        });
    }

    /**
     * 删除输入的密码
     */
    private void deletePassword(PasswordView passwordView) {
        passwordView.currentIndex = -1;
        for (int i = 0; i < passwordView.tvList.length; i++) {
            passwordView.tvList[i].setText("");
        }
    }

    /**
     * 显示布局
     */
    private void showView() {
        VerifyDataBean.DataBean data = mDataBean.getData();
        String age = data.getAge();
        String cityName = data.getCityName();
        String headImg = data.getHeadImg();
        String provinceName = data.getProvinceName();
        String relation = data.getRelation();
        String sex = data.getSex();
        String userID = data.getID();
        String userPhone = data.getUserPhone();
        String nickName = data.getNickName();
        LogUtil.d("核实信息--" + data.toString());

        mNickName.setText(nickName);
        mUserId = data.getUserId();
        //背景图片
        String spaceBgImg = data.getSpaceBgImg();
        if (StringUtils.isNoNull(spaceBgImg)) {
            if (spaceBgImg.contains("sysBgImgs")) {
                System.out.println("包含");
                GlideImgManager.loadImage(mActivity, IpConfig.HttpPeiniIp + spaceBgImg, mHeadImg, "5");
            } else if (spaceBgImg.contains("spaceBgImg")) {
                GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + spaceBgImg, mHeadImg, "5");
                System.out.println("不包含");
            } else {

            }
        }
        //头像
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + headImg, mImageHead, sex);
        //城市
        mProvinceNamecityName.setText(age + "岁 " + provinceName + " " + cityName);
        //性别
        switch (sex) {
            case "1":
                mSex.setBackgroundResource(R.mipmap.sqnan);
                break;
            case "2":
                mSex.setBackgroundResource(R.mipmap.sqnv);
                break;
        }
        //id号码
        mUserID.setText(userID);
        //手机号码
        mUserPhone.setText(ZzUtils.phone(userPhone));
        //关系
        mRelation.setText(relation);
    }

    @Override
    public void initData() {
        if (null != mMDataBean && !TextUtils.isEmpty(mOther)) {
            mDataBean = mMDataBean;
            showView();
            return;
        }
        initNetWork();
    }

    private void initNetWork() {
        if (!TextUtils.isEmpty(mOrderids)) {
            mGoldNumbar.setEnabled(false);
            mGoldNumbar.setText(mNumbars);
            mPayId = mOrderids;
            mNumbar = mNumbars;
        }
        mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .payGoldSureUserInfo(mUserToken, mOtherId, mOther, mTaskid)
                .enqueue(new RetrofitCallback<VerifyDataBean>() {
                    @Override
                    public void onSuccess(Call<VerifyDataBean> call, Response<VerifyDataBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            mDataBean = response.body();
                            if (mDataBean.getResultCode() == 1) {
                                LogUtil.d("核实信息" + mDataBean.toString());
                                showView();
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
                        mDialog.dismiss();
                    }
                });
    }

    @OnClick(R.id.pay_donation)
    public void onClick() {
        if (TextUtils.isEmpty(mOrderids)) {
            mNumbar = mGoldNumbar.getText().toString().trim();
            if (TextUtils.isEmpty(mNumbar)) {
                Toasty.normal(mActivity, "请输入转账金额!").show();
                return;
            }

            mDialog.show();
            RetrofitUtil.createHttpsService(PayService.class)
                    .donation(mUserToken, mUserId, Conversion.getToken(), Conversion.getNetAppA(), mNumbar)
                    .enqueue(new RetrofitCallback<PayJinBiOrderIdStrBean>() {
                        @Override
                        public void onSuccess(Call<PayJinBiOrderIdStrBean> call, Response<PayJinBiOrderIdStrBean> response) {
                            mDialog.dismiss();
                            if (response.isSuccessful()) {
                                mPayJinBiOrderIdStrBean = response.body();
                                if (null != mPayJinBiOrderIdStrBean && mPayJinBiOrderIdStrBean.getData() != null) {
                                    SpUtils.putServerB(mActivity, mPayJinBiOrderIdStrBean.getData().getServerB());
                                    if (mPayJinBiOrderIdStrBean.getResultCode() == 1) {
                                        mPayId = mPayJinBiOrderIdStrBean.getData().getPayId();
                                        showPassword();
                                    } else if (mPayJinBiOrderIdStrBean.getResultCode() == 9) {
                                        LoginDialogUtils.isNewLogin(mActivity);
                                    } else if (mPayJinBiOrderIdStrBean.getResultCode() == 0) {
                                        Toasty.normal(mActivity, mPayJinBiOrderIdStrBean.getResultDesc()).show();
                                    } else if (mPayJinBiOrderIdStrBean.getResultCode() == 8) {
                                        showDalog();
                                    } else if (mPayJinBiOrderIdStrBean.getResultCode() == 99) {
                                        showRecharge(mPayJinBiOrderIdStrBean.getResultDesc());
                                    } else {
                                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<PayJinBiOrderIdStrBean> call, Throwable t) {
                            Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            mDialog.dismiss();
                        }
                    });

        } else {
            showPassword();
        }
    }

    private void showRecharge(String resultDesc) {
        switch (resultDesc) {
            case "金币余额不足":
                new AlertDialog.Builder(mActivity)
                        .setTitle("温馨提示")
                        .setMessage("您的剩余金币不足以支付，请充值或选择其他方式付款！")
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("充值", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(mActivity, RechargeActivity.class));
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            default:
                break;
        }

    }

    /**
     * 是否有支付密码
     */
    private void showDalog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("您还没有支付密码,请先设置支付密码.")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mActivity, PayPasswordActivity.class));
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 金币支付
     */
    private void PayJinBiData() {
        mDialog.show();
        RetrofitUtil.createHttpsService(PayService.class)
                .payGoldPrePay(Conversion.getToken(), Conversion.getNetAppA(), mPayId, MD5Utils.encode(mStrPassword))
                .enqueue(new Callback<PayJinBiOrderIdStrBean>() {
                    @Override
                    public void onResponse(Call<PayJinBiOrderIdStrBean> call, Response<PayJinBiOrderIdStrBean> response) {
                        mDialog.dismiss();
                        mPopwindou.dismiss();
                        if (response.isSuccessful()) {
                            PayJinBiOrderIdStrBean body = response.body();
                            if (null != body) {
                                SpUtils.putServerB(mActivity, body.getData().getServerB());
                                if (body.getResultCode() == 1) {
                                    deletePassword(mPasswordView);
                                    Intent intent = new Intent(mActivity, RewardSuccessActivity.class);
                                    intent.putExtra(Conversion.ORDERID, body.getOrderId());
                                    intent.putExtra(Conversion.TYPE, TextUtils.isEmpty(mTaskid) ? "1" : "");
                                    startActivity(intent);
                                    finish();
                                } else if (body.getResultCode() == 0) {
                                    deletePassword(mPasswordView);
                                    Toasty.normal(mActivity, body.getResultDesc()).show();
                                } else if (mPayJinBiOrderIdStrBean.getResultCode() == 8) {
                                    showDalog();
                                    deletePassword(mPasswordView);
                                } else {
                                    deletePassword(mPasswordView);
                                    Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<PayJinBiOrderIdStrBean> call, Throwable t) {
                        mDialog.dismiss();
                        mPopwindou.dismiss();
                        deletePassword(mPasswordView);
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    @Override
    protected void initListener() {
        mImageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserId.equals(mUserToken)) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra(Conversion.USERID, mUserId);
                    startActivity(mIntent);
                }
            }
        });
        TextWatcher mWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contextIndex = s.length() + "/54";
                mTvGoldContextIndex.setText(contextIndex);
            }
        };
        mEtGoldContext.addTextChangedListener(mWatcher);
    }
}
