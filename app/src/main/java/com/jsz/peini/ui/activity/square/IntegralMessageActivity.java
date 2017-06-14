package com.jsz.peini.ui.activity.square;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.JsonResponse;
import com.jsz.peini.model.square.CouponInfoList;
import com.jsz.peini.model.square.CouponInfoUnGet;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/18.
 */
public class IntegralMessageActivity extends BaseActivity {

    private static final String EXTRA_COUPON_ID = "extra_coupon_id";
    private static final String EXTRA_COUPON_INTEGRAL = "extra_coupon_integral";
    private static final String EXTRA_MY_INTEGRAL = "extra_my_integral";

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.couponImg)
    ImageView mCouponImg;
    @InjectView(R.id.couponName)
    TextView mCouponName;
    @InjectView(R.id.couponmoney_rulemoney)
    TextView mCouponmoneyRulemoney;
    @InjectView(R.id.couponmoney_rulemoney_big)
    TextView mCouponmoneyRulemoneyBig;
    @InjectView(R.id.couponDesc)
    TextView mCouponDesc;
    @InjectView(R.id.couponRange)
    TextView mCouponRange;
    @InjectView(R.id.lastDate)
    TextView mLastDate;
    @InjectView(R.id.remindText)
    TextView mRemindText;
    @InjectView(R.id.getNum)
    TextView mGetNum;
    @InjectView(R.id.btn_exchange)
    Button mBtnExchange;
    @InjectView(R.id.ll_change)
    LinearLayout mLlChange;

    private IntegralMessageActivity mActivity;
    private int mCouponId;
    private int mCouponIntegral;
    private int mMyIntegral;

    @Override
    public int initLayoutId() {
        return R.layout.activity_integralmessage;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("优惠券详情");
        mCouponId = getIntent().getIntExtra(EXTRA_COUPON_ID, -1);
        mCouponIntegral = getIntent().getIntExtra(EXTRA_COUPON_INTEGRAL, 0);
        mMyIntegral = getIntent().getIntExtra(EXTRA_MY_INTEGRAL, -1);
        //2/1的界面图片
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mCouponImg.getLayoutParams();
        layoutParams.height = UiUtils.getScreenWidth(mActivity) / 2;
        layoutParams.width = UiUtils.getScreenWidth(mActivity);
        mCouponImg.setLayoutParams(layoutParams);
        //获取优惠券详情
        CouponInfoList.CouponInfoListBean data = getIntent().getParcelableExtra(Conversion.DATA);
        if (data != null) {
            CouponInfoUnGet.CouponInfoBean couponInfo = new CouponInfoUnGet.CouponInfoBean();
            couponInfo.setCouponName(data.getCouponName());
            couponInfo.setCouponDesc(data.getCouponDesc());
            couponInfo.setCouponImg(data.getCouponImg());
            couponInfo.setCouponMoney(Integer.parseInt(data.getCouponMoney()));
            couponInfo.setRuleMoney(Integer.parseInt(data.getRuleMoney()));
            couponInfo.setLastDate(data.getLastDate());
            ArrayList<CouponInfoUnGet.CouponInfoBean.CouponRangeBean> couponRangeBeen = new ArrayList<>();
            for (CouponInfoList.CouponInfoListBean.CouponRangeBean couponRangeBean : data.getCouponRange()) {
                couponRangeBeen.add(new CouponInfoUnGet.CouponInfoBean.CouponRangeBean(couponRangeBean.getName()));
            }
            couponInfo.setCouponRange(couponRangeBeen);
            couponInfo.setRemindText(data.getRemindText());
            CouponInfoUnGet couponInfoUnGet = new CouponInfoUnGet();
            couponInfoUnGet.setCouponInfo(couponInfo);
            initShowView(couponInfoUnGet);
            mLlChange.setVisibility(View.GONE);
        } else {
            mLastDate.setVisibility(View.VISIBLE);
            if (mCouponId != 0) {
                initNetWork();
            }
        }
        mGetNum.setText(String.valueOf(mCouponIntegral));

        if (mMyIntegral < mCouponIntegral) {
            mBtnExchange.setClickable(false);
            mBtnExchange.setText("积分不足");
        } else {
            mBtnExchange.setClickable(true);
            mBtnExchange.setText("兑换");
        }


    }

    public static void actionShow(Context context, int couponId, int couponIntegral, int myIntegral) {
        Intent intent = new Intent(context, IntegralMessageActivity.class);
        intent.putExtra(EXTRA_COUPON_ID, couponId);
        intent.putExtra(EXTRA_COUPON_INTEGRAL, couponIntegral);
        intent.putExtra(EXTRA_MY_INTEGRAL, myIntegral);
        context.startActivity(intent);
    }

    @OnClick({R.id.toolbar, R.id.btn_exchange})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.btn_exchange:
                new AlertDialog.Builder(mActivity)
                        .setTitle("温馨提示")
                        .setMessage("确认兑换优惠券吗?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                submitExchange();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
        }
    }

    /**
     * 网络获取
     */
    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getCouponInfo_unGet(mUserToken, String.valueOf(mCouponId), "3")
                .enqueue(new Callback<CouponInfoUnGet>() {
                    @Override
                    public void onResponse(Call<CouponInfoUnGet> call, Response<CouponInfoUnGet> response) {
                        if (response.isSuccessful()) {
                            CouponInfoUnGet body = response.body();
                            if (body != null) {
                                if (response.body().getResultCode() == 1) {
                                    initShowView(response.body());
                                } else if (response.body().getResultCode() == 9) {
                                    LoginDialogUtils.isNewLogin(mActivity);
                                } else if (body.getResultCode() == 0) {
                                    Toasty.normal(mActivity, response.body().getResultDesc()).show();
                                } else {
                                    Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponInfoUnGet> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        finish();
                    }
                });
    }

    private String getSqlitNextText(String sqlit) {
        if (!TextUtils.isEmpty(sqlit)) {
            StringBuilder coupSq = new StringBuilder();
            String[] split;
            if (sqlit.contains(",")) {
                split = sqlit.split(",");
            } else {
                split = sqlit.split("\n");
            }
            coupSq.append("· ");
            for (int i = 0; i < split.length; i++) {
                coupSq.append(split[i] + (split.length == (i + 1) ? "" : "\n· "));
            }
            return coupSq.toString();
        } else {
            return "";
        }
    }

    private void initShowView(CouponInfoUnGet body) {

        if (body.getIsGet() == 1) {
            mBtnExchange.setClickable(false);
            mBtnExchange.setText("已兑换");
        }
        CouponInfoUnGet.CouponInfoBean couponInfo = body.getCouponInfo();
        mCouponName.setText(couponInfo.getCouponName());
        //商品介绍
        mCouponDesc.setText(getSqlitNextText(couponInfo.getCouponDesc()));
        int couponMoney = couponInfo.getCouponMoney();
        int ruleMoney = couponInfo.getRuleMoney();
        if (ruleMoney == 0) {
            mCouponmoneyRulemoney.setText("抵" + couponMoney + "元");
            mCouponmoneyRulemoneyBig.setText("抵" + couponMoney + "元");
        } else {
            mCouponmoneyRulemoney.setText(ruleMoney + "元抵" + couponMoney + "元");
            mCouponmoneyRulemoneyBig.setText(ruleMoney + "元抵" + couponMoney + "元");
        }
//        45元购此券结账时可抵50元现金
//        mCouponmoneyRulemoneyBig.setText(couponInfo.getCouponMoney() + "元购此券结账时可抵" + couponInfo.getRuleMoney() + "元现金");
        String lastDateStr = "· 有效期至 " + couponInfo.getLastDate();
        mLastDate.setText(lastDateStr);
        List<CouponInfoUnGet.CouponInfoBean.CouponRangeBean> couponRange = couponInfo.getCouponRange();
        if (couponRange.size() != 0) {
            StringBuilder coupSq = new StringBuilder();
            coupSq.append("· ");
            for (int i = 0; i < couponRange.size(); i++) {
                coupSq.append(couponRange.get(i).getName() + (couponRange.size() == (i + 1) ? "" : "\n· "));
            }
            //适用范围
            mCouponRange.setText(coupSq.toString());
        } else {
            mCouponRange.setText("");
        }
        mRemindText.setText(getSqlitNextText(couponInfo.getRemindText()));
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + couponInfo.getCouponImg(), mCouponImg);
    }

    private void submitExchange() {
        mDialog.show();
        RetrofitUtil.createHttpsService(SquareService.class)
                //1活动领取；2签到领取;3积分换取；4金币购买；5消费赠送；6任务赠送；7消费最高；8任务最多；9指定id
                .getCouponInfoByReceive(mUserToken, String.valueOf(mCouponId), "3")
                .enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        if (response.isSuccessful()) {
                            mDialog.dismiss();
                            if (response.body() != null) {
                                if (response.body().getResultCode() == 1) {
                                    Toasty.success(mActivity, response.body().getResultDesc()).show();
                                    finish();
                                } else if (response.body().getResultCode() == 9) {
                                    Toasty.normal(mActivity, response.body().getResultDesc()).show();
                                    LoginDialogUtils.isNewLogin(mActivity);
                                } else {
                                    Toasty.normal(mActivity, response.body().getResultDesc()).show();
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        mDialog.dismiss();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        finish();
                    }
                });
    }
}
