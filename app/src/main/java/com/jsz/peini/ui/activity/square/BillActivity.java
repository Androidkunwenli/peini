package com.jsz.peini.ui.activity.square;

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
import com.jsz.peini.model.square.MiBillBean;
import com.jsz.peini.model.square.MiBillBean.OrderInfoListBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.pay.PaySuccessActivity;
import com.jsz.peini.ui.activity.pay.PaythebillActivity;
import com.jsz.peini.ui.activity.pay.WebOrderDetailsActivity;
import com.jsz.peini.ui.activity.task.SellerSuccessActivity;
import com.jsz.peini.ui.adapter.square.OrderListAdapter;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.widget.ProgressActivity;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by th on 2017/1/11.
 */
public class BillActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private BillActivity mActivity;
    private OrderListAdapter mQuickAdapter;

    private int mRequestPageIndex = 1;
    private final int mRequestRows = 10;

    /**
     * 订单类型
     * 1:任务买单；3:到店买单；4:打赏；5:金币转账
     */
    String mOrderType = "";
    /**
     * 状态
     */
    String status = "";
    /**
     * 支付方式
     */
    String payType = "";

    @Override
    public int initLayoutId() {
        return R.layout.activity_bill;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("消费订单");
        mRightButton.setText("筛选");
    }

    @Override
    public void initData() {
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mQuickAdapter = new OrderListAdapter(mActivity);
        mQuickAdapter.setOnLoadMoreListener(this, mSwipeTarget);
        mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
        mSwipeTarget.setAdapter(mQuickAdapter);

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRequestAllOrder(false);
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
//        initRequestAllOrder(false);
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRequestAllOrder(false);
    }

    @Override
    protected void initListener() {

        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderInfoListBean item = (OrderInfoListBean) adapter.getItem(position);
                String orderStatus = item.getOrderStatus();
                //-1已取消；0待付款；1待评价；2已完成；
                switch (orderStatus) {
                    case "-1":
                        LogUtil.d("已取消的订单");
                        break;
                    case "0":
                        onClickUnPay(item);
                        break;
                    case "1":
                        onClickUnEvaluate(item);
                        break;
                    case "2":
                        onClickCompleted(item);
                        break;
                }

            }
        });

    }

    private void onClickUnPay(OrderInfoListBean item) {
        LogUtil.d("需要支付的的订单--待付款");
        String orderType = item.getOrderType();
        switch (orderType) {
            case "1":       //任务买单
                String taskId = item.getTaskId();
                String sellerIdForTask = item.getSellerId();
                if (TextUtils.isEmpty(taskId) || TextUtils.isEmpty(sellerIdForTask)) {
                    return;
                }
                Intent payBillIntentForTask = new Intent(mActivity, PaythebillActivity.class);
                payBillIntentForTask.putExtra(Conversion.SELLERINFOID, sellerIdForTask);
                payBillIntentForTask.putExtra(Conversion.TASKID, taskId);
                payBillIntentForTask.putExtra(Conversion.TYPE, "2");
                startActivity(payBillIntentForTask);
                break;
            case "3":   //到店买单
                String sellerId = item.getSellerId();
                if (TextUtils.isEmpty(sellerId)) {
                    return;
                }
                Intent mIntent = new Intent(mActivity, PaythebillActivity.class);
                mIntent.putExtra(Conversion.SELLERINFOID, sellerId);
                mIntent.putExtra(Conversion.TYPE, "1");
                startActivity(mIntent);
                break;
            case "5":   //金币转赠
                String otherUserId = item.getOtherUserId();
                String orderId = item.getPayId();
                String payMoney = item.getPayMoney();
                if (TextUtils.isEmpty(otherUserId) || TextUtils.isEmpty(orderId) || TextUtils.isEmpty(payMoney)) {
                    return;
                }
                Intent verifyDataIntent = new Intent(mActivity, VerifyDataActivity.class);
                verifyDataIntent.putExtra(Conversion.USERID, otherUserId);
                verifyDataIntent.putExtra(Conversion.ORDERIDS, orderId);
                verifyDataIntent.putExtra(Conversion.NUMBAR, payMoney);
                startActivity(verifyDataIntent);
                break;
        }
    }

    private void onClickUnEvaluate(OrderInfoListBean item) {
        LogUtil.d("需要评价的订单--未评价");
        String orderType = item.getOrderType();
        switch (orderType) {
            case "1"://任务买单
                String sellerId = item.getSellerId();
                String taskId = item.getTaskId();
                String OrderId = item.getOrderId();
                String otherUserId = item.getOtherUserId();
                String title = item.getTitle();
                String otherNickName = item.getOtherNickName();
                if (TextUtils.isEmpty(sellerId)
                        || TextUtils.isEmpty(taskId)
                        || TextUtils.isEmpty(OrderId)
                        || TextUtils.isEmpty(otherUserId)
                        || TextUtils.isEmpty(title)
                        || TextUtils.isEmpty(otherNickName)) {
                    return;
                }
                Intent sellerSuccessIntent = new Intent(mActivity, SellerSuccessActivity.class);
                sellerSuccessIntent.putExtra(Conversion.TASKID, taskId);
                sellerSuccessIntent.putExtra(Conversion.SELLERINFOID, sellerId);
                sellerSuccessIntent.putExtra(Conversion.SELLERNMAE, title);
                sellerSuccessIntent.putExtra(Conversion.OTHERSRNMAE, otherNickName);
                sellerSuccessIntent.putExtra(Conversion.USERID, otherUserId);
                sellerSuccessIntent.putExtra(Conversion.ORDERID, OrderId);
                startActivity(sellerSuccessIntent);
                break;
            case "3"://到店买单
                String title1 = item.getTitle();
                String orderId1 = item.getOrderId();
                String sellerId1 = item.getSellerId();
                if (TextUtils.isEmpty(title1)
                        || TextUtils.isEmpty(orderId1)
                        || TextUtils.isEmpty(sellerId1)) {
                    return;
                }
                Intent mIntent = new Intent(mActivity, PaySuccessActivity.class);
                mIntent.putExtra(Conversion.SELLERINFONAME, title1);
                mIntent.putExtra(Conversion.SELLERINFOID, sellerId1);
                mIntent.putExtra(Conversion.ORDERID, orderId1);
                startActivity(mIntent);
                break;
        }
    }

    private void onClickCompleted(OrderInfoListBean item) {
        String orderId = item.getOrderId();
        String sellerId = item.getSellerId();
        String title = item.getTitle();
        if (TextUtils.isEmpty(orderId) || TextUtils.isEmpty(sellerId) || TextUtils.isEmpty(title)) {
            return;
        }
        Intent intent = new Intent(mActivity, WebOrderDetailsActivity.class);
        intent.putExtra(Conversion.ORDERID, String.valueOf(orderId));
        intent.putExtra(Conversion.SELLERINFOID, String.valueOf(sellerId));
        intent.putExtra(Conversion.SELLERINFONAME, String.valueOf(title));
        startActivity(intent);
    }

    private void initRequestAllOrder(boolean isShowProgress) {
        mRequestPageIndex = 1;
        requestOrderList(isShowProgress);
    }

    private void requestOrderList(boolean isShowProgress) {

        if (isShowProgress) {
            mDialog.show();
        }
        RetrofitUtil.createService(SquareService.class)
                .getMyOrderList(
                        mUserToken,
                        mOrderType,
                        status,
                        payType,
                        mRequestPageIndex,
                        mRequestRows
                )
                .enqueue(new RetrofitCallback<MiBillBean>() {
                    @Override
                    public void onSuccess(Call<MiBillBean> call, Response<MiBillBean> response) {
                        mDialog.dismiss();
                        mSwipeToLoadLayout.setRefreshing(false);
                        if (response.isSuccessful()) {
                            MiBillBean body = response.body();
                            if (body.getResultCode() == 1) {
                                List<OrderInfoListBean> orderInfoList = body.getOrderInfoList();
                                if (mRequestPageIndex == 1) {
                                    mQuickAdapter.setNewData(orderInfoList);
                                    if (mQuickAdapter.getItemCount() == 0 && mRequestPageIndex == 1) {
                                        mPaProgress.showSquareNullData(getResources()
                                                .getDrawable(R.drawable.data_error), "转账、充值、" +
                                                "任务买单、商家买单都会形成账单，陪你会记录您的每一" +
                                                "笔消费记录。");
                                    } else {
                                        mPaProgress.showContent();
                                    }
                                } else {
                                    mQuickAdapter.addData(orderInfoList);
                                }

                                mQuickAdapter.loadMoreComplete();
                                if (orderInfoList != null
                                        && orderInfoList.size() == mRequestRows) {
                                    mRequestPageIndex += 1;
                                } else {
                                    mQuickAdapter.loadMoreEnd();
                                }
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (response.body().getResultCode() == 0) {
                                mQuickAdapter.loadMoreFail();
                                Toasty.normal(mActivity, response.body().getResultDesc()).show();
                            } else {
                                mQuickAdapter.loadMoreFail();
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        } else {
                            mQuickAdapter.loadMoreFail();
                            Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MiBillBean> call, Throwable t) {
                        mDialog.dismiss();
                        mSwipeToLoadLayout.setRefreshing(false);
                        mQuickAdapter.loadMoreFail();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == Conversion.DATA_SUCCESS && resultCode == Conversion.DATASUCCESS) {
                mOrderType = data.getStringExtra("orderType");
                status = data.getStringExtra("status");
                payType = data.getStringExtra("payType");
                initRequestAllOrder(false);
                LogUtil.d("筛选返回的数据" + mOrderType + "--" + status + "--" + payType);
            }
        }
    }

    @OnClick({R.id.toolbar, R.id.right_button})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;

            case R.id.right_button:
                Intent intent = new Intent(mActivity, BillScreenActivity.class);
                intent.putExtra("mOrderType", mOrderType);  //订单类型
                intent.putExtra("status", status);          //状态
                intent.putExtra("payType", payType);        //支付方式
                startActivityForResult(intent, Conversion.DATA_SUCCESS);
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        requestOrderList(false);
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
}
