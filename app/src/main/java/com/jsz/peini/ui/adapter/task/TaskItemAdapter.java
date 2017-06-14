package com.jsz.peini.ui.adapter.task;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsz.peini.R;
import com.jsz.peini.model.tabulation.TaskListBean;
import com.jsz.peini.presenter.IpConfig;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class TaskItemAdapter extends PagerAdapter {

    private Activity mActivity;
    private List<TaskListBean.TaskAllListBean> mTaskAllList = new ArrayList<>();
    private LinkedList<WeakReference<View>> reusableViews = new LinkedList<>();

    private int mReallyCount;

    public TaskItemAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (mTaskAllList == null) {
            return 0;
        } else if (mTaskAllList.size() <= 1) {
            return mTaskAllList.size();
        } else {
            return Short.MAX_VALUE;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.i("TaskItemAdapter", "position : " + position);
        if (mReallyCount == 0) {
            return null;
        }

        View view;
        if (reusableViews.size() > 0 && reusableViews.getFirst() != null) {
            view = initView(reusableViews.getFirst().get(), position);
            reusableViews.removeFirst();
        } else {
            view = initView(null, position);
        }
        Log.i("TaskItemAdapter", "view : " + view.toString());
        Log.i("TaskItemAdapter", "viewHolder : " + view.getTag().toString());

        container.addView(view);//添加进viewpager轮播容器
        return view;//返回当前图片
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        if (reusableViews.size() > 0) {
            reusableViews.clear();
        }
//        Glide.get(mActivity).clearMemory();
        ((ViewPager) container).removeView((View) object);
        if (object != null) {
            reusableViews.addLast(new WeakReference<>((View) object));
        }
    }

    public void setTaskAllList(List<TaskListBean.TaskAllListBean> taskAllList) {
        mTaskAllList.clear();
        mTaskAllList.addAll(taskAllList);
        mReallyCount = mTaskAllList != null && mTaskAllList.size() > 0 ? mTaskAllList.size() : 0;
        notifyDataSetChanged();
    }

    public int getStartPageIndex() {
        if (mReallyCount == 0) {
            return 0;
        }
        int index = getCount() / 2;
        int remainder = index % mReallyCount;
        index = index - remainder;
        return index;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, int id);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    class ViewHolder {
        @InjectView(R.id.tv_address)
        TextView tvAddress;
        @InjectView(R.id.tv_distance)
        TextView tvDistance;
        @InjectView(R.id.iv_avatar)
        ImageView ivAvatar;
        @InjectView(R.id.tv_nickname)
        TextView tvNickname;
        @InjectView(R.id.tv_sex)
        ImageView tvSex;
        @InjectView(R.id.tv_age)
        TextView tvAge;
        @InjectView(R.id.ll_age_sex)
        LinearLayout llAgeSex;
        @InjectView(R.id.iv_gold)
        ImageView ivGold;
        @InjectView(R.id.iv_buy)
        ImageView ivBuy;
        @InjectView(R.id.iv_integrity)
        ImageView ivIntegrity;
        @InjectView(R.id.tv_reputation)
        TextView tvReputation;
        @InjectView(R.id.tv_who_pay)
        TextView tvWhoPay;
        @InjectView(R.id.tag_flow_layout)
        TagFlowLayout tagFlowLayout;
        @InjectView(R.id.tv_task_purpose)
        TextView tvTaskPurpose;
        @InjectView(R.id.tv_store_name)
        TextView tvStoreName;
        @InjectView(R.id.tv_time)
        TextView tvTime;
        @InjectView(R.id.tv_hope)
        TextView tvHope;

        private int taskId;
        private int position;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        @OnClick(R.id.ll_item_viewpager_task)
        public void onClick() {
            if (mListener != null) {
                mListener.onItemClick(position, taskId);
            }
        }
    }

    private View initView(View view, int position) {

        final ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_viewpager_task, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int positionIndex = position % mReallyCount;
        TaskListBean.TaskAllListBean taskBean = mTaskAllList.get(positionIndex);

        viewHolder.taskId = taskBean.getId();
        viewHolder.position = positionIndex;

        viewHolder.tvAddress.setText(taskBean.getSellerAddress());//地点
        int distanceInt = taskBean.getDistanceNum(); //距离
        String distanceStr;
        if (distanceInt < 1000) {
            distanceStr = distanceInt + "m";
        } else {
            float size = distanceInt / 1000f;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
            distanceStr = df.format(size) + "km";//返回的是String类型的
        }
        viewHolder.tvDistance.setText(distanceStr);

        viewHolder.tvNickname.setText(taskBean.getOthernickName());
        String age = taskBean.getAge() + "岁";
        viewHolder.tvAge.setText(age);
        int sex = taskBean.getSex(); //男女
        switch (sex) {
            case 1:
                viewHolder.tvSex.setImageResource(R.mipmap.nan);
                viewHolder.llAgeSex.setBackgroundResource(R.drawable.agesex_shape_nan);
                break;
            case 2:
                viewHolder.tvSex.setImageResource(R.mipmap.nv);
                viewHolder.llAgeSex.setBackgroundResource(R.drawable.agesex_shape_nv);
                break;
            default:
                break;
        }

        viewHolder.ivGold.setVisibility(0 != (taskBean.getGoldList()) ? View.VISIBLE : View.GONE);
        viewHolder.ivBuy.setVisibility(0 != (taskBean.getBuyList()) ? View.VISIBLE : View.GONE);
        viewHolder.ivIntegrity.setVisibility(0 != (taskBean.getIntegrityList()) ? View.VISIBLE : View.GONE);

        final String imageHead = taskBean.getImageHead();
        if (!TextUtils.isEmpty(imageHead)) {
            Glide.with(mActivity).load(IpConfig.HttpPic + imageHead)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(viewHolder.ivAvatar);
        } else if (sex == 1) {
            viewHolder.ivAvatar.setImageResource(R.mipmap.ic_nan);
        } else if (sex == 2) {
            viewHolder.ivAvatar.setImageResource(R.mipmap.ic_nv);
        }

        int reputation = taskBean.getReputation();//信誉
        viewHolder.tvReputation.setText(String.valueOf(reputation));

        String Sex;
        String Hope;
        int otherSex = taskBean.getOtherSex();
        if (otherSex == 1) {
            Sex = "帅哥";
        } else if (otherSex == 2) {
            Sex = "妹子";
        } else {
            Sex = "性别不限";
        }
        //判断谁发布的任务
        int publishType = taskBean.getPublishType();
        //（1我接ta 2 ta接我3自由行）
        int otherGo = taskBean.getOtherGo();
        //1我买单2他买单3AA制
        int otherBuy = taskBean.getOtherBuy();
        switch (publishType) {
            case 1:
            case 0:
                /**
                 *买单（1我买单2他买单3AA制）
                 出行（1我接ta 2 ta接我3自由行）
                 */
                if (otherBuy == 1) {
                    viewHolder.tvWhoPay.setText("发起人买单");
                } else if (otherBuy == 2) {
                    viewHolder.tvWhoPay.setText("接收人买单");
                } else if (otherBuy == 3) {
                    viewHolder.tvWhoPay.setText("AA制");
                } else {
                    viewHolder.tvWhoPay.setVisibility(View.GONE);
                }
                if (otherGo == 1) {
                    Hope = "我接Ta";
                } else if (otherGo == 2) {
                    Hope = "Ta接我";
                } else {
                    Hope = "自由行";
                }
                break;
            default:
                Hope = "";
                break;
        }
        viewHolder.tvTaskPurpose.setText(taskBean.getSellerSmallName()); //美食
        viewHolder.tvStoreName.setText(taskBean.getSellerInfoName());//地点
        viewHolder.tvTime.setText(taskBean.getTaskAppointedTime());//时间
        int otherLowAge = taskBean.getOtherLowAge();
        int otherHighAge = taskBean.getOtherHignAge();
        String hopeAge;
        if (otherLowAge == 0 || otherHighAge == 0) {
            hopeAge = "年龄不限";
        } else if (otherLowAge == 60 || otherHighAge == 60) {
            hopeAge = "60岁以上";
        } else {
            hopeAge = otherLowAge + "-" + otherHighAge + "岁";
        }
        viewHolder.tvHope.setText(hopeAge + "  " + Sex + "  " + Hope);
        List<TaskListBean.TaskAllListBean.StringList> userLabel = taskBean.getUserLabel();
        if (userLabel.size() > 0) {
            viewHolder.tagFlowLayout.setVisibility(View.VISIBLE);
            viewHolder.tagFlowLayout.setAdapter(new TagAdapter<TaskListBean.TaskAllListBean.StringList>(userLabel) {
                @Override
                public View getView(FlowLayout parent, int position, TaskListBean.TaskAllListBean.StringList userLabelBean) {
                    TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.tv2, parent, false);
                    tv.setText(userLabelBean.getLabelName());
                    return tv;
                }
            });
        } else {
            viewHolder.tagFlowLayout.setVisibility(View.GONE);
        }

        return view;
    }
}
