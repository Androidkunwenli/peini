package com.jsz.peini.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsz.peini.R;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.UiUtils;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunwe on 2016/11/26.
 * 商家的轮播图界面
 */

public class TestNormalAdapter extends LoopPagerAdapter {
    private List<AdModel.AdvertiseListBean> mAdModels = new ArrayList<>();
    private Activity mActivity;

    public TestNormalAdapter(RollPagerView viewPager, Activity activity, List<AdModel.AdvertiseListBean> adModels) {
        super(viewPager);
        mAdModels = adModels;
        mActivity = activity;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        AdModel.AdvertiseListBean advertiseListBean = mAdModels.get(position);
        ImageView view = new ImageView(mActivity);// UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_imagelist, container);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        String url = IpConfig.HttpPic + advertiseListBean.getAdImgUrl();
        LogUtil.d("商家url" + url);
        GlideImgManager.loadImage(mActivity, url, view, "3");
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return view;
    }

    @Override
    public int getRealCount() {
        return Conversion.getSize(mAdModels);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setData(List<AdModel.AdvertiseListBean> adModels) {
        mAdModels = adModels;
        notifyDataSetChanged();
    }
}
