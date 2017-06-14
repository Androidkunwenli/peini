package com.jsz.peini.ui.activity.pay;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.jsz.peini.PeiNiApp;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.OnPasswordInputFinish;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.pay.PayJinBiOrderIdStrBean;
import com.jsz.peini.model.pay.PayOrderIdStrBean;
import com.jsz.peini.model.pay.PaySuccessfulBean;
import com.jsz.peini.model.pay.WeiXinPayOrderIdStrBean;
import com.jsz.peini.presenter.PayService;
import com.jsz.peini.ui.activity.setting.PayPasswordActivity;
import com.jsz.peini.ui.activity.square.OfficialActivity;
import com.jsz.peini.ui.activity.square.RechargeActivity;
import com.jsz.peini.ui.view.PasswordView;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.MD5Utils;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.pay.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 官方活动支付
 * Created by huizhe.ju on 2017/3/13.
 */
public class OfficialActivityPayActivity extends BaseActivity {

    private static final int SDK_PAY_FLAG = 1;
    private static final String EXTRA_ORDER_ID = "extra_order_id";
    private static final String WX_RESULT_EXT_DATA_FLAG_ACTIVITY_PAY = "wx_result_ext_data_flag_activity_pay";

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.radio_group_pay_method)
    RadioGroup mRadioGroupPayMethod;

    private OfficialActivityPayActivity mActivity;

    private String mStrPassword;
    private String mOrderId;
    private int mPayMethod = 1;

    private WeixinResultReceiver mReceiver;

    @Override
    public int initLayoutId() {
        return R.layout.activity_official_avtivity_pay;
    }

    public static void actionShow(OfficialActivity context, String orderId, int requestCode) {
        Intent intent = new Intent(context, OfficialActivityPayActivity.class);
        intent.putExtra(EXTRA_ORDER_ID, orderId);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("活动支付");

        mOrderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        if (TextUtils.isEmpty(mOrderId)) {
            Toasty.normal(mActivity, "数据异常").show();
            finish();
        }
        mReceiver = new WeixinResultReceiver();
        registerReceiver(mReceiver, new IntentFilter(Conversion.WX_RESULT));
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
//                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                        PaySuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toasty.normal(mActivity, "支付失败").show();
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
        mRadioGroupPayMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_gold:
                        LogUtil.d("金币支付");
                        mPayMethod = 1;
                        break;
                    case R.id.rb_we_chat:
                        LogUtil.d("微信支付");
                        mPayMethod = 2;
                        break;
                    case R.id.rb_ali_pay:
                        LogUtil.d("支付宝支付");
                        mPayMethod = 3;
                        break;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @OnClick({R.id.toolbar, R.id.btn_pay_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.btn_pay_confirm:
                if (mPayMethod == 0) {
                    Toasty.warning(mActivity, "请选择支付方式").show();
                    return;
                }
                if (mPayMethod == 2 && !PeiNiUtils.isWeixinAvilible(mActivity)) {
                    Toasty.normal(mActivity, "您尚未安装微信!").show();
                    return;
                }
                if (mPayMethod == 1) {
                    getJInBiOrder(mOrderId);
                } else if (mPayMethod == 2) {
                    getWeiXinOrder(mOrderId);
                } else if (mPayMethod == 3) {
                    getZhiFuBaoOrder(mOrderId);
                }
                break;
        }
    }

    private void initPasswordView(final String goldPayId) {
        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_paythebill));
        View view = UiUtils.inflate(mActivity, R.layout.pop_passwordview);
        popwindou.init(view, Gravity.BOTTOM, true);
        final PasswordView passwordView = (PasswordView) view.findViewById(R.id.pay_passwordpop);
        passwordView.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                mStrPassword = passwordView.getStrPassword();
                if (mStrPassword.length() == 6) {
                    PayJinBiData(goldPayId);
                }
                popwindou.dismiss();

            }
        });
        passwordView.getCancelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePassword(passwordView);
                popwindou.dismiss();
            }
        });
        view.findViewById(R.id.passwordview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePassword(passwordView);
                popwindou.dismiss();
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
     * 金币预支付接口
     */
    private void getJInBiOrder(String orderId) {
        RetrofitUtil.createHttpsService(PayService.class)
                .payGold(Conversion.getToken(), Conversion.getNetAppA(), mUserToken, orderId)
                .enqueue(new Callback<PayJinBiOrderIdStrBean>() {
                    @Override
                    public void onResponse(Call<PayJinBiOrderIdStrBean> call, Response<PayJinBiOrderIdStrBean> response) {
                        if (response.isSuccessful()) {
                            PayJinBiOrderIdStrBean body = response.body();
                            String serverB = body.getData().getServerB();
                            SpUtils.putServerB(mActivity, serverB);
                            if (body.getResultCode() == 1) {
                                String goldPayId = body.getData().getPayId();
                                initPasswordView(goldPayId);
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (body.getResultCode() == 8) {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                showDalog();
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayJinBiOrderIdStrBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
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
     *
     * @param payId
     */
    private void PayJinBiData(String payId) {
        RetrofitUtil.createHttpsService(PayService.class)
                .payGoldPrePay(Conversion.getToken(), Conversion.getNetAppA(), payId, MD5Utils.encode(mStrPassword))
                .enqueue(new Callback<PayJinBiOrderIdStrBean>() {
                    @Override
                    public void onResponse(Call<PayJinBiOrderIdStrBean> call, Response<PayJinBiOrderIdStrBean> response) {
                        LogUtil.d("金币支付返回的数据---" + response.body().toString());
                        if (response.isSuccessful()) {
                            PayJinBiOrderIdStrBean body = response.body();
                            String serverB = body.getData().getServerB();
                            SpUtils.putServerB(mActivity, serverB);
                            if (body.getResultCode() == 1) {
                                Toasty.success(mActivity, "支付成功").show();
                                PaySuccess();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                                showRecharge(body.getResultDesc());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayJinBiOrderIdStrBean> call, Throwable t) {
                    }
                });
    }

    /**
     * 微信预支付
     */
    private void getWeiXinOrder(String orderId) {
        RetrofitUtil.createHttpsService(PayService.class)
                .payWx(mUserToken, orderId)
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
        payReq.extData = WX_RESULT_EXT_DATA_FLAG_ACTIVITY_PAY;
        wxapi.sendReq(payReq);
        finish();
    }

    /**
     * 支付宝预支付接口
     */
    private void getZhiFuBaoOrder(String orderId) {
        RetrofitUtil.createHttpsService(PayService.class)
                .payAli(mUserToken, orderId)
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
                            } else {
                                Toasty.normal(mActivity, response.body().getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayOrderIdStrBean> call, Throwable t) {

                    }
                });
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
     * 支付成功执行的操作
     */
    private void PaySuccess() {
        Toasty.success(mActivity, "支付成功").show();
        setResult(RESULT_OK);
        /**
         *微信帮助回调
         */
        isWeiXinCallback();
    }

    private void isWeiXinCallback() {
        RetrofitUtil.createHttpsService(PayService.class).queryOrder(mOrderId).enqueue(new RetrofitCallback<PaySuccessfulBean>() {
            @Override
            public void onSuccess(Call<PaySuccessfulBean> call, Response<PaySuccessfulBean> response) {
                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PaySuccessfulBean> call, Throwable t) {
            }
        });
    }

    private class WeixinResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Conversion.WX_RESULT.equals(intent.getAction())) {
                if (!WX_RESULT_EXT_DATA_FLAG_ACTIVITY_PAY.equals(intent.getStringExtra(Conversion.WX_RESULT_EXT_DATA_FLAG))) {
                    return;
                }
                int result = intent.getIntExtra(Conversion.WX_RESULT_CODE_FLAG, -1);
                if (result == 0) {
                    PaySuccess();
                } else if (result == -2) {
                    Toasty.normal(mActivity, "取消支付").show();
                } else {
                    Toasty.normal(mActivity, "支付失败").show();
                }
            }
        }
    }
}
