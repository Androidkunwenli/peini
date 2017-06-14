package com.jsz.peini.ui.activity.square;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.square.TaCreditBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.news.SystemMessageActivity;
import com.jsz.peini.ui.adapter.square.TaSquareHiscreditAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ToastUtils;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/23.
 */
public class TaSquareHiscreditActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @InjectView(R.id.iv_toolbar_image)
    ImageView mIvToolbarImage;
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
    private String mUserId;
    private TaSquareHiscreditActivity mActivity;
    private TaCreditBean mCreditBean;
    private TaSquareHiscreditAdapter mHiscreditAdapter;

    List<TaCreditBean> taCreditBeen = new ArrayList<>();

    @Override
    public int initLayoutId() {
        return R.layout.activity_tasquarehiscredit;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("他人信用");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUserId = getIntent().getStringExtra("userId");
        mHiscreditAdapter = new TaSquareHiscreditAdapter(mActivity, taCreditBeen);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mSwipeTarget.setAdapter(mHiscreditAdapter);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                inItNetWork();
            }
        });
    }

    @Override
    public void initInternet() {
        inItNetWork();
    }

    private void inItNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getCredit(mUserToken, mUserId)
                .enqueue(new RetrofitCallback<TaCreditBean>() {
                    @Override
                    public void onSuccess(Call<TaCreditBean> call, Response<TaCreditBean> response) {
                        mSwipeToLoadLayout.setRefreshing(false);
                        if (response.isSuccessful()) {
                            TaCreditBean body = response.body();
                            if (body.getResultCode() == 1) {
                                taCreditBeen.clear();
                                taCreditBeen.add(body);
                                mHiscreditAdapter.notifyDataSetChanged();
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.error(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TaCreditBean> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);
                        Toasty.error(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    @Override
    public void onLoadMoreRequested() {

    }
}
