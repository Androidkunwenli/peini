package com.jsz.peini.ui.activity.setting;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by th on 2016/12/6.
 */
public class IdeaActivity extends BaseActivity {
    @InjectView(R.id.idea_toolbar)
    Toolbar idea_toolbar;
    @InjectView(R.id.ides_button)
    Button ides_button;
    @InjectView(R.id.imageView3)
    ImageView imageView3;
    @InjectView(R.id.report_ok)
    FrameLayout reportOk;

    @Override
    public int initLayoutId() {
        return R.layout.activity_idea;
    }

    @Override
    public void initView() {
        idea_toolbar.setTitle("");
        setSupportActionBar(idea_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示返回????
        idea_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @OnClick({R.id.ides_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ides_button: //关闭
                finish();
                break;
        }
    }
}
