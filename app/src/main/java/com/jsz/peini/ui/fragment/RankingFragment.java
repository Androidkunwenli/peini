package com.jsz.peini.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.ui.activity.news.NewsMapAdapter;
import com.jsz.peini.ui.fragment.ranking.BuyFragment;
import com.jsz.peini.ui.fragment.ranking.GoldFragMent;
import com.jsz.peini.ui.fragment.ranking.IntegrityFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by kunwe on 2016/11/25.
 * 排行榜界面
 */

public class RankingFragment extends BaseFragment {
    @InjectView(R.id.ranking_tab)
    TabLayout mRankingTab;
    @InjectView(R.id.ranking_vp)
    ViewPager mRankingVp;
    public List<String> mListTitle;
    public List<Fragment> mFragment;
    public NewsMapAdapter mAdapter;

    @Override
    public View initViews() {
        return View.inflate(mActivity, R.layout.fragment_ranking, null);
    }

    @Override
    public void initData() {
        mListTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        /*标题*/
        mListTitle.add("金币榜");
        mListTitle.add("土豪榜");
        mListTitle.add("诚信榜");
        /*    goldList=0, 金币
            buyList=0, 土豪
            integrityList=0 诚信*/
        /*初始化布局*/
        mFragment.add(new GoldFragMent());
        mFragment.add(new BuyFragment());
        mFragment.add(new IntegrityFragment());
        initDataMassage();

    }

    private void initDataMassage() {
        //设置TabLayout的模式
        mRankingTab.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        for (int i = 0; i < mListTitle.size(); i++) {
            mRankingTab.addTab(mRankingTab.newTab().setText(mListTitle.get(i)));
        }
        mAdapter = new NewsMapAdapter(getChildFragmentManager(), mFragment, mListTitle);
        mRankingVp.setOffscreenPageLimit(mFragment.size());
        mRankingVp.setAdapter(mAdapter);
        //为TabLayout设置ViewPager
        mRankingTab.setupWithViewPager(mRankingVp);
        //使用ViewPager的适配器
//        mRankingTab.setTabsFromPagerAdapter(mAdapter);
        mRankingTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mRankingVp.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
