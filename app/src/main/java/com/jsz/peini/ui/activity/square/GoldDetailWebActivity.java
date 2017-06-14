package com.jsz.peini.ui.activity.square;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.SwipeBackActivity;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.pay.PaySuccessActivity;
import com.jsz.peini.ui.activity.task.SellerSuccessActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * H5容器
 * Created by huizhe.ju on 2017/3/11.
 */
public class GoldDetailWebActivity extends SwipeBackActivity {

    private static final int REQUEST_CODE = 1001;
    private static final String TARGET_URL_TAG = "getMygoldList/";


//    金币明细
//    路径：/getMygoldList
//    参数：userId  String
//    orderType   Integer 1充值 2赠送3消费4转增
//    stream        Integer 1收入0支出


    @InjectView(R.id.fl_container)
    FrameLayout flContainer;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_right_button)
    TextView tvRightButton;
    @InjectView(R.id.progressBar1)
    ProgressBar progressBar1;


    /**
     * 1充值 2赠送 3消费 4转增
     */
    private String mOrderType;
    /**
     * 1收入0支出
     */
    private String mStream;
    private Context mContext;

    private WebView mWebView;
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (setClickEvent(view, url)) {
                view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("web", "end load:" + url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i("web", "start loading:" + url);
            super.onPageStarted(view, url, favicon);
        }
    };

    private boolean setClickEvent(WebView view, String url) {
        Intent intent;
        String sellerinfoname = "";
        String taskId = "";
        String sellerInfoId = "";
        String orderId = "";
        String userId = "";
        String MOthersrnmae = "";
        if (url.contains(IpConfig.HttpPeiniIp + "h5_toComtSeller")) {//优惠买单评价
            String[] split = url.split("&");
            if (split.length == 3) {
                orderId = split[0].split("=")[1];
                try {
                    sellerinfoname = java.net.URLDecoder.decode(split[1].split("=")[1], "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sellerInfoId = split[2].split("=")[1];
            }
            if (!TextUtils.isEmpty(orderId) && !TextUtils.isEmpty(sellerInfoId) && !TextUtils.isEmpty(sellerinfoname)) {
                intent = new Intent(mContext, PaySuccessActivity.class);
                intent.putExtra(Conversion.ORDERID, orderId);
                intent.putExtra(Conversion.SELLERINFOID, sellerInfoId);
                intent.putExtra(Conversion.SELLERINFONAME, sellerinfoname);
                startActivity(intent);
            }
            return false;
        } else if (url.contains(IpConfig.HttpPeiniIp + "h5_toComtShopAndUser")) {//任务买单评价
            String[] split = url.split("&");
            if (split.length == 6) {
                taskId = split[0].split("=")[1];
                sellerInfoId = split[1].split("=")[1];
                sellerinfoname = java.net.URLDecoder.decode(split[2].split("=")[1]);
                userId = split[3].split("=")[1];
                MOthersrnmae = java.net.URLDecoder.decode(split[4].split("=")[1]);
                orderId = split[5].split("=")[1];
            }
            if (!TextUtils.isEmpty(taskId) &&
                    !TextUtils.isEmpty(sellerInfoId) &&
                    !TextUtils.isEmpty(sellerinfoname) &&
                    !TextUtils.isEmpty(userId) &&
                    !TextUtils.isEmpty(MOthersrnmae) &&
                    !TextUtils.isEmpty(orderId)) {
                intent = new Intent(mContext, SellerSuccessActivity.class);
                intent.putExtra(Conversion.TASKID, taskId);
                intent.putExtra(Conversion.SELLERINFOID, sellerInfoId);
                intent.putExtra(Conversion.SELLERNMAE, sellerinfoname);
                intent.putExtra(Conversion.OTHERSRNMAE, MOthersrnmae);
                intent.putExtra(Conversion.USERID, userId);
                intent.putExtra(Conversion.ORDERID, orderId);
                startActivity(intent);
            }
            return false;
        } else if (url.contains(IpConfig.HttpPeiniIp + "h5_toGoldDonation")) {//跳转打赏页面
            String[] split = url.split("id=");
            if (split.length == 2) {
                taskId = split[1];
            }
            if (!TextUtils.isEmpty(taskId)) {
                intent = new Intent(mContext, VerifyDataActivity.class);
                intent.putExtra(Conversion.USERID, "");
                intent.putExtra(Conversion.TASKID, taskId);
                startActivity(intent);
            }
            return false;
        } else {
            return true;
        }
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            tvTitle.setText(TextUtils.isEmpty(title) ? "账单明细" : title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            if (newProgress == 100) {
                progressBar1.setVisibility(View.GONE);
            } else {
                progressBar1.setVisibility(View.VISIBLE);
                progressBar1.setProgress(newProgress);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        ButterKnife.inject(this);

        mContext = this;
        initWebView();
        flContainer.addView(mWebView);
        tvRightButton.setText("筛选");
        tvRightButton.setVisibility(View.VISIBLE);
        tvTitle.setText("金币明细");

        String targetUrl = getTargetUrl();

        mWebView.loadUrl(targetUrl);
    }

    private String getTargetUrl() {
        StringBuilder targetUrlStringBuilder = new StringBuilder(IpConfig.HttpPeiniIp + TARGET_URL_TAG);
        targetUrlStringBuilder.append("?userId=").append(SpUtils.getUserToken(mContext)).append("&orderType=");
        if (!TextUtils.isEmpty(mOrderType)) {
            targetUrlStringBuilder.append(mOrderType);
        }
        targetUrlStringBuilder.append("&stream=");
        if (!TextUtils.isEmpty(mStream)) {
            targetUrlStringBuilder.append(mStream);
        }
        targetUrlStringBuilder.append("&t=").append(System.currentTimeMillis()).append(Math.random());
        return targetUrlStringBuilder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebView = new WebView(this);

        mWebView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.setScrollBarSize(0);
        }
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);

        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.requestFocus();

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings mWebSettings = mWebView.getSettings();
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

//        if (Build.VERSION.SDK_INT >= 16) {
//            mWebSettings.setAllowFileAccessFromFileURLs(true);
//        }
        mWebSettings.setDatabaseEnabled(true);

        mWebSettings.setUserAgentString(mWebSettings.getUserAgentString()
                + ";native-android");// 获得浏览器的环境

        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.tv_right_button:
                Intent intent = new Intent(mContext, GoldDetailFilterActivity.class);
                intent.putExtra("orderType", mOrderType);
                intent.putExtra("stream", mStream);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mOrderType = data.getStringExtra("orderType");
            mStream = data.getStringExtra("stream");

            String targetUrl = getTargetUrl();
            if (mWebView != null) {
                mWebView.loadUrl(targetUrl);
            }
        }
    }
}
