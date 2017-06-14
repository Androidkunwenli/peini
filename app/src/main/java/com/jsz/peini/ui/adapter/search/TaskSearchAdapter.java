package com.jsz.peini.ui.adapter.search;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.gen.HistoryHotBean;
import com.jsz.peini.model.filter.HotWordBean;
import com.jsz.peini.ui.view.SpacesItemDecoration;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 15089 on 2017/2/13.
 */
public class TaskSearchAdapter extends RecyclerView.Adapter {

    private final List<HistoryHotBean> mHistoryHotBeen;
    private int TASK_SEARCH_ONE = 0;
    private int TASK_SEARCH_TOW = 1;
    private int TASK_SEARCH_THREE = 2;
    private final Activity mActivity;
    private final List<HotWordBean.DataBean> mList;
    private int mHeaderCount = 1;
    private int mBottomCount = 0;

    public TaskSearchAdapter(Activity activity, List<HotWordBean.DataBean> list, List<HistoryHotBean> historyHotBeen) {
        mActivity = activity;
        mList = list;
        mHistoryHotBeen = historyHotBeen;
        if (getSize() != 0) {
            mBottomCount = 1;
        } else {
            mBottomCount = 0;
        }
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
                    mListener.ItemHotClick(id, hotName, hotNum);
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

        HistoryHotBean hotBean = mHistoryHotBeen.get(position);
        if (hotBean != null) {//mList.size() != 0 &&
            ViewHolder viewHolder = (ViewHolder) holder;
            final String name = hotBean.getName();
            viewHolder.mSearchAddress.setText(name);
            final String distance = hotBean.getDistance();
            viewHolder.mSearchAddressDistance.setText(distance);
            final String address = hotBean.getAddress();
            viewHolder.mSearchAddressInformation.setText(address);
            final double latitude = hotBean.getLatitude();
            final double longitude = hotBean.getLongitude();
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.itemClick(name, distance, address, latitude, longitude);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getSize() + mHeaderCount + mBottomCount;
    }

    private int getSize() {
        return mHistoryHotBeen.size() > 0 ? mHistoryHotBeen.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.search_address)
        TextView mSearchAddress;
        @InjectView(R.id.search_address_distance)
        TextView mSearchAddressDistance;
        @InjectView(R.id.search_address_information)
        TextView mSearchAddressInformation;

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
        void ItemHotClick(int id, String hotName, int hotNum);


        void itemClick(String name, String distance, String address, double latitude, double longitude);

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
