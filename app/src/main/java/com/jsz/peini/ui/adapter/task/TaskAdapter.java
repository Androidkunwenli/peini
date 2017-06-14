package com.jsz.peini.ui.adapter.task;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsz.peini.model.tabulation.TabulationMessageBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.LogUtil;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

/**
 * Created by kunwe on 2016/12/2.
 */
public class TaskAdapter extends LoopPagerAdapter {
    private TaskDetailActivity mActivity;
    private List<TabulationMessageBean.TaskInfoListBean.SellerImageBean> mSellerImage;

    public TaskAdapter(RollPagerView taskVp, TaskDetailActivity actvivty, List<TabulationMessageBean.TaskInfoListBean.SellerImageBean> sellerImage) {
        super(taskVp);
        mActivity = actvivty;
        mSellerImage = sellerImage;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        String url = IpConfig.HttpPic + mSellerImage.get(position).getImageSrc();
        GlideImgManager.loadImage(mActivity, url, view, "3");
        LogUtil.d("轮播图的uil====" + url);
        return view;
    }

    @Override
    public int getRealCount() {
        return mSellerImage != null && mSellerImage.size() > 0 ? mSellerImage.size() : 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}