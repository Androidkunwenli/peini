package com.jsz.peini.ui.activity.news;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.OfficialActivity;
import com.jsz.peini.ui.activity.square.TaPhotoImageActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareHomepageActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;

/**
 * Created by lenovo on 2017/4/28.
 */

public class SecretaryActivity extends BaseActivity {
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
    private SecretaryActivity mActivity;

    @Override
    public int initLayoutId() {
        return R.layout.activity_web_all;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initInternet() {
        mActivity = this;
        mTitle.setText("陪你小助手");
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
        //http://192.168.150.239:8280/pnservice/toMytipSms?userId=981cd58047e044848a234aa0a0d15220
        String url = IpConfig.HttpPeiniIp + "toMytipSms?userId=" + SpUtils.getUserToken(mActivity);
        mWebView.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (setClickEvent(url)) {
                    view.loadUrl(url + StringUtils.markByContainQuestion(url) + "t=" + System.currentTimeMillis() + Math.random());
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        };
        mWebView.setWebViewClient(mWebViewClient);
    }

    /**
     * 点击事件
     * taskInfo?id= 任务
     * otherUserInfo?id=他人空间
     */
    private boolean setClickEvent(String url) {
        Intent mIntent;
        if (url.contains("myUserInfo")) {//我的空间
            MiSquareActivity.actionShow(mActivity);
            return false;
        } else if (url.contains("appHomeIndex")) {//首页
            mIntent = new Intent(mActivity, HomeActivity.class);
            mIntent.putExtra(Conversion.TYPE, 10);
            startActivity(mIntent);
            return false;
        } else if (url.contains("myFans")) {//粉丝
            mIntent = new Intent(mActivity, FansActivity.class);
            startActivity(mIntent);
            return false;
        } else if (url.contains("appendInfo")) {//完善注册
            if (SpUtils.isCompleteUserInfo(mActivity)) {
                Toasty.normal(mActivity, "您已经完成了信息").show();
            } else {
                CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
            }
            return false;
        } else if (url.contains("taskInfo?id=")) {//任务详情
            String[] split = url.split("id=");
            String id;
            if (split.length == 2) {
                id = split[1];
            } else {
                id = "";
            }
            if (!TextUtils.isEmpty(id)) {
                mIntent = new Intent(mActivity, TaskDetailActivity.class);
                mIntent.putExtra(Conversion.ID, id);
                startActivity(mIntent);
            }
            return false;
        } else if (url.contains("otherUserInfo?id=")) {//他人空间
            String[] split = url.split("id=");
            String userid;
            if (split.length == 2) {
                userid = split[1];
            } else {
                userid = "";
            }
            if (!TextUtils.isEmpty(userid)) {
                mIntent = new Intent(mActivity, TaPhotoImageActivity.class);
                mIntent.putExtra("extra_other_user_id", userid);
                startActivity(mIntent);
            }
            return false;
        } else if (url.contains("squareInfoList?id=")) {//广场
            String[] split = url.split("id=");
            String userid;
            if (split.length == 2) {
                userid = split[1];
            } else {
                userid = "";
            }
            //他的空间
            mIntent = new Intent(mActivity, TaSquareHomepageActivity.class);
            mIntent.putExtra(Conversion.TOKEN, userid);
            startActivity(mIntent);
            return false;
        } else if (url.contains("activityInfo?id=")) {//活动
            String[] split = url.split("id=");
            String userid;
            if (split.length == 2) {
                userid = split[1];
            } else {
                userid = "";
            }
            //他的空间
            mIntent = new Intent(mActivity, OfficialActivity.class);
            mIntent.putExtra(Conversion.URl, userid);
            startActivity(mIntent);
            return false;
        } else {
            return true;
        }
    }
}
