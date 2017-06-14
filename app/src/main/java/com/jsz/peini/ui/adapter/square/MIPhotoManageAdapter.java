package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
 * Created by th on 2017/1/5.
 */
public class MIPhotoManageAdapter extends BaseRecyclerViewAdapter {

    private Activity mActivity;
    private List<ImageListBean> mUserImageAll;

    public void setIsManaging(boolean isManaging) {
        mIsManaging = isManaging;
    }

    /**
     * 是否管理
     */
    private boolean mIsManaging;

    public MIPhotoManageAdapter(Activity activity, List<ImageListBean> userImageAll, boolean isManaging) {
        super(userImageAll);
        mActivity = activity;
        mUserImageAll = userImageAll;
        mIsManaging = isManaging;
    }

    @Override
    public void setList(List list) {
        super.setList(list);
        mUserImageAll = list;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.index = position;

        ImageListBean imageListBean = mUserImageAll.get(position);
        String imageView = IpConfig.HttpPic + imageListBean.getImageSrc() + "";

        holder.mCbMiImageViewManage.setVisibility(mIsManaging ? View.VISIBLE : View.GONE);
        holder.mCbMiImageViewManage.setChecked(imageListBean.isChecked());
//        if (mIsCheck) {
//            List<ImageListBean> userImageAll = mUserImageAll;
//            for (int i = 0; i < userImageAll.size(); i++) {
//                mStringImage.add(userImageAll.get(i).getId() + "");
//            }
//        } else {
//            mStringImage.remove(imageListBean.getId() + "");
//        }
        if (StringUtils.isNoNull(imageView)) {
            GlideImgManager.loadImage(mActivity, imageView, holder.mImageView, "5");
        }
//        holder.mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mOnCheckedChangedListener.setCheckPhoto(position, imageView);
//            }
//        });
//        holder.mCbMiImageViewManage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean select) {
////                if (select) {
////                    LogUtil.d(select + "这个是选择了多少个--" + mStringImage.size() + "具体的第几个---", position + "附上连接的地址" + imageView);
////                    mOnCheckedChangedListener.setCheckItem(position, imageView);
////                    mStringImage.add(imageListBean.getId() + "");
////                } else {
////                    mStringImage.remove(imageListBean.getId() + "");
////                }
////                mOnCheckedChangedListener.setCheckAll(mStringImage.size() == mUserImageAll.size());
//            }
//        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_photo_manage, viewGroup, false);
        int screenWidth = UiUtils.getScreenWidth(mActivity) / 4;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, screenWidth);
        view.setLayoutParams(lp);
        return new ViewHolder(view);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.imageview)
        ImageView mImageView;
        @InjectView(R.id.mi_imageview_manage)
        CheckBox mCbMiImageViewManage;

        private int index;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @OnClick({R.id.imageview, R.id.mi_imageview_manage})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imageview:
                    if (mIsManaging) {
                        boolean isChecked = mCbMiImageViewManage.isChecked();
                        setImageChecked(index, !isChecked);
                        mCbMiImageViewManage.setChecked(!isChecked);
                        if (mOnCheckedChangedListener != null) {
                            mOnCheckedChangedListener.setCheckItem(index);
                        }
                    } else {
                        if (mOnCheckedChangedListener != null) {
                            mOnCheckedChangedListener.setClickPhoto(index);
                        }
                    }

                    break;
                case R.id.mi_imageview_manage:
                    boolean isChecked = mCbMiImageViewManage.isChecked();
                    setImageChecked(index, isChecked);
                    mCbMiImageViewManage.setChecked(isChecked);
                    if (mOnCheckedChangedListener != null) {
                        mOnCheckedChangedListener.setCheckItem(index);
                    }
                    break;
            }
        }

    }

    private void setImageChecked(int index, boolean isChecked) {
        if (index >= 0 && index < mUserImageAll.size()) {
            if (mUserImageAll.get(index) != null) {
                mUserImageAll.get(index).setChecked(isChecked);
            }
        }
    }

    private onCheckedAllChangedListener mOnCheckedChangedListener;

    public void setOnCheckedChangedListener(onCheckedAllChangedListener onCheckedChangedListener) {
        mOnCheckedChangedListener = onCheckedChangedListener;
    }

    public interface onCheckedAllChangedListener {

        /**
         * 选择了哪个条目 和连接的地址
         */
        void setCheckItem(int position);

        void setClickPhoto(int index);
    }
}
