package com.jsz.peini.ui.adapter.seller;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.seller.SellerInfoBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * Created by kunwe on 2016/11/26.
 */
public class StoreAdapter extends BaseQuickAdapter<SellerInfoBean, BaseViewHolder> {

    private Context mContext;

    public StoreAdapter(Context context) {
        super(R.layout.store_item_shopping);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellerInfoBean sellerInfoBean) {
        helper.setVisible(R.id.view_divider_line, helper.getAdapterPosition() != 1)
                .setText(R.id.sellerName, sellerInfoBean.getSellerName())                 //名称
                .setVisible(R.id.iv_weather_order, "1".equals(sellerInfoBean.getWeatherOrder())) //是否预订
                .setText(R.id.price, "¥" + sellerInfoBean.getPrice() + "/人")         //价格
        ;

        /**位置,吃饭类型*/
        String bigName = sellerInfoBean.getDistrictName();
        String districtName = StringUtils.isNoNull(bigName) ? bigName : "";
        String labelsName = TextUtils.isEmpty(sellerInfoBean.getLabels_name()) ? "" : sellerInfoBean.getLabels_name();
        String districtNameLabelsName = districtName + " | " + labelsName;
        helper.setText(R.id.districtNamelabelsName, districtNameLabelsName);

        /**距离*/
        String distanceStr;
        int distanceInt = sellerInfoBean.getDistance();
        if (distanceInt < 1000) {
            distanceStr = distanceInt + "m";
        } else {
            float size = distanceInt / 1000f;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
            distanceStr = df.format(size) + "km";//返回的是String类型的
        }
        helper.setText(R.id.distance, distanceStr);

        /**商家图片*/
        ImageView imageView = helper.getView(R.id.imageSrc);
        String imageSrc = sellerInfoBean.getSellerLogo();
        GlideImgManager.loadImage(mContext, IpConfig.HttpPic + imageSrc, imageView);
        /**评分*/
        RatingBar sellerScore = helper.getView(R.id.sellerScore);
        float countSelected = sellerInfoBean.getSellerScore() / 20f;
        sellerScore.setRating(countSelected);
        sellerScore.setVisibility(View.VISIBLE);

        TextView tvCountMJName = helper.getView(R.id.countMJ_name);
        LinearLayout llCountMJ = helper.getView(R.id.countMJ);

        //优惠
        String countMJ = sellerInfoBean.getCountMJ();
        if (StringUtils.isNoNull(countMJ)) {
            tvCountMJName.setText(countMJ);
            llCountMJ.setVisibility(View.VISIBLE);
        } else {
            llCountMJ.setVisibility(View.GONE);
        }

        TextView tvCountJBName = helper.getView(R.id.countJB_name);
        LinearLayout llCountJB = helper.getView(R.id.countJB);

        //金币优惠
        String countJB = sellerInfoBean.getCountJB();
        if (StringUtils.isNoNull(countJB)) {
            tvCountJBName.setText(countJB);
            llCountJB.setVisibility(View.VISIBLE);
        } else {
            llCountJB.setVisibility(View.GONE);
        }
    }
}
