package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

/**
 * Created by th on 2017/2/9.
 */
public class AdvertiseListStringAdapter extends LoopPagerAdapter {
    public final List<String> mAdModels;
    public final Activity mActivity;

    public AdvertiseListStringAdapter(RollPagerView viewPager, Activity activity, List<String> adModels) {
        super(viewPager);
        mAdModels = adModels;
        mActivity = activity;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        String advertiseListBean = mAdModels.get(position);
        ImageView view = new ImageView(mActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        LogUtil.d("店铺获取的图片----" + advertiseListBean);
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + advertiseListBean, view, "");
        return view;
    }

    @Override
    public int getRealCount() {
        return Conversion.getSize(mAdModels);
    }
}