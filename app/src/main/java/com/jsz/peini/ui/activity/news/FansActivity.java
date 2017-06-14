package com.jsz.peini.ui.activity.news;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.CustomLoadMoreView;
import com.jsz.peini.model.news.MyFans;
import com.jsz.peini.presenter.news.NewService;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.activity.square.VerifyDataActivity;
import com.jsz.peini.ui.adapter.news.NewFansAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.widget.ProgressActivity;
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
 * Created by th on 2017/1/2.
 */
public class FansActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    ArrayList mList = new ArrayList();
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
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
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    private NewFansAdapter mAdapter;
    private MyFans mBody;
    private FansActivity mFansActivity;
    private String mPay;
    private Intent mIntent;

    @Override
    public int initLayoutId() {
        return R.layout.activity_fans;
    }

    @Override
    public void initView() {
        super.initView();
        mFansActivity = this;
        mTitle.setText("粉丝");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPay = getIntent().getStringExtra("pay");
    }

    @Override
    public void initData() {
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mFansActivity));
        mAdapter = new NewFansAdapter(mFansActivity);
        mAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSwipeTarget.setAdapter(mAdapter);
        initNetWork();
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestPageIndex = 1;
                initNetWork();
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        //item  点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String userId = ((MyFans.ObjectListBean) adapter.getData().get(position)).getUserId();
                if (mPay != null && mPay.equals("pay")) {
                    mIntent = new Intent(mFansActivity, VerifyDataActivity.class);
                    mIntent.putExtra("userId", userId);
                    mFansActivity.startActivity(mIntent);
                    mFansActivity.finish();
                    return;
                }
                if (userId.equals(SpUtils.getUserToken(mFansActivity))) {
                    MiSquareActivity.actionShow(mFansActivity);
                } else {
                    mIntent = new Intent(mFansActivity, TaSquareActivity.class);
                    mIntent.putExtra("userId", userId);
                    mFansActivity.startActivity(mIntent);
                }
            }
        });

        /**上啦加载*/
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    //自动加载
    @Override
    public void onLoadMoreRequested() {
        initNetWork();
    }

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    private void initNetWork() {
        RetrofitUtil.createService(NewService.class)
                .myFans(mUserToken, mRequestPageIndex, mRequestRows)
                .enqueue(new Callback<MyFans>() {
                    @Override
                    public void onResponse(Call<MyFans> call, Response<MyFans> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        if (response.isSuccessful()) {
                            MyFans body = response.body();
                            if (body.getResultCode() == 1) {
                                List<MyFans.ObjectListBean> objectList = body.getObjectList();
                                if (mRequestPageIndex == 1) {
                                    mAdapter.setNewData(objectList);
                                    if (objectList != null && objectList.size() == 0) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "广场晒动态，可以增加粉丝关注度哦~");
                                    } else {
                                        mPaProgress.showContent();
                                    }
                                } else {
                                    mAdapter.addData(objectList);
                                }
                                mAdapter.loadMoreComplete();
                                if (objectList != null && objectList.size() == mRequestRows) {
                                    mRequestPageIndex += 1;
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mFansActivity);
                            } else {
                                Toasty.normal(mFansActivity, Conversion.NETWORKERROR).show();
                                mAdapter.loadMoreFail();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<MyFans> call, Throwable t) {
                        mAdapter.loadMoreFail();
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        Toasty.normal(mFansActivity, Conversion.NETWORKERROR).show();

                    }
                });
    }
}
