package com.jsz.peini.ui.activity.square;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
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
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2017/4/15.
 */

public class RewardSuccessActivity extends BaseActivity {
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
    @InjectView(R.id.tv_areward)
    TextView mTvAreward;
    @InjectView(R.id.gold_web)
    WebView mWebView;
    private WebViewClient mWebViewClient;
    private RewardSuccessActivity mActivity;
    private String mOrderid;

    @Override
    public int initLayoutId() {
        return R.layout.activity_rewardsuccess;
    }

    @Override
    public void initData() {
        mActivity = this;
        mOrderid = getIntent().getStringExtra(Conversion.ORDERID);
        String type = getIntent().getStringExtra(Conversion.TYPE);
        if (TextUtils.isEmpty(mOrderid)) {
            finish();
        }
        mTitle.setText(TextUtils.isEmpty(type) ? "打赏成功" : "转账成功");
        mTvAreward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (setClickEvent(view, url)) {
//                    view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
//                }
//                return true;
//            }


        };
        mWebView.setWebViewClient(mWebViewClient);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = IpConfig.HttpPeiniIp + "wap/pay-details?userId=" + SpUtils.getUserToken(mActivity) + "&orderId=" + mOrderid;
        mWebView.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
    }
}
