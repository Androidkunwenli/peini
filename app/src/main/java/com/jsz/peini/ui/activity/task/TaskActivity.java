package com.jsz.peini.ui.activity.task;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jsz.peini.R;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.model.eventbus.SelectorSellerBean;
import com.jsz.peini.model.eventbus.TaskReleaseRefreshTaskAndMapList;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.KeyBoardUtils;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.ToastUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ImageHendDialogFragment;
import com.jsz.peini.widget.UseCameraActivity;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2016/12/7.
 */
public class TaskActivity extends BaseActivity implements ImageHendDialogFragment.SelectPhotoMonitorDialogListener, NonGestureLockInterface {
    @InjectView(R.id.task_sex_no)
    RadioButton taskSexNo;
    @InjectView(R.id.task_sex_man)
    RadioButton taskSexMan;
    @InjectView(R.id.task_sex_woman)
    RadioButton taskSexWoman;
    @InjectView(R.id.task_sex)
    RadioGroup taskSex;
    @InjectView(R.id.task_pay_mi)
    RadioButton taskPayMi;
    @InjectView(R.id.task_pay_you)
    RadioButton taskPayYou;
    @InjectView(R.id.task_pay_aa)
    RadioButton taskPayAa;
    @InjectView(R.id.task_pay)
    RadioGroup taskPay;
    @InjectView(R.id.task_goout_mi)
    RadioButton taskGooutMi;
    @InjectView(R.id.task_goout_you)
    RadioButton taskGooutYou;
    @InjectView(R.id.task_goout_like)
    RadioButton taskGooutLike;
    @InjectView(R.id.task_goout)
    RadioGroup taskGoout;
    @InjectView(R.id.task_describe)
    EditText task_describe; //输入的文本
    @InjectView(R.id.task_change)
    TextView task_change; //文本的监听
    @InjectView(R.id.task_address)
    TextView taskAddress;
    @InjectView(R.id.task_regist_address)
    LinearLayout taskRegistAddress;
    @InjectView(R.id.task_time)
    TextView taskTime;
    @InjectView(R.id.task_regist_time)
    LinearLayout taskRegistTime;
    @InjectView(R.id.task_age)
    TextView taskAge;
    @InjectView(R.id.task_regist_age)
    LinearLayout taskRegistAge;
    /**
     * 约会地址的类型
     */
    @InjectView(R.id.task_type)
    ImageView mTaskType;
    /**
     * 约会的具体位置
     */
    @InjectView(R.id.task_location)
    TextView mTaskLocation;
    /**
     * 约会的点击事件
     */
    @InjectView(R.id.task_rendezvous)
    LinearLayout mTaskRendezvous;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;


    public TaskActivity mActivity;
    /**
     * 约会信息  入参
     */
    private String mTaskDesc = "";//约会的信息
    private String mOtherSex = "3";//性别
    private String mOtherGo = "1";//我接她
    private String mOtherBuy = "1";//我买单
    private String misAge = ""; //小年龄
    private String maxAge = ""; //大年龄
    private String mSellerInfoId = "";//商家
    private String mTaskData = "";//时间


    public WheelView mWheelViewThree, mWheelViewOne, mWheelViewTow;
    //日期
    public ArrayList<String> mArrayList;
    public ArrayList<Integer> mArrayAgeList;

    public ArrayList<String> mArrayDateList;
    public ArrayList<Integer> mArrayHourList;
    public ArrayList<Integer> mArrayMinuteList;
    public Intent mIntent;

    public String mDate;
    public int mHour;
    public int mMinute;
    public Popwindou mPop;
    public int mMis;
    public int mMax;
    public String mAge;
    private int PHOTO_REQUEST_CUT = 100;//截取图片
    private int RESULT_LOAD_IMAGE2 = 200; //拍照
    private int RESULT_LOAD_IMAGE = 300; //选择图片
    private Uri mCroppedImageUri;
    private ImageHendDialogFragment mDialogFragment;

