package com.jsz.peini.ui.adapter.seller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.seller.SellerCodesBySellerCodesBean;
import com.jsz.peini.model.seller.SellerInfoBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.web.WebAllActivity;
import com.jsz.peini.ui.adapter.TestNormalAdapter;
import com.jsz.peini.ui.view.RoundAngleImageView;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kunwe on 2016/11/26.
 */
public class SellerAdapter extends RecyclerView.Adapter {

    private static final int ITEM_TYPE_HEADER = 1;
    private static final int ITEM_TYPE_HEADER_TWO = 2;
    private static final int ITEM_TYPE_CONTENT = 3;
    private static final int ITEM_TYPE_FOURTH = 4;
    private List<SellerInfoBean> mList;
    private List<AdModel.AdvertiseListBean> mAdData;
    private List<SellerCodesBySellerCodesBean.SellerCodesBean> mSellerCodesBeanList;
    private Activity mMainActivity;
    private int mHeaderCount = 2;//头部View个数
    private int mFourth = 1;//一个底部布局
    private TestNormalAdapter mTestNormalAdapter;

    public SellerAdapter(Activity mainActivity, List<SellerInfoBean> sellerInfo,
                         List<AdModel.AdvertiseListBean> advertiseList,
                         List<SellerCodesBySellerCodesBean.SellerCodesBean> sellerCodes) {
        mAdData = advertiseList;
        mMainActivity = mainActivity;
        mSellerCodesBeanList = sellerCodes;
        mList = sellerInfo;
    }

    /**
     * else if (position == 2) {
     * return ITEM_TYPE_HEADER_TWO;
     * }
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == 1) {
            return ITEM_TYPE_HEADER_TWO;
        } else if (mFourth != 0 && position >= (mHeaderCount + getSize())) {
            return ITEM_TYPE_FOURTH;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new ViewHolder(LayoutInflater.from(mMainActivity).inflate(R.layout.item_seller_codes, parent, false));
        } else if (viewType == ITEM_TYPE_HEADER_TWO) {
            return new ViewHolderTow(LayoutInflater.from(mMainActivity).inflate(R.layout.item_2, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new ViewHolderThere(LayoutInflater.from(mMainActivity).inflate(R.layout.seller_item_shopping, parent, false));
        } else if (viewType == ITEM_TYPE_FOURTH) {
            return new ViewHolderFourth(LayoutInflater.from(mMainActivity).inflate(R.layout.seller_item_shopping_null, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            setHeaderView((ViewHolder) holder, position);
        } else if (holder instanceof ViewHolderTow) {
            setMyRankView((ViewHolderTow) holder, position);
        } else if (holder instanceof ViewHolderThere) {
            setViewHolderOneView((ViewHolderThere) holder, position - mHeaderCount);
        } else if (holder instanceof ViewHolderFourth) {
            setViewHolderFourthView(holder);
        }
    }

    /**
     * 底部布局的条目的高度
     */
    private void setViewHolderFourthView(RecyclerView.ViewHolder holder) {
        ViewHolderFourth fourth = (ViewHolderFourth) holder;

    }

