package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.model.seller.SellerCodesBySellerCodesBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 商家业态
 * Created by huizhe.ju on 2017/3/3.
 */
public class SellerSubAdapter extends BaseRecyclerViewAdapter {

    private Activity mActivity;
    private List<SellerCodesBySellerCodesBean.SellerCodesBean> mSellerCodesBeanList;

    public SellerSubAdapter(Activity activity, List<SellerCodesBySellerCodesBean.SellerCodesBean> sellerCodesBeanList) {
        super(sellerCodesBeanList);
        mActivity = activity;
        mSellerCodesBeanList = sellerCodesBeanList;
    }

    @Override
    public void setList(List list) {
        super.setList(list);
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        Glide.with(mActivity).load(IpConfig.HttpPic + mSellerCodesBeanList.get(position).getImgSrc())
                .placeholder(R.mipmap.white_bj_circular).error(R.mipmap.white_bj_circular)
                .into(holder.mIvSellerFoot);
        holder.mTvSellerName.setText(mSellerCodesBeanList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_sub_seller_codes, viewGroup, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.seller_foot)
        ImageView mIvSellerFoot;
        @InjectView(R.id.tv_seller_name)
        TextView mTvSellerName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int index);
    }
}
