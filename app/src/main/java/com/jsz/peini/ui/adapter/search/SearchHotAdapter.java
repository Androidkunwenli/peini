package com.jsz.peini.ui.adapter.search;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.search.SearchHot;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

/**
 * Created by th on 2016/12/16.
 */
public class SearchHotAdapter extends BaseAdapter {

    private final Activity mActivity;
    private final List<SearchHot.SellerListBean> sellerList;

    public SearchHotAdapter(Activity isSearchActivity, List<SearchHot.SellerListBean> sellerList) {
        this.mActivity = isSearchActivity;
        this.sellerList = sellerList;
    }

    @Override
    public int getCount() {
        return sellerList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = UiUtils.inflate(mActivity,R.layout.issearch_item);
            holder.issearch_map = (ImageView) convertView.findViewById(R.id.issearch_map);
            holder.issearch_text = (TextView) convertView.findViewById(R.id.issearch_text);
            holder.issearch_history = (TextView) convertView.findViewById(R.id.issearch_history);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.issearch_text.setText(sellerList.get(i).getSellerName());
        String text = sellerList.get(i).getSellerAddress() + "";
        LogUtil.i("获取到的地址", text);
        if (!text.equals("null")) {
            holder.issearch_history.setText(text);
            holder.issearch_history.setVisibility(View.VISIBLE);
        } else {
            holder.issearch_history.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView issearch_map;
        ImageView search_quan;
        ImageView search_hui;
        TextView issearch_text;
        TextView issearch_history;
    }
}



