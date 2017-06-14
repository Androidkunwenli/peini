package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.CouponInfoList;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.MiCouponAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 我的所有优惠券
 * Created by huizhe.ju on 2016/12/5.
 */
public class MiCouponActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private MiCouponActivity mActivity;

    private MiCouponAdapter mQuickAdapter = null;
    private TextView mTvCouponNumber;

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    @Override
    public int initLayoutId() {
        return R.layout.activity_mi_coupon;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("我的优惠券");
    }

    @Override
    public void initData() {
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mQuickAdapter = new MiCouponAdapter(mActivity);
        mQuickAdapter.addHeaderView(getHeaderView());
        mQuickAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSwipeTarget.setAdapter(mQuickAdapter);

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRequestAllCoupon(false);
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        initRequestAllCoupon(true);
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CouponInfoList.CouponInfoListBean data = ((CouponInfoList.CouponInfoListBean) adapter.getData().get(position));
                Intent intent = new Intent(mActivity, IntegralMessageActivity.class);
                intent.putExtra(Conversion.DATA, data);
                intent.putExtra(Conversion.TYPE, 1);
                startActivity(intent);

            }
        });
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.item_mi_coupon_head, null);
        mTvCouponNumber = (TextView) view.findViewById(R.id.tv_coupon_number);
        TextView tvHelp = (TextView) view.findViewById(R.id.tv_help);
        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, IntegraHelpActivity.class));
            }
        });
        return view;
    }

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.item_mi_coupon_tail, null);
        TextView tvOverDue = (TextView) view.findViewById(R.id.tv_show_overdue_coupon);
        tvOverDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, MiOverdueCouponActivity.class));
            }
        });
        return view;
    }

    private void initRequestAllCoupon(boolean isShowProgress) {
        mRequestPageIndex = 1;
        requestAllCoupon(isShowProgress);
    }

    private void requestAllCoupon(boolean isShowProgress) {
        if (isShowProgress) {
            mDialog.show();
        }
        RetrofitUtil.createService(SquareService.class)
                .getCouponInfoByAll(mUserToken, "", mRequestPageIndex, mRequestRows)
                .enqueue(new Callback<CouponInfoList>() {
                    @Override
                    public void onResponse(Call<CouponInfoList> call, Response<CouponInfoList> response) {
                        mDialog.dismiss();
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        if (response.isSuccessful()) {
                            CouponInfoList body = response.body();
                            if (body.getResultCode() == 1) {
                                List<CouponInfoList.CouponInfoListBean> couponInfoList = body.getCouponInfoList();
                                if (mRequestPageIndex == 1) {
                                    mQuickAdapter.setNewData(couponInfoList);
                                } else {
                                    mQuickAdapter.addData(couponInfoList);
                                }
                                mQuickAdapter.loadMoreComplete();
                                mTvCouponNumber.setText(body.getTotalCnt());

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
                                Toasty.normal(mActivity, response.body().getResultDesc()).show();
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
                    }
                });
    }

    private int getAvailableNumber(List<CouponInfoList.CouponInfoListBean> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        int result = 0;
        for (CouponInfoList.CouponInfoListBean couponInfoListBean : list) {
            if (couponInfoListBean != null && couponInfoListBean.getDays() >= 0) {
                result++;
            }
        }
        return result;
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
        requestAllCoupon(false);
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
