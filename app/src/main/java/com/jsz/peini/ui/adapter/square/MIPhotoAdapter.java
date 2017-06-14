package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.UserImageAllByUserId;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by th on 2017/1/5.
 */
public class MIPhotoAdapter extends BaseRecyclerViewAdapter {

    private Activity mActivity;
    private List<UserImageAllByUserId.UserImageListBean> mUserImageList;
    private boolean mIsManaging;

//    private RecyclerView.RecycledViewPool recycledViewPool;
    private MIPhotoAdapter mAdapter;

    public MIPhotoAdapter(Activity activity, List<UserImageAllByUserId.UserImageListBean> mUserImageList) {
        super(mUserImageList);
        mActivity = activity;
        this.mUserImageList = mUserImageList;
        mAdapter = this;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.position = position;
        UserImageAllByUserId.UserImageListBean userImageListBean = mUserImageList.get(position);
        String imageTime = userImageListBean.getImageTime();
        if (StringUtils.isNoNull(imageTime)) {
            holder.mTvImageTime.setText(imageTime);
        }

        /**是否管理照片*/
        holder.mCbMiPhotoManageTime.setVisibility(mIsManaging ? View.VISIBLE : View.GONE);

        if (mIsManaging) {
            holder.mCbMiPhotoManageTime.setChecked(isAllChecked(position));
        }

        List<ImageListBean> userImageAll = userImageListBean.getUserImageAll();
        if (holder.mMiPhotoManageAdapterRecyclerView.getAdapter() == null) {
            MIPhotoManageAdapter mMIPhotoManageAdapter = new MIPhotoManageAdapter(mActivity, userImageAll, mIsManaging);
            GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 4);
//            layoutManager.setRecycleChildrenOnDetach(true);
            holder.mMiPhotoManageAdapterRecyclerView.setLayoutManager(layoutManager);

//            if (recycledViewPool == null) {
//                recycledViewPool = holder.mMiPhotoManageAdapterRecyclerView.getRecycledViewPool();
//            } else {
//                holder.mMiPhotoManageAdapterRecyclerView.setRecycledViewPool(recycledViewPool);
//            }

            holder.mMiPhotoManageAdapterRecyclerView.setAdapter(mMIPhotoManageAdapter);

            mMIPhotoManageAdapter.setOnCheckedChangedListener(new MIPhotoManageAdapter.onCheckedAllChangedListener() {
                @Override
                public void setCheckItem(int index) {
//                    mAdapter.notifyItemChanged(position);
                    mAdapter.notifyDataSetChanged();
                    if (mOnCheckedChangedListener != null) {
                        mOnCheckedChangedListener.setCheckedPhoto();
                    }
                }

                @Override
                public void setClickPhoto(int index) {
                    mOnCheckedChangedListener.setClickPhoto(position, index);
                }
            });
        } else {
            ((MIPhotoManageAdapter) holder.mMiPhotoManageAdapterRecyclerView.getAdapter()).setIsManaging(mIsManaging);
            ((MIPhotoManageAdapter) holder.mMiPhotoManageAdapterRecyclerView.getAdapter()).setList(userImageAll);
            holder.mMiPhotoManageAdapterRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.mi_photo_manage, viewGroup, false);
        return new ViewHolder(view);
    }

    public void setImageManage(boolean imageManage) {
        mIsManaging = imageManage;
        clearImagesCheck();
        notifyDataSetChanged();
    }

    private void setImagesChecked(int position, boolean isChecked) {
        if (mUserImageList != null && position >= 0 && position < mUserImageList.size()) {
            UserImageAllByUserId.UserImageListBean userImageListBean = mUserImageList.get(position);
            if (userImageListBean != null && userImageListBean.getUserImageAll() != null
                    && userImageListBean.getUserImageAll().size() > 0) {
                for (ImageListBean itemBean : userImageListBean.getUserImageAll()) {
                    itemBean.setChecked(isChecked);
                }
            }
        }
    }

    private void clearImagesCheck() {
        if (mUserImageList != null && mUserImageList.size() > 0) {
            for (UserImageAllByUserId.UserImageListBean userImageListBean : mUserImageList) {
                if (userImageListBean != null && userImageListBean.getUserImageAll() != null
                        && userImageListBean.getUserImageAll().size() > 0) {
                    for (ImageListBean imageListBean : userImageListBean.getUserImageAll()) {
                        imageListBean.setChecked(false);
                    }
                }
            }
        }
    }

    private boolean isAllChecked(int position) {
        if (mUserImageList != null && position >= 0 && position < mUserImageList.size()) {
            UserImageAllByUserId.UserImageListBean userImageListBean = mUserImageList.get(position);
            if (userImageListBean != null && userImageListBean.getUserImageAll() != null
                    && userImageListBean.getUserImageAll().size() > 0) {
                for (ImageListBean itemBean : userImageListBean.getUserImageAll()) {
                    if (!itemBean.isChecked()) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.mi_photo_manage_adapter)
        RecyclerView mMiPhotoManageAdapterRecyclerView;
        @InjectView(R.id.imagetime)
        TextView mTvImageTime;
        @InjectView(R.id.mi_image_manage_time)
        CheckBox mCbMiPhotoManageTime;

        private int position;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @OnClick(R.id.mi_image_manage_time)
        public void onClick() {
            setImagesChecked(position, mCbMiPhotoManageTime.isChecked());
//            mAdapter.notifyItemChanged(position);
            mAdapter.notifyDataSetChanged();
            if (mOnCheckedChangedListener != null) {
                mOnCheckedChangedListener.setCheckedPhoto();
            }
        }
    }

    private onCheckedChangedListener mOnCheckedChangedListener;

    public void setOnCheckedChangedListener(onCheckedChangedListener onCheckedChangedListener) {
        mOnCheckedChangedListener = onCheckedChangedListener;
    }

    public interface onCheckedChangedListener {
        /**
         * 点击的图片索引
         */
        void setClickPhoto(int position, int index);

        void setCheckedPhoto();
    }
}