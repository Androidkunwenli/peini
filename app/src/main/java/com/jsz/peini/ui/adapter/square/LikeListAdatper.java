package com.jsz.peini.ui.adapter.square;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.LikeListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.LikeListActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2017/3/6.
 */

public class LikeListAdatper extends RecyclerView.Adapter {

    private final LikeListActivity mActivity;
    private final ArrayList<LikeListBean> mList;

    public LikeListAdatper(LikeListActivity activity, ArrayList<LikeListBean> list) {
        mActivity = activity;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.news_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        LikeListBean bean = mList.get(position);
        final String userId = bean.getUserId();
        String sex = bean.getSex();
        viewHolder.mNickname.setText(bean.getUserNickname());
        viewHolder.mDoubleDesc.setVisibility(View.GONE);
        String url;
        String imageHead = bean.getImageHead();
        if (imageHead.contains("PEINI_CACHE")) {
            url = imageHead;
        } else {
            url = IpConfig.HttpPic + imageHead;
        }
        GlideImgManager.loadImage(mActivity, url, viewHolder.mImageHead, sex + "");
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(userId, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    private int getSize() {
        return mList.size() > 0 ? mList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.nickname)
        TextView mNickname;
        @InjectView(R.id.doubleDesc)
        TextView mDoubleDesc;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private ItemClickListener mListener;

    public void setItemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemClickListener {
        void onItemClick(String userId, int position);
    }
}
