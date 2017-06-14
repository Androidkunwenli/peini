package com.jsz.peini.ui.activity.square;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.square.SquareNewData;
import com.jsz.peini.ui.adapter.square.SquareNewAdapter;


import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2017/6/10.
 */

public class SquareNewActivity extends BaseActivity {
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private Activity mActivity;
    ArrayList<SquareNewData.SquareNewList> newLists = new ArrayList<>();

    @Override
    public int initLayoutId() {
        return R.layout.activity_square_new_list;
    }

    @Override
    public void initData() {
        mActivity = this;
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        SquareNewAdapter adapter = new SquareNewAdapter(mActivity);
        mSwipeTarget.setAdapter(adapter);
        for (int i = 0; i < 100; i++) {
            newLists.add(new SquareNewData.SquareNewList("图片", "名字"));
        }
        adapter.setNewData(newLists);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(SquareNewActivity.this,SquareNewMessageActivity.class));
            }
        });

    }
}
