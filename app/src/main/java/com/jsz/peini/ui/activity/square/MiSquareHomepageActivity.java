package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.ui.fragment.SquareFragment;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;

import butterknife.InjectView;

/**
 * Created by 15089 on 2017/2/20.
 */
public class MiSquareHomepageActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.misquarehomepage)
    FrameLayout mMisquarehomepage;
    private String mType;
    private String mToken;
    private Intent mIntent;
    private MiSquareHomepageActivity mActivity;

    @Override
    public int initLayoutId() {
        return R.layout.activity_misquarehomepage;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("我的广场");
        mRightButton.setText("发布");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        mType = intent.getStringExtra(Conversion.TYPE);
        mToken = intent.getStringExtra(Conversion.TOKEN);
        LogUtil.d("mType" + mType + "mToken" + mToken);
        addFragment();
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i("这个是广场发布的按钮", "广场发布的按钮");
                mIntent = new Intent(mActivity, SuareReleaseActivity.class);
                startActivityForResult(mIntent, 100);
            }
        });
    }

    private void addFragment() {
        //new出EaseChatFragment或其子类的实例
        SquareFragment squareFragment = new SquareFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putString(Conversion.TYPE, mType);
        args.putString(Conversion.TOKEN, mToken);
        squareFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.misquarehomepage, squareFragment).commit();
    }
}
