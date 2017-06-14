package com.jsz.peini.ui.activity.web;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.StringUtils;

import butterknife.InjectView;

public class WebAllActivity extends BaseNotSlideActivity {

    @InjectView(R.id.web_view)
    WebView mWebView;
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
    private String mAdTitle;
    private String mAdLink;
    private String mAdImgUrl;
    private AdModel.AdvertiseListBean mBean;

    @Override
    public int initLayoutId() {
        return R.layout.activity_web_all;
    }

    @Override
    public void initData() {
        mBean = getIntent().getParcelableExtra("adItemBean");
        mAdTitle = getIntent().getStringExtra("adTitle");
        mAdLink = getIntent().getStringExtra("adLink");
        mAdImgUrl = getIntent().getStringExtra("adImgUrl");

        mTitle.setText(String.valueOf(mBean.getAdTitle()));

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(10000, intent);
                finish();
            }
        });
    }

    @Override
    public void initInternet() {
        //启用支持javascript
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
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setUserAgentString(mWebSettings.getUserAgentString()
                + ";native-android");// 获得浏览器的环境

        mWebView.loadUrl(mAdLink + StringUtils.markByContainQuestion(mAdLink) + "t=" + System.currentTimeMillis() + Math.random());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            Intent intent = getIntent();
            setResult(10000, intent);
            finish();
        }
    }
}
