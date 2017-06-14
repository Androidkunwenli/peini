package com.jsz.peini.ui.adapter.seller;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.seller.SellerInfoBean;
import com.jsz.peini.model.seller.SellerTabulationBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.search.SellerTabulationActivity;
import com.jsz.peini.ui.view.CustomRatingBar;
import com.jsz.peini.ui.view.RoundAngleImageView;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lenovo on 2017/3/14.
 */

public class IsSearchAdapter extends RecyclerView.Adapter {

    private final List<SellerInfoBean> mSellerInfo;
    private SellerTabulationActivity mActivity;

    public IsSearchAdapter(SellerTabulationActivity activity, List<SellerInfoBean> sellerInfo) {
        mActivity = activity;
        mSellerInfo = sellerInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(UiUtils.inflateRecyclerviewAdapter(mActivity, R.layout.seller_item_shopping, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        final SellerInfoBean sellerInfoBean = mSellerInfo.get(position);
        if (getSize() > 0 && sellerInfoBean != null) {
            //标题name
            String sellerName = sellerInfoBean.getSellerName();
            holder.mSellerName.setText(sellerName);

            //1,需要预定 0,不需要
            String weatherOrder = sellerInfoBean.getWeatherOrder();
            //是否可以预定
            holder.mIvWeatherOrder.setVisibility("1".equals(weatherOrder) ? View.VISIBLE : View.GONE);

            //位置,吃饭类型
            String bigName = sellerInfoBean.getDistrictName();
            String districtName = StringUtils.isNoNull(bigName) ? bigName : "";
            String labelsName = TextUtils.isEmpty(sellerInfoBean.getLabels_name()) ? "" : sellerInfoBean.getLabels_name();
            String districtNameLabelsName = districtName + " | " + labelsName;
            holder.mDistrictNamelabelsName.setText(districtNameLabelsName);

            /*距离*/
            String distanceStr;
            int distanceInt = sellerInfoBean.getDistance();
            if (distanceInt < 1000) {
                distanceStr = distanceInt + "m";
            } else {
                float size = distanceInt / 1000f;
                DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
                distanceStr = df.format(size) + "km";//返回的是String类型的
            }
            holder.mDistance.setText(distanceStr);

            /**商家图片*/
            String imageSrc = sellerInfoBean.getSellerLogo();
            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + imageSrc, holder.mImageSrc);
            //评分
            float countSelected = sellerInfoBean.getSellerScore() / 20f;
            holder.mSellerScore.setRating(countSelected);
            holder.mSellerScore.setVisibility(View.VISIBLE);

            //价格
            String Price = "¥" + sellerInfoBean.getPrice() + "/人";
            holder.mPrice.setText(Price);

            //优惠
            String countMJ = sellerInfoBean.getCountMJ();
            if (StringUtils.isNoNull(countMJ)) {
                holder.mCountMJName.setText(countMJ);
                holder.mCountMJ.setVisibility(View.VISIBLE);
            } else {
                holder.mCountMJ.setVisibility(View.GONE);
            }

            //金币优惠
            String countJB = sellerInfoBean.getCountJB();
            if (StringUtils.isNoNull(countJB)) {
                holder.mCountJBName.setText(countJB);
                holder.mCountJB.setVisibility(View.VISIBLE);
            } else {
                holder.mCountJB.setVisibility(View.GONE);
            }
            //点击事件
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = sellerInfoBean.getId();
                    mListener.onSellerItemClick(id, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getSize();
    }

    private int getSize() {
        return mSellerInfo != null && mSellerInfo.size() > 0 ? mSellerInfo.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.imageSrc)
        RoundAngleImageView mImageSrc;
        @InjectView(R.id.sellerName)
        TextView mSellerName;
        @InjectView(R.id.iv_weather_order)
        ImageView mIvWeatherOrder;
        @InjectView(R.id.sellerScore)
        RatingBar mSellerScore;
        @InjectView(R.id.price)
        TextView mPrice;
        @InjectView(R.id.districtNamelabelsName)
        TextView mDistrictNamelabelsName;
        @InjectView(R.id.distance)
        TextView mDistance;
        @InjectView(R.id.countJB_image)
        ImageView mCountJBImage;
        @InjectView(R.id.countJB_name)
        TextView mCountJBName;
        @InjectView(R.id.countJB)
        LinearLayout mCountJB;
        @InjectView(R.id.countMJ_image)
        ImageView mCountMJImage;
        @InjectView(R.id.countMJ_name)
        TextView mCountMJName;
        @InjectView(R.id.countMJ)
        LinearLayout mCountMJ;
        @InjectView(R.id.seller_activity)
        LinearLayout mSellerActivity;
        @InjectView(R.id.item_selector)
        LinearLayout mItemSelector;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);

        }
    }

    private ItemClickListener mListener;

    public void setItemClickListener(ItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemClickListener {
        /**
         * 条目的点击事件
         */
        void onSellerItemClick(int id, int position);
    }

}
