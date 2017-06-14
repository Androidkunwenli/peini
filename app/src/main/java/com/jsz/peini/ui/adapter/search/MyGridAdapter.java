package com.jsz.peini.ui.adapter.search;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseViewHolder;
import com.jsz.peini.model.search.SearchBean;
import com.jsz.peini.ui.activity.search.IsSearchActivity;

import java.util.List;

public class MyGridAdapter extends BaseAdapter {
    private final List<SearchBean.DataBean> wordList;
    private Activity mContext;

    public MyGridAdapter(IsSearchActivity context, List<SearchBean.DataBean> wordList) {
        this.wordList = wordList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return wordList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
        tv.setText(wordList.get(position).getHotName());
        return convertView;
    }
}
