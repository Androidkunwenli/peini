package com.jsz.peini.ui.activity.pay;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.OnPasswordInputFinish;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.pay.PayJinBiOrderIdStrBean;
import com.jsz.peini.model.pay.PayOrderIdStrBean;
import com.jsz.peini.model.pay.PaySuccessfulBean;
import com.jsz.peini.model.pay.SellerDiscountBean;
import com.jsz.peini.model.pay.WeiXinPayOrderIdStrBean;
import com.jsz.peini.model.square.MiWealthABean;
import com.jsz.peini.presenter.PayService;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.seller.CouponActivity;
import com.jsz.peini.ui.activity.setting.PayPasswordActivity;
import com.jsz.peini.ui.activity.square.RechargeActivity;
import com.jsz.peini.ui.view.PasswordView;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.pay.PayResult;
import com.jsz.peini.widget.CashierInputFilter;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaythebillActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
    private static final int REQUEST_CODE_FOR_COUPON_ID = 3;
    private static final String WX_RESULT_EXT_DATA_FLAG_PAY_BILL = "wx_result_ext_data_flag_pay_bill";

    @InjectView(R.id.pay_checkBox)
    CheckBox payCheckBox;           //是否有不参与优惠的金额
    @InjectView(R.id.pay_edt)
    EditText payEdt;                //总额
    @InjectView(R.id.pay_hui_edt)
    EditText payHuiEdt;            //不参与优惠的金额
    @InjectView(R.id.pay_ispreferential)
    LinearLayout payIspreferential;
    @InjectView(R.id.radiogroup_ItcSelect)
    RadioGroup mitcSelect;
    @InjectView(R.id.pay_preferentialText)
    TextView payPreferential;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.tv_money_discounted)
    TextView mTvMoneyDiscounted;
    @InjectView(R.id.tv_full_money_sign)
    TextView mTvFullMoneySign;
    @InjectView(R.id.tv_not_discount_money_sign)
    TextView mTvNotDiscountMoneySign;
    @InjectView(R.id.tv_discount_gold)
    TextView mTvDiscountGold;
    @InjectView(R.id.tv_discount_weChatPay)
    TextView mTvDiscountWeChatPay;
    @InjectView(R.id.tv_discount_AliPay)
    TextView mTvDiscountAliPay;

    private int pay = 1;
    private PaythebillActivity mActivity;
    public String mStrPassword;
    public Intent mIntent;
    private String mPayNum;
    private String mPayHuiNum;
    private String mOrderIdStr;
    private String mPayId;
    private String mCouponId = "";
    private String mCouponMoney = "0";
    private int mGoldDiscount = 100;
    private int mWeChatDiscount = 100;
    private int mAliPayDiscount = 100;
    private BigDecimal mMoneyDiscounted;
    /**
     * 任务支付=====商家支付
     * 2  ==========  1
     */
    private String mType = "";
    public String mSellerInfoId = "";
    private String mSellerinfoname = "";
    private String mUserId = "";
    private String mOrderId = "";
    private String mMOthersrnmae = "";
    private String mTaskId = "";

    private WeixinResultReceiver mReceiver;
    private String mRangid;
    private String mGetId;
    private PasswordView mPasswordView;
    private Popwindou mPopwindou;
    private BigDecimal mGold;

    private AlertDialog mAlertDialog;

    @Override
    public int initLayoutId() {
        return R.layout.activity_paythebill;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("优惠买单");
        Intent intent = getIntent();
        mType = intent.getStringExtra(Conversion.TYPE);
        if ("1".equals(mType)) {
            //店铺id
            mSellerInfoId = intent.getStringExtra(Conversion.SELLERINFOID);
            //商家名字
            mSellerinfoname = intent.getStringExtra(Conversion.SELLERNMAE);
        } else {
            //店铺id
            mSellerInfoId = intent.getStringExtra(Conversion.SELLERINFOID);
            //商家名字
            mSellerinfoname = intent.getStringExtra(Conversion.SELLERNMAE);
            //任务id
            mTaskId = intent.getStringExtra(Conversion.TASKID);
            //他人token
            mUserId = intent.getStringExtra(Conversion.USERID);
            //他人mane
            mMOthersrnmae = intent.getStringExtra(Conversion.OTHERSRNMAE);
            //订单id
            mOrderId = intent.getStringExtra(Conversion.ORDERID);
        }
        LogUtil.d("评价的id==" + mTaskId + "他人token==" + mUserId + "商家名字==" + mSellerinfoname + "他人name==" + mMOthersrnmae + "订单id==" + mOrderId + "====" + mSellerInfoId);

        mReceiver = new WeixinResultReceiver();
        registerReceiver(mReceiver, new IntentFilter(Conversion.WX_RESULT));

        PayWay();//初始化支付方式

        payEdt.setFilters(new CashierInputFilter[]{new CashierInputFilter()});
        payHuiEdt.setFilters(new CashierInputFilter[]{new CashierInputFilter()});
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        PaySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void initData() {
        //判断需不需要优惠信息
        payCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    String payStr = payEdt.getText().toString().trim();
                    if (TextUtils.isEmpty(payStr) || Float.parseFloat(payStr) <= 0) {
                        payCheckBox.setChecked(false);
                        Toasty.normal(mActivity, "请先输入消费总金额").show();
                    } else {
                        payIspreferential.setVisibility(View.VISIBLE);
                    }
                } else {
                    payIspreferential.setVisibility(View.GONE);
                    payHuiEdt.setText(null);
                    mPayHuiNum = "";
                }
            }
        });
        payEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String payEdtNumbar = payEdt.getText().toString().trim();
                if (charSequence.length() == 0) {
                    mTvFullMoneySign.setVisibility(View.GONE);
                    payEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    if (payCheckBox.isChecked()) {
                        payCheckBox.setChecked(false);
                        Toasty.normal(mActivity, "请先输入消费总金额").show();
                    }
                } else {
                    if (payEdtNumbar.startsWith(".")) {
                        payEdtNumbar = "0" + payEdtNumbar;
                    }
                    BigDecimal bigDecimal;
                    try {
                        bigDecimal = new BigDecimal(payEdtNumbar);
                    } catch (NumberFormatException e) {
                        Toasty.normal(mActivity, "请输入正确的金额").show();
                        return;
                    }

                    if (mTvFullMoneySign.getVisibility() != View.VISIBLE) {
                        mTvFullMoneySign.setVisibility(View.VISIBLE);
                        payEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
                    }
//                    if (pay == 1 && !TextUtils.isEmpty(String.valueOf(mGold)) && bigDecimal.compareTo(mGold) == 1) {
//                        showRecharge("金币余额不足");
//                    }
                    if (payCheckBox.isChecked()) {
                        String payHuiStr = payHuiEdt.getText().toString().trim();
                        if (!TextUtils.isEmpty(payHuiStr)
                                && Float.parseFloat(charSequence.toString()) < Float.parseFloat(payHuiStr)) {
                            Toasty.normal(mActivity, "不参与优惠金额不可大于消费总金额").show();
                            payCheckBox.setChecked(false);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvMoneyDiscounted.setText(Conversion.setFormatNum(calcDiscountedMoney()));
            }
        });

        payHuiEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    mTvNotDiscountMoneySign.setVisibility(View.GONE);
                    payHuiEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                } else {
                    String payStr = payEdt.getText().toString().trim();
                    if (TextUtils.isEmpty(payStr) || Float.parseFloat(payStr) <= 0) {
                        payCheckBox.setChecked(false);
                        return;
                    }
                    if (Float.parseFloat(payStr) >= Float.parseFloat(charSequence.toString())) {
                        if (mTvNotDiscountMoneySign.getVisibility() != View.VISIBLE) {
                            mTvNotDiscountMoneySign.setVisibility(View.VISIBLE);
                            payHuiEdt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
                        }
                    } else {
                        Toasty.normal(mActivity, "不参与优惠金额不可大于消费总金额").show();
                        KeyBoardUtils.hideKeyBoard(mActivity, payHuiEdt);
                        payHuiEdt.clearFocus();
                        payHuiEdt.setText(null);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTvMoneyDiscounted.setText(Conversion.setFormatNum(calcDiscountedMoney()));
            }
        });
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("询问服务员后输入");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        payEdt.setHint(new SpannedString(ss));
        payHuiEdt.setHint(new SpannedString(ss));
        getSellerDiscount();
    }

    private String calcDiscountedMoney() {

        BigDecimal moneyOriginal;
        BigDecimal moneyNotDiscount;
        mPayNum = payEdt.getText().toString().trim();
        mPayHuiNum = payHuiEdt.getText().toString().trim();

        try {
            if (TextUtils.isEmpty(mPayNum)) {
                mPayNum = "0";
                return "0";
            }
            moneyOriginal = new BigDecimal(mPayNum);
            if (TextUtils.isEmpty(mPayHuiNum)) {
                mPayHuiNum = "0";
            }
            moneyNotDiscount = new BigDecimal(mPayHuiNum);
        } catch (NumberFormatException e) {
            Toasty.normal(mActivity, "金额输入有误").show();
            return "0";
        }

        int discountRate = 100;
        switch (pay) {
            case 1:
                discountRate = mGoldDiscount;
                break;
            case 2:
                discountRate = mWeChatDiscount;
                break;
            case 3:
                discountRate = mAliPayDiscount;
                break;
            default:
                break;
        }

        BigDecimal resultBd;
        resultBd = moneyOriginal.subtract(moneyNotDiscount);
        mMoneyDiscounted = resultBd.multiply(new BigDecimal(discountRate))
                .divide(new BigDecimal(100), 2, RoundingMode.UP)
                .add(moneyNotDiscount);

        if (!TextUtils.isEmpty(mCouponId)) {
            mCouponId = "";
            mCouponMoney = "0";
            mGetId = "";
            payPreferential.setText(null);
            Toasty.normal(mActivity, "请重新选择优惠券").show();
        }

        if (mMoneyDiscounted.signum() == -1) {
            mMoneyDiscounted = new BigDecimal(0);
        }
//        return getFormatResult(mMoneyDiscounted);
        return mMoneyDiscounted.toString();
    }

    private BigDecimal calcCorrectDiscountedMoney() {

        BigDecimal moneyOriginal;
        BigDecimal moneyNotDiscount;
        mPayNum = payEdt.getText().toString().trim();
        mPayHuiNum = payHuiEdt.getText().toString().trim();

        try {
            if (TextUtils.isEmpty(mPayNum)) {
                mPayNum = "0";
                return new BigDecimal(0);
            }
            moneyOriginal = new BigDecimal(mPayNum);
            if (TextUtils.isEmpty(mPayHuiNum)) {
                mPayHuiNum = "0";
            }
            moneyNotDiscount = new BigDecimal(mPayHuiNum);
        } catch (NumberFormatException e) {
            return new BigDecimal(0);
        }

        int discountRate = 100;
        switch (pay) {
            case 1:
                discountRate = mGoldDiscount;
                break;
            case 2:
                discountRate = mWeChatDiscount;
                break;
            case 3:
                discountRate = mAliPayDiscount;
                break;
            default:
                break;
        }

        BigDecimal resultBd = moneyOriginal.subtract(moneyNotDiscount)
                .multiply(new BigDecimal(discountRate))
                .divide(new BigDecimal(100), 2, RoundingMode.UP)
                .add(moneyNotDiscount);

        if (resultBd.signum() == -1) {
            resultBd = new BigDecimal(0);
        }
        return resultBd;
    }

    public String getFormatResult(BigDecimal numberIn) {
        int scale = 2;//设置位数
        RoundingMode roundingMode = RoundingMode.UP;
        BigDecimal bd = numberIn;
        bd = bd.setScale(scale, roundingMode);
        return bd.toString();
    }

    private void PayWay() {
        mitcSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.pay_jinbi:
                        LogUtil.d("金币支付");
                        pay = 1;
                        break;
                    case R.id.pay_weixin:
                        LogUtil.d("微信支付");
                        pay = 2;
                        break;
                    case R.id.pay_zhifubao:
                        LogUtil.d("支付宝支付");
                        pay = 3;
                        break;
                }
                mTvMoneyDiscounted.setText(Conversion.setFormatNum(calcDiscountedMoney()));
            }
        });
    }

    @OnClick({R.id.toolbar, R.id.pay_confirm, R.id.pay_preferential})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.pay_confirm:
                KeyBoardUtils.hideKeyBoard(mActivity, payHuiEdt);
                mPayNum = payEdt.getText().toString().trim();
                mPayHuiNum = payHuiEdt.getText().toString().trim();
                String discountedNum = mTvMoneyDiscounted.getText().toString().trim();
                if (TextUtils.isEmpty(mPayNum) || "0".equals(mPayNum) || mPayNum.startsWith("-")) {
                    Toasty.normal(mActivity, "请输入金额").show();
                    return;
                } else if (pay == 0) {
                    Toasty.normal(mActivity, "请选择支付方式").show();
                    return;
                } else if (pay == 2 && !PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "您尚未安装微信!").show();
                    return;
                } else if (pay == 1 && !TextUtils.isEmpty(String.valueOf(mGold)) && new BigDecimal(discountedNum).compareTo(mGold) == 1) {
                    showRecharge("金币余额不足");
                    return;
                } else if (pay != 1 && new BigDecimal(discountedNum).compareTo(new BigDecimal(0)) != 1) {
                    Toasty.normal(mActivity, "微信支付宝暂不支持支付0元!").show();
                    return;
                } else if (!SpUtils.getFirstPay(mActivity)) {
                    isFirstPay();
                    return;
                }
                PayData();
                break;
            case R.id.pay_preferential:
                mPayNum = payEdt.getText().toString().trim();

                if (TextUtils.isEmpty(mPayNum) || pay == 0) {
                    Toasty.normal(mActivity, "请先输入消费总额，并选择支付方式").show();
                    return;
                }

                CouponActivity.actionShow(this, REQUEST_CODE_FOR_COUPON_ID, mSellerInfoId, mPayNum,
                        mPayHuiNum, String.valueOf(pay), mGetId);
                break;
        }
    }

    /**
     * 第一次支付
     */
    private boolean mIsChecked;

    private void isFirstPay() {
        View contentView = LayoutInflater.from(mActivity).inflate(
                R.layout.toast_pay_kindlyreminder, null);
        contentView.setFocusable(true);
        contentView.setFocusableInTouchMode(true);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        ColorDrawable dw = new ColorDrawable(0x99000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(mActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        CheckBox mIsFirstPay = (CheckBox) contentView.findViewById(R.id.cb_isfirst_pay);
        TextView mTvDetermine = (TextView) contentView.findViewById(R.id.tv_determine);
        TextView mTvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);
        mIsChecked = false;
        mIsFirstPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mIsChecked = isChecked;
            }
        });
        mTvDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //确认
                SpUtils.put(mActivity, "FirstPay", mIsChecked);
                KeyBoardUtils.hideKeyBoard(mActivity, payHuiEdt);
                popupWindow.dismiss();
                PayData();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //取消
                SpUtils.put(mActivity, "FirstPay", false);
                KeyBoardUtils.hideKeyBoard(mActivity, payHuiEdt);
                popupWindow.dismiss();
