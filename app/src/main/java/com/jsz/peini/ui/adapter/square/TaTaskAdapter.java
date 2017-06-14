package com.jsz.peini.ui.adapter.square;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.TaTaskBean;
import com.jsz.peini.ui.activity.square.TaTaskActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/2/8.
 */
public class TaTaskAdapter extends RecyclerView.Adapter {
    public final List<TaTaskBean.TaskInfoByUserIdListBean> mList;
    public final TaTaskActivity mActivity;

    public TaTaskAdapter(TaTaskActivity activity, List<TaTaskBean.TaskInfoByUserIdListBean> list) {
        mList = list;
        mActivity = activity;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_ta_task, parent, false));

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TaTaskBean.TaskInfoByUserIdListBean taskInfoByUserIdListBean = mList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        String sellerInfoName = taskInfoByUserIdListBean.getSellerInfoName();
        String taskAppointedTime = taskInfoByUserIdListBean.getTaskAppointedTime();
        int otherSex = taskInfoByUserIdListBean.getOtherSex();
        int otherGo = taskInfoByUserIdListBean.getOtherGo();
        viewHolder.mTvSellerInfoName.setText(sellerInfoName);
        viewHolder.mTvTaskAppointedTime.setText(taskAppointedTime);
        String otherSexText;
        String otherGoText;
        if (otherSex == 1) {
            otherSexText = "帅哥";
        } else if (otherSex == 2) {
            otherSexText = "妹子";
        } else {
            otherSexText = "性别不限";
        }
        switch (otherGo) {
            case 1:
                otherGoText = "我接Ta";
                break;
            case 2:
                otherGoText = "Ta接我";
                break;
            case 3:
                otherGoText = "自由行";
                break;
            default:
                otherGoText = "";
                break;
        }
        /**
         * 最小年龄
         */
        int otherLowAge = taskInfoByUserIdListBean.getOtherLowAge();
        /**
         * 最大年龄
         */
        int otherHighAge = taskInfoByUserIdListBean.getOtherHighAge();
        String hopeAge;
        if (otherLowAge == 0 || otherHighAge == 0) {
            hopeAge = "年龄不限";
        } else if (otherLowAge == 60 || otherHighAge == 60) {
            hopeAge = "60岁以上";
        } else {
            hopeAge = otherLowAge + "-" + otherHighAge + "岁";
        }
        String mTvOtherLowAgeotherHighAgeotherSexotherGootherBuyText = hopeAge + " " + otherSexText + " " + otherGoText;
        viewHolder.mTvOtherLowAgeotherHighAgeotherSexotherGootherBuy.setText(mTvOtherLowAgeotherHighAgeotherSexotherGootherBuyText);
        int taskStatus = taskInfoByUserIdListBean.getTaskStatus();
        String taskStatusText;
        //1发布中2进行中3待评价4已完成5已关闭）
        switch (taskStatus) {
            case 1:
                taskStatusText = "发布中";
                break;
            case 2:
                taskStatusText = "进行中";
                break;
            case 3:
                taskStatusText = "待评价";
                break;
            case 4:
            case 6:
            case 7:
                taskStatusText = "已完成";
                break;
            case 5:
                taskStatusText = "已关闭";
                break;
            default:
                taskStatusText = "";
                break;
        }
        viewHolder.mTvTaskStatus.setText(taskStatusText);
        final int id = taskInfoByUserIdListBean.getId();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(position, id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_sellerInfoName)
        TextView mTvSellerInfoName;
        @InjectView(R.id.tv_taskAppointedTime)
        TextView mTvTaskAppointedTime;
        @InjectView(R.id.tv_otherLowAgeotherHighAgeotherSexotherGootherBuy)
        TextView mTvOtherLowAgeotherHighAgeotherSexotherGootherBuy;
        @InjectView(R.id.tv_taskStatus)
        TextView mTvTaskStatus;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnItemClickListener mListener;

    public void setItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int id);
    }
}
