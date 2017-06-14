package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.LikeListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by th on 2016/12/24.
 */
// RecyclerView.Adapter<RecyclerView.ViewHolder>
public class SquareLikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int ITEM_TYPE_HEADER = 0;
    private int ITEM_TYPE_CONTENT = 1;
    private List<LikeListBean> mList;
    private Context mActivity;

    public SquareLikeAdapter(Context activity, List<LikeListBean> list) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeadViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.square_head_like, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.square_like, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            setHeaderView((HeadViewHolder) holder, position);
        } else if (holder instanceof ViewHolder) {
            setMyRankView((ViewHolder) holder, position - 1);
        }
    }

    private void setMyRankView(ViewHolder holder, int position) {
        final LikeListBean likeListBean = mList.get(position);
        String sex = likeListBean.getSex();
        String url;
        String imageHead = likeListBean.getImageHead();
        if (imageHead.contains("PEINI_CACHE")) {
            url = imageHead;
        } else {
            url = IpConfig.HttpPic + imageHead;
        }
        GlideImgManager.loadImage(mActivity, url, holder.mSquareLikeImageview, sex + "");
        holder.mSquareLikeImageview.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击头像的跳转事件---点赞列表
                String userId = likeListBean.getUserId();
                if (SpUtils.getUserToken(mActivity).equals(userId)) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    Intent intent = new Intent(mActivity, TaSquareActivity.class);
                    intent.putExtra(Conversion.USERID, userId);
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    private void setHeaderView(HeadViewHolder holder, int position) {
        holder.mSquareLikeImageview.setImageResource(R.mipmap.like);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    private int getSize() {
        return mList != null && mList.size() > 0 ? mList.size() + 1 : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.square_like_imageview)
        CircleImageView mSquareLikeImageview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.square_like_imageview)
        ImageView mSquareLikeImageview;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }
    }
}
