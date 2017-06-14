package com.jsz.peini.ui.adapter.ranking;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.ranking.RanKingBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.SpUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.jsz.peini.utils.UiUtils.getResources;

/**
 * Created by th on 2017/1/19.
 */
public class RanKingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int ITEM_TYPE_HEND = 0;
    private int ITEM_TYPE_CONTENT = 1;
    private int ITEM_TYPE_BOTTOM = 2;
    private final List<RanKingBean.RankListBean> mRankList;
    private final LayoutInflater mLayoutInflater;
    private final Activity mActivity;
    private boolean isShowTab = true;
    /*1:金币榜2:土豪榜3:诚信榜*/
    private int mRType;
    private Intent mIntent;
    private int ConTentHend = 1;
    private RanKingBean mHeadData;

    public RanKingAdapter(Activity activity, List<RanKingBean.RankListBean> body, int RType, String userToken) {
        mLayoutInflater = LayoutInflater.from(activity);
        mActivity = activity;
        mRType = RType;
        mRankList = body;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEND;
        } else if (position >= 3 + ConTentHend) {
            return ITEM_TYPE_CONTENT;
        } else {
            return ITEM_TYPE_BOTTOM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return new ViewHolderTwo(mLayoutInflater.inflate(R.layout.item_ranking_small, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new ViewHolderOne(mLayoutInflater.inflate(R.layout.item_ranking, parent, false));
        } else if (viewType == ITEM_TYPE_HEND) {
            return new ViewHolderHead(mLayoutInflater.inflate(R.layout.item_ranking_head, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderOne) {
            setViewHolderOneView((ViewHolderOne) holder, position);
        } else if (holder instanceof ViewHolderTwo) {
            setViewHolderTwoView((ViewHolderTwo) holder, position);
        } else if (holder instanceof ViewHolderHead) {
            setViewHolderHead((ViewHolderHead) holder, position);
        }
    }

    private void setViewHolderHead(ViewHolderHead holder, int position) {
        if (null == mHeadData) {
            return;
        }
        RanKingBean.MyRankBean myRank = mHeadData.getMyRank();
        holder.mRowNo.setText("第" + myRank.getRowNo() + "名");
        holder.mAge.setText(myRank.getAge() + "");
        holder.mNickname.setText(myRank.getNickname() + "");
        holder.mIndustry.setText("" + myRank.getIndustry());
        String goldNumStr = formatNum(myRank.getNum());
        switch (mRType) {
            case 1:
                holder.mNum.setText(Html.fromHtml("充值金币 <font color='#fb4e30'>" + goldNumStr));
                break;
            case 2:
                holder.mNum.setText(Html.fromHtml("消费金额 <font color='#fb4e30'>" + goldNumStr));
                break;
            case 3:
                holder.mNum.setText(Html.fromHtml("信用分值 <font color='#fb4e30'>" + goldNumStr));
                break;
            default:
                break;
        }
        int sex = myRank.getSex();
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + myRank.getImageHead(), holder.mImageHead, sex + "");
/*        if (sex == 1) { //男
            holder.mIndustry.setBackgroundResource(R.color.nan);
            holder.mSex.setImageResource(R.mipmap.nan);
            holder.mAge.setTextColor(getResources().getColor(R.color.nan));
        } else {
            holder.mIndustry.setBackgroundResource(R.color.nv);
            holder.mSex.setImageResource(R.mipmap.nv);
            holder.mAge.setTextColor(getResources().getColor(R.color.nv));
        }*/
        /**男女*/
        holder.mAge.setText(myRank.getAge() + "岁");
        switch (sex) {
            case 1:
                holder.mSex.setImageResource(R.mipmap.nan);
                holder.mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nan);
                break;
            case 2:
                holder.mSex.setImageResource(R.mipmap.nv);
                holder.mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nv);
                break;
            default:
                break;
        }
        /*跳转我的空间详情*/
        final String userId = myRank.getUserId();
        holder.mImageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.equals(SpUtils.getUserToken(mActivity))) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra("userId", userId);
                    mActivity.startActivity(mIntent);
                }
            }
        });
    }

    private void setViewHolderTwoView(ViewHolderTwo holder, final int positio) {
        int position = positio - 1;
        RanKingBean.RankListBean rankListBean = mRankList.get(position);
        holder.mNickname.setText(rankListBean.getNickname() + "");
        holder.mAge.setText(rankListBean.getAge() + "");
        holder.mNum.setText(formatNum(rankListBean.getNum()));
        holder.mIndustry.setText(rankListBean.getIndustry() + "");
        int index = position + 1;
        holder.mText4.setText("NO." + index);
//        if (index == 10) {
//            holder.mText4.setPadding(UiUtils.dip2px(mActivity, 10), 0, UiUtils.dip2px(mActivity, 7), 0);
//        }else {
//            holder.mText4.setPadding(UiUtils.dip2px(mActivity, 10), 0, UiUtils.dip2px(mActivity, 10), 0);
//        }
        switch (mRType) {
            case 1:
                holder.mTvInformation.setText("充值金币");
                break;
            case 2:
                holder.mTvInformation.setText("消费金额");
                break;
            case 3:
                holder.mTvInformation.setText("信用分值");
                break;
            default:
                break;
        }
        int sex = mRankList.get(position).getSex();
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + mRankList.get(position).getImageHead(), holder.mImageHead, sex + "");
        /**男女*/
        holder.mAge.setText(rankListBean.getAge() + "岁");
        switch (sex) {
            case 1:
                holder.mSex.setImageResource(R.mipmap.nan);
                holder.mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nan);
                break;
            case 2:
                holder.mSex.setImageResource(R.mipmap.nv);
                holder.mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nv);
                break;
            default:
                break;
        }
       /*跳转我的空间详情*/
        final String userId = mRankList.get(position).getUserId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.equals(SpUtils.getUserToken(mActivity))) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra("userId", userId);
                    mActivity.startActivity(mIntent);
                }
            }
        });
    }

    private void setViewHolderOneView(ViewHolderOne holder, final int position) {
        int i = position - 1;
        switch (mRType) {
            case 1://金币榜
                if (i == 0) {
                    holder.mNumbar.setImageResource(R.mipmap.item1);
                    holder.mType.setImageResource(R.mipmap.gold1);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.yi));
                } else if (i == 1) {
                    holder.mNumbar.setImageResource(R.mipmap.item2);
                    holder.mType.setImageResource(R.mipmap.gold2);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.er));
                } else if (i == 2) {
                    holder.mNumbar.setImageResource(R.mipmap.item3);
                    holder.mType.setImageResource(R.mipmap.gold3);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.san));
                }
                break;
            case 2://土豪榜
                if (i == 0) {
                    holder.mNumbar.setImageResource(R.mipmap.item1);
                    holder.mType.setImageResource(R.mipmap.buy1);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.yi));
                } else if (i == 1) {
                    holder.mNumbar.setImageResource(R.mipmap.item2);
                    holder.mType.setImageResource(R.mipmap.buy2);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.er));
                } else if (i == 2) {
                    holder.mNumbar.setImageResource(R.mipmap.item3);
                    holder.mType.setImageResource(R.mipmap.buy3);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.san));
                }
                break;
            case 3://信誉榜
                if (i == 0) {
                    holder.mNumbar.setImageResource(R.mipmap.item1);
                    holder.mType.setImageResource(R.mipmap.integrity1);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.yi));
                } else if (i == 1) {
                    holder.mNumbar.setImageResource(R.mipmap.item2);
                    holder.mType.setImageResource(R.mipmap.integrity2);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.er));
                } else if (i == 2) {
                    holder.mNumbar.setImageResource(R.mipmap.item3);
                    holder.mType.setImageResource(R.mipmap.integrity3);
                    holder.mImageHead.setBorderColor(getResources().getColor(R.color.san));
                }
                break;
        }
        RanKingBean.RankListBean rankListBean = mRankList.get(i);
        holder.mNickname.setText(rankListBean.getNickname() + "");
        holder.mNum.setText(formatNum(rankListBean.getNum()));
        holder.mAge.setText(rankListBean.getAge() + "");
        holder.mIndustry.setText(rankListBean.getIndustry() + "");
        switch (mRType) {
            case 1:
                holder.mTvInformation.setText("充值金币");
                break;
            case 2:
                holder.mTvInformation.setText("消费金额");
                break;
            case 3:
                holder.mTvInformation.setText("信用分值");
                break;
            default:
                break;
        }
        //性别
        int sex = mRankList.get(i).getSex();
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + mRankList.get(i).getImageHead(), holder.mImageHead, sex + "");
        /**男女*/
        holder.mAge.setText(rankListBean.getAge() + "岁");
        switch (sex) {
            case 1:
                holder.mSex.setImageResource(R.mipmap.nan);
                holder.mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nan);
                break;
            case 2:
                holder.mSex.setImageResource(R.mipmap.nv);
                holder.mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nv);
                break;
            default:
                break;
        }
        /*跳转我的空间详情*/
        final String userId = mRankList.get(i).getUserId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.equals(SpUtils.getUserToken(mActivity))) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra("userId", userId);
                    mActivity.startActivity(mIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Conversion.getSize(mRankList) + ConTentHend;
    }

    public void setHeadData(RanKingBean headData) {
        mHeadData = headData;
    }


    class ViewHolderTwo extends RecyclerView.ViewHolder {
        @InjectView(R.id.text4)
        TextView mText4;
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.nickname)
        TextView mNickname;
        @InjectView(R.id.tv_sex)
        ImageView mSex;
        @InjectView(R.id.industry)
        TextView mIndustry;
        @InjectView(R.id.tv_age)
        TextView mAge;
        @InjectView(R.id.num)
        TextView mNum;
        @InjectView(R.id.tv_information)
        TextView mTvInformation;
        @InjectView(R.id.ll_age_sex)
        LinearLayout mLlAgeSex;

        ViewHolderTwo(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        @InjectView(R.id.numbar)
        ImageView mNumbar;
        @InjectView(R.id.type)
        ImageView mType;
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.nickname)
        TextView mNickname;
        @InjectView(R.id.tv_sex)
        ImageView mSex;
        @InjectView(R.id.tv_age)
        TextView mAge;
        @InjectView(R.id.industry)
        TextView mIndustry;
        @InjectView(R.id.num)
        TextView mNum;
        @InjectView(R.id.ll_age_sex)
        LinearLayout mLlAgeSex;
        @InjectView(R.id.tv_information)
        TextView mTvInformation;

        ViewHolderOne(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    private String formatNum(String numStr) {
        if (TextUtils.isEmpty(numStr)) {
            return "0";
        }
        if (numStr.contains(".")) {
            return numStr.replaceAll("\\.*0*$", "");
        } else {
            return numStr;
        }
    }

    class ViewHolderHead extends RecyclerView.ViewHolder {
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.rowNo)
        TextView mRowNo;
        @InjectView(R.id.nickname)
        TextView mNickname;
        @InjectView(R.id.tv_sex)
        ImageView mSex;
        @InjectView(R.id.tv_age)
        TextView mAge;
        @InjectView(R.id.industry)
        TextView mIndustry;
        @InjectView(R.id.num)
        TextView mNum;
        @InjectView(R.id.ll_age_sex)
        LinearLayout mLlAgeSex;

        public ViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
