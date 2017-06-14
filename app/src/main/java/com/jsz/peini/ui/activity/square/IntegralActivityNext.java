package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.CouponInfoListAllUnGetByScore;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.IntegralAdapter;
import com.jsz.peini.ui.view.SpacesItemDecoration;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/17.
 */
public class IntegralActivityNext extends BaseActivity {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    public IntegralActivityNext mActivity;
    List<CouponInfoListAllUnGetByScore.CouponListBean> mList;
    @InjectView(R.id.more)
    TextView mMore;
    public IntegralAdapter mAdapter;
    public Intent mIntent;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private int mMyIntegral = -1;

    @Override
    public int initLayoutId() {
        return R.layout.activity_integral_next;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("积分兑换");
        mList = new ArrayList<>();
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRightButton.setText("帮助");
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, IntegraHelpActivity.class));
            }
        });

        mMore.setVisibility(mList.size() >= 6 ? View.GONE : View.VISIBLE);
        mSwipeTarget.addItemDecoration(new SpacesItemDecoration(2, 3, true));
        mSwipeTarget.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAdapter = new IntegralAdapter(mActivity, mList, 2);
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new IntegralAdapter.OnItemClickListener() {
            @Override
            public void OnItemListener(int couponId, int couponIntegral) {
                IntegralMessageActivity.actionShow(mActivity, couponId, couponIntegral, mMyIntegral);
            }
        });
        /**上啦加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initNetWork(true);
            }
        });
        /**下拉刷新*/
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initNetWork(false);
            }
        });
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    /**
     * 网络访问
     */
    private int page = 1;

    private void initNetWork(final boolean isFirst) {
        if (isFirst) {
            page = 1;
        } else {
            page++;
        }
        RetrofitUtil.createService(SquareService.class)
                .getCouponInfoList_allUnGet_byScore(mUserToken, page, 10)
                .enqueue(new Callback<CouponInfoListAllUnGetByScore>() {
                    @Override
                    public void onResponse(Call<CouponInfoListAllUnGetByScore> call, final Response<CouponInfoListAllUnGetByScore> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        if (response.isSuccessful()) {
                            CouponInfoListAllUnGetByScore body = response.body();
                            if (body.getResultCode() == 1) {
                                mMyIntegral = response.body().getUserInfo().getScore();
                                if (isFirst) {
                                    mList.clear();
                                    mList.addAll(response.body().getCouponList());
                                } else {
                                    mList.addAll(response.body().getCouponList());
                                }
                                mAdapter.notifyDataSetChanged();
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
                    public void onFailure(Call<CouponInfoListAllUnGetByScore> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();

                    }
                });
    }
}
