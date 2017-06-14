package com.jsz.peini.ui.fragment.news;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.base.CustomLoadMoreView;
import com.jsz.peini.model.news.NewsList;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.news.NewService;
import com.jsz.peini.ui.activity.news.ContactPhoneActivity;
import com.jsz.peini.ui.activity.news.FansActivity;
import com.jsz.peini.ui.activity.news.SystemMessageActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.adapter.news.NewsRecyclerviewAdapter;
import com.jsz.peini.ui.view.RecyclerViewDivider;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/12/2.
 */
public class NewsFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    private static final String TAG = "NewsFragment";
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
    private NewsRecyclerviewAdapter mAdapter;
    private Intent mIntent;
    private CircleImageView mImageHead;

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_news);
    }

    @Override
    public void initData() {
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new NewsRecyclerviewAdapter(mActivity);
        mAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.addHeaderView(getHeadView());
        mSwipeTarget.setAdapter(mAdapter);
        //上啦加载
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRequestPageIndex = 1;
                initNetWork();
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String userId = ((NewsList.ObjectListBean) adapter.getData().get(position)).getUserId();
                if (userId.equals(SpUtils.getUserToken(mActivity))) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra("userId", userId);
                    startActivity(mIntent);
                }
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

    //访问网络
    private void initNetWork() {
        RetrofitUtil.createService(NewService.class)
                .myConcern(mUserToken, mRequestPageIndex, mRequestRows)
                .enqueue(new Callback<NewsList>() {
                    @Override
                    public void onResponse(Call<NewsList> call, Response<NewsList> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        if (response.isSuccessful()) {
                            NewsList mNewsList = response.body();
                            if (mNewsList.getResultCode() == 1) {
                                List<NewsList.ObjectListBean> objectList = mNewsList.getObjectList();
                                if (mRequestPageIndex == 1) {
                                    mAdapter.setNewData(objectList);
                                    if (objectList != null && objectList.size() == 0) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "关注别人，更快了解TA的动态更新。");
                                    }
                                    setHeadView(mNewsList);
                                } else {
                                    mAdapter.addData(objectList);
                                }
                                mAdapter.loadMoreComplete();
                                if (objectList != null && objectList.size() == mRequestRows) {
                                    mRequestPageIndex += 1;
                                } else {
                                    mAdapter.loadMoreEnd();
                                }
                            } else if (mNewsList.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                mAdapter.loadMoreFail();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsList> call, Throwable t) {
                        mAdapter.loadMoreFail();
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    //添加头布局
    public View getHeadView() {
        View view = UiUtils.inflate(mActivity, R.layout.news_head);
        //粉丝头像
        mImageHead = (CircleImageView) view.findViewById(R.id.imageHead);
        RelativeLayout tlContact = (RelativeLayout) view.findViewById(R.id.tl_contact);//通讯录
        RelativeLayout rlFollower = (RelativeLayout) view.findViewById(R.id.rl_follower);//粉丝
        RelativeLayout rlFollow = (RelativeLayout) view.findViewById(R.id.rl_follow);//关注
        tlContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mActivity, ContactPhoneActivity.class);
                startActivity(mIntent);
            }
        });
        rlFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mActivity, FansActivity.class);
                startActivity(mIntent);
            }
        });
        rlFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.normal(mActivity, "没有东西呢").show();
            }
        });
        return view;
    }

    public void setHeadView(NewsList headView) {
        if (headView != null) {
            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + headView.getImageHead(), mImageHead, "7");

        }
    }
}