    @Override
    public int initLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    public void initView() {
        mActivity = TaskActivity.this;
        EventBus.getDefault().register(mActivity);
        mTitle.setText("任务发布");
        mRightButton.setText("发布");
        Intent intent = getIntent();
        mSellerInfoId = intent.getStringExtra(Conversion.SELLERINFOID);
        String sellerInfoName = intent.getStringExtra(Conversion.SELLERINFONAME);
        mTaskLocation.setText(TextUtils.isEmpty(sellerInfoName) ? "请选择" : sellerInfoName);
        String sellerBigName = intent.getStringExtra(Conversion.SELLERBIGNAME);
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + sellerBigName, mTaskType, "7");
    }

    @Override
    public void initData() {
        initEditText();
        SelectButton();
    }

    /**
     * 输入文本监听框
     */
    private void initEditText() {
        task_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                task_change.setText(editable.toString().length() + "/200");
            }
        });
    }

    /**
     * 选择框
     */
    private void SelectButton() {
        taskSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.task_sex_no:
                        LogUtil.i("任务自由行选择", "不限");
                        mOtherSex = "3";
                        break;
                    case R.id.task_sex_man:
                        mOtherSex = "1";
                        LogUtil.i("任务自由行选择", "男");
                        break;
                    case R.id.task_sex_woman:
                        mOtherSex = "2";
                        LogUtil.i("任务自由行选择", "女");
                        break;
                }
            }
        });
        taskGoout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.task_goout_like:
                        mOtherGo = "3";
                        LogUtil.i("任务自由行选择", "自由行");
                        break;
                    case R.id.task_goout_mi:
                        mOtherGo = "1";
                        LogUtil.i("任务自由行选择", "我接她");
                        break;
                    case R.id.task_goout_you:
                        mOtherGo = "2";
                        LogUtil.i("任务自由行选择", "你接我");
                        break;
                }
            }
        });
        taskPay.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.task_pay_aa:
                        mOtherBuy = "3";
                        LogUtil.i("任务买单选择", "AA制");
                        break;
                    case R.id.task_pay_you:
                        mOtherBuy = "2";
                        LogUtil.i("任务买单选择", "你买单");
                        break;
                    case R.id.task_pay_mi:
                        mOtherBuy = "1";
                        LogUtil.i("任务买单选择", "我买单");
                        break;
                }
            }
        });
    }

    private void initNetWork() {
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        RetrofitUtil.createService(TaskService.class).setTaskInfo(
                mUserToken, "3", "64", "" + mSellerInfoId, mTaskData, mTaskDesc, mOtherSex, misAge, maxAge,
                mOtherBuy, mOtherGo,
                SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity))
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            mDialog.dismiss();
                            mRightButton.setClickable(true);
                            SuccessfulBean successfulBean = response.body();
                            if (successfulBean.getResultCode() == 1) {
                                if (!TextUtils.isEmpty(successfulBean.getIsHead())) { //不为空
                                    if ("1".equals(successfulBean.getIsHead())) { //不通过
                                        mDialogFragment = new ImageHendDialogFragment(mActivity, mActivity, 1);
                                        mDialogFragment.show(getSupportFragmentManager(), "1");
                                        return;
                                    } else if ("0".equals(successfulBean.getIsHead())) { //审核中
                                        mDialogFragment = new ImageHendDialogFragment(mActivity, mActivity, 2);
                                        mDialogFragment.show(getSupportFragmentManager(), "2");
                                        return;
                                    } else if ("2".equals(successfulBean.getIsHead())) { //通过
                                        EventBus.getDefault().post(new TaskReleaseRefreshTaskAndMapList(true));
                                        Toasty.success(mActivity, successfulBean.getResultDesc()).show();
                                        finish();
                                    }
                                } else {
                                    EventBus.getDefault().post(new TaskReleaseRefreshTaskAndMapList(true));
                                    Toasty.success(mActivity, successfulBean.getResultDesc()).show();
                                    finish();

                                }
                            } else if (successfulBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, successfulBean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        mRightButton.setClickable(true);
                        mDialog.dismiss();
                        finish();
                    }
                });
    }

    @OnClick({R.id.toolbar, R.id.right_button, R.id.task_regist_address, R.id.task_regist_time, R.id.task_regist_age, R.id.task_rendezvous})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                finish();
                break;
            case R.id.right_button://发布按钮
                KeyBoardUtils.hideKeyBoard(mActivity, task_describe);
                publishTask();
                break;
            case R.id.task_regist_address:
                PopupWindowAddress();
                break;
            case R.id.task_regist_time:
                PoWindowTime(taskTime);
                break;
            case R.id.task_regist_age:
                PopupWindowAge(taskAge);
                break;
            /*约会商家选择的点击事件的点击事件*/
            case R.id.task_rendezvous:
                mIntent = new Intent(mActivity, SelectSellerActivity.class);
                mIntent.putExtra(Conversion.CHOICE, "Choice");
                startActivity(mIntent);
                break;
        }
    }

    private void publishTask() {
        mTaskDesc = task_describe.getText().toString().trim();
        if ("任务地点".equals(mTaskLocation.getText().toString().trim())) {
            Toasty.normal(mActivity, "请选择任务地点").show();
            return;
        }
        if (TextUtils.isEmpty(mSellerInfoId)) {
            Toasty.normal(mActivity, "请选择任务地点").show();
            return;
        }
        if ("任务时间".equals(mTaskData)) {
            Toasty.normal(mActivity, "请选择任务时间").show();
            return;
        }
        if (TextUtils.isEmpty(mTaskData)) {
            Toasty.normal(mActivity, "请选择任务时间").show();
            return;
        }
        if (TextUtils.isEmpty(mOtherSex)) {
            Toasty.normal(mActivity, "请选择希望的性别").show();
            return;
        }
        if (TextUtils.isEmpty(mOtherGo)) {
            Toasty.normal(mActivity, "请选择出行方式").show();
            return;
        }
        if (TextUtils.isEmpty(mOtherBuy)) {
            Toasty.normal(mActivity, "请选择谁来买单").show();
            return;
        }

        if (SpUtils.isCompleteUserInfo(mActivity)) {
            mRightButton.setClickable(false);
            initNetWork();
        } else {
            CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
        }

    }

    /**
     * 年龄选择
     *
     * @param taskAge
     */
    private void PopupWindowAge(final TextView taskAge) {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_task));
        View view = UiUtils.inflate(mActivity, R.layout.pop_tow_selector);
        mPop.init(view, Gravity.BOTTOM, true);

        view.findViewById(R.id.ok_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMis > mMax) {
                    Toasty.normal(mActivity, "开始年龄不能大于截止年龄或截止年龄不能小于起始年龄").show();
                    return;
                }
                if (mMis == 17 || mMax == 17) {
                    mAge = "不限";
                    misAge = "";
                    maxAge = "";
                } else if (mMis == 60 || mMax == 60) {
                    mAge = "60岁以上";
                    misAge = "60";
                    maxAge = "60";
                } else {
                    mAge = mMis + "岁 - " + mMax + "岁";
                    misAge = String.valueOf(mMis);
                    maxAge = String.valueOf(mMax);
                }
                taskAge.setText(mAge);
                mPop.dismiss();
            }
        });
        view.findViewById(R.id.cancel_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });

        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.backgroundf1f1f1);
        style.selectedTextSize = 20;
        style.textSize = 18;
        style.textAlpha = 0.5f;
        style.selectedTextZoom = 5;

        mWheelViewOne = (WheelView) view.findViewById(R.id.main_wheelview);
        mWheelViewOne.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewOne.setSkin(WheelView.Skin.Holo);
        mWheelViewOne.setWheelSize(5);
        mWheelViewOne.setWheelData(OneAge());
        mWheelViewOne.setStyle(style);

        mWheelViewTow = (WheelView) view.findViewById(R.id.sub_wheelview);
        mWheelViewTow.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewTow.setSkin(WheelView.Skin.Holo);
        mWheelViewTow.setWheelSize(5);
        mWheelViewTow.setWheelData(OneAge());
        mWheelViewTow.setStyle(style);
        mWheelViewOne.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                int onePosition = mWheelViewOne.getCurrentPosition();
                int twoPosition = mWheelViewTow.getCurrentPosition();
                if (position == 0) {
                    mWheelViewTow.smoothScrollToPosition(0);
                } else if (position < mArrayAgeList.size() - 1 && twoPosition == mArrayAgeList.size() - 1) {
                    mWheelViewTow.smoothScrollToPosition(mArrayAgeList.size() - 2);
                } else if (onePosition > twoPosition) {
                    mWheelViewTow.smoothScrollToPosition(position + 4);
                }
                mMis = mArrayAgeList.get(position);
                LogUtil.d("年龄" + mMis + "   " + o);
            }
        });
        mWheelViewTow.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                int onePosition = mWheelViewOne.getCurrentPosition();
                int twoPosition = mWheelViewTow.getCurrentPosition();
                if (position == mArrayAgeList.size() - 1) {
                    mWheelViewOne.smoothScrollToPosition(mArrayAgeList.size() + 4);
                } else if (onePosition > twoPosition) {
                    mWheelViewTow.smoothScrollToPosition(onePosition + 4);
                } else if (twoPosition > 0 && onePosition == 0) {
                    mWheelViewOne.smoothScrollToPosition(5);
                }
                mMax = mArrayAgeList.get(position);
                LogUtil.d("年龄" + mMax + "   " + o);
            }
        });
    }

    private List<String> OneAge() {
        mArrayList = new ArrayList<>();
        mArrayList.add("不限");
        for (int i = 18; i <= 59; i++) {
            mArrayList.add(i + "岁");
        }
        mArrayList.add("60以上");

        if (mArrayAgeList == null) {
            mArrayAgeList = new ArrayList<>();
            for (int i = 17; i <= 60; i++) {
                mArrayAgeList.add(i);
            }
        }

        return mArrayList;
    }

    /**
     * 时间选择
     *
     * @param taskTime
     */
    private void PoWindowTime(final TextView taskTime) {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_task));
        View view = UiUtils.inflate(mActivity, R.layout.pop_three_selector);
        mPop.init(view, Gravity.BOTTOM, true);
        view.findViewById(R.id.ok_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mWheelViewOne.getCurrentPosition() == 0) {
                    int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
                    if (mHour < currentHour || (mHour == currentHour && mMinute < currentMinute)) {
                        Toasty.normal(mActivity, "任务时间需大于当前时间").show();
                        return;
                    }
                }

                String hourStr = mHour < 10 ? "0" + mHour : "" + mHour;
                String minuteStr = mMinute < 10 ? "0" + mMinute : "" + mMinute;
                String yyyy = mYyyy.format(new Date());
                mTaskData = yyyy + "-" + mDate + " " + hourStr + ":" + minuteStr;
                taskTime.setText(mTaskData);
                mPop.dismiss();
            }
        });
        view.findViewById(R.id.cancel_selector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });


        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.backgroundf1f1f1);
        style.selectedTextSize = 20;
        style.textSize = 18;
        style.textAlpha = 0.5f;
        style.selectedTextZoom = 5;

        mWheelViewOne = (WheelView) view.findViewById(R.id.one_wheelview);
        mWheelViewOne.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewOne.setSkin(WheelView.Skin.Holo);
        mWheelViewOne.setWheelSize(5);
        mWheelViewOne.setWheelData(OneData());
        mWheelViewOne.setStyle(style);

        mWheelViewTow = (WheelView) view.findViewById(R.id.tow_wheelview);
        mWheelViewTow.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewTow.setSkin(WheelView.Skin.Holo);
        mWheelViewTow.setWheelSize(5);
        mWheelViewTow.setWheelData(getHoursData());
        mWheelViewTow.setStyle(style);

        mWheelViewThree = (WheelView) view.findViewById(R.id.three_wheelview);
        mWheelViewThree.setWheelAdapter(new ArrayWheelAdapter(this));
        mWheelViewThree.setSkin(WheelView.Skin.Holo);
        mWheelViewThree.setWheelSize(5);
        mWheelViewThree.setWheelData(getMinutesData());
        mWheelViewThree.setStyle(style);
        mWheelViewOne.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mDate = mArrayDateList.get(position);
                LogUtil.d("时间日期" + mDate);
            }
        });
        mWheelViewTow.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mHour = mArrayHourList.get(position);
                LogUtil.d("时间小时:" + mHour);
            }
        });
        mWheelViewThree.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                mMinute = mArrayMinuteList.get(position);
                LogUtil.d("时间分钟" + mMinute);
            }
        });


    }

    SimpleDateFormat mYyyy = new SimpleDateFormat("yyyy");
    SimpleDateFormat format = new SimpleDateFormat("MM-dd");
    Calendar c;

    private List OneData() {
        c = Calendar.getInstance();
        if (mArrayDateList == null) {
            mArrayDateList = new ArrayList<>();
        } else {
            mArrayDateList.clear();
        }
        mArrayList = new ArrayList<>();
        String str = format.format(new Date());
        mArrayList.add("今天" + str);
        mArrayDateList.add(str);

        c.add(Calendar.DAY_OF_MONTH, 1);
        String format = this.format.format(c.getTime());
        System.out.println("增加一天后日期 ：  " + format);
        mArrayList.add("明天" + format);
        mArrayDateList.add(format);

        c.add(Calendar.DAY_OF_MONTH, 1);
        String format1 = this.format.format(c.getTime());
        System.out.println("增加两天后日期 ：  " + format1);
        mArrayList.add("后天" + format1);
        mArrayDateList.add(format1);
        return mArrayList;
    }

    private List getHoursData() {
        mArrayList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            mArrayList.add(i + "点");
        }
        if (mArrayHourList == null) {
            mArrayHourList = new ArrayList<>();
            for (int i = 0; i < 24; i++) {
                mArrayHourList.add(i);
            }
        }
        return mArrayList;
    }

    private List getMinutesData() {
        mArrayList = new ArrayList<>();
        for (int i = 0; i < 60; i += 10) {
            mArrayList.add(i + "分");
        }
        if (mArrayMinuteList == null) {
            mArrayMinuteList = new ArrayList<>();
            for (int i = 0; i < 60; i += 10) {
                mArrayMinuteList.add(i);
            }
        }

        return mArrayList;
    }

    /**
     * 地址选择
     */
    private void PopupWindowAddress() {
        ToastUtils.ToastAddress(mActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mActivity);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectorSellerBean event) {
        if (event == null) {
            return;
        }
        String type = event.getTyge();
        String name = event.getName();
        mSellerInfoId = event.getId();
        GlideImgManager.loadImage(mActivity, IpConfig.HttpPic + type, mTaskType);
        mTaskLocation.setText(name);
        LogUtil.d("界面穿透式回调" + type + "name--" + name + "mSellerInfoId--" + mSellerInfoId);
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
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            crop(selectedImage, false);
//            startPhotoZoom(new File(selectedImage.getPath()));
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && null != data) {
            String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
            String correction = Conversion.correctImage(extra);
//            crop(Uri.parse(extra), true);
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
