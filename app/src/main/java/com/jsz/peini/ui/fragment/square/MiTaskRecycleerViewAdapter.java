package com.jsz.peini.ui.fragment.square;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.model.square.MyTaskAllBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by th on 2017/1/24.
 */
public class MiTaskRecycleerViewAdapter extends RecyclerView.Adapter {

    private static final int RECYCLERVIEW_ITEM_1 = 0;
    private static final int RECYCLERVIEW_ITEM_2 = 1;
    public final Activity mActivity;
    public final List<MyTaskAllBean.TaskInfoByUserIdListBean> mList;
    public int mHeader = 0;
    private MyTaskAllBean mBean;


    public MiTaskRecycleerViewAdapter(Activity activity, List<MyTaskAllBean.TaskInfoByUserIdListBean> list) {
        mActivity = activity;
        mList = list;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) {
//            return RECYCLERVIEW_ITEM_1;
//        } else {
//            return RECYCLERVIEW_ITEM_2;
//        }
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == RECYCLERVIEW_ITEM_1) {
//            return new ViewHolderOne(LayoutInflater.from(mActivity).inflate(R.layout.item_1_mitask, parent, false));
//        } else {
        return new ViewHolderTow(LayoutInflater.from(mActivity).inflate(R.layout.item_2_mitask, parent, false));
//        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderOne) {
            initOneView((ViewHolderOne) holder, position);
        } else {
            initTowView((ViewHolderTow) holder, position - mHeader);
        }
    }

    private void initTowView(ViewHolderTow holder, final int position) {
        final MyTaskAllBean.TaskInfoByUserIdListBean bean = mList.get(position);
        holder.mSellerInfoName.setText(bean.getSellerInfoName());
        holder.mTaskAppointedTime.setText(bean.getTaskAppointedTime());

        /**
         * bean.getIndustry()
         */
        holder.mIndustry.setText(StringUtils.isNull(bean.getIndustry()) ? "" : bean.getIndustry());
        holder.mNickName.setText(bean.getNickName());
        /**
         * 性别
         */
        int sex = bean.getSex();
        int age = bean.getAge();
        holder.mAge.setText(age + "岁");
        if (sex == 1) {
            holder.mlLAgesex.setBackgroundResource(R.drawable.agesex_shape_nan);
            holder.mSex.setImageResource(R.mipmap.nan);
        } else {
            holder.mlLAgesex.setBackgroundResource(R.drawable.agesex_shape_nv);
            holder.mSex.setImageResource(R.mipmap.nv);
        }

//         不显示排行榜数据
//        holder.mIvGold.setVisibility(0 != (bean.getGoldList()) ? View.VISIBLE : View.GONE);
//        holder.mIvBuy.setVisibility(0 != (bean.getBuyList()) ? View.VISIBLE : View.GONE);
//        holder.mIvIntegrity.setVisibility(0 != (bean.getIntegrityList()) ? View.VISIBLE : View.GONE);

        //买单（1我买单2他买单3AA制）
        int otherBuy = bean.getOtherBuy();
        //1我发布的 0发布的
        final int publishType = bean.getPublishType();
        switch (publishType) {
            case 0:
                holder.mPublishType.setText("Ta发布的");
                break;
            case 1:
                holder.mPublishType.setText("我发布的");
                break;
        }
        /**
         * otherGo				出行（1我接ta 2 ta接我3自由行）
         */
        int otherGo = bean.getOtherGo();
        String mOtherGoText = "";
        switch (publishType) {
            case 0:
            case 1:
                switch (otherGo) {
                    case 1:
                        mOtherGoText = "我接Ta";
                        break;
                    case 2:
                        mOtherGoText = "Ta接我";
                        break;
                    case 3:
                        mOtherGoText = "自由行";
                        break;
                    default:
                        break;
                }
                break;
//            case 0:
//                switch (otherGo) {
//                    case 1:
//                        mOtherGoText = "Ta接我";
//                        break;
//                    case 2:
//                        mOtherGoText = "我接Ta";
//                        break;
//                    case 3:
//                        mOtherGoText = "自由行";
//                        break;
//                    default:
//                        break;
//                }
//                break;
            default:
                break;
        }
        //任务状态（全部为空1发布中2进行中3待评价4已完成5已关闭）
        String taskStatus;
        if (publishType == 1) {
            taskStatus = bean.getTaskStatus();
        } else {
            switch (bean.getTaskStatus()) {
                case "1":
                case "5":
                case "6":
                case "7":
                    taskStatus = bean.getTaskStatus();
                    break;
                default:
                    taskStatus = bean.getTaskOtherStatus();
                    break;
            }
        }
        switch (taskStatus) {
            case "1":
                holder.mTaskStatus.setText("发布中");
                holder.mLlTaskTaskstatus.setVisibility(View.GONE);
                holder.mVview.setVisibility(View.GONE);
                holder.mTvGotothishouse.setVisibility(View.GONE);
                holder.mTvEvaluate.setVisibility(View.GONE);
                holder.mOtherPhone.setVisibility(View.GONE);
                holder.mOtherBuy.setVisibility(View.GONE);
                break;
            case "2":
                holder.mTaskStatus.setText("进行中");
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTvGotothishouse.setVisibility(View.GONE);
                holder.mTvEvaluate.setVisibility(View.GONE);
                holder.mOtherPhone.setVisibility(View.VISIBLE);
                if (publishType == 1) { //我发布的
                    holder.mOtherBuy.setVisibility(otherBuy == 1 | otherBuy == 3 ? View.VISIBLE : View.GONE);
                } else {//他发布的
                    holder.mOtherBuy.setVisibility(otherBuy == 3 | otherBuy == 2 ? View.VISIBLE : View.GONE);
                }
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.text999));
                break;
            case "3":
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTaskStatus.setText("待评价");
                holder.mTvGotothishouse.setVisibility(View.VISIBLE);
                holder.mTvEvaluate.setVisibility(View.VISIBLE);
                holder.mOtherPhone.setVisibility(View.GONE);
                holder.mOtherBuy.setVisibility(View.GONE);
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.text999));
                break;
            case "4":
            case "6":
            case "7":
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTaskStatus.setText("已完成");
                holder.mTvGotothishouse.setVisibility(View.VISIBLE);
                holder.mTvEvaluate.setVisibility(View.GONE);
                holder.mOtherPhone.setVisibility(View.GONE);
                holder.mOtherBuy.setVisibility(View.GONE);
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.RED_FB4E30));
                break;
            case "5":
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTaskStatus.setText("已取消");
                holder.mTvGotothishouse.setVisibility(View.VISIBLE);
                holder.mTvEvaluate.setVisibility(View.GONE);
                holder.mOtherPhone.setVisibility(View.GONE);
                holder.mOtherBuy.setVisibility(View.GONE);
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.text999));
                break;
            case "21"://代付款
                holder.mTaskStatus.setText("进行中");
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTvGotothishouse.setVisibility(View.GONE);
                holder.mTvEvaluate.setVisibility(View.GONE);
                holder.mOtherPhone.setVisibility(View.VISIBLE);
                if (publishType == 1) { //我发布的
                    holder.mOtherBuy.setVisibility(otherBuy == 1 | otherBuy == 3 ? View.VISIBLE : View.GONE);
                } else {//他发布的
                    holder.mOtherBuy.setVisibility(otherBuy == 3 | otherBuy == 2 ? View.VISIBLE : View.GONE);
                }
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.text999));
                break;
            case "31"://待评价
                LogUtil.d("待评价" + position);
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTaskStatus.setText("待评价");
                holder.mTvGotothishouse.setVisibility(View.VISIBLE);
                holder.mTvEvaluate.setVisibility(View.VISIBLE);
                holder.mOtherPhone.setVisibility(View.GONE);
                holder.mOtherBuy.setVisibility(View.GONE);
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.text999));
                break;
            case "41"://已取消
                LogUtil.d("已完成" + position);
                holder.mLlTaskTaskstatus.setVisibility(View.VISIBLE);
                holder.mVview.setVisibility(View.VISIBLE);
                holder.mTaskStatus.setText("已完成");
                holder.mTvGotothishouse.setVisibility(View.VISIBLE);
                holder.mTvEvaluate.setVisibility(View.GONE);
                holder.mOtherPhone.setVisibility(View.GONE);
                holder.mOtherBuy.setVisibility(View.GONE);
                holder.mTaskStatus.setTextColor(UiUtils.getResources().getColor(R.color.RED_FB4E30));
                break;
            default:
                holder.mLlTaskTaskstatus.setVisibility(View.GONE);
                holder.mVview.setVisibility(View.GONE);
                break;
        }
        /**
         * 期望性别
         */
        int otherSex = bean.getOtherSex();
        /**
         * 展示出行信息
         */
        String expectSexText;
        switch (otherSex) {
            case 1:
                expectSexText = "帅哥";
                break;
            case 2:
                expectSexText = "妹子";
                break;
            default:
                expectSexText = "性别不限";
                break;
        }
        /**
         * 最小年龄
         */
        int otherLowAge = bean.getOtherLowAge();
        /**
         * 最大年龄
         */
        int otherHighAge = bean.getOtherHighAge();
        String hopeAge;
        if (otherLowAge == 0 || otherHighAge == 0) {
            hopeAge = "年龄不限";
        } else if (otherLowAge == 60 || otherHighAge == 60) {
            hopeAge = "60岁以上";
        } else {
            hopeAge = otherLowAge + "-" + otherHighAge + "岁";
        }
        String otherLowAgeotherHighAgeotherSexotherGootherBuyText
                = hopeAge + "  " + expectSexText + "  " + mOtherGoText;
        holder.mOtherLowAgeotherHighAgeotherSexotherGootherBuy.setText(otherLowAgeotherHighAgeotherSexotherGootherBuyText);
        String url = IpConfig.HttpPic + bean.getImageHead();
        GlideImgManager.loadImage(mActivity, url, holder.mImageHead, sex + "");
        final int id = bean.getId();
        final String otherUserId = publishType == 1 ? bean.getOtherUserId() : bean.getUserId();//他人token
        final String otherNickName = publishType == 1 ? bean.getOtherNickName() : bean.getNickName();//他人name
        final int SellerInfoId = bean.getSellerInfoId();//商家id
        final String SellerInfoName = bean.getSellerInfoName();//商家id
        final String SellerBigName = bean.getSellerTypeImg();
        //订单id
        final String OrderId = publishType == 0 ? bean.getOtherOrderId() : bean.getOrderId();//订单id

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                LogUtil.d("长按删除");
                mListener.onLongClickItem(position, id);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d("点击跳转");
                mListener.onClickItem(position, id);
            }
        });
        holder.mLlSellermeassage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SellerMessageActivity.class);
                intent.putExtra(Conversion.ID, String.valueOf(SellerInfoId));
                intent.putExtra(Conversion.BOOLEAN, false);
                mActivity.startActivity(intent);
            }
        });
        holder.mOtherPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mPhone = null;
                String nickname = null;
                switch (publishType) {
                    case 1: //我发布的
                        mPhone = bean.getOtherPhone();
                        nickname = bean.getOtherNickName();
                        break;
                    case 0:
                        mPhone = bean.getUserPhone();
                        nickname = bean.getNickName();
                        break;
                    default:
                        break;
                }
                LogUtil.d("点击联系他的按钮");
