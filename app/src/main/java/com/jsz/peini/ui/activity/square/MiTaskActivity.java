package com.jsz.peini.ui.activity.square;

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
import android.support.design.widget.TabLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.jsz.peini.R;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.MyTaskAllBean;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.san.huanxin.activity.ChatHuanXinActivity;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.pay.PaythebillActivity;
import com.jsz.peini.ui.activity.task.SellerSuccessActivity;
import com.jsz.peini.ui.activity.task.TaskActivity;
import com.jsz.peini.ui.activity.task.TaskCancelActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.ui.adapter.seller.ReportAdapter;
import com.jsz.peini.ui.adapter.square.ScreenAdapter;
import com.jsz.peini.ui.fragment.square.MiTaskRecycleerViewAdapter;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.widget.ImageHendDialogFragment;
import com.jsz.peini.widget.ProgressActivity;
import com.jsz.peini.widget.UseCameraActivity;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import butterknife.InjectView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/24.
 */
public class MiTaskActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, ImageHendDialogFragment.SelectPhotoMonitorDialogListener, NonGestureLockInterface {
    private int PHOTO_REQUEST_CUT = 100;//截取图片
    private int RESULT_LOAD_IMAGE2 = 200; //拍照
    private int RESULT_LOAD_IMAGE = 300; //选择图片
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;
    @InjectView(R.id.tlayout)
    TabLayout mTlayout;
    //    @InjectView(R.id.vp)
//    ViewPager mVp;
    public MiTaskActivity mActivity;
    public List<String> mListTitle;
    public MiTaskRecycleerViewAdapter mAdapter;
    public Intent mIntent;
    @InjectView(R.id.iv_toolbar_image)
    ImageView mIvToolbarImage;
    @InjectView(R.id.kuntoolbar)
    RelativeLayout mKuntoolbar;
    @InjectView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @InjectView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @InjectView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @InjectView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress;
    @InjectView(R.id.tv_regCnt_finishRatio)
    TextView mTvRegCntFinishRatio;
    @InjectView(R.id.task_screen)
    TextView mTaskScreen;
    private ArrayList<MyTaskAllBean.TaskInfoByUserIdListBean> mList;
    private ArrayList<String> mListBeen;
    private Popwindou mPop;
    private RecyclerView mReportRecyclerview;
    private String mTaskStatus;
    private int pageNumber = 1;
    private String mSortm = "";
    private String taskInfoByUserIdListBeanTaskStatus;
    private int mPublishType;
    private ImageHendDialogFragment mDialogFragment;
    private Uri mCroppedImageUri;
    private int mPosition = 0;

