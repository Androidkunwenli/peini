package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.utils.UiUtils;

import java.util.ArrayList;

/**
 * Created by th on 2016/12/15.
 */

public class SellerTabulationAdapter extends BaseAdapter {

    private final ArrayList<String> arrayList;
    public final Activity mMActivity;

    public SellerTabulationAdapter(Activity mActivity, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        mMActivity = mActivity;
    }

    int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        // 判断convertView的状态，来达到复用效果
        if (null == convertView) {
            // 如果convertView为空，则表示第一次显示该条目，需要创建一个view
            view = UiUtils.inflate(mMActivity,R.layout.seller_item_tabulation);
            //新建一个viewholder对象
            holder = new ViewHolder();
            //将findviewbyID的结果赋值给holder对应的成员变量
            holder.item_text = (TextView) view.findViewById(R.id.item_name_text);
            // 将holder与view进行绑定
            view.setTag(holder);
        } else {
            // 否则表示可以复用convertView
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        // 直接操作holder中的成员变量即可，不需要每次都findViewById
        holder.item_text.setText(arrayList.get(position));
        if (position == selectedPosition) {
            holder.item_text.setTextColor(Color.RED);
        } else {
            holder.item_text.setTextColor(Color.BLACK);
        }
        return view;
    }

    class ViewHolder {
        TextView item_text;
    }
}