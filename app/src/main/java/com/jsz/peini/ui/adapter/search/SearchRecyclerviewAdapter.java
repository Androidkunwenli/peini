package com.jsz.peini.ui.adapter.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsz.peini.R;

/**
 * Created by kunwe on 2016/11/28.
 */
public class SearchRecyclerviewAdapter extends RecyclerView.Adapter<SearchRecyclerviewAdapter.MyHolder> {
    private final Context mActivity;

    public SearchRecyclerviewAdapter(Context mActivity) {
        this.mActivity = mActivity;
    }

    //得到child的数量
    @Override
    public int getItemCount() {
        return 20;
    }

    //创建ChildView
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_view_item, parent, false);
        return new MyHolder(view);
    }

    //将数据绑定到每一个childView中
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
    }

    // 通过holder的方式来初始化每一个ChildView的内容
    class MyHolder extends RecyclerView.ViewHolder {
        public MyHolder(View itemView) {
            super(itemView);
        }
    }
}