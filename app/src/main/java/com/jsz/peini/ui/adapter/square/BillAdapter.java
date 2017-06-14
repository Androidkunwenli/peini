package com.jsz.peini.ui.adapter.square;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.MiBillBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.BillActivity;
import com.jsz.peini.ui.adapter.BaseRecyclerViewAdapter;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.LogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jsz.peini.utils.UiUtils.getResources;

public class BillAdapter extends BaseRecyclerViewAdapter {
    //TODO:暂时保留 OrderListAdapter没问题后，删除
    public final BillActivity mActivity;
    public final List<MiBillBean.OrderInfoListBean> mList;

    public BillAdapter(BillActivity billActivity, List<MiBillBean.OrderInfoListBean> list) {
        super(list);
        mActivity = billActivity;
        mList = list;
    }

    @Override
    public void onBindBodyViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        MiBillBean.OrderInfoListBean orderInfoList = mList.get(position);
        //图片
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + orderInfoList.getImgSrc(), holder.mImgSrc, "1");
        //订单编号
        String orderCode = orderInfoList.getOrderCode();
        LogUtil.d("订单编号+" + orderCode);
        String orderCodeText = "订单号: " + orderCode;
        holder.mOrderCode.setText(orderCodeText);
        //时间
        holder.mOrderTime.setText(orderInfoList.getOrderTime());
        //标题
        holder.mTitle.setText(orderInfoList.getTitle());
        //到店买单
        final String orderType = orderInfoList.getOrderType();
        switch (orderType) {
            case "1":
                holder.mOrderType.setText("任务买单");
                holder.mLlWhetherGold.setVisibility(View.VISIBLE);
                holder.mTvGold.setVisibility(View.GONE);
                break;
            case "3":
                holder.mOrderType.setText("到店买单");
                holder.mLlWhetherGold.setVisibility(View.VISIBLE);
                holder.mTvGold.setVisibility(View.GONE);
                break;
            case "4":
                holder.mOrderType.setText("打赏");
                holder.mLlWhetherGold.setVisibility(View.GONE);
                holder.mTvGold.setVisibility(View.VISIBLE);
                break;
            case "5":
                holder.mOrderType.setText("金币转赠");
                holder.mLlWhetherGold.setVisibility(View.GONE);
                holder.mTvGold.setVisibility(View.VISIBLE);
                break;
        }

        //1金币；2微信；3支付宝
        String payType = orderInfoList.getPayType();
        switch (payType) {
            case "1":
                holder.mPayType.setBackgroundResource(R.mipmap.jinbi);
                holder.mPayTypeText.setText("金币消费");
                break;
            case "2":
                holder.mPayType.setBackgroundResource(R.mipmap.weixin);
                holder.mPayTypeText.setText("微信消费");
                break;
            case "3":
                holder.mPayType.setBackgroundResource(R.mipmap.zfb);
                holder.mPayTypeText.setText("支付宝消费");
                break;
        }
        holder.mTotalPay.setText(orderInfoList.getTotalPay());
        holder.mTotalPay.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
        holder.mTotalPay.getPaint().setAntiAlias(true);// 抗锯齿
        String payMoney = orderInfoList.getPayMoney();
        holder.mPayMoney.setText(payMoney);
        holder.mTvGold.setText("合计: ¥" + payMoney);
        //-1已取消；2已完成；0待付款 1待评价
        final String orderStatus = orderInfoList.getOrderStatus();
        switch (orderStatus) {
            case "-1":
                holder.mOrderStatus.setBackgroundResource(R.drawable.quxiao);
                holder.mOrderStatusText.setText("已取消");
                holder.mOrderStatusText.setTextColor(getResources().getColor(R.color.text999));
                holder.mLlHangInTheAir.setVisibility(View.GONE);
                break;
            case "2":
                holder.mOrderStatus.setBackgroundResource(R.mipmap.photo_manage1);
                holder.mOrderStatusText.setText("已完成");
                holder.mOrderStatusText.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                holder.mLlHangInTheAir.setVisibility(View.VISIBLE);
                break;
            case "0":
                holder.mOrderStatus.setBackgroundResource(R.drawable.daifukuan);
                holder.mOrderStatusText.setText("待付款");
                holder.mOrderStatusText.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                holder.mLlHangInTheAir.setVisibility(View.GONE);
                break;
            case "1":
                holder.mOrderStatus.setBackgroundResource(R.drawable.daipingjia);
                holder.mOrderStatusText.setText("待评价");
                holder.mOrderStatusText.setTextColor(getResources().getColor(R.color.RED_FB4E30));
                holder.mLlHangInTheAir.setVisibility(View.VISIBLE);
                break;
        }
        //订单账号
        final String orderId = orderInfoList.getOrderId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (orderStatus) {
                    case "-1":
//                        holder.mOrderStatus.setBackgroundResource(R.drawable.quxiao);
//                        holder.mOrderStatusText.setText("已取消");
                        mListener.onCancelItemClick(orderType, position);
                        break;
                    case "2":
//                        holder.mOrderStatus.setBackgroundResource(R.mipmap.photo_manage1);
//                        holder.mOrderStatusText.setText("已完成");
                        mListener.onCompleteItemClick(orderType, position);
                        break;
                    case "0":
//                        holder.mOrderStatus.setBackgroundResource(R.drawable.daifukuan);
//                        holder.mOrderStatusText.setText("待付款");
                        mListener.onPayItemClick(orderType, position);
                        break;
                    case "1":
                        mListener.onEvaluateItemClick(orderType, position, orderId);
//                        holder.mOrderStatus.setBackgroundResource(R.drawable.daipingjia);
//                        holder.mOrderStatusText.setText("待评价");
                        break;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder setBodyViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.bill_item, viewGroup, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.orderCode)
        TextView mOrderCode;
        @InjectView(R.id.orderTime)
        TextView mOrderTime;
        @InjectView(R.id.imgSrc)
        CircleImageView mImgSrc;
        @InjectView(R.id.title)
        TextView mTitle;
        @InjectView(R.id.orderType)
        TextView mOrderType;
        @InjectView(R.id.orderStatus)
        ImageView mOrderStatus;
        @InjectView(R.id.orderStatus_text)
        TextView mOrderStatusText;
        @InjectView(R.id.payType)
        ImageView mPayType;
        @InjectView(R.id.totalPay)
        TextView mTotalPay;
        @InjectView(R.id.payType_text)
        TextView mPayTypeText;
        @InjectView(R.id.payMoney)
        TextView mPayMoney;
        @InjectView(R.id.ll_whethergold)
        LinearLayout mLlWhetherGold;
        @InjectView(R.id.ll_hangintheair)
        LinearLayout mLlHangInTheAir;
        @InjectView(R.id.tv_gold)
        TextView mTvGold;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    private OnItemClickListener mListener;

    public void setItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface OnItemClickListener {
        /**
         * 评价
         * orderType  订单类型
         */
        void onEvaluateItemClick(String orderType, int position, String orderId);

        /**
         * 去付款按钮
         */
        void onPayItemClick(String orderType, int position);

        /**
         * 已完成
         *
         * @param orderType
         * @param position
         */
        void onCompleteItemClick(String orderType, int position);

        /**
         * 已取消
         *
         * @param orderType
         * @param position
         */
        void onCancelItemClick(String orderType, int position);
    }

}
