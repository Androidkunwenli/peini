package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.utils.Conversion;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by lenovo on 2017/3/3.
 */
public class BillScreenActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.tv_source0)
    TextView mTvSource0;
    @InjectView(R.id.tv_source1)
    TextView mTvSource1;
    @InjectView(R.id.tv_source3)
    TextView mTvSource3;
    @InjectView(R.id.tv_source5)
    TextView mTvSource5;
    @InjectView(R.id.tv_source4)
    TextView mTvSource4;
    @InjectView(R.id.tv_status5)
    TextView mTvStatus5;
    @InjectView(R.id.tv_status_1)
    TextView mTvStatus_1;
    @InjectView(R.id.tv_status2)
    TextView mTvStatus2;
    @InjectView(R.id.tv_status1)
    TextView mTvStatus1;
    @InjectView(R.id.tv_status0)
    TextView mTvStatus0;
    @InjectView(R.id.tv_payType0)
    TextView mTvPayType0;
    @InjectView(R.id.tv_payType1)
    TextView mTvPayType1;
    @InjectView(R.id.tv_payType2)
    TextView mTvPayType2;
    @InjectView(R.id.tv_payType3)
    TextView mTvPayType3;

    @Override
    public int initLayoutId() {
        return R.layout.activity_billscreen;
    }

    @Override
    public void initView() {
        mTitle.setText("订单筛选");
        mRightButton.setText("确认");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*   mIntent.putExtra("mOrderType", mOrderType);
                mIntent.putExtra("status", status);
                mIntent.putExtra("stream", stream);
                mIntent.putExtra("stream", stream);*/

        Intent intent = getIntent();
        orderType = intent.getStringExtra("mOrderType");
        status = intent.getStringExtra("status");
        payType = intent.getStringExtra("payType");
        /*1任务买单；3到店买单；4打赏；5金币转赠
        -1已取消；2已完成；0待付款 1待评价
        1收入；0支出
        1金币；2微信；3支付宝
        */
        switch (orderType) {
            case "":
                mTvSource0.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "1":
                mTvSource1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "3":
                mTvSource3.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "4":
                mTvSource4.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "5":
                mTvSource5.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
        switch (status) {
            case "":
                mTvStatus5.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "-1":
                mTvStatus_1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "2":
                mTvStatus2.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "0":
                mTvStatus0.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "1":
                mTvStatus1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
        switch (payType) {
            case "":
                mTvPayType0.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "1":
                mTvPayType1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "2":
                mTvPayType2.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "3":
                mTvPayType3.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
    }

    /**
     * 订单类型
     */
    String orderType = "";
    /**
     * 状态
     */
    String status = "";
    /**
     * 支付方式
     */
    String payType = "";

    @OnClick({R.id.right_button, R.id.tv_source0, R.id.tv_source1, R.id.tv_source3, R.id.tv_source5, R.id.tv_source4,
            R.id.tv_status5, R.id.tv_status_1, R.id.tv_status2, R.id.tv_status1, R.id.tv_status0,
            R.id.tv_payType0, R.id.tv_payType1, R.id.tv_payType2, R.id.tv_payType3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                Intent intent = new Intent();
                intent.putExtra("orderType", orderType);
                intent.putExtra("status", status);
                intent.putExtra("payType", payType);
                setResult(Conversion.DATASUCCESS, intent);
                finish();
                break;
            case R.id.tv_source0:
                orderType = "";
                mTvSource0.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvSource1.setTextColor(getResources().getColor(R.color.text333));
                mTvSource3.setTextColor(getResources().getColor(R.color.text333));
                mTvSource4.setTextColor(getResources().getColor(R.color.text333));
                mTvSource5.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_source1:
                orderType = "1";
                mTvSource0.setTextColor(getResources().getColor(R.color.text333));
                mTvSource1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvSource3.setTextColor(getResources().getColor(R.color.text333));
                mTvSource4.setTextColor(getResources().getColor(R.color.text333));
                mTvSource5.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_source3:
                orderType = "3";
                mTvSource0.setTextColor(getResources().getColor(R.color.text333));
                mTvSource1.setTextColor(getResources().getColor(R.color.text333));
                mTvSource3.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvSource4.setTextColor(getResources().getColor(R.color.text333));
                mTvSource5.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_source5:
                orderType = "5";
                mTvSource0.setTextColor(getResources().getColor(R.color.text333));
                mTvSource1.setTextColor(getResources().getColor(R.color.text333));
                mTvSource3.setTextColor(getResources().getColor(R.color.text333));
                mTvSource4.setTextColor(getResources().getColor(R.color.text333));
                mTvSource5.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case R.id.tv_source4:
                orderType = "4";
                mTvSource0.setTextColor(getResources().getColor(R.color.text333));
                mTvSource1.setTextColor(getResources().getColor(R.color.text333));
                mTvSource3.setTextColor(getResources().getColor(R.color.text333));
                mTvSource4.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvSource5.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_status5:
                status = "";
                mTvStatus5.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvStatus_1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus2.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus0.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_status_1:
                status = "-1";
                mTvStatus5.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus_1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvStatus2.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus0.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_status2:
                status = "2";
                mTvStatus5.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus_1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus2.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvStatus1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus0.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_status1:
                status = "1";
                mTvStatus5.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus_1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus2.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvStatus0.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_status0:
                status = "0";
                mTvStatus5.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus_1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus2.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus1.setTextColor(getResources().getColor(R.color.text333));
                mTvStatus0.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case R.id.tv_payType0:
                payType = "";
                mTvPayType0.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvPayType1.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType2.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType3.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_payType1:
                payType = "1";
                mTvPayType0.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType1.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvPayType2.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType3.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_payType2:
                payType = "2";
                mTvPayType0.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType1.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType2.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                mTvPayType3.setTextColor(getResources().getColor(R.color.text333));
                break;
            case R.id.tv_payType3:
                payType = "3";
                mTvPayType0.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType1.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType2.setTextColor(getResources().getColor(R.color.text333));
                mTvPayType3.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
    }
}
