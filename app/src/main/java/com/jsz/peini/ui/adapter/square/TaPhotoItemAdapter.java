package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jsz.peini.R;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 他人相册子RecyclerView适配器
 * Created by huizhe.ju on 2017/2/25.
 */
public class TaPhotoItemAdapter extends BaseRecyclerViewAdapter {

    private Context mContext;
    private List<ImageListBean> mImageList;

    public TaPhotoItemAdapter(Context context, List<ImageListBean> imageList) {
        super(imageList);
        mContext = context;
        mImageList = imageList;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.index = position;

        ImageListBean imageListBean = mImageList.get(position);
        String imageView = IpConfig.HttpPic + imageListBean.getImageSrc();
        if (StringUtils.isNoNull(imageView)) {
            GlideImgManager.loadImage(mContext, imageView, holder.mImageView, "");
        }
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ta_photo_gallery_item, viewGroup, false);
        int screenWidth = UiUtils.getScreenWidth(mContext) / 4;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, screenWidth);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_ta_photo_gallery_item)
        ImageView mImageView;

        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @OnClick(R.id.iv_ta_photo_gallery_item)
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_ta_photo_gallery_item:
                    if (mOnChickPhotoListener != null) {
                        mOnChickPhotoListener.onClickPhoto(index);
                    }
                    break;
            }
        }

    }

    private OnChickPhotoListener mOnChickPhotoListener;

    public void setOnChickPhotoListener(OnChickPhotoListener onChickPhotoListener) {
        mOnChickPhotoListener = onChickPhotoListener;
    }

    public interface OnChickPhotoListener {
        void onClickPhoto(int index);
    }
}
