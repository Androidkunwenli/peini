package com.jsz.peini.ui.fragment.ranking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.model.eventbus.LoodingData;
import com.jsz.peini.model.ranking.RanKingBean;
import com.jsz.peini.presenter.ranking.RanKingService;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.ui.adapter.ranking.RanKingAdapter;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpDataUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.FastScrollLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 金币榜
 */
public class IntegrityFragment extends BaseFragment {
    String[] s = new String[]{"本月", "上月", "总榜"};

    @InjectView(R.id.ranking_rv)
    RecyclerView mRankingRv;
    @InjectView(R.id.nexttab)
    TabLayout mNexttab;
    public RanKingAdapter mAdapter;
    /*rType	Int		1:金币榜2:土豪榜3:诚信榜*/
    public int mRType = 3;
    public List<RanKingBean.RankListBean> mListTitle = new ArrayList<>();
    String dType;
    public RanKingBean mBody;
    private Intent mIntent;

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_gold);
    }

    @Override
    public void initData() {
        mNexttab.setVisibility(View.GONE);
        dType = "1";
        mRankingRv.setLayoutManager(new FastScrollLinearLayoutManager(mActivity));
        mAdapter = new RanKingAdapter(mActivity, mListTitle, mRType, mUserToken);
        mRankingRv.setAdapter(mAdapter);
        //设置TabLayout的模式
        mNexttab.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        for (String ss : s) {
            mNexttab.addTab(mNexttab.newTab().setText(ss));
        }
        //初始化数据
        String getRank = (String) SpDataUtils.get(mActivity, "getIntegrity", "");
        if (!TextUtils.isEmpty(getRank)) {
            mBody = new Gson().fromJson(getRank, RanKingBean.class);
            if (mBody != null && mBody.getRankList().size() != 0) {
                mListTitle.clear();
                mListTitle.addAll(mBody.getRankList());
                mAdapter.setHeadData(mBody);
                mAdapter.notifyDataSetChanged();
                mNexttab.setVisibility(View.VISIBLE);
                mRankingRv.setVisibility(View.VISIBLE);
            } else {
                mNexttab.setVisibility(View.GONE);
                mRankingRv.setVisibility(View.GONE);
            }
        }
        //结束初始化
        mNexttab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                switch (tabPosition) {
                    case 0:
                        dType = "1";
                        break;
                    case 1:
                        dType = "2";
                        break;
                    case 2:
                        dType = "0";
                        break;
                    case 3:
                        dType = "0";
                        break;
                }
                mRankingRv.smoothScrollToPosition(0);
                initNetWork();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 访问网络获取数据
     */
    private void initNetWork() {
        RetrofitUtil.createService(RanKingService.class).getRank(dType, mRType + "", mUserToken).enqueue(new Callback<RanKingBean>() {
            @Override
            public void onResponse(Call<RanKingBean> call, final Response<RanKingBean> response) {
                if (response.isSuccessful()) {
                    mBody = response.body();
                    if (response.body().getResultCode() == 1) {
                        if ("1".equals(dType)) {
                            SpDataUtils.put(mActivity, "getIntegrity", new Gson().toJson(mBody));
                        }
                        mAdapter.setHeadData(response.body());
                        mListTitle.clear();
                        mListTitle.addAll(mBody.getRankList());
                        mAdapter.notifyDataSetChanged();

                        mNexttab.setVisibility(View.GONE);
                        mRankingRv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<RanKingBean> call, Throwable t) {

            }
        });
    }


    private String formatNum(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return "0";
        }
        if (numStr.contains(".")) {
            return numStr.replaceAll("\\.*0*$", "");
        } else {
            return numStr;
        }
    }

    /*侧边栏界面关闭了*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LoodingData loodingData) {
        if (loodingData.isLooding() && loodingData.getPage() == 4) {
            LogUtil.d("点击了排行榜按钮");
            ExecutorService executorService = new PriorityExecutor(5, false);
            PriorityRunnable priorityRunnable = new PriorityRunnable(Priority.NORMAL, new Runnable() {
                @Override
                public void run() {
                    initNetWork();
                }
            });
            executorService.execute(priorityRunnable);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
