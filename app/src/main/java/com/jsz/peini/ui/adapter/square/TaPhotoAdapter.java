package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.UserImageAllByUserId;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 他人相册RecyclerView适配器
 * Created by huizhe.ju on 2017/2/25.
 */
public class TaPhotoAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<UserImageAllByUserId.UserImageListBean> mImageList;
    private RecyclerView.RecycledViewPool recycledViewPool;

    public TaPhotoAdapter(Context context, List<UserImageAllByUserId.UserImageListBean> mUserImageList) {
        super(mUserImageList);
        mContext = context;
        mImageList = mUserImageList;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        UserImageAllByUserId.UserImageListBean userImageListBean = mImageList.get(position);
        String imageTime = userImageListBean.getImageTime();
        if (StringUtils.isNoNull(imageTime)) {
            holder.mTvImageTime.setText(imageTime);
        }
//        if (holder.mMiPhotoManageAdapterRecyclerView.getAdapter() == null) {
        List<ImageListBean> itemImageList = userImageListBean.getUserImageAll();
        TaPhotoItemAdapter taPhotoItemAdapter = new TaPhotoItemAdapter(mContext, itemImageList);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 4);
        layoutManager.setRecycleChildrenOnDetach(true);
        holder.mRecyclerView.setLayoutManager(layoutManager);
        if (recycledViewPool == null) {
            recycledViewPool = holder.mRecyclerView.getRecycledViewPool();
        } else {
            holder.mRecyclerView.setRecycledViewPool(recycledViewPool);
        }
        holder.mRecyclerView.setAdapter(taPhotoItemAdapter);

        final int positionIndex = position;
        taPhotoItemAdapter.setOnChickPhotoListener(new TaPhotoItemAdapter.OnChickPhotoListener() {
            @Override
            public void onClickPhoto(int index) {
                mOnClickPhotoListener.setClickPhoto(positionIndex, index);
            }
        });
//        } else {
//            ((MIPhotoManageAdapter) holder.mMiPhotoManageAdapterRecyclerView.getAdapter()).setIsManaging(mIsManaging);
//            ((MIPhotoManageAdapter) holder.mMiPhotoManageAdapterRecyclerView.getAdapter()).setList(userImageAll);
//            holder.mMiPhotoManageAdapterRecyclerView.getAdapter().notifyDataSetChanged();
//        }
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ta_photo_gallery, viewGroup, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tvImageTime)
        TextView mTvImageTime;
        @InjectView(R.id.recycler_view_ta_photo_gallery)
        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    private OnClickPhotoListener mOnClickPhotoListener;

    public void setOnClickPhotoListener(OnClickPhotoListener onClickPhotoListener) {
        mOnClickPhotoListener = onClickPhotoListener;
    }

    public interface OnClickPhotoListener {
        /**
         * 点击的图片索引
         */
        void setClickPhoto(int position, int index);
    }
}