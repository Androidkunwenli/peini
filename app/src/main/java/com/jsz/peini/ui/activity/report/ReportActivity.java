package com.jsz.peini.ui.activity.report;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.listener.ImageCompressListener;
import com.jsz.peini.model.question.QuestionEnterBean;
import com.jsz.peini.model.report.ReportModel;
import com.jsz.peini.presenter.question.QuestionService;
import com.jsz.peini.presenter.report.ReportService;
import com.jsz.peini.ui.adapter.seller.ReportAdapter;
import com.jsz.peini.ui.adapter.square.ReleasePhotoAdapter;
import com.jsz.peini.ui.view.ImageSelector.ImageLoader;
import com.jsz.peini.ui.view.ImageSelector.ImgSelActivity;
import com.jsz.peini.ui.view.ImageSelector.ImgSelConfig;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Bitmap.BitmapAndStringUtils;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.UseCameraActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends BaseActivity implements NonGestureLockInterface {
    private static final int RESULT_LOAD_IMAGE2 = 200;
    private static final int RESULT_LOAD_IMAGE = 100;
    @InjectView(R.id.back_btn)
    RelativeLayout backBtn;
    @InjectView(R.id.seller_select_accusation)
    TextView seller_select_accusation;
    @InjectView(R.id.report_yijian)
    EditText report_yijian;
    @InjectView(R.id.report_number)
    TextView report_number;
    @InjectView(R.id.report_recyclerview)
    RecyclerView mReportRecyclerview;
    @InjectView(R.id.image_numbar)
    TextView mImageNumbar;
    @InjectView(R.id.report_come)
    FrameLayout mReportCome;
    @InjectView(R.id.report_ok)
    LinearLayout mReportOk;
    @InjectView(R.id.button)
    Button mButton;
    @InjectView(R.id.activity_seller_report)
    LinearLayout mActivitySellerReport;
    @InjectView(R.id.seller_title_title)
    TextView mSellerTitleTitle;
    @InjectView(R.id.tv_title)
    TextView mTvTitle;
    @InjectView(R.id.tv_content)
    TextView mTvContent;
    private String content = ""; //举报内容
    private String reportId = ""; //被举报对象id
    private String reportCause = "";//举报原因
    public String mReportType;//举报类型

    private File mCaptureFile;
    /**
     * 举报
     */
    List<ReportModel.ReportReasonBean> mReportModels = new ArrayList<>();
    public ReportActivity mActivity;
    private Popwindou mPopwindou;
    /*图片的列表*/
    List<String> mList = new ArrayList<>();
    public ReleasePhotoAdapter mPhotoAdapter;
    public Popwindou mPop;
    public Intent mIntent;
    public MultipartBody.Part[] mParts;

    private boolean isNonShowGestureLock = true;

    @Override
    public int initLayoutId() {
        return R.layout.activity_seller_report;
    }

    @Override
    public void initView() {
        mActivity = this;
        isSuccessful(false);
        Intent intent = getIntent();
        mReportType = intent.getStringExtra("type");
        /**
         * 1问题反馈；用户举报；举报商家；任务举报；图片举报
         */
        switch (mReportType) {
            case "1":
                mSellerTitleTitle.setText("问题反馈");
                break;
            case "2":
                mSellerTitleTitle.setText("用户举报");
                mTvTitle.setText("举报成功");
                mTvContent.setText("举报成功！我们会尽快处理您的问题。");
                break;
            case "3":
                mSellerTitleTitle.setText("商家举报");
                mTvTitle.setText("举报成功");
                mTvContent.setText("举报成功！我们会尽快处理您的问题。");
                break;
            case "4":
                mSellerTitleTitle.setText("任务举报");
                mTvTitle.setText("举报成功");
                mTvContent.setText("举报成功！我们会尽快处理您的问题。");
                break;
            case "5":
                mSellerTitleTitle.setText("图片举报");
                mTvTitle.setText("举报成功");
                mTvContent.setText("举报成功！我们会尽快处理您的问题。");
                break;
        }
        reportId = intent.getStringExtra("reportId");

    }

    @Override
    public void initData() {
        initNetWork();
        initAddImage();
        //输入框的监听器
        report_yijian.addTextChangedListener(getWatcher());
    }


    /*选择图片*/
    private void initAddImage() {
        /**
         * 这个是加载图片的列表
         */
        mPhotoAdapter = new ReleasePhotoAdapter(mActivity, mList);
        final GridLayoutManager manager = new GridLayoutManager(mActivity, 4);
        mReportRecyclerview.setLayoutManager(manager);
        mReportRecyclerview.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnPhotoFootClickListener(new ReleasePhotoAdapter.OnPhotoFootClickListener() {
            @Override
            public void FootClickListener(int position) {
                initImageSelect();
                PeiNiUtils.getOffKeyset(mActivity);
            }

            @Override
            public void deleteListener(final int position) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("确定要删除照片吗?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mList.remove(position);
                                mPhotoAdapter.notifyItemRemoved(position);
                                mPhotoAdapter.notifyDataSetChanged();
                                initSetImageNumbar(mList);
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    /**
     * 图片选择
     */
    private void initImageSelect() {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_suare_release));
        View view = UiUtils.inflate(mActivity, R.layout.item_popupwindows_selete);
        mPop.init(view, Gravity.BOTTOM, true);
        view.findViewById(R.id.item_popupwindows_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //从相册选择
                isNonShowGestureLock = false;
                mPop.dismiss();
                isOpenPhoto();
            }
        });
        view.findViewById(R.id.item_popupwindows_Photo)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {  //拍照
                        /**
                         * 在启动拍照之前最好先判断一下sdcard是否可用
                         */
                        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
                        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
                            Intent intent = new Intent(mActivity,
                                    UseCameraActivity.class);
                            startActivityForResult(intent, RESULT_LOAD_IMAGE2);
                            isNonShowGestureLock = false;
                        } else {
                            Toast.makeText(mActivity, "sdcard不可用", Toast.LENGTH_SHORT).show();
                        }
                        mPop.dismiss();
                    }
                });
        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //取消
                mPop.dismiss();
            }
        });
    }

    /**
     * 打开相册
     */
    private void isOpenPhoto() {
        // 自由配置选项
        ImgSelConfig config = new ImgSelConfig.Builder(mActivity, new ImageLoader() {
            // 自定义图片加载器
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        }).multiSelect(true)
                // 第一个是否显示相机
                .needCamera(false)
                // 最大选择图片数量
                .maxNum(9)
                //选择图片还是上传图片
                .setType(2)
                //已选照片
                .setList(mList.size())
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, RESULT_LOAD_IMAGE);
    }

    @OnClick({R.id.back_btn, R.id.seller_select_accusation, R.id.activity_seller_report, R.id.seller_question, R.id.tv_button, R.id.ll_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.seller_select_accusation:
                /*这个是举报i界面的 弹窗选择按钮的*/
                if (mReportModels.size() > 0) {
                    initSelect();
                    PeiNiUtils.getOffKeyset(mActivity);
                } else {
                    Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                }
                break;
            case R.id.seller_question:
                /*这个是举报i界面的 弹窗选择按钮的*/
                if (!StringUtils.isNoNull(reportCause)) {
                    Toasty.normal(mActivity, "请您先选择举报原因").show();
                    return;
                }
                if (!StringUtils.isNoNull(content)) {
                    Toasty.normal(mActivity, "请您简要描述所遇到的问题").show();
                    return;
                }
                mDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initWork();
                    }
                }).start();
                break;
            case R.id.activity_seller_report:
                setClose(view);
                break;
            case R.id.tv_button:
                setClose(view);
                break;
            case R.id.ll_button:
                setClose(view);
                break;
        }
    }

    private void setClose(View view) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void initSelect() {
        mPopwindou = new Popwindou(this, UiUtils.inflate(mActivity, R.layout.activity_seller_report));
        View view = UiUtils.inflate(mActivity, R.layout.pop_recyclerview);
        mPopwindou.init(view, Gravity.BOTTOM, true);
        RecyclerView report_recyclerview = (RecyclerView) view.findViewById(R.id.report_recyclerview);
        report_recyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        ReportAdapter adapter = new ReportAdapter(mActivity, mReportModels);
        report_recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
            @Override
            public void ItemClic(int position) {
                mPopwindou.dismiss();
                ReportModel.ReportReasonBean reportReasonBean = mReportModels.get(position);
                mReportType = reportReasonBean.getTypeid() + "";
                reportCause = reportReasonBean.getResid() + "";
                seller_select_accusation.setText(reportReasonBean.getResname() + "");
                LogUtil.d("选择的那个条目-" + position + "--" + reportReasonBean.getResname());
            }
        });
        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopwindou.dismiss();
            }
        });
    }

    /**
     * 获取举报内容
     */
    private void initNetWork() {
        RetrofitUtil.createService(ReportService.class).getReportReasons(mReportType).enqueue(new Callback<ReportModel>() {
            @Override
            public void onResponse(Call<ReportModel> call, Response<ReportModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 1) {
                        LogUtil.d("这个是获取举报内容-成功" + response.body().toString());
                        mReportModels.clear();
                        mReportModels.addAll(response.body().getReportReason());
                    } else {
                        showNetErrorDialog();
                        LogUtil.d("这个是获取举报内容-失败" + response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportModel> call, Throwable t) {
                showNetErrorDialog();
            }
        });
    }

    /**
     * 点击按钮举报
     */
    private void initWork() {
        QuestionService service = RetrofitUtil.createService(QuestionService.class);
        Call<QuestionEnterBean> beanCall = null;
        mParts = new MultipartBody.Part[mList.size()];
        for (int i = 0; i < mParts.length; i++) {
            File file = new File(BitmapAndStringUtils.saveBitmap(mActivity, mList.get(i), 0));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
            mParts[i] = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        }
        if (mList.size() != 0) {
            beanCall = service.setQuestion(mUserToken, content, mReportType, reportId, reportCause, mParts);
        } else {
            beanCall = service.setQuestionNull(mUserToken, content, mReportType, reportId, reportCause);
        }
        if (beanCall == null) {
            return;
        }
        beanCall.enqueue(new Callback<QuestionEnterBean>() {
            @Override
            public void onResponse(Call<QuestionEnterBean> call, Response<QuestionEnterBean> response) {
                mDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 1) {
                        LogUtil.d("举报成功商家举报界面成功" + response.body().toString());
                        isSuccessful(true);
                    } else {
                        LogUtil.d("举报成功商家举报界面失败" + response.body().toString());
                        isSuccessful(false);
                        showReportNetErrorDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<QuestionEnterBean> call, Throwable t) {
                mDialog.dismiss();
                LogUtil.d("举报成功商家举报界面失败" + t.getMessage());
                isSuccessful(false);
                showReportNetErrorDialog();
            }
        });
    }

    /**
     * 成功
     *
     * @param b
     */
    private void isSuccessful(boolean b) {
        mReportCome.setVisibility(b ? View.GONE : View.VISIBLE);
        mReportOk.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_LOAD_IMAGE && null != data) {
            final ArrayList<String> list = data.getStringArrayListExtra(Conversion.LIST);
            for (String s : list) {
                if (mList.size() < 9) {
                    mList.add(s);
                    mPhotoAdapter.setPhotoList(mList);
                    initSetImageNumbar(mList);
                }
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE2 && null != data) {
            String extra = data.getStringExtra(UseCameraActivity.IMAGE_PATH);
            String correction = Conversion.correctImage(extra);
            mList.add(correction);
            if (StringUtils.isNoNull(correction)) {
                mPhotoAdapter.setPhotoList(mList);
                initSetImageNumbar(mList);
            }
        }
    }

    private void initSetImageNumbar(List<String> list) {
        mImageNumbar.setText(list.size() + "/" + 9);
    }

    @NonNull
    private TextWatcher getWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                report_number.setText((charSequence.length()) + "/" + "200");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                content = report_yijian.getText().toString().trim();
            }
        };
    }

    @OnClick(R.id.button)
    public void onClick() {
        finish();
    }

    private void showNetErrorDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("当前网络不可用，请检查您的设置")
                .setCancelable(false)
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initNetWork();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();
    }

    private void showReportNetErrorDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("温馨提示")
                .setMessage("当前网络不可用，请检查您的设置")
                .setCancelable(false)
                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                initWork();
                            }
                        }).start();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isNonShowGestureLock = true;
    }

    @Override
    public boolean isGestureLock() {
        return isNonShowGestureLock;
    }
}
