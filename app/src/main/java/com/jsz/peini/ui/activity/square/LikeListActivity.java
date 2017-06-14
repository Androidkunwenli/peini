package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.LikeListBean;
import com.jsz.peini.ui.adapter.square.LikeListAdatper;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;

import java.util.ArrayList;

import butterknife.InjectView;

/**
 * Created by lenovo on 2017/3/6.
 */
public class LikeListActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.rl_like_list)
    RecyclerView mRlLikeList;
    private ArrayList<LikeListBean> mList;
    private String mNumbar;
    private LikeListActivity mActivity;
    private LikeListAdatper mAdapter;
    private Intent mIntent;

    @Override
    public int initLayoutId() {
        return R.layout.activity_likelist;
    }

    @Override
    public void initView() {
        mActivity = this;
        Intent intent = getIntent();
        mList = (ArrayList<LikeListBean>) intent.getSerializableExtra(Conversion.LIST);
        mNumbar = "等 " + mList.size() + " 人点过赞";
        mTitle.setText(mNumbar);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LogUtil.d("点赞列表显示的数据 \n" + mList.toString());
    }

    @Override
    public void initData() {
        mRlLikeList.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new LikeListAdatper(mActivity, mList);
        mRlLikeList.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAdapter.setItemClickListener(new LikeListAdatper.ItemClickListener() {
            @Override
            public void onItemClick(String userId, int position) {
                if (SpUtils.getUserToken(mActivity).equals(userId)) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra(Conversion.USERID, userId);
                    startActivity(mIntent);
                }
            }
        });
    }
}
