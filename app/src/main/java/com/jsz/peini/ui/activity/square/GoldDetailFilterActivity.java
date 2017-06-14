package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/3/3.
 */
public class GoldDetailFilterActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;

    @InjectView(R.id.tv_type_all)
    TextView tvTypeAll;
    @InjectView(R.id.tv_type_recharge)
    TextView tvTypeRecharge;
    @InjectView(R.id.tv_type_gift)
    TextView tvTypeGift;
    @InjectView(R.id.tv_type_purchase)
    TextView tvTypePurchase;
    @InjectView(R.id.tv_type_transfer)
    TextView tvTypeTransfer;
    @InjectView(R.id.tv_stream_all)
    TextView tvStreamAll;
    @InjectView(R.id.tv_stream_income)
    TextView tvStreamIncome;
    @InjectView(R.id.tv_stream_payout)
    TextView tvStreamPayout;

    private GoldDetailFilterActivity mActivity;

    /**
     * 订单类型
     */
    String mOrderType = "";
    /**
     * 流向
     */
    String mStream = "";

    @Override
    public int initLayoutId() {
        return R.layout.activity_gold_detail_filter;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("金币筛选");
        mRightButton.setText("确认");

        mOrderType = getIntent().getStringExtra("orderType");
        mStream = getIntent().getStringExtra("stream");
        if (mOrderType == null) {
            mOrderType = "";
        }
        switch (mOrderType) {
            case "":
                tvTypeAll.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "1":
                tvTypeRecharge.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "2":
                tvTypeGift.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "3":
                tvTypePurchase.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "4":
                tvTypeTransfer.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }

        if (mStream == null) {
            mStream = "";
        }
        switch (mStream) {
            case "":
                tvStreamAll.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "1":
                tvStreamIncome.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "0":
                tvStreamPayout.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
    }

    @OnClick({R.id.toolbar, R.id.right_button,
            R.id.tv_type_all, R.id.tv_type_recharge, R.id.tv_type_gift, R.id.tv_type_purchase, R.id.tv_type_transfer,
            R.id.tv_stream_all, R.id.tv_stream_income, R.id.tv_stream_payout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.right_button:
                Intent intent = new Intent();
                intent.putExtra("orderType", mOrderType);
                intent.putExtra("stream", mStream);
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.tv_type_all:
                mOrderType = "";
                tvTypeAll.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                tvTypeRecharge.setTextColor(getResources().getColor(R.color.text333));
                tvTypeGift.setTextColor(getResources().getColor(R.color.text333));
                tvTypePurchase.setTextColor(getResources().getColor(R.color.text333));
                tvTypeTransfer.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_type_recharge:
                mOrderType = "1";
                tvTypeAll.setTextColor(getResources().getColor(R.color.text333));
                tvTypeRecharge.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                tvTypeGift.setTextColor(getResources().getColor(R.color.text333));
                tvTypePurchase.setTextColor(getResources().getColor(R.color.text333));
                tvTypeTransfer.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_type_gift:
                mOrderType = "2";
                tvTypeAll.setTextColor(getResources().getColor(R.color.text333));
                tvTypeRecharge.setTextColor(getResources().getColor(R.color.text333));
                tvTypeGift.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                tvTypePurchase.setTextColor(getResources().getColor(R.color.text333));
                tvTypeTransfer.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_type_purchase:
                mOrderType = "3";
                tvTypeAll.setTextColor(getResources().getColor(R.color.text333));
                tvTypeRecharge.setTextColor(getResources().getColor(R.color.text333));
                tvTypeGift.setTextColor(getResources().getColor(R.color.text333));
                tvTypePurchase.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                tvTypeTransfer.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_type_transfer:
                mOrderType = "4";
                tvTypeAll.setTextColor(getResources().getColor(R.color.text333));
                tvTypeRecharge.setTextColor(getResources().getColor(R.color.text333));
                tvTypeGift.setTextColor(getResources().getColor(R.color.text333));
                tvTypePurchase.setTextColor(getResources().getColor(R.color.text333));
                tvTypeTransfer.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case R.id.tv_stream_all:
                mStream = "";
                tvStreamAll.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                tvStreamIncome.setTextColor(getResources().getColor(R.color.text333));
                tvStreamPayout.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_stream_income:
                mStream = "1";
                tvStreamAll.setTextColor(getResources().getColor(R.color.text333));
                tvStreamIncome.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                tvStreamPayout.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_stream_payout:
                mStream = "0";
                tvStreamAll.setTextColor(getResources().getColor(R.color.text333));
                tvStreamIncome.setTextColor(getResources().getColor(R.color.text333));
                tvStreamPayout.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
    }
}
