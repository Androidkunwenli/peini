package com.jsz.peini.ui.adapter.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsz.peini.R;
import com.jsz.peini.model.search.SearchHot;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by kunwe on 2016/11/26.
 */
public class SellerTabulationAdapter extends BaseRecyclerViewAdapter {
    private final Context mActivity;
    private final List<SearchHot.SellerListBean> sellerInfo;

    public SellerTabulationAdapter(Context mActivity, List<SearchHot.SellerListBean> sellerInfo) {
        super(sellerInfo);
        this.mActivity = mActivity;
        this.sellerInfo = sellerInfo;
    }

    //得到child的数量
    @Override
    public int getItemCount() {
        return sellerInfo.size();
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
    }
    @Override
    public void onBindHeadViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindHeadViewHolder(viewHolder, position);
    }
    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seller_item_shopping, viewGroup, false);
        return new MyHolder(view);
    }
    @Override
    public RecyclerView.ViewHolder setHeadViewHolder(ViewGroup viewGroup, View headView) {
        return new HeadViewHolder(headView);
    }
    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
    class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
