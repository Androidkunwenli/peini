package com.jsz.peini.ui.fragment.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.model.eventbus.TaskEvaluationSuccess;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.assess.AssessService;
import com.jsz.peini.ui.activity.square.VerifyDataActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovo on 2017/4/27.
 */

public class TaskPaySuccessFragmnet extends BaseFragment {
    @InjectView(R.id.imageView4)
    ImageView mImageView4;
    @InjectView(R.id.textView2)
    TextView mTextView2;
    @InjectView(R.id.tv_sellername)
    TextView mTvSellername;
    @InjectView(R.id.ratingBar1)
    XLHRatingBar mRatingBar1;
    @InjectView(R.id.ratingBar2)
    XLHRatingBar mRatingBar2;
    @InjectView(R.id.ratingBar3)
    XLHRatingBar mRatingBar3;
    @InjectView(R.id.tv_taskname)
    TextView mTvTaskname;
    @InjectView(R.id.ratingBar4)
    XLHRatingBar mRatingBar4;
    @InjectView(R.id.ratingBar5)
    XLHRatingBar mRatingBar5;
    @InjectView(R.id.ratingBar6)
    XLHRatingBar mRatingBar6;
    @InjectView(R.id.cb_no_areward)
    CheckBox mCbNoAreward;
    @InjectView(R.id.et_areward_numbar)
    EditText mEtArewardNumbar;
    @InjectView(R.id.cb_yes_areward)
    CheckBox mCbYesAreward;
    @InjectView(R.id.seller_success)
    Button mSellerSuccess;
    @InjectView(R.id.sv_evaluate)
    ScrollView mSvEvaluate;
    @InjectView(R.id.tv_areward)
    TextView mTvAreward;
    @InjectView(R.id.sv_areward)
    ScrollView mSvAreward;
    private String mTaskId;
    private String mSellerinfoid;
    private String mOthersUserId;
    private String mOrderId;
    private int countSelected1;
    private int countSelected2;
    private int countSelected3;
    private int countSelected4;
    private int countSelected5;
    private int countSelected6;
    private Intent mIntent;

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_taskpay_success);
    }

    /*获取数据*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            //任务id
            mTaskId = bundle.getString(Conversion.TASKID);
            //店铺id
            mSellerinfoid = bundle.getString(Conversion.SELLERINFOID);
            //他人token
            mOthersUserId = bundle.getString(Conversion.USERID);
            //订单id
            mOrderId = bundle.getString(Conversion.ORDERID);
            //商家名字
            String mSellernmae = bundle.getString(Conversion.SELLERNMAE);
            //他人mane
            String mOthersrnmae = bundle.getString(Conversion.OTHERSRNMAE);
            //商家名字
            mTvSellername.setText(mSellernmae);
            //他人名字
            mTvTaskname.setText(mOthersrnmae);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initData() {
        /**得到星星的个数*/
        mRatingBar1.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected1 = countSelected;
            }
        });
        mRatingBar2.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected2 = countSelected;
            }
        });
        mRatingBar3.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected3 = countSelected;
            }
        });
        mRatingBar4.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected4 = countSelected;
            }
        });
        mRatingBar5.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected5 = countSelected;
            }
        });
        mRatingBar6.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
            @Override
            public void onChange(int countSelected) {
                countSelected6 = countSelected;
            }
        });

        mSellerSuccess.setOnClickListener(new View.OnClickListener() {
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
        RetrofitUtil.createService(AssessService.class)
                .sellerAndUserJudge(
                        mSellerinfoid,//店铺id
                        mTaskId,//任务id
                        String.valueOf(countSelected1 * 20),
                        String.valueOf(countSelected2 * 20),
                        String.valueOf(countSelected3 * 20),
                        SpUtils.getUserToken(mActivity),
                        mOthersUserId,//被评分任务id
                        String.valueOf(countSelected4 * 20),
                        String.valueOf(countSelected5 * 20),
                        String.valueOf(countSelected6 * 20),
                        mOrderId).enqueue(new Callback<SuccessfulBean>() {
            @Override
            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful()) {
                    SuccessfulBean body = response.body();
                    if (body.getResultCode() == 1) {
                        EventBus.getDefault().post(new TaskEvaluationSuccess(true));
                        mSvEvaluate.setVisibility(View.GONE);
                        mSvAreward.setVisibility(View.VISIBLE);
                    } else if (body.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else {
                        Toasty.normal(mActivity, body.getResultDesc()).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {

                mActivity.finish();
            }
        });
    }

    @OnClick(R.id.tv_areward)
    public void onClick() {
        mIntent = new Intent(mActivity, VerifyDataActivity.class);
        mIntent.putExtra(Conversion.USERID, mOthersUserId);
        mIntent.putExtra(Conversion.TASKID, mTaskId);
        mActivity.startActivity(mIntent);
        mActivity.finish();
    }
}
