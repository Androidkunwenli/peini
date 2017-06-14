package com.jsz.peini.ui.activity.square;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.CustomLoadMoreView;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.ScoreHistoryListBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.fragment.detail.DetailNewAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/18.
 */
public class IntegralDetailActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.detail_tablayout)
    TabLayout mDetailTablayout;
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

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    public List<String> mListTitle;
    public DetailNewAdapter mAdapter;
    public Activity mActivity;
    /**
     * 支出
     */
    String type = "";

    @Override
    public int initLayoutId() {
        return R.layout.activity_integraldetail;
    }

    @Override
    public void initView() {

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActivity = this;
        mTitle.setText("积分明细");
        mListTitle = new ArrayList<>();
        mListTitle.add("全部");
        mListTitle.add("收入");
        mListTitle.add("支出");
        //设置TabLayout的模式
        mDetailTablayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        for (int i = 0; i < mListTitle.size(); i++) {
            mDetailTablayout.addTab(mDetailTablayout.newTab().setText(mListTitle.get(i)));
        }

        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new DetailNewAdapter(mActivity);
        mAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSwipeTarget.setAdapter(mAdapter);
        /**上啦加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestPageIndex = 1;
                initNetWork();
            }
        });
        /**下拉刷新*/
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mDetailTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mAdapter.setNewData(null);
                mSwipeToLoadLayout.setRefreshing(true);
                mRequestPageIndex = 1;
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        type = "";
                        break;
                    case 1:
                        type = "1";
                        break;
                    case 2:
                        type = "0";
                        break;
                }
                initNetWork();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        try {
            settab();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }


        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                String hisId = ((ScoreHistoryListBean.DataBean) adapter.getData().get(position)).getHisId();
                dialog(hisId, position);
                return true;
            }
        });
        //加载网络
        autoRefresh();
    }

    private void autoRefresh() {
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

    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getScoreHistoryList(mRequestPageIndex, mRequestRows, mUserToken, type)
                .enqueue(new Callback<ScoreHistoryListBean>() {
                    @Override
                    public void onResponse(Call<ScoreHistoryListBean> call, Response<ScoreHistoryListBean> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        if (response.isSuccessful()) {
                            ScoreHistoryListBean body = response.body();
                            if (body.getResultCode() == 1) {
                                List<ScoreHistoryListBean.DataBean> data = body.getData();
                                if (mRequestPageIndex == 1) {
                                    mAdapter.setNewData(data);
                                    if (data.size() == 0) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "您目前还没有进行过积分兑换!");
                                    }
                                } else {
                                    mAdapter.addData(data);
                                }
                                mAdapter.loadMoreComplete();
                                if (data != null && data.size() == mRequestRows) {
                                    mRequestPageIndex += 1;
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                mAdapter.loadMoreFail();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ScoreHistoryListBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mAdapter.loadMoreFail();
                    }
                });
    }

    protected void dialog(final String hisid, final int position) {
        new AlertDialog.Builder(mActivity)
                .setTitle("删除积分明细")
                .setMessage("确定删除吗?")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initDelete(hisid, position);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void initDelete(String hisid, final int position) {
        mAdapter.getData().remove(position);
        mAdapter.notifyItemRemoved(position);
        RetrofitUtil.createService(SquareService.class)
                .delScoreHistory(mUserToken, hisid)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
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
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    private void settab() throws NoSuchFieldException, IllegalAccessException {
        Class<?> tablayout = mDetailTablayout.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(mDetailTablayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            params.leftMargin = UiUtils.dip2px(mActivity, 25f);
            params.rightMargin = UiUtils.dip2px(mActivity, 25f);
//            params.setMarginStart(UiUtils.dip2px(mActivity, 25f));
//            params.setMarginEnd(UiUtils.dip2px(mActivity, 25f));

            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
