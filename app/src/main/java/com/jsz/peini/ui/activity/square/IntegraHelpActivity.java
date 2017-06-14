package com.jsz.peini.ui.activity.square;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.presenter.IpConfig;

import butterknife.InjectView;

/**
 * Created by th on 2017/1/17.
 */
public class IntegraHelpActivity extends BaseActivity {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    public Activity mActivity;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.wv_help_html)
    WebView mWvHelpHtml;

    @Override
    public int initLayoutId() {
        return R.layout.activity_integra_help;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("使用帮助");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mWvHelpHtml.getSettings().setJavaScriptEnabled(true);
        mWvHelpHtml.loadUrl(IpConfig.HttpPeiniIp + "help.html" + "?t=" + System.currentTimeMillis() + Math.random());
    }
}
