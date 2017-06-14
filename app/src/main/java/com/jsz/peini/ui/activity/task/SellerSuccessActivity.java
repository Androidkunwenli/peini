package com.jsz.peini.ui.activity.task;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.eventbus.TaskEvaluationSuccess;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.assess.AssessService;
import com.jsz.peini.ui.activity.square.VerifyDataActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2016/12/7.
 */
public class SellerSuccessActivity extends BaseActivity {
    @InjectView(R.id.ratingBar1)
    XLHRatingBar ratingBar1;
    @InjectView(R.id.ratingBar2)
    XLHRatingBar ratingBar2;
    @InjectView(R.id.ratingBar3)
    XLHRatingBar ratingBar3;
    @InjectView(R.id.ratingBar4)
    XLHRatingBar ratingBar4;
    @InjectView(R.id.ratingBar5)
    XLHRatingBar ratingBar5;
    @InjectView(R.id.ratingBar6)
    XLHRatingBar ratingBar6;
    @InjectView(R.id.seller_success)
    Button sellerSuccess;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.cb_no_areward)
    CheckBox mCbNoAreward;
    @InjectView(R.id.et_areward_numbar)
    EditText mEtArewardNumbar;
    @InjectView(R.id.cb_yes_areward)
    CheckBox mCbYesAreward;
    @InjectView(R.id.tv_sellername)
    TextView mTvSellername;
    @InjectView(R.id.tv_taskname)
    TextView mTvTaskname;
    //    @InjectView(R.id.tv_areward)
//    TextView mTvAreward;
    @InjectView(R.id.sv_evaluate)
    ScrollView mSvEvaluate;
    //    @InjectView(R.id.sv_areward)
//    ScrollView mSvAreward;
    private int countSelected1;
    private int countSelected2;
    private int countSelected3;
    private int countSelected4;
    private int countSelected5;
    private int countSelected6;
    private String mOrderId;
    private String mTaskId;
    private String mSellerinfoid;
    private String mUserId;
    private SellerSuccessActivity mActivity;
    private Intent mIntent;

    @Override
    public int initLayoutId() {
        return R.layout.activity_seller_success;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("信用评价");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSvEvaluate.setVisibility(View.VISIBLE);
//        mSvAreward.setVisibility(View.GONE);
        Intent intent = getIntent();
        //任务id
        mTaskId = intent.getStringExtra(Conversion.TASKID);
        //店铺id
        mSellerinfoid = intent.getStringExtra(Conversion.SELLERINFOID);
        //他人token
        mUserId = intent.getStringExtra(Conversion.USERID);
        //商家名字
        String mSellernmae = intent.getStringExtra(Conversion.SELLERNMAE);
        //他人mane
        String mOthersrnmae = intent.getStringExtra(Conversion.OTHERSRNMAE);
        //订单id
        mOrderId = intent.getStringExtra(Conversion.ORDERID);
        //商家名字
        mTvSellername.setText(mSellernmae);
        //他人名字
        mTvTaskname.setText(mOthersrnmae);
        LogUtil.d("评价的id" + mTaskId + "他人token" + mUserId + "商家名字" + mSellernmae + "他人name" + mOthersrnmae + "订单id" + mOrderId);
    }

    @Override
    public void initData() {
        /**得到星星的个数*/
        ratingBar1.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected1 = countSelected;
            }
        });
        ratingBar2.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected2 = countSelected;
            }
        });
        ratingBar3.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected3 = countSelected;
            }
        });
        ratingBar4.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected4 = countSelected;
            }
        });
        ratingBar5.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected5 = countSelected;
            }
        });
        ratingBar6.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected6 = countSelected;
            }
        });

        sellerSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.i("请假的星星个数", "" + countSelected1 + "  " + countSelected2 + "  " + countSelected3);
                if (countSelected1 == 0 || countSelected2 == 0 || countSelected3 == 0) {
                    Toasty.normal(mActivity, "请先进行店铺打分！").show();
                    return;
                }
                if (countSelected4 == 0 || countSelected5 == 0 || countSelected6 == 0) {
                    Toasty.normal(mActivity, "请先进行店铺打分！").show();
                    return;
                }
                initNetWork();
            }
        });

    }

    private void initNetWork() {
        mDialog.show();
        RetrofitUtil.createService(AssessService.class)
                .sellerAndUserJudge(
                        mSellerinfoid,//店铺id
                        mTaskId,//任务id
                        String.valueOf(countSelected1 * 20),
                        String.valueOf(countSelected2 * 20),
                        String.valueOf(countSelected3 * 20),
                        SpUtils.getUserToken(mActivity),//评分用户id
                        mUserId,//被评分任务id
                        String.valueOf(countSelected4 * 20),
                        String.valueOf(countSelected5 * 20),
                        String.valueOf(countSelected6 * 20),
                        mOrderId).enqueue(new Callback<SuccessfulBean>() {
            @Override
            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful()) {
                    mDialog.dismiss();
                    SuccessfulBean body = response.body();
                    if (body.getResultCode() == 1) {
                        EventBus.getDefault().post(new TaskEvaluationSuccess(true));
                        finish();
//                        mSvEvaluate.setVisibility(View.GONE);
//                        mSvAreward.setVisibility(View.VISIBLE);
                    } else if (body.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else {
                        Toasty.normal(mActivity, body.getResultDesc()).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                mDialog.dismiss();
                finish();
            }
        });
    }

//    @OnClick(R.id.tv_areward)
//    public void onClick() {
//        mIntent = new Intent(mActivity, VerifyDataActivity.class);
//        mIntent.putExtra(Conversion.USERID, mUserId);
//        mIntent.putExtra(Conversion.TASKID, mTaskId);
//        startActivity(mIntent);
//        finish();
//    }
}
