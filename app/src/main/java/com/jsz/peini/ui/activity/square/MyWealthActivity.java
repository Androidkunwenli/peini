package com.jsz.peini.ui.activity.square;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.MiWealthABean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.MyWealthAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpDataUtils;
import com.jsz.peini.utils.SpUtils;
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
 * 我的财富
 * Created huizhe.ju th on 2017/04/03.
 */
public class MyWealthActivity extends BaseActivity {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @InjectView(R.id.iv_toolbar_image)
    ImageView mImageView;
    private MyWealthActivity mActivity;
    private MyWealthAdapter mWealthAdapter;
    private List<MiWealthABean.UserInfoBean> mUserInfoBeanList;
    private String mMyWealth;

    @Override
    public int initLayoutId() {
        return R.layout.activity_me_wealth;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("我的财富");
        mImageView.setBackgroundResource(getIntent().getIntExtra(Conversion.TYPE, 0) == 1 ? R.mipmap.back : R.mipmap.san);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserInfoBeanList = new ArrayList<>();
        mWealthAdapter = new MyWealthAdapter(this);
        mRecyclerView.setAdapter(mWealthAdapter);
        //初始化数据
        mMyWealth = (String) SpDataUtils.get(mActivity, "myWealth", "");
        if (!TextUtils.isEmpty(mMyWealth)) {
            MiWealthABean miWealthABean = new Gson().fromJson(mMyWealth, MiWealthABean.class);
            mUserInfoBeanList.clear();
            mUserInfoBeanList.add(miWealthABean.getUserInfo());
            mWealthAdapter.setData(mUserInfoBeanList);
        }
        //结束初始化
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        //下拉刷新
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestGoldAndScore(false);
            }
        });
    }

    boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        requestGoldAndScore(isFirst);
        isFirst = false;
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
        }
    }

    /**
     * 发起网络请求
     */
    private void requestGoldAndScore(boolean isShowProgress) {
        if (isShowProgress && TextUtils.isEmpty(mMyWealth))
            mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .getUserGoldAndScore(SpUtils.getUserToken(mActivity))
                .enqueue(new Callback<MiWealthABean>() {
                    @Override
                    public void onResponse(Call<MiWealthABean> call, final Response<MiWealthABean> response) {
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                        if (mSwipeToLoadLayout.isRefreshing())
                            mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        if (response.isSuccessful()) {
                            MiWealthABean resultCode = response.body();
                            if (resultCode.getResultCode() == 1) {
                                SpDataUtils.put(mActivity, "myWealth", new Gson().toJson(resultCode));
                                mUserInfoBeanList.clear();
                                mUserInfoBeanList.add(resultCode.getUserInfo());
                                mWealthAdapter.setData(mUserInfoBeanList);
                            } else if (resultCode.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<MiWealthABean> call, Throwable t) {
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                        if (mSwipeToLoadLayout.isRefreshing())
                            mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }
}
