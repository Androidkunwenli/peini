package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

/**
 * Created by ljb on 2015/9/9.
 */
public class ChannelAdapter extends BaseAdapter {

    private List<String> mDatas;
    private Activity mContext;

    /**
     * 标识最后一个item是否显示数据,默认显示
     */
    private boolean lastVisable = true;

    /**
     * 要移除的Item位置
     */
    private int mRemovePosition = -1;
    private boolean mIsEdit;

    public ChannelAdapter(List<String> datas, Activity context, boolean b) {
        this.mDatas = datas;
        this.mContext = context;
        mIsEdit = b;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public String getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = UiUtils.inflate(mContext, R.layout.item_channel);
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.iv_new = (ImageView) view.findViewById(R.id.iv_new);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        //判断最后一个Item是否需要设置数据
        if (!lastVisable && position == getCount() - 1) {
            holder.tv_content.setText("");
        } else {
            holder.tv_content.setText(getItem(position));
        }

        //隐藏要移除的Item内容
        if (mRemovePosition == position) {
            holder.tv_content.setText("");
        }

        return view;
    }

    /**
     * 设置最后一个item是否显示数据
     */
    public void setLastItemVisibility(boolean visible) {
        this.lastVisable = visible;
    }

    /**
     * 新增一个item
     */
    public void addItem(String itemData) {
        mDatas.add(itemData);
        notifyDataSetChanged();
    }

    public List<String> getItem() {
        notifyDataSetChanged();
        return mDatas;
    }

    /**
     * 要移除Item的位置
     */
    public void setmRemovePosition(int position) {
        mRemovePosition = position;
        notifyDataSetChanged();
    }

    /**
     * 移除mRemovePosition对应的Item
     */
    public void remove() {
        mDatas.remove(mRemovePosition);
        mRemovePosition = -1;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        TextView tv_content;
        ImageView iv_new;
    }
}
