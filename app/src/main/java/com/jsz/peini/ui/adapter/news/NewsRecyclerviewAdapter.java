package com.jsz.peini.ui.adapter.news;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.news.NewsList;
import com.jsz.peini.presenter.IpConfig;

public class NewsRecyclerviewAdapter extends BaseQuickAdapter<NewsList.ObjectListBean, BaseViewHolder> {

    private Activity mActivity;

    public NewsRecyclerviewAdapter(Activity activity) {
        super(R.layout.news_view_item);
        mActivity = activity;
    }


    @Override
    protected void convert(final BaseViewHolder holder, NewsList.ObjectListBean listBean) {
        String DoubleDesc = listBean.getDoubleDesc() + "";
        holder.setText(R.id.doubleDesc, DoubleDesc.equals("1") ? "双方关注" : "");
        holder.setText(R.id.nickname, listBean.getNickname());
        String url = IpConfig.HttpPic + listBean.getImageHead();
        Glide.with(mActivity.getApplicationContext())
                .load(url)
                .asBitmap()
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