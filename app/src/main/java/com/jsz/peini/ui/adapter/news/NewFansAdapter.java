package com.jsz.peini.ui.adapter.news;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.news.MyFans;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.news.FansActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.activity.square.VerifyDataActivity;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/1/2.
 */
public class NewFansAdapter extends BaseQuickAdapter<MyFans.ObjectListBean, BaseViewHolder> {

    private Activity mActivity;

    public NewFansAdapter(Activity activity) {
        super(R.layout.news_view_item);
        mActivity = activity;
    }

    @Override
    protected void convert(final BaseViewHolder holder, MyFans.ObjectListBean listBean) {
        String nickname = listBean.getNickname();
        holder.setText(R.id.nickname, nickname);
        String createTime = listBean.getCreateTime();
        holder.setText(R.id.doubleDesc, createTime);
        String url = IpConfig.HttpPic + listBean.getImageHead();
        Glide.with(mActivity.getApplicationContext())
                .load(url)
                .asBitmap()
                .error(R.mipmap.ic_nan)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        holder.setImageBitmap(R.id.imageHead, resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        holder.setImageResource(R.id.imageHead, R.mipmap.ic_nan);
                    }
                });
    }
}
