package com.jsz.peini.ui.activity.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.ui.activity.search.IsSearchActivity;
import com.jsz.peini.ui.fragment.StoreFragment;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;

import butterknife.InjectView;

/**
 * Created by th on 2017/1/13.
 */
public class SelectSellerActivity extends BaseActivity implements StoreFragment.OnFragmentInteractionListener {


    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    FrameLayout mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.selectseller)
    FrameLayout mSelectseller;
    public String mChoice;
    private Intent mIntent;
    private SelectSellerActivity mActivity;

    @Override
    public int initLayoutId() {
        return R.layout.activity_selectseller;
    }

    @Override
    public void initView() {
        mActivity = SelectSellerActivity.this;
        CacheActivity.addActivity(mActivity);
        mTitle.setText("商家选择");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mChoice = getIntent().getStringExtra(Conversion.CHOICE);

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(mActivity, IsSearchActivity.class);
                mIntent.putExtra(Conversion.TYPE, "NoFinish");
                mIntent.putExtra(Conversion.CHOICE, "Choice");
                startActivity(mIntent);
            }
        });

        addFragment();
    }

    private void addFragment() {
        //new出EaseChatFragment或其子类的实例
        StoreFragment storeFragment = new StoreFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putString(Conversion.CHOICE, mChoice);
        storeFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.selectseller, storeFragment).commit();
    }

    @Override
    public void onFragmentInteraction() {
        finish();
    }
}
