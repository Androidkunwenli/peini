package com.jsz.peini.ui.activity.task;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.question.QuestionEnterBean;
import com.jsz.peini.presenter.question.QuestionService;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.UiUtils;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskReportActivity extends BaseActivity {

    @InjectView(R.id.back_btn)
    RelativeLayout backBtn;
    @InjectView(R.id.seller_select_accusation)
    TextView seller_select_accusation;
    @InjectView(R.id.report_yijian)
    EditText report_yijian;
    @InjectView(R.id.report_number)
    TextView report_number;
    @InjectView(R.id.seller_question)
    Button mSellerQuestion;
    private PopupWindow pop;
    private LinearLayout ll_popup;
    Bitmap bitmap;
    private View parentView;

    private String userId = "1"; //举报人id
    public String mContent = ""; //举报内容
    private String reportType = "1"; //举报类型
    private String reportId = "1"; //被举报对象id
    private String reportCause = "";//举报原因

    private Retrofit mRetrofit;
    public TaskReportActivity mActivity;


    @Override
    public int initLayoutId() {
        return R.layout.activity_seller_report;
    }

    @Override
    public void initView() {
        mActivity = this;
    }

    private void initWork() {
        RetrofitUtil.createService(QuestionService.class).setQuestionNull(userId, mContent, reportType, reportId, reportCause)
                .enqueue(new Callback<QuestionEnterBean>() {
                    @Override
                    public void onResponse(Call<QuestionEnterBean> call, Response<QuestionEnterBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResultCode() == 1) {
                                LogUtil.i("举报成功", "商家举报界面");
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionEnterBean> call, Throwable t) {
                    }
                });
    }


    private void initSelect() {
        Popwindou popwindou = new Popwindou(this, UiUtils.inflate(mActivity, R.layout.activity_seller_report));
        View view = UiUtils.inflate(mActivity, R.layout.pop_recyclerview);
        popwindou.init(view, Gravity.BOTTOM, true);

    }

    @Override
    public void initData() {
        //输入框的监听器
        report_yijian.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                report_number.setText((charSequence.length()) + "/" + "200");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mContent = report_yijian.getText().toString().trim();
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.seller_select_accusation, R.id.seller_question})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.seller_select_accusation:
                /*这个是举报i界面的 弹窗选择按钮的*/
                initSelect();
                break;
            case R.id.seller_question:
                /*这个举报的按钮*/
                initWork();
                break;
        }
    }


}
