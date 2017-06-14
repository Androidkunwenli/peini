package com.jsz.peini.ui.activity.task;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jsz.peini.R;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.eventbus.TaskEvaluationSuccess;
import com.jsz.peini.model.eventbus.TaskReleaseRefreshTaskAndMapList;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.tabulation.TabulationMessageBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.san.huanxin.activity.ChatHuanXinActivity;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.pay.PaythebillActivity;
import com.jsz.peini.ui.activity.report.ReportActivity;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.TaSquareActivity;
import com.jsz.peini.ui.adapter.task.TaskAdapter;
import com.jsz.peini.ui.fragment.task.TaskPaySuccessFragmnet;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ShareUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.UrlUtils;
import com.jsz.peini.widget.ImageHendDialogFragment;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.UseCameraActivity;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskDetailActivity extends BaseActivity implements ImageHendDialogFragment.SelectPhotoMonitorDialogListener, NonGestureLockInterface {
    private int PHOTO_REQUEST_CUT = 100;//截取图片
    private int RESULT_LOAD_IMAGE2 = 200; //拍照
    private int RESULT_LOAD_IMAGE = 300; //选择图片
    private ImageHendDialogFragment mDialogFragment;
    @InjectView(R.id.tv_title_task)
    TextView mTvTitleTask;//名称
    @InjectView(R.id.iv_task_more)
    ImageView mIvTaskMore; //更多
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    @InjectView(R.id.sv_show_task)
    ScrollView mSvShowTask;
    @InjectView(R.id.task_vp)
    RollPagerView mTaskVp;
    @InjectView(R.id.is_show)
    LinearLayout mIsShow;
    @InjectView(R.id.iv_viewpager_bj)
    ImageView mIvViewPagerBj;
    @InjectView(R.id.civ_user_avatar)
    CircleImageView mCivUserAvatar;
    @InjectView(R.id.tv_user_name)
    TextView mTvUserName;
    @InjectView(R.id.tv_sex)
    ImageView mIvUserSex;
    @InjectView(R.id.tv_age)
    TextView mTvUserAge;
    @InjectView(R.id.ll_age_sex)
    LinearLayout mLlAgeSex;
    @InjectView(R.id.tv_task_distance)
    TextView mTvTaskDistance;
    @InjectView(R.id.tv_user_reputation)
    TextView mTvUserReputation;
    @InjectView(R.id.tv_task_pay)
    TextView mTvTaskPay;
    @InjectView(R.id.filter_flow_layout)
    TagFlowLayout mFilterFlowLayout;
    @InjectView(R.id.task_labelsname)
    TextView mTaskLabelsname;
    @InjectView(R.id.task_map)
    TextView mTaskMap;
    @InjectView(R.id.task_time)
    TextView mTaskTime;
    @InjectView(R.id.tv_expect)
    TextView mTvTaskExpect;
    @InjectView(R.id.task_text)
    TextView mTaskText;
    @InjectView(R.id.task_mejoin)
    Button mTaskMejoin;
    @InjectView(R.id.task_youjoin)
    Button mTaskYoujoin;
    @InjectView(R.id.task_telephone)
    Button mTaskTelephone;
    @InjectView(R.id.task_linear1)
    LinearLayout mTaskLinear1;
    @InjectView(R.id.task_mi_pay)
    Button mTaskMiPay;
    @InjectView(R.id.task_linear3)
    LinearLayout mTaskLinear3;
    @InjectView(R.id.seller_success_pingjia)
    Button mSellerSuccessPingjia;
    @InjectView(R.id.task_linear2)
    LinearLayout mTaskLinear2;
    @InjectView(R.id.bt_gotothishouse)
    Button mBtgotothishouse;
    @InjectView(R.id.iv_gold)
    ImageView mIvGold;
    @InjectView(R.id.iv_buy)
    ImageView mIvBuy;
    @InjectView(R.id.iv_integrity)
    ImageView mIvIntegrity;
    private TaskAdapter taskadapter;
    private Intent mIntent;
    private String mTaskId;
    /*this*/
    private TaskDetailActivity mActivity;
    private List<TabulationMessageBean.TaskInfoListBean> mTaskInfoList;
    private int beanId;
    private int mSellerInfoId;

    private String mOtherUserPhone = null;
    private String mOtherUserName = null;
    private String mSellerInfoName;
    private String mOtherNickname;
    private String mOrderId;
    private String mOtherUserId;
    private String mSellerBigName;

    private int mTaskStatus = -1;
    private int mPublishType = 0;
    private String mBeanUserId;
    private List<TabulationMessageBean.TaskInfoListBean.SellerImageBean> mSellerImage = new ArrayList<>();
    private String mNickName;
    private String mTaskDesc;
    private String mImageHead;
    private int mSex;
    private String mSellerSmallName;
    private String mSellerTypeImg;
    private TabulationMessageBean.TaskInfoListBean mTaskInfoListBean;
    private Uri mCroppedImageUri;

    @Override
    public int initLayoutId() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (!SpUtils.isCompleteUserInfo(this)) {
            CompleteUserInfoActivity.actionShow(this, SpUtils.getUserToken(this));
            finish();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mActivity = this;
        //注册事件
        EventBus.getDefault().register(this);
        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_success), "正在拼命加载中...");
        mSvShowTask.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mIvViewPagerBj.setVisibility(View.VISIBLE);
        taskadapter = new TaskAdapter(mTaskVp, mActivity, mSellerImage);
        mTaskVp.setAdapter(taskadapter);
        mTaskVp.setHintView(new ColorPointHintView(this, Conversion.FB4E30, Color.WHITE));
        mTaskVp.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtil.d("轮播图的点击事件" + position);
            }
        });
    }

    //是否第一次刷新
    boolean isNotifyDataSetChanged = true;

    private void initNetWork() {
//        LogUtil.d("这个是访问详情的接口" + mTaskId + "--" + mUserToken + "--" + SpUtils.getXpoint(mActivity) + "=====" + SpUtils.getYpoint(mActivity));
        RetrofitUtil.createService(TaskService.class)
                .getTaskInfo(mTaskId, mUserToken, SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity), type, orderId)
                .enqueue(new Callback<TabulationMessageBean>() {
                    @Override
                    public void onResponse(Call<TabulationMessageBean> call, final Response<TabulationMessageBean> response) {
                        if (response.isSuccessful()) {
                            TabulationMessageBean bean = response.body();
                            if (bean.getResultCode() == 1) {
                                mPaProgress.showContent();
                                setShwoView(bean);
                            } else if (bean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (bean.getResultCode() == 0) {
                                Toasty.normal(mActivity, bean.getResultDesc()).show();
                            } else if (bean.getResultCode() == 104) {
                                mIvTaskMore.setVisibility(View.GONE); //分享
                                mTvTitleTask.setVisibility(View.VISIBLE);//显示标题
                                mTvTitleTask.setText("任务评分");
                                TabulationMessageBean.TaskInfoListBean sellerName = bean.getTaskInfoList().get(0);
                                if (sellerName != null) {
                                    mPaProgress.removeAllViews();
                                    addFragment(sellerName);
                                }
                            } else {
                                finish();
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<TabulationMessageBean> call, Throwable t) {
                        finish();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 显示界面
     */
    private void setShwoView(TabulationMessageBean bean) {
        mTvTitleTask.setVisibility(View.VISIBLE); //显示标题
        mIvTaskMore.setVisibility(View.VISIBLE);//显示分享
        mTvTitleTask.setText("任务详情");
        //添加界面
        mPaProgress.removeAllViews();
        mPaProgress.addView(mSvShowTask);
        mTaskInfoList = bean.getTaskInfoList();
        if (isNotifyDataSetChanged) {
            if (mSellerImage.size() == 0) {
                mSellerImage.clear();
                mSellerImage.addAll(mTaskInfoList.get(0).getSellerImage());
                if (mSellerImage.size() > 0) {
                    mIvViewPagerBj.setVisibility(View.GONE);
                    taskadapter.notifyDataSetChanged();
                }
            }
            isNotifyDataSetChanged = false;//之请求一次就行了
        }
        initLabel(mTaskInfoList.get(0).getOtherUserLabel(), mTaskInfoList.get(0).getUserLabel());
        initDataView(mTaskInfoList);
        mSvShowTask.setVisibility(View.VISIBLE);
    }

    /**
     * 跳转界面评价
     */
    private void addFragment(TabulationMessageBean.TaskInfoListBean sellerName) {
        //new出EaseChatFragment或其子类的实例
        TaskPaySuccessFragmnet taskPaySuccessFragmnet = new TaskPaySuccessFragmnet();
        //传入参数
        Bundle args = new Bundle();
        args.putString(Conversion.TASKID, String.valueOf(sellerName.getId()));//任务id
        args.putString(Conversion.SELLERINFOID, String.valueOf(sellerName.getSellerInfoId()));//商家id
        args.putString(Conversion.USERID, String.valueOf(sellerName.getOtherUserId()));//他人token
        args.putString(Conversion.ORDERID, String.valueOf(sellerName.getOrderId()));//订单id
        args.putString(Conversion.SELLERNMAE, String.valueOf(sellerName.getSellerInfoName()));//商家名字
        args.putString(Conversion.OTHERSRNMAE, String.valueOf(sellerName.getOthernickName()));//他们名字
        taskPaySuccessFragmnet.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.pa_progress, taskPaySuccessFragmnet).commit();
        isBottomColumn(5);
    }

    /**
     * 文本填充
     *
     * @param otherUserLabel
     * @param taskInfoList
     */
    private void initLabel(List<TabulationMessageBean.TaskInfoListBean.UserLabelBean> otherUserLabel,
                           List<TabulationMessageBean.TaskInfoListBean.UserLabelBean> taskInfoList) {
        if (taskInfoList == null || taskInfoList.size() <= 0) {
            return;
        }
        final LayoutInflater mInflater = LayoutInflater.from(mActivity);
        mFilterFlowLayout.setAdapter(new TagAdapter<TabulationMessageBean.TaskInfoListBean.UserLabelBean>(taskInfoList) {
            @Override
            public View getView(FlowLayout parent, int position, TabulationMessageBean.TaskInfoListBean.UserLabelBean s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv2, mFilterFlowLayout, false);
                tv.setText(s.getLabelName());
                return tv;
            }
        });
    }

    /**
     * 填充布局信息
     *
     * @param taskInfoList
     */
    private void initDataView(List<TabulationMessageBean.TaskInfoListBean> taskInfoList) {
        boolean empty = taskInfoList.isEmpty();
        LogUtil.i("这个是判断集合有没有元素的", "又或者么有" + empty);
        mTaskInfoListBean = taskInfoList.get(0);
        mImageHead = mTaskInfoListBean.getImageHead();
        String mAge = mTaskInfoListBean.getAge() + "岁";
        mTvUserAge.setText(mAge);
        int sex = mTaskInfoListBean.getSex(); //男女
        mSex = sex;
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + mImageHead, mCivUserAvatar, String.valueOf(sex));
        switch (sex) {
            case 1:
                mIvUserSex.setImageResource(R.mipmap.nan);
                mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nan);
                break;
            case 2:
                mIvUserSex.setImageResource(R.mipmap.nv);
                mLlAgeSex.setBackgroundResource(R.drawable.agesex_shape_nv);
                break;
            default:
                break;
        }
        mIvGold.setVisibility(0 != (mTaskInfoListBean.getGoldList()) ? View.VISIBLE : View.GONE);
        mIvBuy.setVisibility(0 != (mTaskInfoListBean.getBuyList()) ? View.VISIBLE : View.GONE);
        mIvIntegrity.setVisibility(0 != (mTaskInfoListBean.getIntegrityList()) ? View.VISIBLE : View.GONE);
        String distanceStr;
        int distanceInt = mTaskInfoListBean.getDistance();
        if (distanceInt < 1000) {
            distanceStr = distanceInt + "m";
        } else {
            float size = distanceInt / 1000f;
            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
            distanceStr = df.format(size) + "km";//返回的是String类型的
        }
        mTvTaskDistance.setText(distanceStr);

        int reputation = mTaskInfoListBean.getReputation();//信誉
        if (!TextUtils.isEmpty(reputation + "")) {
            String taskReputationStr = reputation + "";
            mTvUserReputation.setText(taskReputationStr);
        } else {
            mTvUserReputation.setVisibility(View.GONE);
        }
        mSellerSmallName = String.valueOf(mTaskInfoListBean.getSellerSmallName());
        mTaskLabelsname.setText(mSellerSmallName); //美食
        String taskAppointedTime = String.valueOf(mTaskInfoListBean.getTaskAppointedTime());
        mTaskTime.setText(taskAppointedTime);//时间

        int minAge = mTaskInfoListBean.getOtherLowAge();
        int maxAge = mTaskInfoListBean.getOtherHignAge();
        String mSex;
        String Hope;
        if (mTaskInfoListBean.getOtherSex() == 1) {
            mSex = "帅哥";
        } else if (mTaskInfoListBean.getOtherSex() == 2) {
            mSex = "妹子";
        } else {
            mSex = "性别不限";
        }
        String TaskDesc = mTaskInfoListBean.getTaskDesc() + "";
        mTaskText.setText(TaskDesc);
        /**判断最底层的买单界面布局*/
        //买单（1我买单2他买单3AA制）
        int otherBuy = mTaskInfoListBean.getOtherBuy();
        //1 我发布的; 0 Ta发
        mPublishType = mTaskInfoListBean.getPublishType();
        switch (mPublishType) {
            case 0://Ta发布的
            case 1://我发布的
                /**
                 *买单（1我买单2他买单3AA制）
                 出行（1我接ta 2 ta接我3自由行）
                 */
                switch (otherBuy) {
                    case 1:
                        mTvTaskPay.setText("发起人买单");
//                        mTvTaskPay.setText("Ta买单");
                        break;
                    case 2:
                        mTvTaskPay.setText("接收人买单");
//                        mTvTaskPay.setText("我买单");
                        break;
                    case 3:
                        mTvTaskPay.setText("AA制");
                        break;
                    default:
                        mTvTaskPay.setVisibility(View.GONE);
                        break;
                }
                if (mTaskInfoListBean.getOtherGo() == 1) {
                    Hope = "我接Ta";
                } else if (mTaskInfoListBean.getOtherGo() == 2) {
                    Hope = "Ta接我";
                } else {
                    Hope = "自由行";
                }
                break;
//            case 1://我发布的
//                switch (otherBuy) {
//                    case 1:
//                        mTvTaskPay.setText("我买单");
//                        break;
//                    case 2:
//                        mTvTaskPay.setText("Ta买单");
//                        break;
//                    case 3:
//                        mTvTaskPay.setText("AA制");
//                        break;
//                    default:
//                        mTvTaskPay.setVisibility(View.GONE);
//                        break;
//                }
//                if (bean.getOtherGo() == 1) {
//                    Hope = "我接Ta";
//                } else if (bean.getOtherGo() == 2) {
//                    Hope = "Ta接我";
//                } else {
//                    Hope = "自由行";
//                }
//                break;
            default:
                Hope = "";
                break;
        }

        String ageStr;
        if (minAge == 0 || maxAge == 0) {
            ageStr = "年龄不限";
        } else if (minAge == 60 || maxAge == 60) {
            ageStr = "60岁以上";
        } else {
            ageStr = minAge + "-" + maxAge + "岁";
        }
        mTvTaskExpect.setText(ageStr + "  " + mSex + "  " + Hope);

        //任务状态（全部为空1发布中2进行中3待评价4已完成5已关闭）
        if (mPublishType == 1) {
            mTaskStatus = mTaskInfoListBean.getTaskStatus();
        } else {
            switch (mTaskInfoListBean.getTaskStatus()) {
                case 1:
                case 5:
                case 6:
                case 7:
                    mTaskStatus = mTaskInfoListBean.getTaskStatus();
                    break;
                default:
                    mTaskStatus = Integer.parseInt(mTaskInfoListBean.getTaskOtherStatus());
                    break;
            }
        }
        switch (mTaskStatus) {
            case 1://发布中
                if (mPublishType == 1) {
                    isBottomColumn(5);
                } else {
                    isBottomColumn(0);
                }
                break;
            case 2://进行中
                /**买单（1我买单2他买单3AA制）
                 int otherBuy = bean.getOtherBuy();*/
                if (mPublishType == 1) {
                    switch (otherBuy) {
                        case 1:
                            isBottomColumn(1);
                            break;
                        case 2:
                            isBottomColumn(2);
                            break;
                        case 3:
                            isBottomColumn(1);
                            break;
                    }
                } else {
                    switch (otherBuy) {
                        case 1:
                            isBottomColumn(2);
                            break;
                        case 2:
                            isBottomColumn(1);
                            break;
                        case 3:
                            isBottomColumn(1);
                            break;
                    }
                }
                break;
            case 3://待评价
                isBottomColumn(3);
                break;
            case 4://已完成
            case 6://待评价
            case 7://待评价
                isBottomColumn(6);
                break;
            case 5://已取消
                isBottomColumn(6);
                break;
            case 21://代付款
                LogUtil.d("代付款");
                if (mPublishType == 1) {
                    switch (otherBuy) {
                        case 1:
                            isBottomColumn(1);
                            break;
                        case 2:
                            isBottomColumn(2);
                            break;
                        case 3:
                            isBottomColumn(1);
                            break;
                    }
                } else {
                    switch (otherBuy) {
                        case 1:
                            isBottomColumn(2);
                            break;
                        case 2:
                            isBottomColumn(1);
                            break;
                        case 3:
                            isBottomColumn(1);
                            break;
                    }
                }
                break;
            case 31://待评价
                LogUtil.d("待评价");
                isBottomColumn(3);
                break;
            case 41://已取消
                LogUtil.d("已取消");
                isBottomColumn(6);
                break;
        }

        //1. 我发的 0.TA发的
        if (mPublishType == 1) {
            mOtherUserPhone = mTaskInfoListBean.getOtherUserPhone();
            mOtherUserName = mTaskInfoListBean.getOthernickName();
            mOtherNickname = mTaskInfoListBean.getOthernickName();
        } else {
            mOtherNickname = mTaskInfoListBean.getNickName();
            mOtherUserPhone = mTaskInfoListBean.getUserPhone();
            mOtherUserName = mTaskInfoListBean.getNickName();
        }
        beanId = mTaskInfoListBean.getId();
        mSellerInfoId = mTaskInfoListBean.getSellerInfoId();
        mNickName = mTaskInfoListBean.getNickName();
        mTaskDesc = mTaskInfoListBean.getTaskDesc();
        mTvUserName.setText(mNickName); //名字
        mSellerInfoName = mTaskInfoListBean.getSellerInfoName();
        mTaskMap.setText(mSellerInfoName);//地点
        mOrderId = mTaskInfoListBean.getOrderId();
        mOtherUserId = mTaskInfoListBean.getOtherUserId();
        mBeanUserId = mTaskInfoListBean.getUserId();
        mSellerBigName = mTaskInfoListBean.getSellerBigName();
        mSellerTypeImg = mTaskInfoListBean.getSellerTypeImg();

    }

    /**
     * 点击事件
     */
    @OnClick({R.id.toolbar, R.id.civ_user_avatar, R.id.iv_task_more, R.id.seller_success_pingjia, R.id.bt_gotothishouse,
            R.id.bt_gotothishouse1, R.id.task_mejoin,
            R.id.ll_sellermeassage,
            R.id.contacthim, R.id.task_telephone, R.id.task_mi_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar://关闭界面
                finish();
                break;
            case R.id.civ_user_avatar://头像跳转界面
                if (1 == mPublishType) {
                    MiSquareActivity.actionShow(mActivity);
                } else {
                    mIntent = new Intent(mActivity, TaSquareActivity.class);
                    mIntent.putExtra(Conversion.USERID, mBeanUserId);
                    startActivity(mIntent);
                }
                break;
            case R.id.iv_task_more://举报界面
                showPopwindows();
                break;
            case R.id.seller_success_pingjia:
                LogUtil.d("点击去评价按钮");
                mIntent = new Intent(mActivity, SellerSuccessActivity.class);
                mIntent.putExtra(Conversion.TASKID, beanId + "");
                mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId + "");
                mIntent.putExtra(Conversion.SELLERNMAE, mSellerInfoName + "");
                mIntent.putExtra(Conversion.OTHERSRNMAE, mOtherNickname + "");
                mIntent.putExtra(Conversion.USERID, mOtherUserId + "");
                mIntent.putExtra(Conversion.ORDERID, mOrderId + "");
                startActivity(mIntent);
                break;
            case R.id.task_mejoin:
                if (mTaskInfoList != null) {
                    isHend();
                }
                break;
            case R.id.task_mi_pay:
                mIntent = new Intent(this, PaythebillActivity.class);
                mIntent.putExtra(Conversion.TASKID, beanId + "");
                mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId + "");
                mIntent.putExtra(Conversion.SELLERNMAE, mSellerInfoName + "");
                mIntent.putExtra(Conversion.OTHERSRNMAE, mOtherNickname + "");
                mIntent.putExtra(Conversion.USERID, mOtherUserId + "");
                mIntent.putExtra(Conversion.ORDERID, mOrderId + "");
                mIntent.putExtra(Conversion.TYPE, "2");
                startActivity(mIntent);
                break;
            case R.id.contacthim://联系他
                ChatHuanXinActivity.actionShow(mActivity, mOtherUserPhone, mOtherUserName);
                break;
            case R.id.task_telephone://联系他
                ChatHuanXinActivity.actionShow(mActivity, mOtherUserPhone, mOtherUserName);
                break;
            case R.id.bt_gotothishouse://还去这家
            case R.id.bt_gotothishouse1://还去这家
                mIntent = new Intent(mActivity, TaskActivity.class);
                mIntent.putExtra(Conversion.SELLERINFOID, mSellerInfoId + "");
                mIntent.putExtra(Conversion.SELLERBIGNAME, mSellerTypeImg); //图标
                mIntent.putExtra(Conversion.SELLERINFONAME, mSellerInfoName);//名字
                startActivity(mIntent);
                break;
            case R.id.ll_sellermeassage:
                //beanId
                mIntent = new Intent(mActivity, SellerMessageActivity.class);
                mIntent.putExtra(Conversion.ID, String.valueOf(mSellerInfoId));
                mIntent.putExtra(Conversion.BOOLEAN, false);
                startActivity(mIntent);
                break;
        }
    }

    private void isHend() {
        RetrofitUtil.createService(TaskService.class)
                .userIsHead(SpUtils.getUserToken(mActivity))
                .enqueue(new RetrofitCallback<SuccessfulBean>() {
                    @Override
                    public void onSuccess(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean successfulBean = response.body();
                            if (successfulBean.getResultCode() == 1) {
                                if (!TextUtils.isEmpty(successfulBean.getIsHead())) { //不为空
                                    if ("1".equals(successfulBean.getIsHead())) { //不通过
                                        mDialogFragment = new ImageHendDialogFragment(mActivity, mActivity, 1);
                                        mDialogFragment.show(getSupportFragmentManager(), "1");
                                    } else if ("0".equals(successfulBean.getIsHead())) { //审核中
                                        mDialogFragment = new ImageHendDialogFragment(mActivity, mActivity, 2);
                                        mDialogFragment.show(getSupportFragmentManager(), "2");
                                    } else if ("2".equals(successfulBean.getIsHead())) { //通过
                                        showJoinConfirmDialog();
                                    }
                                } else {
                                    showJoinConfirmDialog();
                                }
                            } else if (successfulBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (successfulBean.getResultCode() == 0) {
                                Toasty.normal(mActivity, successfulBean.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {

                    }
                });
    }

    String type = "";
    String orderId = "";

    @Override
    protected void onResume() {
        super.onResume();
        isNonShowGestureLock = true;
        type = getIntent().getStringExtra(Conversion.TYPE);
        mTaskId = getIntent().getStringExtra(Conversion.ID);
        if (TextUtils.isEmpty(mTaskId)) {
            finish();
        }
        if (!TextUtils.isEmpty(mTaskId)) {
            if ("1".equals(type)) {
                String[] split = mTaskId.split(",");
                if (split.length > 0) {
                    // TODO: 2017/4/27 待确认 he韩总
                    if (split.length == 2) {
                        orderId = split[0];
                        mTaskId = split[1];
                    } else if (split.length == 1) {
                        type = "";
                        mTaskId = split[0];
                    }
                }
                initNetWork();
            } else {
                initNetWork();
            }
        } else {
            finish();
        }
    }

    private void showJoinConfirmDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("您是否要参加任务")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isJoin();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
//                        mDialogFragment = new ImageHendDialogFragment(mActivity, mActivity, 1);
//                        mDialogFragment.show(getSupportFragmentManager(), "1");
                    }
                })
                .show();
    }

    /**
     * 参加任务成功后发送一条环信消息
     */
    private void sendEmMessage() {
        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        String content = "hi，我报名了你的任务~很高兴认识你，我会准点到达，不见不散哦~";
        EMMessage message = EMMessage.createTxtSendMessage(content, mOtherUserPhone);
        //如果是群聊，设置chattype，默认是单聊
//        if (chatType == CHATTYPE_GROUP)
//            message.setChatType(ChatType.GroupChat);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 这个是我要参加的接口
     */
    private void isJoin() {
        mDialog.show();
        int id = mTaskInfoList.get(0).getId();
        RetrofitUtil.createService(TaskService.class)
                .updateTaskInfo(mUserToken, id + "", "1")
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            SuccessfulBean successfulBean = response.body();
                            if (successfulBean.getResultCode() == 1) {
                                sendEmMessage();
                                EventBus.getDefault().post(new TaskReleaseRefreshTaskAndMapList(true));
                                ChatHuanXinActivity.actionShow(mActivity, mOtherUserPhone, mOtherUserName);
                            } else if (successfulBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else if (successfulBean.getResultCode() == 0) {
                                Toasty.normal(mActivity, successfulBean.getResultDesc()).show();
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        mDialog.dismiss();
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 这个是任务详情底部显示的布局
     */
    public void isBottomColumn(int choice) {
        switch (choice) {
            case 0: //我要参加
                mTaskMejoin.setVisibility(View.VISIBLE);
                mTaskLinear1.setVisibility(View.GONE);
                mTaskLinear2.setVisibility(View.GONE);
                mTaskLinear3.setVisibility(View.GONE);
                mBtgotothishouse.setVisibility(View.GONE);
                break;
            case 2://Ta买单
                mTaskMejoin.setVisibility(View.GONE);
                mTaskLinear1.setVisibility(View.VISIBLE);
                mTaskLinear2.setVisibility(View.GONE);
                mTaskLinear3.setVisibility(View.GONE);
                mBtgotothishouse.setVisibility(View.GONE);
                break;
            case 3://再来一单
                mTaskMejoin.setVisibility(View.GONE);
                mTaskLinear1.setVisibility(View.GONE);
                mTaskLinear2.setVisibility(View.VISIBLE);
                mBtgotothishouse.setVisibility(View.GONE);
                mTaskLinear3.setVisibility(View.GONE);
                break;
            case 1://我买单
                mTaskMejoin.setVisibility(View.GONE);
                mTaskLinear1.setVisibility(View.GONE);
                mTaskLinear2.setVisibility(View.GONE);
                mTaskLinear3.setVisibility(View.VISIBLE);
                mBtgotothishouse.setVisibility(View.GONE);
                break;
            case 5://发布中
                mTaskMejoin.setVisibility(View.GONE);
                mTaskLinear1.setVisibility(View.GONE);
                mTaskLinear2.setVisibility(View.GONE);
                mTaskLinear3.setVisibility(View.GONE);
                mBtgotothishouse.setVisibility(View.GONE);
                break;
            case 6://还去这家
                mBtgotothishouse.setVisibility(View.VISIBLE);
                mTaskMejoin.setVisibility(View.GONE);
                mTaskLinear1.setVisibility(View.GONE);
                mTaskLinear2.setVisibility(View.GONE);
                mTaskLinear3.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 显示投诉框
     */
    private void showPopwindows() {
        final Popwindou pop = new Popwindou(this, UiUtils.inflate(mActivity, R.layout.activity_task_detail));
        View view = UiUtils.inflate(mActivity, R.layout.popwindows_more);
        pop.init(view, Gravity.BOTTOM, true);

        TextView tvShare = (TextView) view.findViewById(R.id.more_1);
        TextView tvReport = (TextView) view.findViewById(R.id.more_2);
        TextView tvCancelTask = (TextView) view.findViewById(R.id.more_3);
        TextView tvCancel = (TextView) view.findViewById(R.id.more_4);

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
                ShowShred();
            }
        });
        tvReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
                mIntent = new Intent(mActivity, ReportActivity.class);
                mIntent.putExtra("type", "4");
                mIntent.putExtra("reportId", mTaskId);
                startActivity(mIntent);
            }
        });
        tvCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
                showCancelTaskWarmingDialog();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });

        //任务状态（全部为空1发布中2进行中3待评价4已完成5已关闭）
        switch (mTaskStatus) {
            case 1:
                if (mPublishType == 1) {
                    tvReport.setVisibility(View.GONE);
                } else if (mPublishType == 0) {
                    tvCancelTask.setVisibility(View.GONE);
                }
                break;
            case 3:
            case 4:
            case 5:
                tvCancelTask.setVisibility(View.GONE);
                break;
        }
    }

    //显示分享底部分享栏目
    private void ShowShred() {
        String shareTitle = mTaskInfoListBean.getShareTitle();
        String shareContent = mTaskInfoListBean.getShareContent();
        if (TextUtils.isEmpty(shareTitle) && TextUtils.isEmpty(shareContent)) {
            Toasty.normal(mActivity, "该任务暂无分享内容").show();
            return;
        }
        UMImage image = null;
        if (!TextUtils.isEmpty(mImageHead)) {
            image = new UMImage(mActivity, UrlUtils.encode(IpConfig.HttpPic + mImageHead));//网络图片
        } else {
            if (mSex == 1) {
                image = new UMImage(mActivity, R.mipmap.ic_nan);
            } else if (mSex == 2) {
                image = new UMImage(mActivity, R.mipmap.ic_nv);
            }
        }
        String url = IpConfig.HttpPeiniIp + "share/taskDetail?taskId=" + mTaskId;
        UMWeb web = new UMWeb(url);
        web.setTitle(shareTitle);//标题
        web.setThumb(image);
        web.setDescription(shareContent);//描述
        new ShareUtils(mActivity, web);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(TaskEvaluationSuccess success) {
        if (success.isRefresh()) {
//            isBottomColumn(6);
            finish();
        }
    }

    private void showCancelTaskWarmingDialog() {
        mIntent = new Intent(mActivity, TaskCancelActivity.class);
        mIntent.putExtra("id", mTaskId);
        if (mTaskStatus == 1 && mPublishType == 1) {
            mIntent.putExtra("type", false);
        }
        startActivity(mIntent);
//        new AlertDialog.Builder(mActivity)
//                .setTitle("取消任务")
//                .setMessage("取消任务将降低您的信用值")
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                })
//                .show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    @Override
    public void SelectPhoto() { //选择照片
        mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mIntent.setType("image/*");
        isNonShowGestureLock = false;
        startActivityForResult(mIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void PhotoGraph() { // 拍照
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
//                    File file = new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "capture_" + System.currentTimeMillis() + ".jpg");
//                    if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
//                    tempUri = Uri.fromFile(file);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//                    } else {
//                        Uri imageUri = FileProvider.getUriForFile(mActivity, "com.jsz.peini.fileprovider", file);//通过FileProvider创建一个content类型的Uri
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
//                    }
//                    startActivityForResult(intent, RESULT_LOAD_IMAGE2);
            Intent intent = new Intent(mActivity,
                    UseCameraActivity.class);
            startActivityForResult(intent, RESULT_LOAD_IMAGE2);
            isNonShowGestureLock = false;
        } else {
            Toasty.normal(mActivity, "sdcard不可用").show();
        }
    }

    private boolean isNonShowGestureLock = true;

    @Override
    public boolean isGestureLock() {
        return isNonShowGestureLock;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            crop(selectedImage, false);
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && null != data) {
            String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
            String correction = Conversion.correctImage(extra);
            startPhotoZoom(new File(correction));
        }
        if (requestCode == PHOTO_REQUEST_CUT && resultCode == RESULT_OK && data != null) {
            final String path = mCroppedImageUri.getPath();
            if (mDialogFragment != null && mCroppedImageUri != null && StringUtils.isNoNull(path)) {
                mDialogFragment.setImageUrl(path);
                ExecutorService executorService = new PriorityExecutor(5, false);
                PriorityRunnable priorityRunnable = new PriorityRunnable(Priority.NORMAL, new Runnable() {
                    @Override
                    public void run() {
                        initUpdateUserImageHead(path);
                    }
                });
                executorService.execute(priorityRunnable);
            }

        }
    }

    private void initUpdateUserImageHead(final String bitmap) {
        final File file = new File(bitmap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RetrofitUtil.createService(SquareService.class)
                .updateUserImageHead(part, mUserToken)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean body = response.body();
                            if (body.getResultCode() == 1) {
                                if (!TextUtils.isEmpty(body.getImageUrl())) {
                                    SpUtils.setImageHead(mActivity, body.getImageUrl());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                    }
                });
    }

    /*
    * 剪切图片 相册选择
    */
    private void crop(Uri uri, boolean isAfterCapture) {
        if (uri == null) {
            LogUtil.i("alanjet", "The uri is not exist.");
        }
        LogUtil.d("tempUri" + uri);

        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        Uri imageUri;
        if (isAfterCapture && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && uri != null) {
            File file = new File(uri.getPath());
            imageUri = FileProvider.getUriForFile(mActivity, "com.jsz.peini.fileprovider", file);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            imageUri = uri;
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 800);
        intent.putExtra("aspectY", 800);
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("return-data", false);
        mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /**
     * 剪裁图片
     */
    private void startPhotoZoom(File file) {
        LogUtil.i("TAG", getImageContentUri(this, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(this, file), "image/*");//自己使用Content Uri替换File Uri
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 800);
            intent.putExtra("aspectY", 800);
            intent.putExtra("outputX", 800);
            intent.putExtra("outputY", 800);
            intent.putExtra("scale", true);
            intent.putExtra("scaleUpIfNeeded", true);
            intent.putExtra("return-data", false);
            mCroppedImageUri = Uri.fromFile(new File(Conversion.LOCAL_IMAGE_CACHE_PATH, "crop_" + System.currentTimeMillis() + ".jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImageUri);//定义输出的File Uri
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            intent.putExtra("noFaceDetection", true);
            startActivityForResult(intent, PHOTO_REQUEST_CUT);
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Your device doesn't support the crop action!";
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
}
