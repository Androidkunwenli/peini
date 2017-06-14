package com.jsz.peini.ui.adapter.square;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.square.CouponInfoListAllUnGetByScore;
import com.jsz.peini.model.square.ExchangeBean;
import com.jsz.peini.model.square.MiLabelBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.IntegralActivityNext;
import com.jsz.peini.ui.activity.square.IntegralMessageActivity;
import com.jsz.peini.ui.activity.web.WebAllActivity;
import com.jsz.peini.ui.adapter.TestNormalAdapter;
import com.jsz.peini.ui.view.SpacesItemDecoration;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2017/5/11.
 */

public class CxchangBeanAdapter extends RecyclerView.Adapter {

    private Activity mActivity;
    private int mMyIntegral = -1;

    private List<CouponInfoListAllUnGetByScore.CouponListBean> mMCouponList;
    private CouponInfoListAllUnGetByScore.UserInfoBean mMUserInfo;
    private List<AdModel.AdvertiseListBean> mMAdvertiseList;

    public CxchangBeanAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_integral_one, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (mMUserInfo != null) {
            setIntegralView(viewHolder, mMUserInfo);
            viewHolder.mLlUserinfo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlUserinfo.setVisibility(View.GONE);
        }
        if (mMCouponList != null && mMCouponList.size() != 0) {
            showCouponList(viewHolder, mMCouponList);
            viewHolder.mRecycler.setVisibility(View.VISIBLE);
            viewHolder.mLlIntegral.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mLlIntegral.setVisibility(View.GONE);
            viewHolder.mRecycler.setVisibility(View.GONE);
        }
        if (mMAdvertiseList != null && mMAdvertiseList.size() != 0) {
            showAdvertiseList(viewHolder, mMAdvertiseList);
            viewHolder.mFlViewpager.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mFlViewpager.setVisibility(View.GONE);
        }
    }


    private void showAdvertiseList(ViewHolder holder, final List<AdModel.AdvertiseListBean> advertiseList) {
        if (advertiseList.size() == 0) {
            holder.mLlIntegral.setVisibility(View.GONE);
        } else {
            holder.mLlIntegral.setVisibility(View.VISIBLE);
        }
        if (advertiseList.size() == 0) {
            holder.mFlViewpager.setVisibility(View.GONE);
        } else if (advertiseList.size() == 1) {
            setAdDataLisstOne(holder, advertiseList);
            holder.mIvViewpagerBj.setVisibility(View.VISIBLE);
            holder.mViewPager.setVisibility(View.GONE);
            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + advertiseList.get(0).getAdImgUrl(), holder.mIvViewpagerBj, "3");
            holder.mFlViewpager.setVisibility(View.VISIBLE);
        } else if (advertiseList.size() > 1) {
            setAdData(holder, advertiseList);
            holder.mIvViewpagerBj.setVisibility(View.GONE);
            holder.mViewPager.setVisibility(View.VISIBLE);
            holder.mFlViewpager.setVisibility(View.VISIBLE);
        }
    }

    private void setAdDataLisstOne(ViewHolder holder, final List<AdModel.AdvertiseListBean> advertiseList) {
        holder.mIvViewpagerBj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advertiseList.size() > 0) {
                    AdModel.AdvertiseListBean adItemBean = advertiseList.get(0);
                    if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                        LogUtil.d("轮播图的点击事件---" + IpConfig.HttpPic + adItemBean.getAdImgUrl());
                        if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
                            String userId = SpUtils.getUserToken(mActivity);
                            String xPoint = SpUtils.getXpoint(mActivity);
                            String yPoint = SpUtils.getYpoint(mActivity);
                            String targetUrl = adItemBean.getAdLink() + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
                            Intent intent = new Intent(mActivity, WebAllActivity.class);
                            intent.putExtra("adItemBean", adItemBean);
                            intent.putExtra("adLink", targetUrl);
                            mActivity.startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private void setAdData(ViewHolder holder, final List<AdModel.AdvertiseListBean> advertiseList) {
        TestNormalAdapter testNormalAdapter = new TestNormalAdapter(holder.mViewPager, mActivity, advertiseList);
        holder.mViewPager.setAdapter(testNormalAdapter);
        holder.mViewPager.setHintView(new ColorPointHintView(mActivity, Conversion.FB4E30, Color.WHITE));
        holder.mViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                AdModel.AdvertiseListBean adItemBean = advertiseList.get(position);
                if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                    LogUtil.d("轮播图的点击事件---" + IpConfig.HttpPic + adItemBean.getAdImgUrl());
                    if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
                        String userId = SpUtils.getUserToken(mActivity);
                        String xPoint = SpUtils.getXpoint(mActivity);
                        String yPoint = SpUtils.getYpoint(mActivity);
                        String targetUrl = adItemBean.getAdLink() + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
                        Intent intent = new Intent(mActivity, WebAllActivity.class);
                        intent.putExtra("adItemBean", adItemBean);
                        intent.putExtra("adLink", targetUrl);
                        mActivity.startActivity(intent);
                    }
                }
            }
        });
    }

    private void showCouponList(ViewHolder holder, List<CouponInfoListAllUnGetByScore.CouponListBean> couponList) {
        IntegralAdapter integralAdapter = new IntegralAdapter(mActivity, couponList, 3);
        holder.mRecycler.setLayoutManager(new GridLayoutManager(mActivity, 3));
        holder.mRecycler.setAdapter(integralAdapter);
        integralAdapter.setOnItemClickListener(new IntegralAdapter.OnItemClickListener() {
            @Override
            public void OnItemListener(int couponId, int couponIntegral) {
                IntegralMessageActivity.actionShow(mActivity, couponId, couponIntegral, mMyIntegral);
            }
        });
        holder.mLlIntegral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, IntegralActivityNext.class));
            }
        });
    }

    private void setIntegralView(ViewHolder holder, CouponInfoListAllUnGetByScore.UserInfoBean userInfo) {
        holder.mIvGold.setVisibility(userInfo.getGoldList() != 0 ? View.VISIBLE : View.GONE);
        holder.mIvBuy.setVisibility(userInfo.getBuyList() != 0 ? View.VISIBLE : View.GONE);
        holder.mIvIntegrity.setVisibility(userInfo.getIntegrityList() != 0 ? View.VISIBLE : View.GONE);
        holder.mNickName.setText(userInfo.getNickName());
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + userInfo.getImageStr(), holder.mImageHead, "1");
        //多少积分
        mMyIntegral = userInfo.getScore();
        holder.mScore.setText(String.valueOf(mMyIntegral));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setmCouponList(List<CouponInfoListAllUnGetByScore.CouponListBean> MCouponList) {
        mMCouponList = MCouponList;
        notifyDataSetChanged();
    }

    public void setmUserInfo(CouponInfoListAllUnGetByScore.UserInfoBean MUserInfo) {
        mMUserInfo = MUserInfo;
        notifyDataSetChanged();
    }

    public void setmAdvertiseList(List<AdModel.AdvertiseListBean> MAdvertiseList) {
        mMAdvertiseList = MAdvertiseList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ll_userinfo)
        LinearLayout mLlUserinfo;
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.nickName)
        TextView mNickName;
        @InjectView(R.id.iv_gold)
        ImageView mIvGold;
        @InjectView(R.id.iv_buy)
        ImageView mIvBuy;
        @InjectView(R.id.iv_integrity)
        ImageView mIvIntegrity;
        @InjectView(R.id.score)
        TextView mScore;
        @InjectView(R.id.iv_viewpager_bj)
        ImageView mIvViewpagerBj;
        @InjectView(R.id.viewPager)
        RollPagerView mViewPager;
        @InjectView(R.id.fl_viewpager)
        FrameLayout mFlViewpager;
        @InjectView(R.id.more)
        TextView mMore;
        @InjectView(R.id.ll_integral)
        LinearLayout mLlIntegral;
        @InjectView(R.id.recycler)
        RecyclerView mRecycler;
        @InjectView(R.id.tv_contact_label)
        TextView mTvContactLabel;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