//                String towPhone = bean.getOtherPhone() + "," + bean.getUserPhone();
//                mListener.onChatClickItem(position, mPhone, towPhone);
                mListener.onChatClickItem(position, mPhone, nickname);
            }
        });
        final String userId = bean.getUserId();
        holder.mImageHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNull(userId)) {
                    return;
                }
                if (publishType == 1) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    Intent intent = new Intent(mActivity, TaSquareActivity.class);
                    intent.putExtra(Conversion.USERID, userId);
                    mActivity.startActivity(intent);
                }
            }
        });
        holder.mOtherBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtil.d("点击去买单的按钮");
                mListener.onTaskStatusClickItem(position, id, SellerInfoId, SellerInfoName, otherNickName, otherUserId, OrderId);
            }
        });
        holder.mTvEvaluate.setOnClickListener(new View.OnClickListener() { //评价
            @Override
            public void onClick(View v) {
                mListener.onTaskEvaluateClickItem(position, id, SellerInfoId, SellerInfoName, otherNickName, otherUserId, OrderId);
            }
        });
        holder.mTvGotothishouse.setOnClickListener(new View.OnClickListener() { //还去这家
            @Override
            public void onClick(View v) {
                mListener.onTaskGotothishouseClickItem(position, SellerInfoId, SellerInfoName, SellerBigName);
            }
        });
    }

    private void initOneView(ViewHolderOne holder, final int position) {
//        if (mList != null && mList.size() != 0) {
//            holder.mLlHendScreen.setVisibility(View.VISIBLE);
//        } else {
//            holder.mLlHendScreen.setVisibility(View.GONE);
//        }
        if (mBean != null) {
            int regCnt = mBean.getCompleteness().getRegCnt();
            DecimalFormat df = new DecimalFormat(".##");
            float finishRatio = mBean.getCompleteness().getFinishRatio();
            String format = df.format(finishRatio);
            holder.mTvRegCntFinishRatio.setText("任务状态(已参加任务" + regCnt + "个)");//,完成度" + format + "%)");
            holder.mTaskScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mListener.onScreenClickItem();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getSize() + mHeader;
    }

    private int getSize() {
        return mList.size() > 0 ? mList.size() : 0;
    }

    public void setBean(MyTaskAllBean bean) {
        mBean = bean;
        notifyDataSetChanged();
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        @InjectView(R.id.task_screen)
        TextView mTaskScreen;
        @InjectView(R.id.tv_regCnt_finishRatio)
        TextView mTvRegCntFinishRatio;
        @InjectView(R.id.ll_hend_screen)
        LinearLayout mLlHendScreen;

        public ViewHolderOne(View inflate) {
            super(inflate);
            ButterKnife.inject(this, inflate);
        }
    }

    class ViewHolderTow extends RecyclerView.ViewHolder {
        @InjectView(R.id.iv_gold)
        ImageView mIvGold;
        @InjectView(R.id.iv_buy)
        ImageView mIvBuy;
        @InjectView(R.id.iv_integrity)
        ImageView mIvIntegrity;
        @InjectView(R.id.ll_task_taskstatus)
        LinearLayout mLlTaskTaskstatus;
        @InjectView(R.id.v_view)
        View mVview;
        @InjectView(R.id.imageHead)
        CircleImageView mImageHead;
        @InjectView(R.id.nickName)
        TextView mNickName;
        @InjectView(R.id.tv_sex)
        ImageView mSex;
        @InjectView(R.id.tv_age)
        TextView mAge;
        @InjectView(R.id.ll_age_sex)
        LinearLayout mlLAgesex;
        @InjectView(R.id.industry)
        TextView mIndustry;
        @InjectView(R.id.publishType)
        TextView mPublishType;
        @InjectView(R.id.sellerInfoName)
        TextView mSellerInfoName;
        @InjectView(R.id.taskAppointedTime)
        TextView mTaskAppointedTime;
        @InjectView(R.id.otherLowAgeotherHighAgeotherSexotherGootherBuy)
        TextView mOtherLowAgeotherHighAgeotherSexotherGootherBuy;
        @InjectView(R.id.taskStatus)
        TextView mTaskStatus;
        /**
         * 联系他
         */
        @InjectView(R.id.otherPhone)
        TextView mOtherPhone;
        /**
         * 我买单
         */
        @InjectView(R.id.otherBuy)
        TextView mOtherBuy;
        /**
         * 去评价
         */
        @InjectView(R.id.tv_evaluate)
        TextView mTvEvaluate;
        /**
         * 还去这家
         */
        @InjectView(R.id.tv_gotothishouse)
        TextView mTvGotothishouse;
        /**
         * 点击店铺
         */
        @InjectView(R.id.ll_sellermeassage)
        LinearLayout mLlSellermeassage;

        public ViewHolderTow(View inflate) {
            super(inflate);
            ButterKnife.inject(this, inflate);
        }
    }

    private OnLongClickListener mListener;

    public void setOnLongClickListener(OnLongClickListener listener) {
        mListener = listener;
    }

    public interface OnLongClickListener {
        /**
         * 长按
         */
        void onLongClickItem(int position, int id);

//        /**
//         * 筛选
//         */
//        void onScreenClickItem();

        /**
         * 点击
         */
        void onClickItem(int position, int id);

        /**
         * 聊天
         */
        void onChatClickItem(int position, String mPhone, String nickname);
//        void onChatClickItem(int position, String mPhone, String towPhone);

        /**
         * 买单的点击事件
         */
        void onTaskStatusClickItem(int position, int mId, int sellerInfoId, String sellerInfoName, String otherNickName, String otherUserId, String orderId);

        /**
         * 评价的点击事件
         */
        void onTaskEvaluateClickItem(int position, int id, int SellerInfoId, String sellerInfoName, String otherNickName, String otherUserId, String OrderId);

        /**
         * 再来一单的点击事件
         *
         * @param position
         * @param sellerInfoId
         * @param sellerInfoName
         * @param sellerBigName
         */
        void onTaskGotothishouseClickItem(int position, int sellerInfoId, String sellerInfoName, String sellerBigName);


    }
}
