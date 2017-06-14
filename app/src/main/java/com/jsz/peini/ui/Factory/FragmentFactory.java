package com.jsz.peini.ui.Factory;

import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.ui.fragment.ManageFragment;
import com.jsz.peini.ui.fragment.MapFragment;
import com.jsz.peini.ui.fragment.RankingFragment;
import com.jsz.peini.ui.fragment.SettingFragment;
import com.jsz.peini.ui.fragment.SquareFragment;
import com.jsz.peini.ui.fragment.StoreFragment;
import com.jsz.peini.ui.fragment.TabulationFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kunwe on 2016/10/14.
 */

public class FragmentFactory {
    public static final int FRAGMENT_HOME = 0;//首页
    public static final int FRAGMENT_SQUARE = 1;//广场
    public static final int FRAGMENT_SELLER = 2;//商家
    public static final int FRAGMENT_RANKING = 3;//排行榜
    public static final int FRAGMENT_MANAGE = 4;//店铺管理
    public static final int FRAGMEN_TABULATION = 5;//列表
    public static final int FRAGMEN_SETTING = 6;//设置
    public static Map<Integer, BaseFragment> MAP = new HashMap<>();

    public static BaseFragment setFragmentView(int position) {
        BaseFragment fragment = null;
        if (MAP.containsKey(position)) {
            fragment = MAP.get(position);
            return fragment;
        }
        switch (position) {
            case FRAGMENT_HOME:
                fragment = new MapFragment();
                break;
            case FRAGMENT_SQUARE:
                fragment = new SquareFragment();
                break;
            case FRAGMENT_SELLER:
//                fragment = new SellerFragment();
                fragment = new StoreFragment();
                break;
            case FRAGMENT_RANKING:
                fragment = new RankingFragment();
                break;
            case FRAGMENT_MANAGE:
                fragment = new ManageFragment();
                break;
            case FRAGMEN_TABULATION:
                fragment = new TabulationFragment();
                break;
            case FRAGMEN_SETTING:
                fragment = new SettingFragment();
                break;
        }
        MAP.put(position, fragment);
        return fragment;
    }
}
