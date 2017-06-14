package com.jsz.peini.ui.adapter.square;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.RecentDonationData;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.news.FansActivity;
import com.jsz.peini.ui.activity.square.AccountActivity;
import com.jsz.peini.ui.activity.square.GoldCoinActivity;
import com.jsz.peini.ui.activity.square.MiAttentionActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 15089 on 2017/2/21.
 */
public class GoldCoinAdapter extends RecyclerView.Adapter {


    private final List<RecentDonationData.DataBean.ListBean> mList;
    private int ADDOFLATE = 0;
    private int ADDITEM = 1;
    private int ITEM_TYPE_HEADER = 2;
    private final GoldCoinActivity mActivity;
    private Intent mIntent;


    public GoldCoinAdapter(GoldCoinActivity activity, List<RecentDonationData.DataBean.ListBean> list) {
        mActivity = activity;
        mList = list;

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ADDOFLATE;
        } else if (position >= getSize() + 1) {
            return ITEM_TYPE_HEADER;
        } else {
            return ADDITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADDOFLATE) {
            return new ViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_gold, parent));
        } else if (viewType == ITEM_TYPE_HEADER) {
            return new FootViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_nulldata, parent));
        } else {
            return new ViewHolderNext(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.item_gold_next, parent));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            setItemClickListener(holder);
        } else if (holder instanceof ViewHolderNext) {
            setViewNextData(holder, position - 1);
        } else if (holder instanceof FootViewHolder) {
            showFootView((FootViewHolder) holder);
        }
    }

    private void showFootView(FootViewHolder holder) {
        if (mList != null && mList.size() != 0) {
            holder.mDataSuccess.setVisibility(View.GONE);
        } else {
            holder.mDataSuccess.setVisibility(View.VISIBLE);
        }
    }

    /*8点击事件*/
    private void setItemClickListener(RecyclerView.ViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mItemFs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("粉丝");
                mIntent = new Intent(mActivity, FansActivity.class);
                mIntent.putExtra("pay", "pay");
                mActivity.startActivity(mIntent);
            }
        });
        viewHolder.mItemGz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("关注");
                mIntent = new Intent(mActivity, MiAttentionActivity.class);
                mActivity.startActivity(mIntent);
            }
        });
        viewHolder.mItemZdyh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("指定账户");
                mIntent = new Intent(mActivity, AccountActivity.class);
                mActivity.startActivity(mIntent);
            }
        });
    }

    private void setViewNextData(RecyclerView.ViewHolder holder, final int position) {
        ViewHolderNext holderNext = (ViewHolderNext) holder;
        final RecentDonationData.DataBean.ListBean dataInfo = mList.get(position);
        holderNext.mTitle.setText(dataInfo.getNickName());
        if (dataInfo.getRelation().equals("双方关注")) {
            holderNext.mIsFollow.setText("双方关注");
            holderNext.mIsFollow.setVisibility(View.VISIBLE);
        } else {
            holderNext.mIsFollow.setVisibility(View.GONE);
        }
        //显示头像
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + dataInfo.getHeadImg(), holderNext.mImage, "1");
        holderNext.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, dataInfo.getHisId(), dataInfo.getOtherId());
            }
        });
        holderNext.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onLongItemClick(position, dataInfo.getHisId(), dataInfo.getOtherId());
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return getSize() + 2;
    }

    private int getSize() {
        return mList.size() > 0 ? mList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.item_gz)
        LinearLayout mItemGz;
        @InjectView(R.id.item_fs)
        LinearLayout mItemFs;
        @InjectView(R.id.item_zdyh)
        LinearLayout mItemZdyh;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }

    class ViewHolderNext extends RecyclerView.ViewHolder {
        @InjectView(R.id.image)
        CircleImageView mImage;
        @InjectView(R.id.title)
        TextView mTitle;
        @InjectView(R.id.is_follow)
        TextView mIsFollow;

        public ViewHolderNext(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.data_success)
        LinearLayout mDataSuccess;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        /*点击*/
        void onItemClick(int position, String hisId, String otherId);

        /*长按*/
        void onLongItemClick(int position, String hisId, String otherId);
    }
}
