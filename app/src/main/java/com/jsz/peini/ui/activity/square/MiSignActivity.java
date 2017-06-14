package com.jsz.peini.ui.activity.square;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.square.MiSignBean;
import com.jsz.peini.model.square.MiSignDataBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.getDayNumAdapter;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by th on 2017/2/9.
 */
public class MiSignActivity extends BaseActivity {
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.calendar)
    RecyclerView mCalendar;
    @InjectView(R.id.time)
    TextView mTime;
    String[] mStrings = new String[]{"日", "一", "二", "三", "四", "五", "六"};
    Time t = new Time();
    //未签到
    @InjectView(R.id.misign)
    TextView mMisign;
    //积分
    @InjectView(R.id.score)
    TextView mScore;
    //已签到文本连续签到
    @InjectView(R.id.continuitySign_text)
    TextView mContinuitySignText;
    //已签到
    @InjectView(R.id.continuitySign)
    LinearLayout mContinuitySign;
    @InjectView(R.id.ll_marks1)
    LinearLayout mLlMarks1;
    @InjectView(R.id.ll_marks2)
    LinearLayout mLlMarks2;
    @InjectView(R.id.ll_marks3)
    LinearLayout mLlMarks3;
    @InjectView(R.id.tv_marks1)
    TextView mTvMarks1;
    @InjectView(R.id.tv_marks2)
    TextView mTvMarks2;
    @InjectView(R.id.tv_marks3)
    TextView mTvMarks3;
    @InjectView(R.id.iv_backselect)
    ImageView mIvBackselect;
    private int year;
    private int month;
    private int day;
    private int week;
    private List<String> list = new ArrayList<String>();
    private ArrayList<HashMap<String, Object>> sinalist, alisttmp;

    private int dayMaxNum;
    private MiSignActivity mActivity;
    private int mMonthDate;
    private int mMonthWeeks;
    private getDayNumAdapter mDayNumAdapter;
    private MiSignDataBean mBody;
    private List<String> mSignList;
    private Drawable mIvBackselectDrawable;
    private String mDays;

    @Override
    public int initLayoutId() {
        return R.layout.activity_misign;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTitle.setText("本月签到");
        //取本地时间（时间应该从服务器获取）
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //返回键颜色转换
        mIvBackselectDrawable = mIvBackselect.getDrawable();
        mIvBackselect.setImageDrawable(Conversion.tintDrawable(mIvBackselectDrawable, ColorStateList.valueOf(Color.WHITE)));


        t.setToNow();
        year = t.year;
        month = t.month + 1;
        day = t.monthDay;
        week = t.weekDay;
        final String date = year + "-" + month + "-" + day;
        mTime.setText(year + "年" + month + "月");
        LogUtil.d(year + "-" + month + "-" + day + "-" + week);

        Date now = new Date();
        SimpleDateFormat myMonth = new SimpleDateFormat("MM");
        SimpleDateFormat myYear = new SimpleDateFormat("yyyy");
        int month = Integer.parseInt(myMonth.format(now));
        int year = Integer.parseInt(myYear.format(now));
        mMonthDate = monthDate(year, month);
        mMonthWeeks = monthWeeks();
        System.out.println("本月有多少天" + mMonthDate);//本月有多少天
        System.out.println("本月第一天是星期几" + mMonthWeeks);//本月第一天是星期几
    }

    @Override
    public void initData() {
        list.clear();
        for (int i = 0; i < mStrings.length; i++) {
            list.add(mStrings[i]);
        }
        //此处是用处：每个月1号如果不是周一的话，若其假设其为周三，就用2个元素占下集合中的前两位，让1号对应到相应周数。
        for (int i = 0; i < mMonthWeeks; i++) {
            list.add("");
        }
        for (int i = 1; i <= mMonthDate; i++) {
            list.add("0");
        }
        mCalendar.setLayoutManager(new GridLayoutManager(mActivity, 7));
        mDayNumAdapter = new getDayNumAdapter(mActivity, list, mStrings.length, mMonthWeeks, sinalist, day);
        mCalendar.setAdapter(mDayNumAdapter);
    }

    @Override
    public void initInternet() {
        initNetWork();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Conversion.DATASUCCESS:
//                    MiSignDataBean bean = (MiSignDataBean) msg.obj;
                    mDialog.dismiss();
                    if (mBody.getResultCode() == 1) {
                        mSignList = mBody.getSignList();
                        list.clear();
                        for (int i = 0; i < mStrings.length; i++) {
                            list.add(mStrings[i]);
                        }
                        //此处是用处：每个月1号如果不是周一的话，若其假设其为周三，就用2个元素占下集合中的前两位，让1号对应到相应周数。
                        for (int i = 0; i < mMonthWeeks; i++) {
                            list.add("");
                        }
                        for (int i = 0; i < mSignList.size(); i++) {
                            list.add(mSignList.get(i));
                        }
                        mDayNumAdapter.notifyDataSetChanged();
                        if (mBody != null) {
                            initShowView();
                        }
                        LogUtil.d("本月签到---" + mBody.toString());
                    } else if (mBody.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else {
                        mDialog.dismiss();
                        finish();
                    }
                    break;
            }
        }
    };

    /*得到一张现金券*/
    private void showPop(MiSignBean bean) {
        final Popwindou popwindou = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_misign));
        View view = UiUtils.inflate(mActivity, R.layout.activity_misign_pop);
        popwindou.init(view, Gravity.CENTER, true);
        TextView dayAward = (TextView) view.findViewById(R.id.dayAward);
        TextView tvSignNumbar = (TextView) view.findViewById(R.id.tv_sign_numbar);
        tvSignNumbar.setText("您已连续签到" + (TextUtils.isEmpty(mDays) ? 0 : (Integer.parseInt(mDays) + 1)) + "天");
        dayAward.setText(bean.getDayAward());

        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popwindou.dismiss();
            }
        });

    }

    private void initNetWork() {
        mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .getUserSignInfo(mUserToken, year + "-" + month + "-" + "1", year + "-" + month + "-" + mMonthDate)
                .enqueue(new RetrofitCallback<MiSignDataBean>() {
                    @Override
                    public void onSuccess(Call<MiSignDataBean> call, Response<MiSignDataBean> response) {
                        if (response.isSuccessful()) {
                            mBody = response.body();
                            Message message = new Message();
                            message.what = Conversion.DATASUCCESS;
                            message.obj = mBody;
                            mHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Call<MiSignDataBean> call, Throwable t) {
                        mDialog.dismiss();
                        finish();
                    }
                });
    }

    /*填充数据*/
    private void initShowView() {
        //是否显示签到
        String s = mSignList.get(day - 1);
        if (s.equals("1")) {
            mMisign.setVisibility(View.GONE);
            mContinuitySign.setVisibility(View.VISIBLE);
        } else {
            mMisign.setVisibility(View.VISIBLE);
            mContinuitySign.setVisibility(View.GONE);
        }
        //积分
        String Score = mBody.getScore() + "积分";
        mScore.setText(Score);

        //连续签到
        mDays = mBody.getSignDays();
        mContinuitySignText.setText("连续" + mDays + "天");

        //签到奖励
        List<String> marks = mBody.getMarks();
        switch (marks.size()) {
            case 1:
                mLlMarks1.setVisibility(View.VISIBLE);
                mTvMarks1.setText(marks.get(0));
                break;
            case 2:
                mLlMarks1.setVisibility(View.VISIBLE);
                mLlMarks2.setVisibility(View.VISIBLE);
                mTvMarks1.setText(marks.get(0));
                mTvMarks2.setText(marks.get(1));
                break;
            case 3:
                mLlMarks1.setVisibility(View.VISIBLE);
                mLlMarks2.setVisibility(View.VISIBLE);
                mLlMarks3.setVisibility(View.VISIBLE);
                mTvMarks1.setText(marks.get(0));
                mTvMarks2.setText(marks.get(1));
                mTvMarks3.setText(marks.get(2));
                break;
            default:
                if (marks.size() >= 3) {
                    mLlMarks1.setVisibility(View.VISIBLE);
                    mLlMarks2.setVisibility(View.VISIBLE);
                    mLlMarks3.setVisibility(View.VISIBLE);
                    mTvMarks1.setText(marks.get(0));
                    mTvMarks2.setText(marks.get(1));
                    mTvMarks3.setText(marks.get(2));
                } else {
                    mLlMarks1.setVisibility(View.GONE);
                    mLlMarks2.setVisibility(View.GONE);
                    mLlMarks3.setVisibility(View.GONE);

                }
                break;
        }

    }

    /**
     * 访问网络
     */
    private void initNetSignWork() {
        mDialog.show();
        RetrofitUtil.createService(SquareService.class)
                .insertSignInfo(mUserToken, year + "-" + month + "-" + day)
                .enqueue(new RetrofitCallback<MiSignBean>() {
                    @Override
                    public void onSuccess(Call<MiSignBean> call, Response<MiSignBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            MiSignBean bean = response.body();
                            if (bean.getResultCode() == 1) {
                                initNetWork();
                                if (!TextUtils.isEmpty(bean.getDayAward())) {
                                    showPop(bean);
                                    return;
                                }
                                Toasty.success(mActivity, bean.getResultDesc()).show();
                            } else if (bean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, bean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MiSignBean> call, Throwable t) {
                        mDialog.dismiss();
                        finish();
                    }
                });
    }

    /*获取周几*/
    public int monthWeeks() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("E");
        String format1 = format.format(calendar1.getTime());
        if (format1.contains("一")) {
            return 1;
        } else if (format1.contains("二")) {
            return 2;
        } else if (format1.contains("三")) {
            return 3;
        } else if (format1.contains("四")) {
            return 4;
        } else if (format1.contains("五")) {
            return 5;
        } else if (format1.contains("六")) {
            return 6;
        } else if (format1.contains("日")) {
            return 0;
        } else {
            return 0;
        }

    }

    /*获取月数的多少天*/
    public int monthDate(int year, int month) {
        int days = 0;
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

    @OnClick({R.id.misign, R.id.integralall})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.misign:
                initNetSignWork();
                break;
            case R.id.integralall:
                Intent intent = new Intent(mActivity, ExchangeActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIvBackselect.setImageDrawable(Conversion.tintDrawable(mIvBackselectDrawable, ColorStateList.valueOf(Color.BLACK)));
    }

}
