package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.address.SellerAddress;
import com.jsz.peini.utils.UiUtils;

import java.util.List;

/**
 * Created by th on 2016/12/15.
 */

public class RootListViewAdapter extends BaseAdapter {
    private final List<SellerAddress.DistrictListBean> ts;
    private final Activity mActivity;
    public RootListViewAdapter(Activity context, List<SellerAddress.DistrictListBean> ts) {
        this.ts = ts;
        this.mActivity = context;
    }

    int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public int getCount() {
        return ts.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view1 = UiUtils.inflate(mActivity,R.layout.seller_listview_item);
            holder.item_text = (TextView) view1.findViewById(R.id.item_name_text);
            holder.item_layout = (LinearLayout) view1.findViewById(R.id.root_item);
            view1.setTag(holder);
        } else {
            view1 = view;
            holder = (ViewHolder) view1.getTag();
        }
        /**
         * 该项被选中时改变背景色
         */
        if (selectedPosition == i) {
//          Drawable item_bg = news ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.WHITE);
        } else {
//          Drawable item_bg = news ColorDrawable(R.color.sub_list_color);
            holder.item_layout.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.item_text.setText(ts.get(i).getCountyName());
        return view1;
    }

    class ViewHolder {
        TextView item_text;
        LinearLayout item_layout;
    }
}
