package com.jsz.peini.ui.adapter.square;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.MiWealthABean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.BillActivity;
import com.jsz.peini.ui.activity.square.ExchangeActivity;
import com.jsz.peini.ui.activity.square.GoldCoinActivity;
import com.jsz.peini.ui.activity.square.GoldDetailWebActivity;
import com.jsz.peini.ui.activity.square.RechargeActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kunwe on 2016/11/28.
 */

public class MyWealthAdapter extends RecyclerView.Adapter<MyWealthAdapter.ViewHolder> {

    private List<MiWealthABean.UserInfoBean> mUserInfoBeanList;
    private Context mContext;

    public MyWealthAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<MiWealthABean.UserInfoBean> userInfoBeanList) {
        this.mUserInfoBeanList = userInfoBeanList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_wealth, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mUserInfoBeanList != null && mUserInfoBeanList.size() == 1) {
            MiWealthABean.UserInfoBean userInfo = mUserInfoBeanList.get(position);
            /*头像*/
            String imageHead = userInfo.getImageHead();
            if (StringUtils.isNoNull(imageHead)) {
                String url = IpConfig.HttpPic + imageHead;
                LogUtil.d("我的财富头像" + url);
                GlideImgManager.loadImage(mContext, url, holder.mImageHead, holder.mSex);
            }
            /*金币*/
            String gold = String.valueOf(userInfo.getGold());
            if (StringUtils.isNoNull(gold)) {
                holder.mGold.setText(gold);
            }
            //积分
            String score = String.valueOf(userInfo.getScore());
            if (StringUtils.isNoNull(score)) {
                holder.mScore.setText(score);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.gold)
        TextView mGold;
        @InjectView(R.id.wealth_recharge)
        TextView mWealthRecharge;
        @InjectView(R.id.score)
        TextView mScore;

        String mSex;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            mSex = (String) SpUtils.get(mContext, "sex", "");
        }

        @OnClick({R.id.wealth_recharge, R.id.bill, R.id.AReward, R.id.rl_gold_detail, R.id.integral})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.wealth_recharge:
                    mContext.startActivity(new Intent(mContext, RechargeActivity.class));
                    break;
                case R.id.bill:
                    mContext.startActivity(new Intent(mContext, BillActivity.class));
                    break;
                case R.id.AReward:
                    mContext.startActivity(new Intent(mContext, GoldCoinActivity.class));
                    break;
                case R.id.rl_gold_detail:
                    mContext.startActivity(new Intent(mContext, GoldDetailWebActivity.class));
                    break;
                case R.id.integral:
                    mContext.startActivity(new Intent(mContext, ExchangeActivity.class));
                    break;
            }
        }

    }
}