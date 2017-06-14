package com.jsz.peini.ui.fragment.seller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xlhratingbar_lib.XLHRatingBar;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.assess.AssessService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovo on 2017/4/26.
 */

public class SellerPaySuccessFragment extends BaseFragment {

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
    private String mOrderid; //订单ID
    private String mSellerinfoid;//商家ID
    private String mSellerinfoname;//商家名字

    //得到的评分数
    private int countSelected1;
    private int countSelected2;
    private int countSelected3;

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_pay_success);
    }

    /*获取数据*/
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if (bundle != null) {
            mOrderid = bundle.getString(Conversion.ORDERID);
            mSellerinfoid = bundle.getString(Conversion.SELLERINFOID);
            mSellerinfoname = bundle.getString(Conversion.SELLERINFONAME);
            mTvSellerInfoName.setText(mSellerinfoname);
        }
        super.onActivityCreated(savedInstanceState);
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
    public void initListentr() {
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
                                        Toasty.success(mActivity, body.getResultDesc()).show();
                                        mActivity.finish();
                                    } else {
                                        Toasty.normal(mActivity, body.getResultDesc()).show();
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
