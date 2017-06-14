package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.model.square.UserInfoByOtherId;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.LogUtil;

import java.util.List;

/**
 * Created by th on 2016/12/30.
 */
public class TaImageListAdapter extends BaseRecyclerViewAdapter {

    private final List<UserInfoByOtherId.ImageListBean> mImageList;
    private final Activity mActivity;

    public TaImageListAdapter(Activity activity, List<UserInfoByOtherId.ImageListBean> imageList) {
        super(imageList);
        mImageList = imageList;
        mActivity = activity;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        TaImageListAdapter.ViewHolder holder = (TaImageListAdapter.ViewHolder) viewHolder;
        String Pic = IpConfig.HttpPic + "" + mImageList.get(position).getImageSrc() + "";
        LogUtil.d(mActivity.getLocalClassName(), "我的界面" + Pic);
        Glide.with(mActivity)
                .load(Pic)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotokListener.mPhotoItemkListener(position);
            }
        });
    }

    @Override
    public void onBindFootViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        super.onBindFootViewHolder(viewHolder, position);
        TaImageListAdapter.FootViewHolder holder = (TaImageListAdapter.FootViewHolder) viewHolder;
        if (list.size() != 0) {
            holder.mReleasePhotoFoot.setVisibility(View.GONE);
        } else {
            holder.mReleasePhotoFoot.setVisibility(View.VISIBLE);
        }
        holder.mReleasePhotoFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPhotokListener.FootViewItemkListener(position);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_imagelist, viewGroup, false);
        return new TaImageListAdapter.ViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder setFootViewHolder(ViewGroup viewGroup, View footView) {
        return new TaImageListAdapter.FootViewHolder(footView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        ImageView mReleasePhotoFoot;

        FootViewHolder(View itemView) {
            super(itemView);
            mReleasePhotoFoot = (ImageView) itemView.findViewById(R.id.release_photo_foot);
        }
    }

    private OnClicPhotokListener mPhotokListener;

    public void setOnClicPhotokListener(OnClicPhotokListener photokListener) {
        mPhotokListener = photokListener;
    }

    public interface OnClicPhotokListener {
        /**
         * 点击了相册列表
         */
        void mPhotoItemkListener(int position);

        /**
         * 点击了拍照
         */
        void FootViewItemkListener(int position);
    }
}
