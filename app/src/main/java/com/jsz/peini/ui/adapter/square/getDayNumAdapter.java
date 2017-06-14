package com.jsz.peini.ui.adapter.square;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.ui.activity.square.MiSignActivity;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 15089 on 2017/2/11.
 */
public class getDayNumAdapter extends RecyclerView.Adapter {

    private final MiSignActivity mActivity;
    private final List<String> list;
    //星期的7个数
    private final int mWeek;
    //星期几的空白数
    private final int mWhite;
    private final ArrayList<HashMap<String, Object>> mSinalist;
    //今天是第几天
    private final int mDay;

    public getDayNumAdapter(MiSignActivity activity, List<String> ist, int week, int white, ArrayList<HashMap<String, Object>> sinalist, int day) {
        mActivity = activity;
        list = ist;
        //星期的7个数
        mWeek = week;
        //星期几的空白数
        mWhite = white;
        mSinalist = sinalist;
        mDay = day;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.date_mb, parent, false);
        LinearLayout.LayoutParams params
                = new LinearLayout.LayoutParams(
                UiUtils.getScreenWidth(mActivity) / 7,
                UiUtils.dip2px(mActivity, 40));
        view.setLayoutParams(params);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position < mWeek + mWhite) {
            viewHolder.mTxtDayDateMB.setText(list.get(position));
            viewHolder.mTxtDayDateMB.setBackgroundResource(R.color.white000);
        } else {
            String Month = (position - mWeek - mWhite + 1) + "";
            viewHolder.mTxtDayDateMB.setText(Month);
            signInEventClicked(position, viewHolder);
        }

    }

    /**
     * 日历的点击事件
     */
    private void signInEventClicked(int position, ViewHolder viewHolder) {
        final int i = position - mWeek - mWhite + 1;
        LogUtil.d("这个是获取后边的日期---" + i);
//        if (i == mDay) {
//            viewHolder.mTxtDayDateMB.setBackgroundResource(R.mipmap.qiandao_bj);
//        } else {
//            viewHolder.mTxtDayDateMB.setBackgroundResource(R.color.white000);
//        }
        if ("1".equals(list.get(position))) {
            viewHolder.mTxtDayDateMB.setBackgroundResource(R.mipmap.qiandao_bj);
        }
//        else {
//            viewHolder.mTxtDayDateMB.setBackgroundResource(R.color.white000);
//        }
        /*点击事件*/
//        if (i <= mDay) {
//            viewHolder.mTxtDayDateMB.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ToastUtils.showToast(mActivity, "点击的日期---" + i);
//                }
//            });
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.txtDayDateMB)
        TextView mTxtDayDateMB;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
