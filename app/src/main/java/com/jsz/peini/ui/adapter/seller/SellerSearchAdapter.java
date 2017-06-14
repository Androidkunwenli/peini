package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.gen.SellerListHotBean;
import com.jsz.peini.model.filter.HotWordBean;
import com.jsz.peini.ui.activity.search.IsSearchActivity;
import com.jsz.peini.ui.adapter.search.TaskHot9Adapter;
import com.jsz.peini.ui.view.SpacesItemDecoration;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2017/3/4.
 */
public class SellerSearchAdapter extends RecyclerView.Adapter {

    private List<SellerListHotBean> mHistoryHotBeen;
    private int TASK_SEARCH_ONE = 0;
    private int TASK_SEARCH_TOW = 1;
    private int TASK_SEARCH_THREE = 2;
    private final Activity mActivity;
    private final List<HotWordBean.DataBean> mList;
    private int mHeaderCount = 1;
    private int mBottomCount = 0;

    public SellerSearchAdapter(IsSearchActivity activity, List<HotWordBean.DataBean> list, List<SellerListHotBean> historyHotBeen) {
        mActivity = activity;
        mList = list;
        mHistoryHotBeen = historyHotBeen;
    }


    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getSize();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return TASK_SEARCH_ONE;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return TASK_SEARCH_THREE;
        } else {
            //内容View
            return TASK_SEARCH_TOW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TASK_SEARCH_ONE) {
            return new ViewHolderHead(LayoutInflater.from(mActivity).inflate(R.layout.item_task_search_one, parent, false));
        } else if (viewType == TASK_SEARCH_THREE) {
            return new ViewHolderThree(LayoutInflater.from(mActivity).inflate(R.layout.item_delete, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_task_search_end_tow, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderHead) {
            setViewHot9(holder, position);
        } else if (holder instanceof ViewHolder) {
            setHistoryRecord(holder, position - 1);
        } else if (holder instanceof ViewHolderThree) {
            setDelete(holder);
        }
    }

    private void setDelete(RecyclerView.ViewHolder holder) {
        ViewHolderThree holderThree = (ViewHolderThree) holder;
        holderThree.mDeleteHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDetele();
            }
        });
    }

    public void setBottomCount(int BottomCount) {
        mHistoryHotBeen.clear();
        mBottomCount = BottomCount;
        notifyDataSetChanged();
    }


    /**
     * 这个是热门搜索
     */
    private void setViewHot9(RecyclerView.ViewHolder holder, int position) {
        ViewHolderHead holderHead = (ViewHolderHead) holder;
        if (null != mList && mList.size() != 0) {
            holderHead.mTvHand.setVisibility(View.VISIBLE);
            holderHead.mTaskHot9.setVisibility(View.VISIBLE);
            holderHead.mTaskHot9.setLayoutManager(new GridLayoutManager(mActivity, 3));
            TaskHot9Adapter adapter = new TaskHot9Adapter(mActivity, mList);
            holderHead.mTaskHot9.setAdapter(adapter);
            adapter.setOnItemHotClickListener(new TaskHot9Adapter.OnItemHotClickListener() {
                @Override
                public void ItemHotClick(int id, String hotName, int hotNum, int type) {
                    mListener.ItemHotClick(id, type, hotName, hotName);
                }
            });
        } else {
            holderHead.mTvHand.setVisibility(View.GONE);
            holderHead.mTaskHot9.setVisibility(View.GONE);
        }
    }

    /**
     * 这个是保存的的历史记录
     */
    private void setHistoryRecord(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        SellerListHotBean hotBean = mHistoryHotBeen.get(position);
        final String sellerInfoId = hotBean.getSellerInfoId();
        final String sellerName = hotBean.getSellerName();
        final int searchType = hotBean.getSearchType();
        String sellerAddress = hotBean.getSellerAddress();
        /***/
        switch (searchType) {
            case 1:
                viewHolder.mLlTypeOne.setVisibility(View.VISIBLE);
                viewHolder.mLlTypeTow.setVisibility(View.GONE);
                viewHolder.mSearchAddress.setText(sellerName);
                viewHolder.mSearchAddressInformation.setText(sellerAddress);
                viewHolder.mIvImage.setImageResource(R.mipmap.map);
                break;
            case 2:
                viewHolder.mLlTypeTow.setVisibility(View.VISIBLE);
                viewHolder.mLlTypeOne.setVisibility(View.GONE);
                viewHolder.mTvNickName.setText(sellerName);
                viewHolder.mIvImage.setImageResource(R.mipmap.search);
                break;
            default:
                break;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(searchType, sellerInfoId, sellerName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getSize() + mHeaderCount + mBottomCount;
    }

    private int getSize() {
        return mHistoryHotBeen.size() > 0 ? mHistoryHotBeen.size() : 0;
    }

    public void setListHot(List<SellerListHotBean> listHot) {
        mHistoryHotBeen = listHot;
        if (getSize() != 0) {
            mBottomCount = 1;
        } else {
            mBottomCount = 0;
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_nickName)
        TextView mTvNickName;
        @InjectView(R.id.tv_result)
        TextView mTvResult;
        @InjectView(R.id.ll_type_tow)
        LinearLayout mLlTypeTow;
        @InjectView(R.id.search_address)
        TextView mSearchAddress;
        @InjectView(R.id.search_address_distance)
        TextView mSearchAddressDistance;
        @InjectView(R.id.search_address_information)
        TextView mSearchAddressInformation;
        @InjectView(R.id.ll_type_one)
        LinearLayout mLlTypeOne;
        @InjectView(R.id.iv_image)
        ImageView mIvImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    class ViewHolderHead extends RecyclerView.ViewHolder {
        @InjectView(R.id.task_hot_9)
        RecyclerView mTaskHot9;
        @InjectView(R.id.tv_hand)
        TextView mTvHand;

        public ViewHolderHead(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnClickListener mListener;

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        /**
         * 头条目的点击事件
         */
        void ItemHotClick(int id, int searchType, String sellerinfoid, String sellername);


        void itemClick(int searchType, String sellerinfoid, String sellername);

        void onDetele();
    }

    class ViewHolderThree extends RecyclerView.ViewHolder {
        @InjectView(R.id.delete_hot)
        TextView mDeleteHot;

        public ViewHolderThree(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
