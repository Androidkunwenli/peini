package com.jsz.peini.ui.fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.WebActivity;
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;

import butterknife.InjectView;

/**
 * Created by kunwe on 2016/11/25.
 * 管理界面
 */

public class ManageFragment extends BaseFragment {

    /**
     * 订单详情
     */
    private static final String ORDER_DETAIL_URL = "seller/orderDetail";
    /**
     * 订单列表
     */
    private static final String ORDER_LIST_URL = "seller/orderList";
    /**
     * 登录
     */
    private static final String LOGIN_URL = "seller/login";

    @InjectView(R.id.swipe_target)
    WebView mWebManage;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    public boolean isShouldShowMorePoint() {
        return isShouldShowMorePoint;
    }

    private boolean isShouldShowMorePoint = false;

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_manage);
    }

    @Override
    public void initData() {

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mWebManage != null) {
                    mWebManage.clearCache(true);
//                    mWebView.clearHistory();

//                    String currentUrl = mWebView.getUrl();
//                    mWebView.loadUrl(currentUrl + StringUtils.markByContainQuestion(currentUrl) + "t=" + System.currentTimeMillis() + Math.random());
                    mWebManage.reload();
                }
            }
        });
        mSwipeToLoadLayout.setLoadMoreEnabled(false);

        //设置WebView属性，能够执行Javascript脚本
        mWebManage.getSettings().setJavaScriptEnabled(true);
        mWebManage.getSettings().setDomStorageEnabled(true);

        String sellerId = (String) SpUtils.get(mActivity, Conversion.STORE_MANAGE_ID, "");
        String targetUrl;
        if (TextUtils.isEmpty(sellerId)) {
            targetUrl = IpConfig.HttpPeiniIp + LOGIN_URL + "?t=" + System.currentTimeMillis() + Math.random();
            isShouldShowMorePoint = false;
            ((HomeActivity) getContext()).setMorePointIsShow(false);
        } else {
            targetUrl = IpConfig.HttpPeiniIp + ORDER_LIST_URL + "?sellerId=" + sellerId + "&t=" + System.currentTimeMillis() + Math.random();
            isShouldShowMorePoint = true;
            ((HomeActivity) getContext()).setMorePointIsShow(true);
        }
        mWebManage.loadUrl(targetUrl);
        //设置Web视图
        mWebManage.setWebViewClient(new HelloWebViewClient());
        mWebManage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    if (mWebManage != null && mWebManage.canGoBack()
                            && !TextUtils.isEmpty(mWebManage.getUrl())
                            && !mWebManage.getUrl().startsWith(IpConfig.HttpPeiniIp + ORDER_LIST_URL)
                            && !mWebManage.getUrl().startsWith(IpConfig.HttpPeiniIp + LOGIN_URL)) {
                        mWebManage.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void storeExit() {
        SpUtils.remove(mActivity, Conversion.STORE_MANAGE_ID);

        String targetUrl = IpConfig.HttpPeiniIp + LOGIN_URL + "?t=" + System.currentTimeMillis() + Math.random();
        isShouldShowMorePoint = false;
        ((HomeActivity) getContext()).setMorePointIsShow(false);
        if (mWebManage != null) {
            mWebManage.loadUrl(targetUrl);
            mWebManage.clearCache(true);
            mWebManage.clearHistory();
        }
    }

    //Web视图
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith(IpConfig.HttpPeiniIp + ORDER_DETAIL_URL)) {
                WebActivity.actionShow(getContext(), url);
            } else {
                view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());

                if (url.startsWith(IpConfig.HttpPeiniIp + ORDER_LIST_URL)) {
                    String sellerId = url.replaceAll(".*sellerId\\s*=\\s*(\\d+).*", "$1");

                    if (!TextUtils.isEmpty(sellerId)) {
                        SpUtils.put(mActivity, Conversion.STORE_MANAGE_ID, sellerId);
                    }

                    isShouldShowMorePoint = true;
                    ((HomeActivity) getContext()).setMorePointIsShow(true);
                } else if (url.startsWith(IpConfig.HttpPeiniIp + LOGIN_URL)) {
                    isShouldShowMorePoint = false;
                    ((HomeActivity) getContext()).setMorePointIsShow(false);
                }
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mSwipeToLoadLayout.isRefreshing()) {
                mSwipeToLoadLayout.setRefreshing(false);
            }
        }
    }
}