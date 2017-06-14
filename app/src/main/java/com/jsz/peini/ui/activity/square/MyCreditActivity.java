package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.MyCreditBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.view.TextProgressBar;
import com.jsz.peini.ui.view.square.MyColorProgressBar;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/16.
 */
public class MyCreditActivity extends BaseActivity {
    @InjectView(R.id.title)
    TextView mTitle;

    @InjectView(R.id.tpb_self_info_score)
    TextProgressBar mTpbSelfInfoScore;
    @InjectView(R.id.tpb_id_card_score)
    TextProgressBar mTpbIdCardScore;
    @InjectView(R.id.tpb_task_score)
    TextProgressBar mTpbTaskScore;
    @InjectView(R.id.cpb_credit_score)
    MyColorProgressBar mCpbCreditScore;
    @InjectView(R.id.iv_credit_background)
    ImageView mIvCreditBackground;

    public MyCreditActivity mActivity;
    private Intent mIntent;

    @Override
    public int initLayoutId() {
        return R.layout.activity_my_credit;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("我的信用");
        int imageWidth = getImageBackgroundWidth();
        mCpbCreditScore.setDiameter(imageWidth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNetWork();
    }

    /**
     * 网络访问
     */
    private void initNetWork() {
        RetrofitUtil.createService(SquareService.class)
                .getCredit(mUserToken).enqueue(new Callback<MyCreditBean>() {
            @Override
            public void onResponse(Call<MyCreditBean> call, Response<MyCreditBean> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 1) {
                        LogUtil.d(response.body().toString());
                        MyCreditBean.IsMyCreditBean myCredit = response.body().getMyCredit();

                        int creditScore = myCredit.getCreditNum();
                        int selfInfoScore = myCredit.getSelfNum();
                        int idCardScore = myCredit.getIdcardNum();
                        int taskScore = myCredit.getTaskNum();
                        String updateTime = myCredit.getUpdateTime();

                        String dateStr = updateTime.split(" ")[0];
                        mCpbCreditScore.setUnit("评估于" + dateStr);
                        mCpbCreditScore.setCurrentValues(creditScore);
                        mTpbSelfInfoScore.setProgress(selfInfoScore);
                        mTpbIdCardScore.setProgress(idCardScore);
                        mTpbTaskScore.setProgress(taskScore);

                    } else {
                        Toasty.normal(mActivity, response.body().getResultDesc()).show();
                        LogUtil.d(response.body().toString());
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyCreditBean> call, Throwable t) {
                 Toasty.error(mActivity,Conversion.NETWORKERROR).show();
                finish();
            }
        });
    }

    @OnClick({R.id.toolbar, R.id.ll_mimessage, R.id.ll_identity, R.id.ll_mitask})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.ll_mimessage:
                mIntent = new Intent(mActivity, TaSquareMessageActivity.class);
                mIntent.putExtra(Conversion.TYPE, "1");
                startActivity(mIntent);
                break;
            case R.id.ll_identity:

                if (mTpbIdCardScore.getProgress() == 100) {
                    return;
                }
                startActivity(new Intent(mActivity, IdentityAuthenticationActivity.class));
                break;
            case R.id.ll_mitask:
                startActivity(new Intent(mActivity, MiTaskActivity.class));
                break;
        }
    }

    private int getImageBackgroundWidth() {
        return mIvCreditBackground.getLayoutParams().width;
    }
}