    @Override
    public int initLayoutId() {
        return R.layout.activity_mitaskactivity;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        mTitle.setText("我的任务");
        mRightButton.setText("发布");
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListTitle = new ArrayList<>();
        /*标题*/
        mListTitle.add("全部");
        mListTitle.add("发任务");
        mListTitle.add("接任务");
        mTlayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mListTitle.size(); i++) {
            mTlayout.addTab(mTlayout.newTab().setText(mListTitle.get(i)));
        }
        mTlayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPosition = tab.getPosition();
                if (0 == mPosition) {
                    mSortm = "";
                } else if (1 == mPosition) {
                    mSortm = "1";
                } else if (2 == mPosition) {
                    mSortm = "2";
                }
                mTaskStatus = "";
//                mSwipeTarget.setVisibility(View.GONE);
                initNetWork(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SpUtils.isCompleteUserInfo(mActivity)) {
                    isHend();
                } else {
                    CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
                }
            }
        });

    }

    @Override
    public void initData() {
        mList = new ArrayList<>();
        mListBeen = new ArrayList<>();
        mListBeen.add("全部");
        mListBeen.add("发布中");
        mListBeen.add("进行中");
        mListBeen.add("已完成");
        mListBeen.add("已取消");
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new MiTaskRecycleerViewAdapter(mActivity, mList);
        mSwipeTarget.setAdapter(mAdapter);
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);
        mAdapter.setOnLongClickListener(new MiTaskRecycleerViewAdapter.OnLongClickListener() {
            @Override
            public void onLongClickItem(int positon, int id) {
                MyTaskAllBean.TaskInfoByUserIdListBean taskInfoByUserIdListBean = mList.get(positon);
                taskInfoByUserIdListBeanTaskStatus = taskInfoByUserIdListBean.getTaskStatus();
                mPublishType = taskInfoByUserIdListBean.getPublishType();
                switch (taskInfoByUserIdListBeanTaskStatus) {
                    case "1"://发布中
                        popDelete(positon, id, "取消任务");
                        break;
                    case "2"://进行中
                    case "21"://进行中
                        popDelete(positon, id, "取消任务");
                        break;
                    case "4":
                    case "6":
                    case "7"://已完成
                    case "41":
                        popDelete(positon, id, "删除任务");
                        break;
                    case "5"://已取消
                        popDelete(positon, id, "删除任务");
                        break;

                }
            }

//            @Override
//            public void onScreenClickItem() {
//                initPopWindows();
//            }

            //条目的点击事件
            @Override
            public void onClickItem(int position, int id) {
                mIntent = new Intent(mActivity, TaskDetailActivity.class);
                mIntent.putExtra(Conversion.ID, id + "");
                startActivity(mIntent);
            }

            //跳转到聊天界面
            @Override
            public void onChatClickItem(int position, String mPhone, String nickname) {
//                mIntent = new Intent(mActivity, ChatHuanXinActivity.class);
//                mIntent.putExtra(EaseConstant.EXTRA_USER_ID, mPhone);
//                startActivity(mIntent);
                ChatHuanXinActivity.actionShow(mActivity, mPhone, nickname);
            }

            @Override
            public void onTaskStatusClickItem(int position, int mId, int sellerInfoId, String sellerInfoName, String otherNickName, String otherUserId, String orderId) {
                mIntent = new Intent(mActivity, PaythebillActivity.class);
                mIntent.putExtra(Conversion.TYPE, "2");
                mIntent.putExtra(Conversion.TASKID, String.valueOf(mId));
                mIntent.putExtra(Conversion.SELLERINFOID, String.valueOf(sellerInfoId));
                mIntent.putExtra(Conversion.SELLERNMAE, sellerInfoName);
                mIntent.putExtra(Conversion.OTHERSRNMAE, otherNickName);
                mIntent.putExtra(Conversion.USERID, otherUserId);
                mIntent.putExtra(Conversion.ORDERID, orderId);
                startActivity(mIntent);
            }

            @Override
            public void onTaskEvaluateClickItem(int position, int id, int SellerInfoId, String sellerInfoName, String otherNickName, String otherUserId, String OrderId) {
                LogUtil.d("点击去评价按钮");
                mIntent = new Intent(mActivity, SellerSuccessActivity.class);
                mIntent.putExtra(Conversion.TASKID, id + "");
                mIntent.putExtra(Conversion.SELLERINFOID, SellerInfoId + "");
                mIntent.putExtra(Conversion.SELLERNMAE, sellerInfoName);
                mIntent.putExtra(Conversion.OTHERSRNMAE, otherNickName);
                mIntent.putExtra(Conversion.USERID, otherUserId);
                mIntent.putExtra(Conversion.ORDERID, OrderId);
                startActivity(mIntent);
            }

            @Override
            public void onTaskGotothishouseClickItem(int position, int sellerInfoId, String sellerInfoName, String sellerBigName) {
                mIntent = new Intent(mActivity, TaskActivity.class);
                mIntent.putExtra(Conversion.SELLERINFOID, String.valueOf(sellerInfoId));
                mIntent.putExtra(Conversion.SELLERINFONAME, sellerInfoName);
                mIntent.putExtra(Conversion.SELLERBIGNAME, sellerBigName);
                startActivity(mIntent);
            }
        });
    }

    private void initPopWindows() {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.activity_mitaskactivity));
        View view = UiUtils.inflate(mActivity, R.layout.pop_recyclerview);
        mPop.init(view, Gravity.BOTTOM, true);
        mReportRecyclerview = (RecyclerView) view.findViewById(R.id.report_recyclerview);
        mReportRecyclerview.setLayoutManager(new LinearLayoutManager(mActivity));
        ScreenAdapter adapter = new ScreenAdapter(mActivity, mListBeen);
        mReportRecyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new ReportAdapter.OnItemClickListener() {
            @Override
            public void ItemClic(int position) {
//              任务状态（全部为空1发布中2进行中3待评价4已完成5已关闭
//              mTaskStatus = ((position + "").equals("0") ? "" : position) + "";
                switch (position) {
                    case 0:
                        mTaskStatus = "";
                        break;
                    case 1:
                    case 2:
                        mTaskStatus = String.valueOf(position);
                        break;
                    case 3:
                    case 4:
                        mTaskStatus = String.valueOf(position + 1);
                        break;
                    default:
                        mTaskStatus = "";
                        break;
                }
                initNetWork(true);
                LogUtil.d("筛选" + position);
                mPop.dismiss();
            }
        });
        view.findViewById(R.id.item_popupwindows_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPop.dismiss();
            }
        });
    }

    @Override
    protected void initListener() {
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /*删除*/
    private void popDelete(final int positon, final int id, final String taskStatus) {
        mPop = new Popwindou(mActivity, UiUtils.inflate(mActivity, R.layout.fragment_mitaskall));
        View view = UiUtils.inflate(mActivity, R.layout.item_delete_recycleview);
        mPop.init(view, Gravity.CENTER, true);
        TextView delete = (TextView) view.findViewById(R.id.delete);
        delete.setText(taskStatus);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
                switch (taskStatus) {
                    case "删除任务":
                        deleteTask(positon, id, taskStatus);
                        break;
                    case "取消任务":
                        mIntent = new Intent(mActivity, TaskCancelActivity.class);
                        mIntent.putExtra("id", String.valueOf(id));
                        if ("1".equals(taskInfoByUserIdListBeanTaskStatus) && mPublishType == 1) {
                            mIntent.putExtra("type", false);
                        }
                        startActivity(mIntent);
                        break;
                }

            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
    }

    private void deleteTask(final int positon, int id, String taskStatus) {
        if (mList != null && mList.size() > 0) {
            mList.remove(positon);
            mAdapter.notifyDataSetChanged();
        }
        RetrofitUtil.createService(TaskService.class)
                .delTaskInfo(mUserToken, id)
                .enqueue(new Callback<SuccessfulBean>() {
                    @Override
                    public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                        if (response.isSuccessful()) {
                            SuccessfulBean bean = response.body();
                            if (bean.getResultCode() == 1) {
                                if (mList != null && mList.size() == 0) {
                                    mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "发个任务，让我来满足你的心愿~");
                                } else {
                                    mPaProgress.showContent();
                                }
                            } else if (bean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, bean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuccessfulBean> call, Throwable t) {
                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), Conversion.NETWORKERROR + "~");
                    }
                });
    }

    /**
     * 我的任务访问获取
     *
     * @param
     */
    private void initNetWork(final boolean ifFirst) {
        if (ifFirst) {
            pageNumber = 1;
        } else {
            pageNumber++;
        }
        RetrofitUtil.createService(SquareService.class)
                .getTaskInfoByUserId(mUserToken, mSortm, mTaskStatus, pageNumber, Conversion.PAGESIZE)
                .enqueue(new Callback<MyTaskAllBean>() {
                    @Override
                    public void onResponse(Call<MyTaskAllBean> call, Response<MyTaskAllBean> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        mSwipeTarget.setVisibility(View.VISIBLE);
                        if (response.isSuccessful()) {
                            MyTaskAllBean allBean = response.body();
                            int resultCode = allBean.getResultCode();
                            if (resultCode == 1) {
                                List<MyTaskAllBean.TaskInfoByUserIdListBean> taskInfoByUserIdList = allBean.getTaskInfoByUserIdList();
                                if (ifFirst) {
                                    mList.clear();
                                    mList.addAll(taskInfoByUserIdList);
                                } else {
                                    mList.addAll(taskInfoByUserIdList);
                                }
                                initOneView(allBean);
                                mAdapter.notifyDataSetChanged();
                                if (mList != null && mList.size() == 0) {
                                    if (0 == mPosition) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "发个任务，让我来满足你的心愿~");
                                    } else if (1 == mPosition) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "还没有发布过任务，发个任务试试吧~");
                                    } else if (2 == mPosition) {
                                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "还没有接过任务，接个任务试试吧~");
                                    }
                                } else {
                                    mPaProgress.showContent();
                                }

                            } else if (allBean.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, allBean.getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyTaskAllBean> call, Throwable t) {
                        mList.clear();
                        mAdapter.notifyDataSetChanged();
                        mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), Conversion.NETWORKERROR + "~");
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        if (mList != null && mList.size() == 0) {
                            mSwipeTarget.setVisibility(View.GONE);
                        } else {
                            mSwipeTarget.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void initOneView(MyTaskAllBean mBean) {
        if (mBean != null) {
            int regCnt = mBean.getCompleteness().getRegCnt();
            DecimalFormat df = new DecimalFormat(".##");
            float finishRatio = mBean.getCompleteness().getFinishRatio();
            String format = df.format(finishRatio);
            mTvRegCntFinishRatio.setText("任务状态(已参加任务" + regCnt + "个)");//,完成度" + format + "%)");
            mTaskScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initPopWindows();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        initNetWork(true);
    }

    @Override
    public void onLoadMore() {
        initNetWork(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        initNetWork(true);
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
                                        startActivity(new Intent(mActivity, TaskActivity.class));
                                    }
                                } else {
                                    startActivity(new Intent(mActivity, TaskActivity.class));
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
        if (state.equals(Environment.MEDIA_MOUNTED)) {
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
