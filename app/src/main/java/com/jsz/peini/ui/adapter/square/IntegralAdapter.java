package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.CouponInfoListAllUnGetByScore;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by th on 2017/1/17.
 */
public class IntegralAdapter extends BaseRecyclerViewAdapter {

    private Activity mActivity;
    private List<CouponInfoListAllUnGetByScore.CouponListBean> mList;
    private int mI;

    public IntegralAdapter(Activity activity, List<CouponInfoListAllUnGetByScore.CouponListBean> list, int i) {
        super(list);
        mActivity = activity;
        mList = list;
        mI = i;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        CouponInfoListAllUnGetByScore.CouponListBean couponListBean = mList.get(position);
        String couponIntegralStr = couponListBean.getGet_num() + " 积分"; //多少积分
        holder.mIntegralNumber.setText(couponIntegralStr);

        holder.mText1.setText(couponListBean.getCoupon_name());
        int coupon_money = couponListBean.getCoupon_money();
        int rule_money = couponListBean.getRule_money();

        if (rule_money == 0) {
            holder.mText2.setText("抵" + coupon_money + "元");
        } else {
            holder.mText2.setText(rule_money + "元抵" + coupon_money + "元");
        }

        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + couponListBean.getCouponImg(), holder.mIntegral, "4");
        final int couponId = couponListBean.getId();
        final int couponIntegral = couponListBean.getGet_num();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.OnItemListener(couponId, couponIntegral);
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view;
        if (mI == 2) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_integral_big, viewGroup, false);
        } else {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_integral, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.integral)
        ImageView mIntegral;
        @InjectView(R.id.text1)
        TextView mText1;
        @InjectView(R.id.text2)
        TextView mText2;
        @InjectView(R.id.inergral_numbar)
        TextView mIntegralNumber;

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
        void OnItemListener(int couponId, int couponIntegral);
    }
}
