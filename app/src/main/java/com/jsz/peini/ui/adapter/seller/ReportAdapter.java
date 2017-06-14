package com.jsz.peini.ui.adapter.seller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.report.ReportModel;
import com.jsz.peini.ui.activity.report.ReportActivity;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/1/12.
 */
public class ReportAdapter extends BaseRecyclerViewAdapter {

    public final ReportActivity mActivity;
    List<ReportModel.ReportReasonBean> mReportModels;

    public ReportAdapter(ReportActivity activity, List<ReportModel.ReportReasonBean> list) {
        super(list);
        mActivity = activity;
        mReportModels = list;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        ReportModel.ReportReasonBean reportReasonBean = mReportModels.get(position);
        holder.mPopText.setText(reportReasonBean.getResname() + "");
        holder.mPopText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.ItemClic(position);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_pop_text, viewGroup, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.pop_text)
        TextView mPopText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        void ItemClic(int position);
    }
}
