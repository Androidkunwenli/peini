package com.jsz.peini.ui.activity.news;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.san.huanxin.fragment.HuanXinFragment;
import com.jsz.peini.ui.fragment.news.NewsFragment;
import com.jsz.peini.ui.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by kunwe on 2016/12/2.
 * 关注列表
 */
public class MapNewsActivity extends BaseActivity {
    @InjectView(R.id.toolbar)
    LinearLayout mapToolbar;
    @InjectView(R.id.map_news_pager)
    NoScrollViewPager mapNewsPager;
    @InjectView(R.id.map_news_title)
    TabLayout mapNewsTitle;
    private NewsMapAdapter fAdapter;                               //定义adapter
    private List<Fragment> list_fragment;                        //定义要装fragment的列表
    private List<String> list_title;                                     //tab名称列表


    @Override
    public int initLayoutId() {
        return R.layout.activity_news;
    }

    @Override
    public void initView() {
        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(new HuanXinFragment());
        list_fragment.add(new NewsFragment());
        //tab列表
        list_title = new ArrayList<>();
        list_title.add("消息");
        list_title.add("关注");

        //设置TabLayout的模式
        mapNewsTitle.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        mapNewsTitle.addTab(mapNewsTitle.newTab().setText(list_title.get(0)));
        mapNewsTitle.addTab(mapNewsTitle.newTab().setText(list_title.get(1)));
        fAdapter = new NewsMapAdapter(getSupportFragmentManager(), list_fragment, list_title);

        //viewpager加载adapter
        mapNewsPager.setAdapter(fAdapter);
        //tab_FindFragment_title.setViewPager(vp_FindFragment_pager);
        //TabLayout加载viewpager
        mapNewsTitle.setupWithViewPager(mapNewsPager);

    }

    @Override
    public void initData() {
        mapToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