//                PayData();
            }
        });
    }

    /**
     * 是否有支付密码
     */
    private void showDialog() {
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

    private void initPasswordView() {
        mPopwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_paythebill));
        View view = UiUtils.inflate(mActivity, R.layout.pop_passwordview);
        mPopwindou.init(view, Gravity.BOTTOM, true);
        mPasswordView = (PasswordView) view.findViewById(R.id.pay_passwordpop);
        mPasswordView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                mStrPassword = mPasswordView.getStrPassword();
                if (mStrPassword.length() == 6) {
                    PayJinBiData(mPayId);
                }
                mPopwindou.dismiss();

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
     * 支付的接口的调用
     */
    private void PayData() {
        LogUtil.d("mUserToken=" + mUserToken +
                "\nmTaskid=" + mTaskId +
                "\nmSellerInfoId=" + mSellerInfoId +
                "\nmPayNum=" + mPayNum +
                "\nmPayHuiNum=" + mPayHuiNum +
                "\npay=" + pay +
                "\nmCouponId=" + mCouponId);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        RetrofitUtil.createHttpsService(PayService.class)
                .saveTaskOrder(
                        mUserToken + "",
                        mTaskId + "",
                        mSellerInfoId + "",
                        mPayNum + "",
                        mPayHuiNum + "",
                        pay + "",
                        mCouponId + ""
                        , mRangid)
                .enqueue(new Callback<PaySuccessfulBean>() {
                    @Override
                    public void onResponse(Call<PaySuccessfulBean> call, Response<PaySuccessfulBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            PaySuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, "订单提交成功").show();
                                mOrderIdStr = body.getOrderIdStr();
                                mOrderId = mOrderIdStr + "";
                                if (pay == 1) {
                                    getJInBiOrder(mOrderIdStr);
                                } else if (pay == 2) {
                                    getWeiXinOrder(mOrderIdStr);
                                } else if (pay == 3) {
                                    getZhiFuBaoOrder(mOrderIdStr);
                                }
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 8) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                showDialog();
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PaySuccessfulBean> call, Throwable t) {
                        mDialog.dismiss();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 支付宝预支付接口
     */
    private void getZhiFuBaoOrder(String orderIdStr) {
        RetrofitUtil.createHttpsService(PayService.class)
                .payAli(mUserToken, orderIdStr)
                .enqueue(new Callback<PayOrderIdStrBean>() {
                    @Override
                    public void onResponse(Call<PayOrderIdStrBean> call, Response<PayOrderIdStrBean> response) {
                        if (response.isSuccessful()) {
                            PayOrderIdStrBean body = response.body();
                            if (body.getResultCode() == 1) {
                                LogUtil.d("支付宝预支付接口返回的数据" + body.toString());
                                ZhiFuBaoPay(body.getData().getPayInfo());
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayOrderIdStrBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGold();//获取金币数量
        if (null != mPopwindou) {
            mPopwindou.dismiss();
        }
        KeyBoardUtils.hideKeyBoard(mActivity, payEdt);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mPopwindou) {
            mPopwindou.dismiss();
        }
    }

    /**
     * 微信预支付
     */
    private void getWeiXinOrder(String orderIdStr) {
        RetrofitUtil.createHttpsService(PayService.class)
                .payWx(mUserToken, orderIdStr)
                .enqueue(new RetrofitCallback<WeiXinPayOrderIdStrBean>() {
                    @Override
                    public void onSuccess(Call<WeiXinPayOrderIdStrBean> call, Response<WeiXinPayOrderIdStrBean> response) {
                        if (response.isSuccessful()) {
                            WeiXinPayOrderIdStrBean body = response.body();
                            if (body.getResultCode() == 1) {
                                LogUtil.d("微信预支付接口返回的数据" + body.toString());
                                WeiXinBaoPay(body);
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, "支付失败").show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeiXinPayOrderIdStrBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 微信支付接口会掉
     *
     * @param payInfo
     */
    private void WeiXinBaoPay(WeiXinPayOrderIdStrBean payInfo) {
        WeiXinPayOrderIdStrBean.DataBean data = payInfo.getData();
        IWXAPI wxapi = WXAPIFactory.createWXAPI(mActivity, PeiNiApp.WXAPIPAY);
        wxapi.registerApp(PeiNiApp.WXAPIPAY);
        PayReq payReq = new PayReq();
        payReq.appId = PeiNiApp.WXAPIPAY;
        payReq.partnerId = data.getPartnerid();
        payReq.prepayId = data.getPrepayid();
        payReq.packageValue = data.getPackageX();
        payReq.nonceStr = data.getNoncestr();
        payReq.timeStamp = data.getTimestamp();
        payReq.sign = data.getSign();
        payReq.extData = WX_RESULT_EXT_DATA_FLAG_PAY_BILL;
        wxapi.sendReq(payReq);
    }

    /**
     * 金币预支付接口
     */
    private void getJInBiOrder(String orderIdStr) {
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        RetrofitUtil.createHttpsService(PayService.class)
                .payGold(Conversion.getToken(), Conversion.getNetAppA(), mUserToken, orderIdStr)
                .enqueue(new Callback<PayJinBiOrderIdStrBean>() {
                    @Override
                    public void onResponse(Call<PayJinBiOrderIdStrBean> call, Response<PayJinBiOrderIdStrBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            PayJinBiOrderIdStrBean body = response.body();
                            LogUtil.d("金币预支付接口返回的数据" + body.toString());
                            SpUtils.putServerB(mActivity, body.getData().getServerB());
                            if (body.getResultCode() == 1) {
                                mPayId = body.getData().getPayId();
                                initPasswordView();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 8) {
                                showDialog();
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<PayJinBiOrderIdStrBean> call, Throwable t) {
                        mDialog.dismiss();
                    }
                });
    }

    private void showRecharge(String resultDesc) {
        switch (resultDesc) {
            case "金币余额不足":
                if (mAlertDialog == null) {
                    mAlertDialog = new AlertDialog.Builder(mActivity)
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
                            }).create();
                }
                if (mAlertDialog != null && !mAlertDialog.isShowing()) {
                    mAlertDialog.show();
                }
                break;
            default:
                break;
        }

    }

    /**
     * 支付宝支付
     *
     * @param data
     */
    private void ZhiFuBaoPay(final String data) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(data, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 金币支付
     *
     * @param payId
     */
    private void PayJinBiData(String payId) {
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        RetrofitUtil.createHttpsService(PayService.class)
                .payGoldPrePay(Conversion.getToken(), Conversion.getNetAppA(), payId, MD5Utils.encode(mStrPassword))
                .enqueue(new Callback<PayJinBiOrderIdStrBean>() {
                    @Override
                    public void onResponse(Call<PayJinBiOrderIdStrBean> call, Response<PayJinBiOrderIdStrBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            PayJinBiOrderIdStrBean body = response.body();
                            LogUtil.d("金币支付返回的数据---" + body.toString());
                            if (body.getData() != null) {
                                SpUtils.putServerB(mActivity, body.getData().getServerB());
                            }
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, "支付成功").show();
//                                EventBus.getDefault().post(new TaskReleaseRefreshTaskAndMapList(true));
                                PaySuccess();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 8) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else if (body.getResultCode() == 0) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                showRecharge(body.getResultDesc());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayJinBiOrderIdStrBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        mDialog.dismiss();
                    }
                });
    }

    private void popPasswordError() {
        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_paythebill));
        View view = UiUtils.inflate(mActivity, R.layout.toast_pay_password_error);
        popwindou.init(view, Gravity.CENTER, true);
        view.findViewById(R.id.tv_input).setOnClickListener(new View.OnClickListener() {//从新输入
            @Override
            public void onClick(View v) {
                if (null != mPasswordView) {
                    deletePassword(mPasswordView);
                }
                popwindou.dismiss();
            }
        });
        view.findViewById(R.id.tv_forgetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindou.dismiss();
                startActivity(new Intent(mActivity, PayPasswordActivity.class));
            }
        });
    }

    /**
     * 支付成功执行的操作
     */
    private void PaySuccess() {
        mIntent = new Intent(mActivity, WebOrderDetailsActivity.class);
//        mIntent.putExtra(Conversion.TYPE, mType);//1 商家  2 任务支付
        mIntent.putExtra(Conversion.ORDERID, mOrderId);
        if ("1".equals(mType)) {
            mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId);
            mIntent.putExtra(Conversion.SELLERINFONAME, mSellerinfoname);
        } else {
            mIntent.putExtra(Conversion.TASKID, mTaskId);
            mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId);
            mIntent.putExtra(Conversion.SELLERNMAE, mSellerinfoname);
            mIntent.putExtra(Conversion.OTHERSRNMAE, mMOthersrnmae);
            mIntent.putExtra(Conversion.USERID, mUserId);
        }
        startActivity(mIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_COUPON_ID) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    mCouponId = data.getStringExtra("EXTRA_COUPON_ID");
                    mCouponMoney = data.getStringExtra("EXTRA_COUPON_MONEY");
                    mRangid = data.getStringExtra("EXTRA_RANG_ID");
                    mGetId = data.getStringExtra("EXTRA_GET_ID");
                    String ruleMoney = data.getStringExtra("EXTRA_RULE_MONEY");
                    if (mCouponId == null) {
                        mCouponId = "";
                    } else {
                        String couponStr;
//                        if ("0".equals(ruleMoney) || TextUtils.isEmpty(ruleMoney)) {
//                            couponStr = "抵" + mCouponMoney + "元";
//                        } else {
//                            couponStr = ruleMoney + "元抵" + mCouponMoney + "元";
//                        }
                        if (!TextUtils.isEmpty(mCouponMoney)) {
                            couponStr = "减" + mCouponMoney + "元";
                        } else {
                            couponStr = "";
                        }
                        payPreferential.setText(couponStr);
                        mMoneyDiscounted = calcCorrectDiscountedMoney().subtract(new BigDecimal(mCouponMoney));
                        if (mMoneyDiscounted.signum() == -1) {
                            mMoneyDiscounted = new BigDecimal(0);
                        }
//                        mTvMoneyDiscounted.setText(getFormatResult(mMoneyDiscounted));
                        mTvMoneyDiscounted.setText(Conversion.setFormatNum(mMoneyDiscounted.toString()));
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                mGetId = "";
                mCouponId = "";
                mRangid = "";
                mCouponMoney = "0";
                payPreferential.setText(null);
                mTvMoneyDiscounted.setText(Conversion.setFormatNum(calcDiscountedMoney()));
            }
        }
    }

    /**
     * 17、获取店铺各种支付方式折扣接口
     */
    private void getSellerDiscount() {
        RetrofitUtil.createHttpsService(PayService.class)
                .getSellerDiscount(mSellerInfoId)
                .enqueue(new Callback<SellerDiscountBean>() {
                    @Override
                    public void onResponse(Call<SellerDiscountBean> call, Response<SellerDiscountBean> response) {
                        if (response.isSuccessful()) {
                            SellerDiscountBean body = response.body();
                            if (body.getResultCode() == 1) {

                                int gold = response.body().getDate().getGold();
                                int wxpay = response.body().getDate().getWxpay();
                                int alipay = response.body().getDate().getAlipay();

                                mGoldDiscount = gold;
                                mWeChatDiscount = wxpay;
                                mAliPayDiscount = alipay;


                                mTvDiscountGold.setVisibility(gold == 100 ? View.GONE : View.VISIBLE);
                                mTvDiscountWeChatPay.setVisibility(wxpay == 100 ? View.GONE : View.VISIBLE);
                                mTvDiscountAliPay.setVisibility(alipay == 100 ? View.GONE : View.VISIBLE);

                                mTvDiscountGold.setText(String.valueOf(gold / 10f) + "折");
                                mTvDiscountWeChatPay.setText(String.valueOf(wxpay / 10f) + "折");
                                mTvDiscountAliPay.setText(String.valueOf(alipay / 10f) + "折");
                            } else if (body.getResultCode() == 9) {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<SellerDiscountBean> call, Throwable t) {
                    }
                });
    }

    private class WeixinResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Conversion.WX_RESULT.equals(intent.getAction())) {
                if (!WX_RESULT_EXT_DATA_FLAG_PAY_BILL.equals(intent.getStringExtra(Conversion.WX_RESULT_EXT_DATA_FLAG))) {
                    return;
                }
                int result = intent.getIntExtra(Conversion.WX_RESULT_CODE_FLAG, -1);
                if (result == 0) {
                    Toasty.success(mActivity, "支付成功").show();
                    isWeiXinCallback();
                    PaySuccess();
                } else if (result == -2) {
                    Toasty.normal(mActivity, "取消支付").show();
                } else {
                    Toasty.normal(mActivity, "支付失败").show();
                }
            }
        }
    }

    private void getGold() {
        RetrofitUtil.createService(SquareService.class).getUserGoldAndScore(SpUtils.getUserToken(mActivity)).enqueue(new RetrofitCallback<MiWealthABean>() {
            @Override
            public void onSuccess(Call<MiWealthABean> call, Response<MiWealthABean> response) {
                if (response.isSuccessful()) {
                    MiWealthABean body = response.body();
                    if (body.getResultCode() == 1) {
                        mGold = body.getUserInfo().getGold();
                    } else if (body.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    }
                }
            }

            @Override
            public void onFailure(Call<MiWealthABean> call, Throwable t) {

            }
        });
    }

    private void isWeiXinCallback() {
        RetrofitUtil.createHttpsService(PayService.class).queryOrder(mOrderId).enqueue(new RetrofitCallback<PaySuccessfulBean>() {
            @Override
            public void onSuccess(Call<PaySuccessfulBean> call, Response<PaySuccessfulBean> response) {
            }

            @Override
            public void onFailure(Call<PaySuccessfulBean> call, Throwable t) {
            }
        });
    }

}
