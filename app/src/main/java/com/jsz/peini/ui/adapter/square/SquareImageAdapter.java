package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsz.peini.R;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.LargerImageActivity;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.UiUtils;

import java.util.ArrayList;

public class SquareImageAdapter extends BaseRecyclerViewAdapter {

    private int mMeasuredWidth;
    private Context mActivity;
    private ArrayList<ImageListBean> mList;

    public SquareImageAdapter(Context activity, ArrayList<ImageListBean> list, int measuredWidth) {
        super(list);
        mActivity = activity;
        mList = list;
        mMeasuredWidth = measuredWidth;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        MyViewHolder holder = (MyViewHolder) viewHolder;

        final String imageSmall = mList.get(position).getImageSmall();
        boolean imageIsFile = mList.get(position).isImageIsFile();
        if (!TextUtils.isEmpty(imageSmall)) {
            GlideImgManager.loadImage(mActivity, (imageIsFile ? IpConfig.HttpPic : "") + imageSmall, holder.imageView, "5");
        }

        holder.imageView.setBackgroundResource(R.color.white000);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mActivity, LargerImageActivity.class);
                intent.putExtra(Conversion.LARGERIMAGEACTIVITY, mList);
                intent.putExtra(Conversion.SHOWINDEX, position);
                intent.putExtra(Conversion.TYPE, 1);
                intent.putExtra(Conversion.FILE, !imageSmall.contains("square"));
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_grid, viewGroup, false);
        int itemLength = (UiUtils.getScreenWidth(mActivity) - UiUtils.dip2px(mActivity, 75)) / 3;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemLength, itemLength);
        view.setLayoutParams(lp);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv);
        }
    }

    OnImageClickListener mListener;

    private void setOnCommentClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    interface OnImageClickListener {
    }
}
