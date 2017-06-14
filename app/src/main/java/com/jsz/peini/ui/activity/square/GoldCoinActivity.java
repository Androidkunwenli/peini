package com.jsz.peini.ui.activity.square;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.square.DonationDelData;
import com.jsz.peini.model.square.RecentDonationData;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.setting.PayPasswordActivity;
import com.jsz.peini.ui.adapter.square.GoldCoinAdapter;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
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
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/21.
 */
public class GoldCoinActivity extends BaseActivity {
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
    private List<RecentDonationData.DataBean.ListBean> mList;
    private GoldCoinActivity mActivity;
    private GoldCoinAdapter mGoldCoinAdapter;
    private Intent mIntent;
    private String mType;

    @Override
    public int initLayoutId() {
        return R.layout.activity_goldcoin;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("金币转账");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    int pageNumber = 1;

    @Override
    public void initData() {
        mList = new ArrayList<>();
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mGoldCoinAdapter = new GoldCoinAdapter(mActivity, mList);
        mSwipeTarget.setAdapter(mGoldCoinAdapter);
        /**上啦加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initNetWork(true);
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
    }

    @Override
    protected void initListener() {
        mGoldCoinAdapter.setOnItemClickListener(new GoldCoinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String hisId, String otherId) {
                if (!TextUtils.isEmpty(otherId)) {
                    mIntent = new Intent(mActivity, VerifyDataActivity.class);
                    mIntent.putExtra(Conversion.USERID, otherId);
                    startActivity(mIntent);
                }
            }

            @Override
            public void onLongItemClick(int position, String hisId, String otherId) {
                PopDelete(hisId, position);
            }
        });
    }

    private void PopDelete(final String hisId, final int position) {
        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_goldcoin));
        View view = UiUtils.inflate(mActivity, R.layout.item_delete_recycleview);
        popwindou.init(view, Gravity.CENTER_VERTICAL | Gravity.CENTER, true);
        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//删除
                popwindou.dismiss();
                deleteItem(hisId, position);
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消
                popwindou.dismiss();
            }
        });
    }

    private void deleteItem(String hisId, final int position) {
        mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .recentDonationDel(mUserToken, Conversion.getToken(), Conversion.getNetAppA(), hisId)
                .enqueue(new RetrofitCallback<DonationDelData>() {
                    @Override
                    public void onSuccess(Call<DonationDelData> call, Response<DonationDelData> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            DonationDelData delData = response.body();
                            SpUtils.putServerB(mActivity, delData.getData().getServerB());
                            if (delData.getResultCode() == 1) {
                                mList.remove(position);
                                mGoldCoinAdapter.notifyItemRemoved(position + 1);
                                mGoldCoinAdapter.notifyDataSetChanged();
                            } else if (delData.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (delData.getResultCode() == 0) {
                                Toasty.normal(mActivity, delData.getResultDesc()).show();
                            } else if (delData.getResultCode() == 8) {
                                showDalog();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DonationDelData> call, Throwable t) {
                        mDialog.dismiss();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNetWork(true);
    }

    private void initNetWork(final boolean isFirst) {
        if (isFirst) {
            pageNumber = 1;
        } else {
            pageNumber++;
        }
        RetrofitUtil.createService(SquareService.class)
                .recentDonation(mUserToken, Conversion.getToken(), Conversion.getNetAppA(), pageNumber, "10")
                .enqueue(new RetrofitCallback<RecentDonationData>() {
                    @Override
                    public void onSuccess(Call<RecentDonationData> call, Response<RecentDonationData> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        if (response.isSuccessful()) {
                            RecentDonationData recentDonationData = response.body();
                            String serverB = recentDonationData.getData().getServerB();
                            SpUtils.putServerB(mActivity, serverB);
                            if (recentDonationData.getResultCode() == 1) {
                                LogUtil.d("recentDonationData" + recentDonationData.toString());
                                List<RecentDonationData.DataBean.ListBean> data = recentDonationData.getData().getList();
                                if (isFirst) {
                                    mList.clear();
                                    mList.addAll(data);
                                } else {
                                    mList.addAll(data);
                                }
                                mGoldCoinAdapter.notifyDataSetChanged();
                            } else if (recentDonationData.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (recentDonationData.getResultCode() == 0) {
                                Toasty.normal(mActivity, recentDonationData.getResultDesc()).show();
                            } else if (recentDonationData.getResultCode() == 8) {
                                showDalog();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RecentDonationData> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 是否有支付密码
     */
    private void showDalog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("您还没有支付密码,请先设置支付密码.")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtils.put(mActivity, "PayPassWord", true);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(mActivity, PayPasswordActivity.class));
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
