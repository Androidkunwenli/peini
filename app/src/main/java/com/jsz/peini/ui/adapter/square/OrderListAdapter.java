package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jsz.peini.R;
import com.jsz.peini.model.square.MiBillBean.OrderInfoListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.utils.Bitmap.GlideImgManager;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.jsz.peini.utils.UiUtils.getResources;

public class OrderListAdapter extends BaseQuickAdapter<OrderInfoListBean, BaseViewHolder> {
    private Context mContext;

    public OrderListAdapter(Context context) {
        super(R.layout.item_order_list);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderInfoListBean item) {
        helper.setText(R.id.tv_order_no, "订单号: " + item.getOrderCode())
                .setText(R.id.tv_order_time, item.getOrderTime())
                .setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_total_pay, item.getTotalPay())
                .setText(R.id.tv_pay_money, item.getPayMoney())
                .setText(R.id.tv_gold, "合计: ¥" + item.getPayMoney())
                .setVisible(R.id.ll_whethergold, "1".equals(item.getOrderType())
                        || "3".equals(item.getOrderType()))     //1、3 显示；4、5 隐藏
                .setVisible(R.id.tv_gold, "4".equals(item.getOrderType())
                        || "5".equals(item.getOrderType()))     //1、3 隐藏；4、5 显示
                .setVisible(R.id.ll_hangintheair, "1".equals(item.getOrderStatus())
                        || "2".equals(item.getOrderStatus()))    //-1、0 隐藏；1、2 显示
        ;

        TextView tvTotalPay = helper.getView(R.id.tv_total_pay);
        tvTotalPay.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        tvTotalPay.getPaint().setAntiAlias(true);// 抗锯齿

        GlideImgManager.loadImage(mContext, IpConfig.HttpPic + item.getImgSrc(),
                (CircleImageView) helper.getView(R.id.civ_logo), "1");


        TextView tvOrderType = helper.getView(R.id.tv_order_type);
        String orderType = item.getOrderType();
        switch (orderType) {
            case "1":
                tvOrderType.setText("任务买单");
                break;
            case "3":
                tvOrderType.setText("到店买单");
                break;
            case "4":
                tvOrderType.setText("打赏");
                break;
            case "5":
                tvOrderType.setText("金币转赠");
                break;
        }

        //1金币；2微信；3支付宝
        String payType = item.getPayType();
        ImageView ivPayType = helper.getView(R.id.iv_pay_type);
        TextView tvPayType = helper.getView(R.id.tv_pay_type_text);
        switch (payType) {
            case "1":
                ivPayType.setBackgroundResource(R.mipmap.pian);
                tvPayType.setText("金币消费");
                break;
            case "2":
                ivPayType.setBackgroundResource(R.mipmap.weixin);
                tvPayType.setText("微信消费");
                break;
            case "3":
                ivPayType.setBackgroundResource(R.mipmap.zfb);
                tvPayType.setText("支付宝消费");
                break;
        }

        //-1已取消；2已完成；0待付款 1待评价
        String orderStatus = item.getOrderStatus();
        ImageView ivOrderStatusIcon = helper.getView(R.id.iv_order_status_icon);
        TextView tvOrderStatus = helper.getView(R.id.tv_order_status);
        switch (orderStatus) {
            case "-1":
                ivOrderStatusIcon.setBackgroundResource(R.drawable.quxiao);
                tvOrderStatus.setText("已取消");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.text999));
                break;
            case "0":
                ivOrderStatusIcon.setBackgroundResource(R.drawable.daifukuan);
                tvOrderStatus.setText("待付款");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "1":
                ivOrderStatusIcon.setBackgroundResource(R.drawable.daipingjia);
                tvOrderStatus.setText("待评价");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
            case "2":
                ivOrderStatusIcon.setBackgroundResource(R.mipmap.photo_manage1);
                tvOrderStatus.setText("已完成");
                tvOrderStatus.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                break;
        }
    }
}
