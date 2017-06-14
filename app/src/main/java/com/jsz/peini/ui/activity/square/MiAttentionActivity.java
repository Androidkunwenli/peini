package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
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
import com.jsz.peini.model.news.NewsList;
import com.jsz.peini.presenter.news.NewService;
import com.jsz.peini.ui.adapter.square.NewsAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
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
 * Created by 15089 on 2017/2/23.
 */
public class MiAttentionActivity extends BaseActivity {

    public static final String EXTRA_JUMP_FLAG = "EXTRA_JUMP_FLAG";

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

    private MiAttentionActivity mActivity;
    NewsList mNewsList;
    private NewsAdapter mNewsAdapter;
    private Intent mIntent;
    private List<NewsList.ObjectListBean> mObjectList;

    /**
     * true:跳转到验证身份，false:跳转到他人广场
     */
    private boolean isJumpToVerifyDataActivity = true;

    @Override
    public int initLayoutId() {
        return R.layout.activity_miattention;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("关注");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isJumpToVerifyDataActivity = getIntent().getBooleanExtra(EXTRA_JUMP_FLAG, true);
    }

    @Override
    public void initData() {
        mObjectList = new ArrayList<>();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mNewsAdapter = new NewsAdapter(mActivity, mObjectList);
        mSwipeTarget.setAdapter(mNewsAdapter);
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
    }

    @Override
    protected void initListener() {
        mNewsAdapter.setOnItemEntryClickListener(new NewsAdapter.OnItemEntryClickListener() {
            @Override
            public void ItemObjectList(int id, String userId, int position) {
                if (isJumpToVerifyDataActivity) {
                    mIntent = new Intent(mActivity, VerifyDataActivity.class);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                }
                mIntent.putExtra("userId", userId);
                startActivity(mIntent);
                finish();
            }
        });
    }


    @Override
    public void initInternet() {
        initNetWork(true);
    }

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    private void initNetWork(final boolean isFirst) {
        if (isFirst) {
            mRequestPageIndex = 1;
        } else {
            mRequestPageIndex++;
        }
        RetrofitUtil.createService(NewService.class)
                .myConcern(mUserToken, mRequestPageIndex, mRequestRows)
                .enqueue(new Callback<NewsList>() {
                    @Override
                    public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        if (response.isSuccessful()) {
                            mNewsList = response.body();
                            if (mNewsList.getResultCode() == 1) {
                                if (isFirst) {
                                    mObjectList.clear();
                                    mObjectList.addAll(mNewsList.getObjectList());
                                } else {
                                    mObjectList.addAll(mNewsList.getObjectList());
                                }
                                mNewsAdapter.notifyDataSetChanged();
                            } else if (mNewsList.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (mNewsList.getResultCode() == 0) {
                                Toasty.normal(mActivity, mNewsList.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                            if (mObjectList != null && mObjectList.size() == 0) {
                                mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "关注别人，更快了解TA的动态更新");
                            } else {
                                mPaProgress.showContent();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsList> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }
}
