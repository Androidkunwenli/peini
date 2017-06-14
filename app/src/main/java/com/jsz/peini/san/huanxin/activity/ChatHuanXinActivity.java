package com.jsz.peini.san.huanxin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.news.TaskInfoByPhonesBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.news.NewService;
import com.jsz.peini.san.huanxin.fragment.ChatFragment;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.EaseUtils;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;

import java.text.DecimalFormat;

import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/18.
 */
public class ChatHuanXinActivity extends BaseNotSlideActivity implements ChatFragment.AvatarClickListener {

    private static final String EXTRA_DATA_TO_CHAT_USER_PHONE = "extra_data_to_chat_user_phone";
    private static final String EXTRA_DATA_TO_CHAT_USER_NAME = "extra_data_to_chat_user_name";
    private static final String EXTRA_DATA_IS_SHOW_TASK = "extra_data_is_show_task";

    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.container)
    FrameLayout mContainer;

    @InjectView(R.id.tv_task_desc)
    TextView mTvTaskDesc;
    @InjectView(R.id.tv_distance)
    TextView mTvDistance;
    @InjectView(R.id.tv_seller_name)
    TextView mTvSellerName;
    @InjectView(R.id.tv_who_pay)
    TextView mTvWhoPay;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.civ_image)
    CircleImageView mCivImage;
    @InjectView(R.id.tv_name)
    TextView mTvName;
    @InjectView(R.id.tv_industry)
    TextView mTvIndustry;
    @InjectView(R.id.ll_task)
    LinearLayout mLlTask;

    private ChatFragment mChatFragment;

    private String mToChatUserPhone;
    private String mToChatUserId;
    private ChatHuanXinActivity mActivity;
    private TaskInfoByPhonesBean mBean;

    private String mTaskId = null;
    private boolean mIsShowTask = true;

    @Override
    public int initLayoutId() {
        return R.layout.activity_chathuanxin;
    }

    @Override
    public void initView() {
        mActivity = this;
        mToChatUserPhone = getIntent().getExtras().getString(EXTRA_DATA_TO_CHAT_USER_PHONE);
        String toChatUserName = getIntent().getExtras().getString(EXTRA_DATA_TO_CHAT_USER_NAME);
//        mIsShowTask = getIntent().getBooleanExtra(EXTRA_DATA_IS_SHOW_TASK, false);

        if (TextUtils.isEmpty(mToChatUserPhone)) {
            Toasty.normal(mActivity, "数据异常").show();
            finish();
        }

        if (!TextUtils.isEmpty(toChatUserName)) {
            mTitle.setText(toChatUserName);
        }

        mLlTask.setVisibility(View.GONE);
        mCivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mToChatUserId)) {
                    Intent taIntent = new Intent(mActivity, TaSquareActivity.class);
                    taIntent.putExtra("userId", mToChatUserId);
                    startActivity(taIntent);
                }
            }
        });
        initNetWork();

        String phoneStr = (String) SpUtils.get(mActivity, "phone", "");
        EaseUtils.setUserInfoProvider(mActivity, phoneStr, mToChatUserPhone);
        addFragment();
    }

    public static void actionShow(Context context, String toChatUserPhone, String toChatUserName) {
        Intent intent = new Intent(context, ChatHuanXinActivity.class);
        intent.putExtra(EXTRA_DATA_TO_CHAT_USER_PHONE, toChatUserPhone);
        intent.putExtra(EXTRA_DATA_TO_CHAT_USER_NAME, toChatUserName);
        intent.putExtra(EXTRA_DATA_IS_SHOW_TASK, false);
        context.startActivity(intent);
    }

    public static void actionShow(Context context, String toChatUserPhone) {
        Intent intent = new Intent(context, ChatHuanXinActivity.class);
        intent.putExtra(EXTRA_DATA_TO_CHAT_USER_PHONE, toChatUserPhone);
        intent.putExtra(EXTRA_DATA_IS_SHOW_TASK, true);
        context.startActivity(intent);
    }

    private void initNetWork() {
        mDialog.show();
        String phoneStr = (String) SpUtils.get(mActivity, "phone", "");
        if (TextUtils.isEmpty(phoneStr) || TextUtils.isEmpty(mToChatUserPhone)) {
            return;
        }
        String towPhoneStr = phoneStr + "," + mToChatUserPhone;
        String xPoint = SpUtils.getXpoint(mActivity);
        String yPoint = SpUtils.getYpoint(mActivity);
        RetrofitUtil.createService(NewService.class)
                .getTaskInfoByPhones(SpUtils.getUserToken(mActivity), towPhoneStr, xPoint, yPoint)
                .enqueue(new RetrofitCallback<TaskInfoByPhonesBean>() {
                    @Override
                    public void onSuccess(Call<TaskInfoByPhonesBean> call, Response<TaskInfoByPhonesBean> response) {
                        if (response.isSuccessful()) {
                            mDialog.dismiss();
                            mBean = response.body();
                            if (mBean.getResultCode() == 1) {
                                showTaskView();
                            } else if (mBean.getResultCode() == 9) {
                                mLlTask.setVisibility(View.GONE);
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                mLlTask.setVisibility(View.GONE);
                                Toasty.normal(mActivity, mBean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskInfoByPhonesBean> call, Throwable t) {
                        mLlTask.setVisibility(View.GONE);
                        mDialog.dismiss();
                    }
                });
    }

    /*显示任务信息*/
    private void showTaskView() {
        TaskInfoByPhonesBean.DataBean data = mBean.getData();
        if (mIsShowTask) {
            mLlTask.setVisibility(View.VISIBLE);
            mTitle.setText(data.getNickName());
            GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + data.getHeadImg(), mCivImage, "1");
            mTvTaskDesc.setText(data.getTargetDesc());
            mTvSellerName.setText(data.getSellerName());
            mTvTime.setText(data.getTaskAppointedTime());
            mTvName.setText(data.getNickName());
            mTvIndustry.setText(data.getIndustry());

            int distance = data.getDistance();
            String distanceStr;
            if (distance < 1000) {
                distanceStr = distance + "m";
            } else {
                float size = distance / 1000f;
                DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
                distanceStr = df.format(size) + "km";//返回的是String类型的
            }
            mTvDistance.setText(distanceStr);

            String publishType = data.getPublishType();
            String otherBuy = data.getOtherBuy();
            if (!TextUtils.isEmpty(publishType) && !TextUtils.isEmpty(publishType)) {
                switch (publishType) {
                    case "0"://Ta发布的
                    case "1"://我发布的
                        switch (otherBuy) {
                            case "1":
                                mTvWhoPay.setText("发起人买单");
                                break;
                            case "2":
                                mTvWhoPay.setText("接收人买单");
                                break;
                            case "3":
                                mTvWhoPay.setText("AA制");
                                break;
                            default:
                                mTvWhoPay.setVisibility(View.GONE);
                                break;
                        }
                        break;
                    default:
                        mTvWhoPay.setVisibility(View.GONE);
                        break;
                }
            } else {
                mTvWhoPay.setVisibility(View.GONE);
            }

            mTaskId = data.getTaskId();
        } else {
            mLlTask.setVisibility(View.GONE);
        }
        if (mChatFragment != null && data != null) {

            String taskStatus = data.getStatus();
            String publishType = data.getPublishType();
            String otherTaskStatus = data.getTaskOtherStatus();

            if ("1".equals(publishType) ||
                    ("1".equals(taskStatus)
                            || "5".equals(taskStatus)
                            || "6".equals(taskStatus)
                            || "7".equals(taskStatus))) {
                mChatFragment.setInputEnable("2".equals(taskStatus));
            } else {
                mChatFragment.setInputEnable("21".equals(otherTaskStatus));
            }
        }
        if (data != null) {
            mToChatUserId = data.getOtherId();
        }
    }

    private void addFragment() {

        //new出EaseChatFragment或其子类的实例
        mChatFragment = new ChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putString(EaseConstant.EXTRA_USER_ID, mToChatUserPhone);
        mChatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.container, mChatFragment).commit();
        mChatFragment.setAvatarClickListener(this);
    }

    @Override
    protected void initListener() {
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLlTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mTaskId)) {
                    Intent intent = new Intent(mActivity, TaskDetailActivity.class);
                    intent.putExtra(Conversion.ID, mTaskId);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onUserAvatarClick(String username) {
        if (!TextUtils.isEmpty(username)) {
            if (username.equals(SpUtils.getPhone(mActivity))) {
                MiSquareActivity.actionShow(mActivity);
            } else if (!TextUtils.isEmpty(mToChatUserId)) {
                Intent taIntent = new Intent(mActivity, TaSquareActivity.class);
                taIntent.putExtra("userId", mToChatUserId);
                startActivity(taIntent);
            }
        }
    }
}
