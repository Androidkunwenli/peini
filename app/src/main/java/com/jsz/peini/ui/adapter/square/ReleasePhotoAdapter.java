package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsz.peini.R;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.ui.activity.square.LargerImageActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2016/12/27.
 */
public class ReleasePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_HEADER = 1;
    private static final int ITEM_TYPE_CONTENT = 2;
    private final Activity mActivity;
    private List<String> mList;

    public ReleasePhotoAdapter(Activity context, List<String> list) {
        mList = list;
        mActivity = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= getSize()) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View view = UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_release_photo, parent);
            //动态设置ImageView的宽高，根据自己每行item数量计算
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UiUtils.getScreenWidth(mActivity) / 4 - UiUtils.dip2px(mActivity, 5f),
                    UiUtils.getScreenWidth(mActivity) / 4 - UiUtils.dip2px(mActivity, 5f));
            lp.setMargins(0, 0, 0, UiUtils.dip2px(mActivity, 3f));
            view.setLayoutParams(lp);
            return new FootViewHolder(view);
        } else {
            View view = UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_release_photo, parent);
            //动态设置ImageView的宽高，根据自己每行item数量计算
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(UiUtils.getScreenWidth(mActivity) / 4 - UiUtils.dip2px(mActivity, 5f),
                    UiUtils.getScreenWidth(mActivity) / 4 - UiUtils.dip2px(mActivity, 5f));
            lp.setMargins(0, 0, 0, UiUtils.dip2px(mActivity, 3f));
            view.setLayoutParams(lp);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
            setHeaderView((FootViewHolder) holder, position);
        } else if (holder instanceof ViewHolder) {
            setMyRankView((ViewHolder) holder, position);
        }
    }

    private void setMyRankView(ViewHolder holder, final int position) {
        if (mList.size() != 0) {
            String pathname = mList.get(position);
            Glide.with(mActivity).load(pathname).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mReleasePhoto);
            holder.mReleasePhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnPhotoFootClickListener.deleteListener(position);
                    return true;
                }
            });
            holder.mReleasePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ImageListBean> list = new ArrayList<>();
                    list.clear();
                    for (String s : mList) {
                        ImageListBean listBean = new ImageListBean();
                        listBean.setImageSrc(s);
                        list.add(listBean);
                    }
                    Intent intent = new Intent(mActivity, LargerImageActivity.class);
                    intent.putExtra(Conversion.LARGERIMAGEACTIVITY, list);
                    intent.putExtra(Conversion.SHOWINDEX, position);
                    intent.putExtra(Conversion.TYPE, 1);
                    intent.putExtra(Conversion.FILE, true);
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    private void setHeaderView(FootViewHolder holder, final int position) {
        if (getSize() > 8) {
            holder.mReleasePhoto.setVisibility(View.GONE);
        } else {
            holder.mReleasePhoto.setVisibility(View.VISIBLE);
        }
        holder.mReleasePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnPhotoFootClickListener.FootClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getSize() + 1;
    }

    private int getSize() {
        return mList != null && mList.size() > 0 ? mList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.release_photo)
        ImageView mReleasePhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.release_photo)
        ImageView mReleasePhoto;

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public OnPhotoFootClickListener mOnPhotoFootClickListener;

    public void setOnPhotoFootClickListener(OnPhotoFootClickListener onPhotoFootClickListener) {
        mOnPhotoFootClickListener = onPhotoFootClickListener;
    }

    public interface OnPhotoFootClickListener {
        void FootClickListener(int position);

        void deleteListener(int position);
    }

    public void setPhotoList(List photoList) {
        mList = photoList;
        notifyDataSetChanged();
    }
}
