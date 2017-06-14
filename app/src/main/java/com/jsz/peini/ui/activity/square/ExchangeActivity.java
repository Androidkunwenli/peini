package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.square.CouponInfoListAllUnGetByScore;
import com.jsz.peini.model.square.ExchangeBean;
import com.jsz.peini.presenter.ad.Ad;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.CxchangBeanAdapter;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 积分兑换
 */

public class ExchangeActivity extends BaseActivity {

    @InjectView(R.id.iv_toolbar_image)
    ImageView mIvToolbarImage;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private ExchangeActivity mActivity;
    private Intent mIntent;
    /**
     * 数据
     */
    List<ExchangeBean> mBeen = new ArrayList<>();
    private CxchangBeanAdapter mAdapter;
    private List<CouponInfoListAllUnGetByScore.CouponListBean> mCouponList;
    private CouponInfoListAllUnGetByScore.UserInfoBean mUserInfo;
    private List<AdModel.AdvertiseListBean> mAdvertiseList;

    @Override
    public int initLayoutId() {
        return R.layout.activity_integral;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("积分兑换");
        mRightButton.setText("明细");
    }

    @Override
    public void initData() {
        mAdapter = new CxchangBeanAdapter(mActivity);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBeen.clear();
                initNetWork();
                getAdList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBeen.clear();
        initNetWork();
        getAdList();
    }


    @OnClick({R.id.toolbar, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.right_button:
                mIntent = new Intent(mActivity, IntegralDetailActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getCouponInfoList_allUnGet_byScore(mUserToken, 1, 6)
                .enqueue(new Callback<CouponInfoListAllUnGetByScore>() {
                    @Override
                    public void onResponse(Call<CouponInfoListAllUnGetByScore> call, final Response<CouponInfoListAllUnGetByScore> response) {
                        if (response.isSuccessful()) {
                            CouponInfoListAllUnGetByScore allUnGetByScore = response.body();
                            if (allUnGetByScore.getResultCode() == 1) {
                                mCouponList = allUnGetByScore.getCouponList();
                                mUserInfo = allUnGetByScore.getUserInfo();
                                mAdapter.setmCouponList(mCouponList);
                                mAdapter.setmUserInfo(mUserInfo);
                            } else if (allUnGetByScore.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, allUnGetByScore.getResultDesc()).show();
                            }
                            mSwipeToLoadLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponInfoListAllUnGetByScore> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                });
    }


    public void getAdList() {
        RetrofitUtil.createService(Ad.class).getAdvertise("6").enqueue(new Callback<AdModel>() {
            @Override
            public void onResponse(Call<AdModel> call, Response<AdModel> response) {
                if (response.isSuccessful()) {
                    AdModel body = response.body();
                    if (body.getResultCode() == 1) {
                        mAdvertiseList = body.getAdvertiseList();
                        mAdapter.setmAdvertiseList(mAdvertiseList);
                    }
                    mSwipeToLoadLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<AdModel> call, Throwable t) {
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });
    }
}
