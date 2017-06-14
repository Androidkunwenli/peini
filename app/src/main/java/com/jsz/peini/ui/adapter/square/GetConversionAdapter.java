package com.jsz.peini.ui.adapter.square;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.pay.ConversionListBean;
import com.jsz.peini.ui.activity.square.RechargeActivity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by 15089 on 2017/2/17.
 */
public class GetConversionAdapter extends RecyclerView.Adapter {

    private final RechargeActivity mActivity;
    private final List<ConversionListBean.ListBean> mData;

    int GoldChoice = -1;

    public GetConversionAdapter(RechargeActivity activity, List<ConversionListBean.ListBean> data) {
        mActivity = activity;
        mData = data;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderHead(LayoutInflater.from(mActivity).inflate(R.layout.item_getconversion, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolderHead holderHead = (ViewHolderHead) holder;
        ConversionListBean.ListBean listBean = mData.get(position);
        final float payNum = listBean.getPayNum();
        final float goldNum = listBean.getGoldNum();
        final int id = listBean.getId();
        if (position == GoldChoice) {
            holderHead.mGoldChoice.setBackgroundResource(R.mipmap.selector_money_true);
        } else {
            holderHead.mGoldChoice.setBackgroundResource(R.mipmap.selector_money_fs);
        }
//        System.out.println(formatNum(String.valueOf(payNum)));
//        System.out.println(formatNum(String.valueOf(goldNum)));
        holderHead.mGoldText.setText(formatNum(String.valueOf(payNum)) + "元购买" + formatNum(String.valueOf(goldNum)) + "金币");
        holderHead.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(position, payNum, goldNum, id);
            }
        });
    }

    private String formatNum(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return "0";
        }
        if (numStr.contains(".")) {
            return numStr.replaceAll("\\.*0*$", "");
        } else {
            return numStr;
        }
    }

    public void setGoldChoice(int Choice) {
        GoldChoice = Choice;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolderHead extends RecyclerView.ViewHolder {
        @InjectView(R.id.imagejinbi)
        ImageView mImagejinbi;
        @InjectView(R.id.gold_text)
        TextView mGoldText;
        @InjectView(R.id.gold_choice)
        ImageView mGoldChoice;

        public ViewHolderHead(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnitemClickListener mListener;

    public void setitemClickListener(OnitemClickListener listener) {
        mListener = listener;
    }

    public interface OnitemClickListener {
        /**
         * goldNum 多少钱
         */
        void itemClick(int position, float payNum, float goldNum, int id);
    }

}
