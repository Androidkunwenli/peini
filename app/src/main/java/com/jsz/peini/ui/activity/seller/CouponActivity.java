package com.jsz.peini.ui.activity.seller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.CouponInfoList;
import com.jsz.peini.presenter.PayService;
import com.jsz.peini.ui.activity.square.IntegraHelpActivity;
import com.jsz.peini.ui.activity.square.MiCouponActivity;
import com.jsz.peini.ui.adapter.seller.CouponAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 买单选择优惠券列表
 * Created by huizhe.ju on 2017/02/27.
 */
public class CouponActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    private static final String EXTRA_SELLER_INFO_ID = "extra_sellerInfoId";
    private static final String EXTRA_TOTAL_MONEY = "extra_totalMoney";
    private static final String EXTRA_EXCLUSIVE_MONEY = "extra_exclusiveMoney";
    private static final String EXTRA_PAY_TYPE = "extra_payType";
    private static final String EXTRA_SELECTED_COUPON_GET_ID = "extra_selected_coupon_get_id";

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private CouponActivity mActivity;

    private CouponAdapter mQuickAdapter = null;

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    private String mSellerInfoId;
    private String mTotalMoney;
    private String mExclusiveMoney;
    private String mPayType;
    private String mSelectedCouponGetId;

    private CheckBox mCbUseNoneCoupon;

    public static void actionShow(Context context, int requestCode, String sellerInfoId, String totalMoney,
                                  String exclusiveMoney, String payType, String getId) {
        Intent intent = new Intent(context, CouponActivity.class);
        intent.putExtra(EXTRA_SELLER_INFO_ID, sellerInfoId);
        intent.putExtra(EXTRA_TOTAL_MONEY, totalMoney);
        intent.putExtra(EXTRA_EXCLUSIVE_MONEY, exclusiveMoney);
        intent.putExtra(EXTRA_PAY_TYPE, payType);
        intent.putExtra(EXTRA_SELECTED_COUPON_GET_ID, getId);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    public int initLayoutId() {
        return R.layout.activity_mi_coupon;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("优惠券");
    }

    @Override
    public void initData() {

        mSellerInfoId = getIntent().getStringExtra(EXTRA_SELLER_INFO_ID);
        mTotalMoney = getIntent().getStringExtra(EXTRA_TOTAL_MONEY);
        mExclusiveMoney = getIntent().getStringExtra(EXTRA_EXCLUSIVE_MONEY);
        mPayType = getIntent().getStringExtra(EXTRA_PAY_TYPE);
        mSelectedCouponGetId = getIntent().getStringExtra(EXTRA_SELECTED_COUPON_GET_ID);

        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mQuickAdapter = new CouponAdapter(mActivity, mSelectedCouponGetId);
        mQuickAdapter.addHeaderView(getHeaderView());
        mQuickAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mCbUseNoneCoupon != null) {
                    mCbUseNoneCoupon.setChecked(false);
                }
                CouponInfoList.CouponInfoListBean bean = (CouponInfoList.CouponInfoListBean) adapter.getData().get(position);
                String couponId = String.valueOf(bean.getCouponId());
                String couponMoney = bean.getCouponMoney();
                String ruleMoney = bean.getRuleMoney();
                String rangId = bean.getRangId();
                String getId = bean.getGetId();
                Intent intent = new Intent();
                intent.putExtra("EXTRA_COUPON_ID", couponId);
                intent.putExtra("EXTRA_RANG_ID", rangId);
                intent.putExtra("EXTRA_COUPON_MONEY", couponMoney);
                intent.putExtra("EXTRA_RULE_MONEY", ruleMoney);
                intent.putExtra("EXTRA_GET_ID", getId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mSwipeTarget.setAdapter(mQuickAdapter);

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRequestAvailableCouponForPay(false);
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        initRequestAvailableCouponForPay(true);
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.item_coupon_head, null);
        mCbUseNoneCoupon = (CheckBox) view.findViewById(R.id.cb_use_none_coupon);
        mCbUseNoneCoupon.setChecked(TextUtils.isEmpty(mSelectedCouponGetId));
        mCbUseNoneCoupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mActivity.setResult(Activity.RESULT_CANCELED);
                    mActivity.finish();
                }
            }
        });
        return view;
    }

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.item_coupon_tail, null);
        TextView tvHowToUseCoupon = (TextView) view.findViewById(R.id.tv_how_to_use_coupon);
        TextView tvAllCoupon = (TextView) view.findViewById(R.id.tv_show_all_coupon);
        tvHowToUseCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, IntegraHelpActivity.class));
            }
        });
        tvAllCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, MiCouponActivity.class));
            }
        });
        return view;
    }

    private void initRequestAvailableCouponForPay(boolean isShowProgress) {
        mRequestPageIndex = 1;
        requestAvailableCouponForPay(isShowProgress);
    }

    private void requestAvailableCouponForPay(boolean isShowProgress) {
        if (isShowProgress) {
            mDialog.show();
        }
        RetrofitUtil.createHttpsService(PayService.class)
                .getCouponInfoByPay(mUserToken, mSellerInfoId, mTotalMoney, mExclusiveMoney, mPayType, mRequestPageIndex, mRequestRows)
                .enqueue(new Callback<CouponInfoList>() {
                    @Override
                    public void onResponse(Call<CouponInfoList> call, Response<CouponInfoList> response) {
                        mDialog.dismiss();
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        if (response.isSuccessful()) {
                            CouponInfoList body = response.body();
                            if (body.getResultCode() == 1) {
                                List<CouponInfoList.CouponInfoListBean> couponInfoList = response.body().getCouponInfoList();
                                if (mRequestPageIndex == 1) {
                                    mQuickAdapter.setNewData(couponInfoList);
                                } else {
                                    mQuickAdapter.addData(couponInfoList);
                                }
                                mQuickAdapter.loadMoreComplete();
                                if (couponInfoList != null
                                        && couponInfoList.size() == mRequestRows) {
                                    mRequestPageIndex += 1;
                                } else {
                                    mQuickAdapter.removeAllFooterView();
                                    mQuickAdapter.addFooterView(getFooterView());
                                    mQuickAdapter.loadMoreEnd();
                                }
                            } else if (response.body().getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (response.body().getResultCode() == 0) {
                                mQuickAdapter.loadMoreFail();
                                Toasty.normal(mActivity, body.getResultDesc()).show();
                            } else {
                                mQuickAdapter.loadMoreFail();
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        } else {
                            mQuickAdapter.loadMoreFail();
                            Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponInfoList> call, Throwable t) {
                        mDialog.dismiss();
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mQuickAdapter.loadMoreFail();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        LogUtil.d("获取买单可使用的优惠券" + t.getMessage());
                    }
                });
    }

    @OnClick({R.id.toolbar})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mQuickAdapter.removeAllFooterView();
        requestAvailableCouponForPay(false);
    }

    public final class CustomLoadMoreView extends LoadMoreView {
        @Override
        public int getLayoutId() {
            return R.layout.quick_view_load_more;
        }

        @Override
        protected int getLoadingViewId() {
            return R.id.load_more_loading_view;
        }

        @Override
        protected int getLoadFailViewId() {
            return R.id.load_more_load_fail_view;
        }

        /**
         * isLoadEndGone()为true，可以返回0
         * isLoadEndGone()为false，不能返回0
         */
        @Override
        protected int getLoadEndViewId() {
//            return R.id.load_more_load_end_view;
            return 0;
        }
    }
}
