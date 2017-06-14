package com.jsz.peini.ui.activity.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.eventbus.TaskEvaluationSuccess;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/12/5.
 */
public class TaskCancelActivity extends BaseActivity {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.task_radiobutton1)
    RadioButton mTaskRadiobutton1;
    @InjectView(R.id.task_radiobutton2)
    RadioButton mTaskRadiobutton2;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.rg_cancel)
    RadioGroup mRgCancel;

    public String mCancleType = "";
    public TaskCancelActivity mActivity;
    public String mId;

    @Override
    public int initLayoutId() {
        return R.layout.activity_taskcancel;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        mTitle.setText("取消任务");
        mRightButton.setText("提交");
        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        boolean mIsIndividual = intent.getBooleanExtra(Conversion.TYPE, true);
        LogUtil.d("任务取消的id - " + mId);
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (!StringUtils.isNoNull(mId)) {
            LogUtil.d("进入错误id 未空" + mId);
            finish();
        }
        if (mIsIndividual) {
            mTaskRadiobutton1.setVisibility(View.VISIBLE);
        } else {
            mTaskRadiobutton1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initListener() {

        mRgCancel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.task_radiobutton1:
                        mCancleType = "2";
                        break;
                    case R.id.task_radiobutton2:
                        mCancleType = "1";
                        break;
                }
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCancleType.equals("")) {
                    Toasty.normal(mActivity, "选择取消的类型").show();
                    return;
                }

                if ("1".equals(mCancleType)) {
                    showCancelTaskWarmingDialog();
                } else {
                    initNetWork();
                }
            }
        });
    }

    private void showCancelTaskWarmingDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("警告")
                .setMessage("个人原因取消任务将扣除任务积分3分,是否确定?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initNetWork();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void initNetWork() {
        mDialog.show();
        LogUtil.d("取消任务的请求参数mUserToken = " + mUserToken + "mTaskId--" + mId + "mCancleType--" + mCancleType);
        RetrofitUtil.createService(TaskService.class)
                .cancelTaskInfo(mUserToken, mId, mCancleType)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        mDialog.dismiss();
                        if (response.isSuccessful()) {
                            SuccessfulBean successfulBean = response.body();
                            String toString = successfulBean.toString();
                            LogUtil.d("这个是取消的按钮" + toString);
                            if (successfulBean.getResultCode() == 1) {
                                EventBus.getDefault().post(new TaskEvaluationSuccess(true));
                                finish();
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

                        finish();
                    }
                });
    }
}
