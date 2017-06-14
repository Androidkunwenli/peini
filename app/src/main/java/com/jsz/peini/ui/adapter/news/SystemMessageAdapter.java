package com.jsz.peini.ui.adapter.news;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.tabulation.SystemMessageListBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kunwe on 2016/11/28.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessageListBean.DataBean, BaseViewHolder> {

    public SystemMessageAdapter() {
        super(R.layout.item_system_message);
    }

    @Override
    protected void convert(BaseViewHolder holder, SystemMessageListBean.DataBean dataBean) {
        holder.setText(R.id.tv_message_title, dataBean.getTitle());
        String timeStr = dataBean.getSendTime();
        if (!TextUtils.isEmpty(timeStr)) {
            holder.setText(R.id.tv_message_time, timeStr);
            Matcher matcher = Pattern.compile("(\\d{1,2})-(\\d{1,2})").matcher(timeStr);
            if (matcher.find()) {
                String dateStr = matcher.group(1) + "月" + matcher.group(2) + "日";
                holder.setText(R.id.tv_message_date, dateStr);
            }
        }
        holder.setText(R.id.tv_message_content, dataBean.getSmscontent());
    }
}