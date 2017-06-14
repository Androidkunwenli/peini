package com.jsz.peini.ui.activity.pay;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.assess.AssessService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/12/1.
 */
public class PaySuccessActivity extends BaseActivity {
    @InjectView(R.id.iv_toolbar_image)
    ImageView mIvToolbarImage;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.imageView4)
    ImageView mImageView4;
    @InjectView(R.id.textView2)
    TextView mTextView2;
    @InjectView(R.id.tv_sellerInfoName)
    TextView mTvSellerInfoName;
    @InjectView(R.id.ratingBar)
    XLHRatingBar mRatingBar;
    @InjectView(R.id.ratingBar2)
    XLHRatingBar mRatingBar2;
    @InjectView(R.id.ratingBar3)
    XLHRatingBar mRatingBar3;
    @InjectView(R.id.pay_success)
    Button mPaySuccess;
    private int countSelected1;
    private int countSelected3;
    private int countSelected2;
    private String mOrderid;
    private String mSellerinfoid;
    private PaySuccessActivity mActivity;
    private String mSellerinfoname;

    @Override
    public int initLayoutId() {
        return R.layout.activity_pay_success;
    }

    @Override
    public void initView() {
        mActivity = this;
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText("商家评价");
        mOrderid = getIntent().getStringExtra(Conversion.ORDERID);
        mSellerinfoid = getIntent().getStringExtra(Conversion.SELLERINFOID);
        mSellerinfoname = getIntent().getStringExtra(Conversion.SELLERINFONAME);

        mTvSellerInfoName.setText(mSellerinfoname);

    }

    @Override
    public void initData() {
        /**得到星星的个数*/
        mRatingBar.setOnRatingChangeListener(new XLHRatingBar.OnRatingChangeListener() {
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
    }

    @Override
    protected void initListener() {
        mPaySuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countSelected1 == 0 || countSelected2 == 0 || countSelected3 == 0) {
                    Toasty.normal(mActivity, "请先进行店铺打分！").show();
                    return;
                }
                LogUtil.i("请假的星星个数", "" + countSelected1 + "  " + countSelected2 + "  " + countSelected3);
                RetrofitUtil.createService(AssessService.class).sellerJudge
                        (mUserToken,
                                mSellerinfoid,
                                String.valueOf(countSelected1 * 20),
                                String.valueOf(countSelected2 * 20),
                                String.valueOf(countSelected3 * 20),
                                mOrderid)
                        .enqueue(new Callback<SuccessfulBean>() {
                            @Override
                            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                                if (response.isSuccessful()) {
                                    SuccessfulBean body = response.body();
                                    if (body.getResultCode() == 1) {
//                                        EventBus.getDefault().post(new TaskReleaseRefreshTaskAndMapList(true));
                                        Toasty.success(mActivity, body.getResultDesc()).show();
                                        finish();
                                    } else {
                                        Toasty.success(mActivity, body.getResultDesc()).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        });
            }
        });
    }
}
