package com.jsz.peini.ui.adapter.seller;

import android.content.Context;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.square.CouponInfoList;

import java.util.List;

/**
 * 优惠券
 * Created by huizhe.ju on 2017/02/27.
 */

public class CouponAdapter extends BaseQuickAdapter<CouponInfoList.CouponInfoListBean, BaseViewHolder> {

    private Context mContext;
    private String mSelectedCouponGetId;

    public CouponAdapter(Context context, String selectedCouponGetId) {
        super(R.layout.item_coupon_view);
        this.mContext = context;
        this.mSelectedCouponGetId = selectedCouponGetId;
    }

    @Override
    protected void convert(BaseViewHolder helper, CouponInfoList.CouponInfoListBean item) {
        helper.setText(R.id.tv_discount, item.getCouponMoney())
                .setText(R.id.tv_days_to_expiration, getDaysToExpiration(item.getDays()))
                .setText(R.id.tv_coupon_name, item.getCouponName())
                .setText(R.id.tv_coupon_label, "0".equals(item.getRuleMoney()) ? "直抵券" : "代金券")
                .setChecked(R.id.cb_choose_coupon,
                        !TextUtils.isEmpty(mSelectedCouponGetId)
                                && mSelectedCouponGetId.equals(String.valueOf(item.getGetId())));

        List<CouponInfoList.CouponInfoListBean.CouponRangeBean> couponRange = item.getCouponRange();
        if (couponRange != null && couponRange.size() > 0) {
            LinearLayout llCouponRange = helper.getView(R.id.ll_coupon_range);
            llCouponRange.removeAllViews();
            for (CouponInfoList.CouponInfoListBean.CouponRangeBean couponRangeBean : couponRange) {
                if (couponRangeBean != null) {
                    String name = couponRangeBean.getName();
                    if (!TextUtils.isEmpty(name)) {
                        TextView textView = new TextView(mContext);
                        textView.setTextColor(mContext.getResources().getColor(R.color.text999));
                        textView.setText(name);
                        llCouponRange.addView(textView);
                    }
                }
            }
        }
    }

    private String getDaysToExpiration(int daysToExpiration) {
        String expirationStr;
        if (daysToExpiration > 0) {
            expirationStr = daysToExpiration + "天后过期";
        } else if (daysToExpiration == 0) {
            expirationStr = "今天后过期";
        } else {
            expirationStr = "已过期";
        }
        return expirationStr;
    }
}