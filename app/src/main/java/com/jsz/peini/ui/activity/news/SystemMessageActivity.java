package com.jsz.peini.ui.activity.news;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.tabulation.SystemMessageListBean;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.TaskMessageActivity;
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.activity.square.ExchangeActivity;
import com.jsz.peini.ui.activity.square.IntegralDetailActivity;
import com.jsz.peini.ui.activity.square.MiCouponActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.MyWealthActivity;
import com.jsz.peini.ui.activity.square.OfficialActivity;
import com.jsz.peini.ui.activity.square.TaPhotoImageActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareHomepageActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.ui.adapter.news.SystemMessageAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 系统消息
 * Created by huizhe.ju on 2017/3/17.
 */
public class SystemMessageActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private SystemMessageActivity mActivity;
    private SystemMessageAdapter mQuickAdapter;

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    private Intent mIntent;

    @Override
    public int initLayoutId() {
        return R.layout.activity_system_message;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("系统消息");

        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mQuickAdapter = new SystemMessageAdapter();
        mQuickAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSwipeTarget.setAdapter(mQuickAdapter);
        //下拉刷新
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRequestSystemMsg();
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    private void initRequestSystemMsg() {
        mRequestPageIndex = 1;
        requestSystemMsg();
    }

    @Override
    protected void initListener() {
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int smstype = ((SystemMessageListBean.DataBean) adapter.getData().get(position)).getSmstype();
                String paramid = ((SystemMessageListBean.DataBean) adapter.getData().get(position)).getParamid();
                String mFrom = ((SystemMessageListBean.DataBean) adapter.getData().get(position)).getFrom();
                if (smstype == 0 && TextUtils.isEmpty(paramid)) {
                    Toasty.normal(mActivity, "缺少参数").show();
                    return;
                }
                switch (smstype) {
                    case 1://跳转-对话页面
                    case 4://跳转任务详情
                    case 6:
                    case 7:
                    case 5:
                    case 8://跳转任务详情
                    case 103://跳转任务详情
                    case 104://未评价跳转-评价页面---已评价跳转-任务详情
                    case 107://跳转任务详情
                    case 108://跳转任务详情
                    case 109://跳转任务详情
                    case 116://  2017/5/5 韩总说加的,消息也是116....
                        mIntent = new Intent(mActivity, TaskDetailActivity.class);
                        mIntent.putExtra(Conversion.ID, paramid);
                        mIntent.putExtra(Conversion.TYPE, "1");
                        break;
                    case 2://任务取消通知
                        mIntent = new Intent(mActivity, TaskMessageActivity.class);
                        mIntent.putExtra(Conversion.TASKID, paramid);
                        mIntent.putExtra(Conversion.PHONE, mFrom);
                        break;
                    case 3://优惠券过期通知
                    case 10://优惠券通知
                        mIntent = new Intent(mActivity, MiCouponActivity.class);
                        break;
                    case 102://跳转-店铺详情
                    case 101: //未评价跳转-评价店铺页----已评价跳转-店铺详情
                        mIntent = new Intent(mActivity, SellerMessageActivity.class);
                        mIntent.putExtra(Conversion.ID, paramid);
                        mIntent.putExtra(Conversion.TYPE, "1");
                        break;
                    case 110: //我的财富
                    case 111: //我的财富
                    case 112: //我的财富
                        mIntent = new Intent(mActivity, MyWealthActivity.class);
                        break;
                    case 115://跳转活动详情
                    case 209://跳转活动详情
                        mIntent = new Intent(mActivity, OfficialActivity.class);
                        mIntent.putExtra(Conversion.URl, paramid);
                        break;

                    //200 以上
                    case 201: //完善信息
                        if (SpUtils.isCompleteUserInfo(mActivity)) {
                            Toasty.normal(mActivity, "您已经完成了信息").show();
                        } else {
                            String userToken = SpUtils.getUserToken(mActivity);
                            if (!TextUtils.isEmpty(userToken)) {
                                mIntent = new Intent(mActivity, CompleteUserInfoActivity.class);
                                mIntent.putExtra("extra_user_id_flag", userToken);
                            }
                        }
                        break;
                    case 202:
                    case 203:
                    case 210://我的空间
                        MiSquareActivity.actionShow(mActivity);
                        break;
                    case 204:  //首页
                        mIntent = new Intent(mActivity, HomeActivity.class);
                        mIntent.putExtra(Conversion.TYPE, 10);
                        break;
                    case 205: //他人空间
                        mIntent = new Intent(mActivity, TaSquareHomepageActivity.class);
                        mIntent.putExtra(Conversion.TOKEN, paramid);
                        break;
                    case 206://任务
                        mIntent = new Intent(mActivity, TaskDetailActivity.class);
                        mIntent.putExtra(Conversion.ID, paramid);
                        break;
                    case 207://他人空间
                        mIntent = new Intent(mActivity, TaPhotoImageActivity.class);
                        mIntent.putExtra("extra_other_user_id", paramid);
                        break;
                    case 208://每天首次登录 粉丝
                        mIntent = new Intent(mActivity, FansActivity.class);
                        break;
                    case 113://积分上传
                        mIntent = new Intent(mActivity, ExchangeActivity.class);
                        break;
                    case 114://积分商城
                        mIntent = new Intent(mActivity, IntegralDetailActivity.class);
                        break;
                }
                startActivity(mIntent);
            }
        });
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

    @Override
    public void onLoadMoreRequested() {
        requestSystemMsg();
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
            return R.id.load_more_load_end_view;
        }
    }

    /**
     * 发起网络请求
     */
    private void requestSystemMsg() {
        RetrofitUtil.createService(TaskService.class)
                .getUserSmsList(mUserToken, mRequestPageIndex, mRequestRows)
                .enqueue(new RetrofitCallback<SystemMessageListBean>() {
                             @Override
                             public void onSuccess(Call<SystemMessageListBean> call, Response<SystemMessageListBean> response) {
                                 mSwipeToLoadLayout.setRefreshing(false);
                                 if (response.isSuccessful()) {
                                     SystemMessageListBean body = response.body();
                                     if (body.getResultCode() == 1) {
                                         List<SystemMessageListBean.DataBean> systemMsgList = body.getData();
                                         if (mRequestPageIndex == 1) {
                                             mQuickAdapter.setNewData(systemMsgList);
                                         } else {
                                             mQuickAdapter.addData(systemMsgList);
                                         }

                                         mQuickAdapter.loadMoreComplete();
                                         if (systemMsgList != null && systemMsgList.size() == mRequestRows) {
                                             mRequestPageIndex += 1;
                                         } else {
                                             mQuickAdapter.loadMoreEnd();
                                         }
                                     } else if (response.body().getResultCode() == 9) {
                                         LoginDialogUtils.isNewLogin(mActivity);
                                     } else {
                                         mQuickAdapter.loadMoreFail();
                                         Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                     }
                                 }
                             }

                             @Override
                             public void onFailure(Call<SystemMessageListBean> call, Throwable t) {
                                 mSwipeToLoadLayout.setRefreshing(false);
                                 mQuickAdapter.loadMoreFail();
                                 Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                             }
                         }

                );
    }
}
