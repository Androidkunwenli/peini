package com.jsz.peini.ui.activity.square;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.ActivityRegisterBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.setting.VersionNoBean;
import com.jsz.peini.presenter.ApiService;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.news.SecretaryActivity;
import com.jsz.peini.ui.activity.pay.OfficialActivityPayActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ShareUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 官方活动
 * Created by huizhe.ju on 2017/3/11.
 */

public class OfficialActivity extends BaseActivity {

    private static final int REQUEST_OFFICIAL_PAY_CODE = 1024;
    private static final String OFFICIAL_DETAIL_URL = IpConfig.HttpPeiniIp + "activity/detail";
    private static final String OFFICIAL_LIST_URL = IpConfig.HttpPeiniIp + "activity/list";
    private static final String OFFICIAL_MEMBER_LIST_URL = IpConfig.HttpPeiniIp + "activity/resondetail";

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.iv_share)
    ImageView mIvShare;
    @InjectView(R.id.wv_official)
    WebView mWebView;
    @InjectView(R.id.tv_sign_up)
    TextView mTvSignUp;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.progressBar1)
    ProgressBar progressBar1;

    private String mAcId;
    private String mShareUrl = null;

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
            if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            interceptUrl(url);
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

    private OfficialActivity mActivity;
    private VersionNoBean.DataBean mData;
    private String mUserToken;

    @Override
    public int initLayoutId() {
        return R.layout.activity_official;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("官方活动");
        mTvSignUp.setVisibility(View.GONE);
        initWebView();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mWebView != null) {
                    mWebView.clearCache(true);
//                    mWebView.clearHistory();

//                    String currentUrl = mWebView.getUrl();
//                    mWebView.loadUrl(currentUrl + StringUtils.markByContainQuestion(currentUrl) + "t=" + System.currentTimeMillis() + Math.random());
                    mWebView.reload();
                }
            }
        });
    }

    @Override
    public void initData() {
        mUserToken = SpUtils.getUserToken(mActivity);
        String GtUrl = getIntent().getStringExtra(Conversion.URl);
        if (!TextUtils.isEmpty(mUserToken)) {
            String xPoint = SpUtils.getXpoint(mActivity);
            String yPoint = SpUtils.getYpoint(mActivity);
            if (TextUtils.isEmpty(GtUrl)) {
                //加载需要显示的网页
                String url = IpConfig.HttpPeiniIp
                        + "activity/list?userId=" + mUserToken
                        + "&xpoint=" + xPoint
                        + "&ypoint=" + yPoint;
                LogUtil.d("官方活动接口 --- " + url);
                mWebView.loadUrl(url + "&t=" + System.currentTimeMillis() + Math.random());
            } else {
                //需要做网络请求_请求接口
                initNetWork(GtUrl);
            }
        }
    }

    private void initNetWork(String gtUrl) {
        RetrofitUtil.createService(ApiService.class).getNotifyLink(gtUrl, SpUtils.getUserToken(mActivity)).enqueue(new RetrofitCallback<SuccessfulBean>() {
            @Override
            public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful()) {
                    SuccessfulBean body = response.body();
                    if (body.getResultCode() == 1) {
                        String url = IpConfig.HttpPeiniIp + body.getData();
                        if (!TextUtils.isEmpty(url))
                            mWebView.loadUrl(url + "&t=" + System.currentTimeMillis() + Math.random());
                    } else {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
            }
        });
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void initWebView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.setScrollBarSize(0);
        }
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.requestFocus();

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

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

    /**
     * 拦截Url
     *
     * @param url url
     */
    private void interceptUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (url.startsWith(OFFICIAL_LIST_URL)) {
            mTitle.setText("官方活动");
        } else if (url.startsWith(OFFICIAL_DETAIL_URL)) {
            mTitle.setText("活动详情");
        } else if (url.startsWith(OFFICIAL_MEMBER_LIST_URL)) {
            mTitle.setText("报名列表");
        }
        if (url.startsWith(OFFICIAL_DETAIL_URL)) {
            mTvSignUp.setVisibility(View.VISIBLE);
            mIvShare.setVisibility(View.VISIBLE);
            mShareUrl = url;

            String statusArg = null;
            String isRegArg = null;
            String matcherStr = "&id=([a-zA-Z0-9]+)&status=(-?\\d*)&isReg=(-?\\d*)";
            Matcher matcher = Pattern.compile(matcherStr).matcher(url);
            if (matcher.find()) {
                mAcId = matcher.group(1);
                statusArg = matcher.group(2);
                isRegArg = matcher.group(3);
                if (StringUtils.isNoNull(mAcId)) {
                    initShare(mAcId);
                }
            } else {
                mAcId = null;
            }

            if ("0".equals(statusArg) && "0".equals(isRegArg)) {
                mTvSignUp.setEnabled(true);
                mTvSignUp.setBackgroundColor(getResources().getColor(R.color.RED_FB4E30));
            } else {
                mTvSignUp.setEnabled(false);
                mTvSignUp.setBackgroundColor(getResources().getColor(R.color.text999));
            }
        } else {
            mTvSignUp.setVisibility(View.GONE);
            mIvShare.setVisibility(View.GONE);
        }
    }

    private void initShare(String acId) {
        RetrofitUtil.createService(TaskService.class).shareGetActivityShare(acId).enqueue(new RetrofitCallback<VersionNoBean>() {
            @Override
            public void onSuccess(Call<VersionNoBean> call, Response<VersionNoBean> response) {
                if (response.isSuccessful()) {
                    VersionNoBean body = response.body();
                    if (response.body().getResultCode() == 1) {
                        mData = body.getData();
                    }
                }
            }

            @Override
            public void onFailure(Call<VersionNoBean> call, Throwable t) {

            }
        });
    }

    private void requestOrderIdStr() {
        if (TextUtils.isEmpty(mAcId)) {
            Toasty.normal(mActivity, "活动报名数据错误，请退出重试").show();
            return;
        }

        RetrofitUtil.createService(TaskService.class)
                .activityRegister(mUserToken, mAcId)
                .enqueue(new Callback<ActivityRegisterBean>() {
                    @Override
                    public void onResponse(Call<ActivityRegisterBean> call, Response<ActivityRegisterBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResultCode() == 1) {
                                String orderId = response.body().getOrderIdStr();
                                if (!TextUtils.isEmpty(orderId)) {
                                    OfficialActivityPayActivity.actionShow(mActivity, orderId, REQUEST_OFFICIAL_PAY_CODE);
                                } else {
                                    refreshCurrentWeb();
                                    Toasty.success(mActivity, "报名成功").show();
//                                    mTvSignUp.setEnabled(false);
//                                    mTvSignUp.setBackgroundColor(getResources().getColor(R.color.text999));
                                }
                            } else if (response.body().getResultCode() == 0) {
                                Toasty.normal(mActivity, response.body().getResultDesc()).show();
                            } else if (response.body().getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, response.body().getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ActivityRegisterBean> call, Throwable t) {
                        Toasty.normal(mActivity, "网络不给力，请稍后重试").show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_OFFICIAL_PAY_CODE && resultCode == RESULT_OK) {
            refreshCurrentWeb();
        }
    }

    /**
     * 清缓存，刷新当前页面
     */
    private void refreshCurrentWeb() {
        if (mWebView == null) {
            return;
        }
        mWebView.clearCache(true);
//        mWebView.clearHistory();
        Log.i("web:", "current url: " + mWebView.getUrl());
        String currentUrl = mWebView.getUrl();
        currentUrl = currentUrl.replaceAll("&status=-?\\d*&isReg=-?\\d*$", "");
        Log.i("web:", "current url: " + currentUrl);
        mWebView.loadUrl(currentUrl + StringUtils.markByContainQuestion(currentUrl) + "t=" + System.currentTimeMillis() + Math.random());
    }

    @OnClick({R.id.tv_sign_up, R.id.iv_share, R.id.toolbar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up:
                showConfirmDialog();
                break;
            case R.id.iv_share:
                ShowShred();
                break;
            case R.id.toolbar:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
        }
    }

    //显示分享底部分享栏目
    private void ShowShred() {
        String shareTitle = mData.getShareTitle();
        String shareContent = mData.getShareContent();
        if (mData != null && TextUtils.isEmpty(shareContent) && TextUtils.isEmpty(shareTitle)) {
            Toasty.normal(mActivity, "该活动暂无分享内容").show();
            return;
        }

        UMImage thumb = new UMImage(mActivity, R.mipmap.ic_launcher);
        String url = mShareUrl;
        final UMWeb web = new UMWeb(url);
        web.setTitle(shareTitle);//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(shareContent);//描述
        new ShareUtils(mActivity, web);
    }

    private void showConfirmDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("您是否要参加活动")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestOrderIdStr();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
