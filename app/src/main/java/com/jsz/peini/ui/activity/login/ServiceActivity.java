package com.jsz.peini.ui.activity.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.jsz.peini.R;
import com.jsz.peini.presenter.IpConfig;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jinshouzhi on 2016/11/25.
 */
public class ServiceActivity extends AppCompatActivity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.inject(this);
        webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        String url = IpConfig.HttpPeiniIp + "terms.html" + "?t=" + System.currentTimeMillis() + Math.random();
        webview.loadUrl(url);
    }


    @OnClick(R.id.toolbar)
    public void onClick() {
        finish();
    }
}
