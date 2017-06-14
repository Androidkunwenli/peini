package com.jsz.peini.ui.adapter.square;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jsz.peini.base.BaseFragment;

import java.util.List;

/**
 * Created by th on 2017/1/24.
 */
public class MiTaskAdapter extends FragmentPagerAdapter {

    public final FragmentManager mFm;
    public final List<BaseFragment> mFragment;
    public final List<String> mListTitle;

    public MiTaskAdapter(FragmentManager fm, List<BaseFragment> fragment, List<String> listTitle) {
        super(fm);
        mFm = fm;
        mFragment = fragment;
        mListTitle = listTitle;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        return mFragment.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return mListTitle.get(position % mListTitle.size());
    }
}