    /**
     * 条目布局
     */
    private void setViewHolderOneView(ViewHolderThere holder, final int position) {
        final SellerInfoBean sellerInfoBean = mList.get(position);
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
            GlideImgManager.loadImage(mMainActivity, IpConfig.HttpPic + imageSrc, holder.mImageSrc);
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

    /**
     * 悬停布局
     */
    private void setMyRankView(ViewHolderTow holder, final int position) {
        holder.mSellerSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(position);
                mListener.onSellerSelectItemClick(1);
            }
        });

        holder.mSellerDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(position);
                mListener.onSellerSelectItemClick(2);
            }
        });
        holder.mSellerTypeofoperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(position);
                mListener.onSellerSelectItemClick(3);
            }
        });
        holder.mSellerSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onClick(position);
                mListener.onSellerSelectItemClick(4);
            }
        });

    }

    /**
     * 头布局
     */
    private void setHeaderView(ViewHolder holder, int position) {
        if (null != mAdData) {
            if (mAdData.size() >= 2) {
                holder.mSliderVp.setVisibility(View.VISIBLE);
                holder.mIvViewPagerBj.setVisibility(View.GONE);
                mTestNormalAdapter = new TestNormalAdapter(holder.mSliderVp, mMainActivity, mAdData);
                holder.mSliderVp.setAdapter(mTestNormalAdapter);
                holder.mSliderVp.setHintView(new ColorPointHintView(mMainActivity, Conversion.FB4E30, Color.WHITE));
                mTestNormalAdapter.setData(mAdData);
                holder.mSliderVp.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        AdModel.AdvertiseListBean adItemBean = mAdData.get(position);
                        if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                            if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
                                String userId = SpUtils.getUserToken(mMainActivity);
                                String xPoint = SpUtils.getXpoint(mMainActivity);
                                String yPoint = SpUtils.getYpoint(mMainActivity);
                                String targetUrl = adItemBean.getAdLink() + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl));
                                Intent intent = new Intent(mMainActivity, WebAllActivity.class);
                                intent.putExtra("adItemBean", adItemBean);
                                intent.putExtra("adLink", targetUrl);
                                mMainActivity.startActivity(intent);
                            }
                        }
                    }
                });
                mTestNormalAdapter.notifyDataSetChanged();
            } else if (mAdData.size() == 0) {
                holder.mSliderVp.setVisibility(View.GONE);
                holder.mIvViewPagerBj.setVisibility(View.GONE);
            } else {
                holder.mSliderVp.setVisibility(View.GONE);
                holder.mIvViewPagerBj.setVisibility(View.VISIBLE);
                if (mAdData.size() == 1) {
                    GlideImgManager.loadImage(mMainActivity, IpConfig.HttpPic + mAdData.get(0).getAdImgUrl(), holder.mIvViewPagerBj, "3");
                    holder.mIvViewPagerBj.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mAdData != null && mAdData.size() > 0) {
                                AdModel.AdvertiseListBean adItemBean = mAdData.get(0);
                                if (adItemBean != null && !TextUtils.isEmpty(adItemBean.getAdLink())) {
                                    if (StringUtils.isHttpPath(adItemBean.getAdLink())) {
                                        String userId = SpUtils.getUserToken(mMainActivity);
                                        String xPoint = SpUtils.getXpoint(mMainActivity);
                                        String yPoint = SpUtils.getYpoint(mMainActivity);
                                        String targetUrl = adItemBean.getAdLink() + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
                                        Intent intent = new Intent(mMainActivity, WebAllActivity.class);
                                        intent.putExtra("adItemBean", adItemBean);
                                        intent.putExtra("adLink", targetUrl);
                                        mMainActivity.startActivity(intent);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    holder.mIvViewPagerBj.setBackgroundResource(R.drawable.ic_peini_banser);
                }
            }
        } else {
            holder.mSliderVp.setVisibility(View.GONE);
            holder.mIvViewPagerBj.setVisibility(View.GONE);
        }
        if (null != mSellerCodesBeanList && mSellerCodesBeanList.size() > 0) {
            SellerSubAdapter sellerSubAdapter = new SellerSubAdapter(mMainActivity, mSellerCodesBeanList);
            sellerSubAdapter.setOnItemClickListener(new SellerSubAdapter.OnItemClickListener() {
                @Override
                public void onClick(int index) {
                    if (mListener != null && mSellerCodesBeanList != null && mSellerCodesBeanList.size() > index) {
                        if (mSellerCodesBeanList.get(index) != null) {
                            mListener.onClickSellerType(mSellerCodesBeanList.get(index).getId(), index);
                        }
                    }
                }
            });
            holder.mRvSellerCodes.setLayoutManager(new GridLayoutManager(mMainActivity, 5));
            holder.mRvSellerCodes.setAdapter(sellerSubAdapter);
        } else {
            holder.mRvSellerCodes.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    private int getCount() {
        return getSize() + mHeaderCount + (mList != null && mList.size() > 6 ? 0 : mFourth);
    }

    private int getSize() {
        return mList != null && mList.size() > 0 ? mList.size() : 0;
    }

    /**
     * 轮播条的Viewholder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.slider_vp)
        RollPagerView mSliderVp;
        @InjectView(R.id.rv_item_seller_codes)
        RecyclerView mRvSellerCodes;
        @InjectView(R.id.iv_viewpager_bj)
        ImageView mIvViewPagerBj;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * 筛选的布局的viewholder
     */
    class ViewHolderTow extends RecyclerView.ViewHolder {
        @InjectView(R.id.image_1)
        ImageView mImage1;
        @InjectView(R.id.seller_seller)
        LinearLayout mSellerSeller;
        @InjectView(R.id.image_2)
        ImageView mImage2;
        @InjectView(R.id.seller_distance)
        LinearLayout mSellerDistance;
        @InjectView(R.id.image_3)
        ImageView mImage3;
        @InjectView(R.id.seller_typeofoperation)
        LinearLayout mSellerTypeofoperation;
        @InjectView(R.id.image_4)
        ImageView mImage4;
        @InjectView(R.id.seller_sort)
        LinearLayout mSellerSort;

        public ViewHolderTow(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    /**
     * 条目的ViewHolder
     */
    class ViewHolderThere extends RecyclerView.ViewHolder {
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

        public ViewHolderThere(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class ViewHolderFourth extends RecyclerView.ViewHolder {
        @InjectView(R.id.ll_viewholderfourth)
        LinearLayout mLlHolderFourth;

        public ViewHolderFourth(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    private OnClickListener mListener;

    public void setOnClickListener(OnClickListener listener) {
        mListener = listener;
    }

    public interface OnClickListener {
        /**
         * 第一条条目的点击事件  负责悬停
         */
        void onClick(int position);

        /**
         * 筛选点击事件的回调
         *
         * @param position 点击筛选的位置
         */
        void onSellerSelectItemClick(int position);

        /**
         * 商家的点击事件
         *
         * @param id       这个是商家的id
         * @param position 这个是条目的position
         */
        void onSellerItemClick(int id, int position);

        /**
         * 业态类型的点击事件
         *
         * @param id       业态的id
         * @param position 点击业态的位置
         */
        void onClickSellerType(String id, int position);
    }
}
