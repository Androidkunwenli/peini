package com.jsz.peini.ui.activity;

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
import com.jsz.peini.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * H5容器
 * Created by huizhe.ju on 2017/3/11.
 */
public class WebActivity extends SwipeBackActivity {

    private static final String EXTRA_TARGET_URL = "extra_target_url";
    private static final String EXTRA_TARGET_TYPE = "extra_target_type";
    private static final String EXTRA_SELLER_ID = "extra_seller_id";

    /**
     * 订单详情
     */
    private static final String ORDER_DETAIL_URL = "seller/orderDetail";
    /**
     * 账单明细
     */
    private static final String PAY_LIST_URL = "seller/payList";
    /**
     * 系统消息
     */
    private static final String SMS_LIST_URL = "seller/smsList";


    @InjectView(R.id.fl_container)
    FrameLayout flContainer;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.progressBar1)
    ProgressBar progressBar1;
    private WebView mWebView;
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
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

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
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

        initWebView();
        flContainer.addView(mWebView);

        String targetUrl = getIntent().getStringExtra(EXTRA_TARGET_URL);
        if (!TextUtils.isEmpty(targetUrl)) {
            if (targetUrl.startsWith(IpConfig.HttpPeiniIp + ORDER_DETAIL_URL)) {
                tvTitle.setText("订单详情");
            }
//            else if (targetUrl.startsWith(IpConfig.HttpPeiniIp + PAY_LIST_URL)) {
//                tvTitle.setText("账单明细");
//            } else if (targetUrl.startsWith(IpConfig.HttpPeiniIp + SMS_LIST_URL)) {
//                tvTitle.setText("系统消息");
//            }
            mWebView.loadUrl(targetUrl + StringUtils.markByContainQuestion(targetUrl) + "t=" + System.currentTimeMillis() + Math.random());
        }

        int targetType = getIntent().getIntExtra(EXTRA_TARGET_TYPE, -1);
        String sellerId = getIntent().getStringExtra(EXTRA_SELLER_ID);
        if (targetType != -1 && !TextUtils.isEmpty(sellerId)) {
            StringBuilder targetSellerUrl = new StringBuilder(IpConfig.HttpPeiniIp);
            if (targetType == 0) {
                tvTitle.setText("账单明细");
                targetSellerUrl.append(PAY_LIST_URL).append("?sellerId=");
            } else if (targetType == 1) {
                tvTitle.setText("系统消息");
                targetSellerUrl.append(SMS_LIST_URL).append("?sellerId=");
            }
            targetSellerUrl.append(sellerId).append("&t=").append(System.currentTimeMillis()).append(Math.random());
            mWebView.loadUrl(targetSellerUrl.toString());
        }
    }

    public static void actionShow(Context context, String targetUrl) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_TARGET_URL, targetUrl);
        context.startActivity(intent);
    }

    /**
     * 跳转方式
     *
     * @param context  context
     * @param type     0，账单明细； 1，系统消息
     * @param sellerId sellerId
     */
    public static void actionShow(Context context, int type, String sellerId) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_TARGET_TYPE, type);
        intent.putExtra(EXTRA_SELLER_ID, sellerId);
        context.startActivity(intent);
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

    @OnClick(R.id.iv_back)
    public void onClick() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }
}
