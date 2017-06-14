package com.jsz.peini.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.san.huanxin.HuanXinService;
import com.jsz.peini.san.huanxin.HuanxinHeadBean;
import com.jsz.peini.service.FloatViewService;
import com.jsz.peini.utils.Bitmap.GlideImgManager;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 15089 on 2017/2/20.
 */

public class TaskMessageActivity extends BaseActivity {
    @InjectView(R.id.is_close)
    ImageView mIsClose;
    @InjectView(R.id.id_refuse)
    TextView mIdRefuse;
    @InjectView(R.id.tv_agree)
    TextView mAgree;
    @InjectView(R.id.iv_image)
    CircleImageView mIvImage;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.tv_content)
    TextView mTvContent;
    @InjectView(R.id.tv_text)
    TextView mTvText;
    private TaskMessageActivity mActivity;
    public String mType;
    public String mTaskid;
    public String mPhone;
    private Intent mIntent;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    public int initLayoutId() {
        return R.layout.activity_taskmessage;
    }

    @Override
    public void initView() {
        mActivity = this;
        Intent intent = getIntent();
        mTaskid = intent.getStringExtra(Conversion.TASKID);
        mPhone = intent.getStringExtra(Conversion.PHONE);
//        mMtype = intent.getStringExtra(Conversion.TYPE);
        stopService(new Intent(mActivity, FloatViewService.class));
    }

    @Override
    public void initInternet() {
        RetrofitUtil.createService(HuanXinService.class)
                .getEmUserHeadAndNickname(mPhone)
                .enqueue(new Callback<HuanxinHeadBean>() {
                    @Override
                    public void onResponse(Call<HuanxinHeadBean> call, Response<HuanxinHeadBean> response) {
                        if (response.isSuccessful()) {
                            Message message = new Message();
                            message.what = Conversion.DATA_SUCCESS;
                            message.obj = response.body();
                            mHandler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onFailure(Call<HuanxinHeadBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();

                    }
                });
    }

    @OnClick({R.id.is_close, R.id.id_refuse, R.id.tv_agree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.is_close:
                mIntent = new Intent(mActivity, FloatViewService.class);
                mIntent.putExtra(Conversion.TASKID, mTaskid);
                mIntent.putExtra(Conversion.PHONE, mPhone);
                mIntent.putExtra("isshow", false);
                startService(mIntent);
                SpUtils.put(mActivity, Conversion.CANCELTASKNUMBER, mTaskid);
                SpUtils.put(mActivity, Conversion.CANCELPHONENUMBER, mTaskid);
                finish();
                break;
            case R.id.id_refuse://取消
                mType = "0";
                initNetWork();
                stopService(new Intent(mActivity, FloatViewService.class));
                SpUtils.remove(mActivity, Conversion.CANCELTASKNUMBER);
                SpUtils.remove(mActivity, Conversion.CANCELPHONENUMBER);
                finish();
                break;
            case R.id.tv_agree: //同意
                mType = "1";
                initNetWork();
                stopService(new Intent(mActivity, FloatViewService.class));
                SpUtils.remove(mActivity, Conversion.CANCELTASKNUMBER);
                SpUtils.remove(mActivity, Conversion.CANCELPHONENUMBER);
                finish();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Conversion.DATA_SUCCESS:
                    HuanxinHeadBean headBean = (HuanxinHeadBean) msg.obj;
                    if (headBean.getResultCode() == 1) {
                        HuanxinHeadBean.DataBean dataBean = headBean.getData().get(0);
                        mTvTitle.setText(dataBean.getNickName());
                        String url = IpConfig.HttpPic + dataBean.getHeadImg();
                        LogUtil.d("取消任务Bean---" + headBean.toString());
                        GlideImgManager.loadImage(mActivity, url, mIvImage, dataBean.getSex());
                    } else if (headBean.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    } else {
                        Toasty.normal(mActivity, headBean.getResultDesc()).show();
                    }
                    break;
            }

        }
    };

    /*cancelTaskInfo_result*/
    private void initNetWork() {
        RetrofitUtil.createService(TaskService.class)
                .cancelTaskInfoResult(mUserToken, mTaskid, mType)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean bean = response.body();
                            if (bean.getResultCode() == 1) {
                                Toasty.success(mActivity, bean.getResultDesc()).show();
                                finish();
                            } else if (bean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, bean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();

                    }
                });
    }
}
