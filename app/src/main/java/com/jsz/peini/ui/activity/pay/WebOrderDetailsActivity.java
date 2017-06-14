package com.jsz.peini.ui.activity.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.VerifyDataActivity;
import com.jsz.peini.ui.activity.task.SellerSuccessActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import es.dmoral.toasty.Toasty;

/**
 * Created by lenovo on 2017/4/28.
 */

public class WebOrderDetailsActivity extends BaseActivity {
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
    @InjectView(R.id.web_view)
    WebView mWebView;
    private WebViewClient mWebViewClient;
    private WebOrderDetailsActivity mActivity;
    private String mOrderid;
    private Intent mIntent;
    private String mSellerInfoId;
    private String mSellerinfoname;
    private String mTaskId;
    private String mUserId;
    private String mMOthersrnmae;
    private String mOrderId;

    @Override
    public int initLayoutId() {
        return R.layout.activity_web_all;
    }

    @Override
    public void initView() {
        mActivity = this;
        mOrderid = getIntent().getStringExtra(Conversion.ORDERID);
        //商家
        mSellerInfoId = getIntent().getStringExtra(Conversion.SELLERINFOID); //商家id
        mSellerinfoname = getIntent().getStringExtra(Conversion.SELLERINFONAME);//商家名字
        //任务
        //任务id
        mTaskId = getIntent().getStringExtra(Conversion.TASKID);
        //他人token
        mUserId = getIntent().getStringExtra(Conversion.USERID);
        //他人mane
        mMOthersrnmae = getIntent().getStringExtra(Conversion.OTHERSRNMAE);
        //订单id
        mOrderId = getIntent().getStringExtra(Conversion.ORDERID);

        if (TextUtils.isEmpty(mOrderid)) {
            Toasty.normal(mActivity, "系统异常,退出界面").show();
            finish();
            return;
        }
        mTitle.setText("账单详情");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //启用支持javascript
        WebSettings mWebSettings = mWebView.getSettings();
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebSettings.setBlockNetworkImage(false); // 是否阻止网络图像
        mWebSettings.setBlockNetworkLoads(false); // 是否阻止网络请求
        mWebSettings.setJavaScriptEnabled(true); // 是否加载JS
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setAppCacheEnabled(false);
        mWebSettings.setUseWideViewPort(false); // 使用广泛视窗
        mWebSettings.setLoadWithOverviewMode(false);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setDatabaseEnabled(true);

        mWebSettings.setUserAgentString(mWebSettings.getUserAgentString()
                + ";native-android");// 获得浏览器的环境
        //网址
        //http://192.168.150.239:8280/pnservice/wap/pay-details?userId=&orderId=

        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (setClickEvent(view, url)) {
                    view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
                }
                return true;
            }
        };
        mWebView.setWebViewClient(mWebViewClient);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = IpConfig.HttpPeiniIp + "wap/pay-details?userId=" + SpUtils.getUserToken(mActivity) + "&orderId=" + mOrderid;
        mWebView.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
    }

    /**
     * 点击事件
     * h5_toComtSeller  优惠买单评价
     * h5_toComtShopAndUser 任务买单评价
     * h5_toGoldDonation  跳转打赏页面
     */
    private boolean setClickEvent(WebView view, String url) {
        if (url.contains(IpConfig.HttpPeiniIp + "h5_toComtSeller")) {//优惠买单评价
            mIntent = new Intent(mActivity, PaySuccessActivity.class);
            mIntent.putExtra(Conversion.ORDERID, mOrderId);
            mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId);
            mIntent.putExtra(Conversion.SELLERINFONAME, mSellerinfoname);
            startActivity(mIntent);
            return false;
        } else if (url.contains(IpConfig.HttpPeiniIp + "h5_toComtShopAndUser")) {//任务买单评价
            mIntent = new Intent(mActivity, SellerSuccessActivity.class);
            mIntent.putExtra(Conversion.TASKID, mTaskId);
            mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId);
            mIntent.putExtra(Conversion.SELLERNMAE, mSellerinfoname);
            mIntent.putExtra(Conversion.OTHERSRNMAE, mMOthersrnmae);
            mIntent.putExtra(Conversion.USERID, mUserId);
            mIntent.putExtra(Conversion.ORDERID, mOrderId);
            startActivity(mIntent);
            return false;
        } else if (url.contains(IpConfig.HttpPeiniIp + "h5_toGoldDonation")) {//跳转打赏页面
            mIntent = new Intent(mActivity, VerifyDataActivity.class);
            mIntent.putExtra(Conversion.USERID, mUserId);
            mIntent.putExtra(Conversion.TASKID, mTaskId);
            startActivity(mIntent);
            return false;
        } else {
            return true;
        }
    }
}
