package com.jsz.peini.ui.fragment;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.jsz.peini.R;
import com.jsz.peini.threadpool.Priority;
import com.jsz.peini.threadpool.PriorityExecutor;
import com.jsz.peini.threadpool.PriorityRunnable;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.eventbus.FilterReturnBean;
import com.jsz.peini.model.eventbus.LoodingData;
import com.jsz.peini.model.eventbus.LoodingTabulationFragMentBean;
import com.jsz.peini.model.eventbus.TaskReleaseRefreshTaskAndMapList;
import com.jsz.peini.model.eventbus.UnreadHuanXinMsgCountBean;
import com.jsz.peini.model.eventbus.ReceivedMessageBean;
import com.jsz.peini.model.search.LatLngBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.setting.UserSmsCntBean;
import com.jsz.peini.model.tabulation.TaskListBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.presenter.task.TaskService;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.news.MapNewsActivity;
import com.jsz.peini.ui.activity.square.OfficialActivity;
import com.jsz.peini.ui.activity.task.TaskActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.ui.adapter.task.TaskItemAdapter;
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
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by kunwe on 2016/11/29.
 * 列表的fragment
 */
public class TabulationFragment extends BaseFragment implements ImageHendDialogFragment.SelectPhotoMonitorDialogListener {
    private TabulationFragment mFragment = this;
    @InjectView(R.id.point_number)
    TextView mPointNumber; //消息的数量
    @InjectView(R.id.pa_progress)
    ProgressActivity mPaProgress; //空界面显示
    @InjectView(R.id.tabuiation_viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.task_setcurrentitem)
    LinearLayout mTaskSetcurrentitem;
    @InjectView(R.id.task_setcurrentitem2)
    LinearLayout mTaskSetcurrentitem2;
    @InjectView(R.id.map_button)
    Button mMapButton;
    private TaskItemAdapter mAdapter;
    private List<TaskListBean.TaskAllListBean> mTaskAllList = new ArrayList<>();
    private String mTaskCity = IpConfig.cityCode;
    private String mSort = "1";
    private String mOtherSex = "";
    private String mOtherLowAge = "";
    private String mOtherHignAge = "";
    private String mOtherLowheight = "";
    private String mOtherHignheight = "";
    private String mIsVideo = "";
    private String mIsIdcard = "";
    private String mSellerType = "";
    public Intent mIntent;

    private int mChatMessageCount = 0;
    private int mSystemMessageCount = 0;
    private ImageHendDialogFragment mDialogFragment;
    private Uri mCroppedImageUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册事件
        EventBus.getDefault().register(this);
    }

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_tabulation);
    }

    @Override
    public void initData() {
        mAdapter = new TaskItemAdapter(mActivity);
        mViewPager.setOffscreenPageLimit(2);//>=3
        mViewPager.setPageMargin(40);//设置page间间距，自行根据需求设置
        //setPageTransformer 决定动画效果
        mViewPager.setPageTransformer(true, new ScaleInTransformer());
        mViewPager.setAdapter(mAdapter);//写法不变
        mAdapter.setOnItemClickListener(new TaskItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, int id) {
                if (null != mTaskAllList && mTaskAllList.size() != 0) {
                    mIntent = new Intent(mActivity, TaskDetailActivity.class);
                    int value = mTaskAllList.get(position).getId();
                    mIntent.putExtra("id", value + "");
                    startActivity(mIntent);
                    LogUtil.d("列表点击的界面" + "第几个 -->  " + position + "\n id---" + value);
                }
            }
        });
    }

    private void initNetWork(final String xpoint, final String ypoint, final String sort, final String otherSex, final String otherLowAge, final String otherHignAge, final String otherLowheight, final String otherHignheight, final String isVideo, final String isIdcard, final String sellerType, final String city) {
        ExecutorService executorService = new PriorityExecutor(5, false);
        PriorityRunnable priorityRunnable = new PriorityRunnable(Priority.NORMAL, new Runnable() {
            @Override
            public void run() {
                getTabulationList(xpoint, ypoint, sort, otherSex, otherLowAge, otherHignAge, otherLowheight, otherHignheight, isVideo, isIdcard, sellerType, city);
            }
        });
        executorService.execute(priorityRunnable);
    }

    private void getTabulationList(String xpoint, String ypoint, String sort, String otherSex, String otherLowAge, String otherHignAge, String otherLowheight, String otherHignheight, String isVideo, String isIdcard, String sellerType, String city) {
        RetrofitUtil.createService(TaskService.class)
                .selectTaskInfoBy(
                        mUserToken,
                        "" + xpoint,
                        "" + ypoint,
                        "" + sort,
                        "" + otherSex,
                        "" + otherLowAge,
                        "" + otherHignAge,
                        "" + otherLowheight,
                        "" + otherHignheight,
                        "" + isVideo,
                        "" + isIdcard,
                        "" + sellerType,
                        "" + city)
                .enqueue(new Callback<TaskListBean>() {
                    @Override
                    public void onResponse(Call<TaskListBean> call, Response<TaskListBean> response) {
                        if (response.isSuccessful()) {
                            TaskListBean bean = response.body();
                            int resultCode = bean.getResultCode();
                            if (resultCode == 1) {
                                mTaskAllList = bean.getTaskAllList();
                                if (mTaskAllList.size() > 0) {
                                    mAdapter.setTaskAllList(mTaskAllList);
                                    mViewPager.setCurrentItem(mAdapter.getStartPageIndex(), true);
                                    mPaProgress.showContent();
                                } else {
                                    mPaProgress.showSquareNullData(getResources().getDrawable(R.drawable.data_error), "列表空空如也,快来发个任务吧!");
                                }
                            } else if (resultCode == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskListBean> call, Throwable t) {
                    }
                });
    }

    @OnClick({R.id.task_setcurrentitem, R.id.task_setcurrentitem2, R.id.map_button, R.id.bt_refresh, R.id.map_news, R.id.mi_task})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_setcurrentitem:
                if (null != mTaskAllList && mTaskAllList.size() > 0) {
                    mViewPager.setCurrentItem(0); //显示到第一个
                }
                break;
            case R.id.task_setcurrentitem2:
                if (null != mTaskAllList && mTaskAllList.size() > 0) {
                    mViewPager.setCurrentItem(mTaskAllList.size()); //显示到最后一个
                }
                break;
            case R.id.map_button:
                if (SpUtils.isCompleteUserInfo(mActivity)) {
                    isHend();
                } else {
                    CompleteUserInfoActivity.actionShow(mActivity, SpUtils.getUserToken(mActivity));
                }
                break;
            case R.id.map_news:
                startActivity(new Intent(getActivity(), MapNewsActivity.class));
                break;
            case R.id.mi_task:
                startActivity(new Intent(getActivity(), OfficialActivity.class));
                break;
            case R.id.bt_refresh:
                initNetWork(SpUtils.getXpoint(mActivity), SpUtils.getYpoint(mActivity), mSort, mOtherSex, mOtherLowAge, mOtherHignAge, mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard, mSellerType, mTaskCity);
//                if (null != mTaskAllList && mTaskAllList.size() > 0) {
//                    mViewPager.setCurrentItem(0); //显示到第一个
//                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            Collection<EMConversation> conversationValues = conversations.values();
            if (conversationValues.size() > 0) {
                int unreadCountSum = 0;
                for (EMConversation emConversation : conversationValues) {
                    if (emConversation != null) {
                        unreadCountSum += emConversation.getUnreadMsgCount();
                    }
                }
                mChatMessageCount = unreadCountSum;
            }
            refreshMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSystemMessageNumber();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(FilterReturnBean messageEvent) {
        mSort = messageEvent.getSort();
        mOtherSex = messageEvent.getOtherSex();
        mOtherLowAge = messageEvent.getOtherLowAge();
        mOtherHignAge = messageEvent.getOtherHignAge();
        mOtherLowheight = messageEvent.getOtherLowheight();
        mOtherHignheight = messageEvent.getOtherHignheight();
        mIsVideo = messageEvent.getIsVideo();
        mIsIdcard = messageEvent.getIsIdcard();
        mSellerType = ",".equals(messageEvent.getSellerType()) ? "" : messageEvent.getSellerType();
        mTaskCity = messageEvent.getTaskCity();
        LogUtil.d(" 列表界面筛选返回的数据 ----  " + messageEvent.toString());
    }

    /**
     * 任务发布成功通知百度地图界面刷新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(TaskReleaseRefreshTaskAndMapList taskAndMapList) {
        LogUtil.d("任务发布成功通知列表刷新----" + taskAndMapList.isRefresh() + "");
        if (taskAndMapList.isRefresh()) {
            initNetWork(mXpoint, mYpoint, mSort, mOtherSex, mOtherLowAge, mOtherHignAge, mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard, mSellerType, mTaskCity);
        }
    }

    /**
     * 任务定位返回的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LatLngBean latLngBean) {
        double latitude = latLngBean.getLatitude();
        double longitude = latLngBean.getLongitude();
        DecimalFormat df = new DecimalFormat("######0.000000");
        String DecimalFormatLatitude = df.format(latitude);
        String DecimalFormatLongitude = df.format(longitude);
        double latitudeDoubleNext = Double.parseDouble(DecimalFormatLatitude);
        double longitudeDoubleNext = Double.parseDouble(DecimalFormatLongitude);
        LogUtil.d("列表访问数据=====================定位数据=========================");
        initNetWork(String.valueOf(latitudeDoubleNext), String.valueOf(longitudeDoubleNext), mSort, mOtherSex, mOtherLowAge, mOtherHignAge, mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard, mSellerType, mTaskCity);

    }

    boolean isLooding = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showLoodingTabulation(LoodingTabulationFragMentBean bean) {
        if (null != bean && bean.isLooding() && isLooding) {
            initNetWork(mXpoint, mYpoint, mSort, mOtherSex, mOtherLowAge, mOtherHignAge, mOtherLowheight, mOtherHignheight, mIsVideo, mIsIdcard, mSellerType, mTaskCity);
            isLooding = false;
        }
    }

    /*侧边栏界面关闭了*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(LoodingData loodingData) {
        if (loodingData.isLooding() && loodingData.getPage() == 0) {
            mSort = "1";
            mOtherSex = "";
            mOtherLowAge = "";
            mOtherHignAge = "";
            mOtherLowheight = "";
            mOtherHignheight = "";
            mIsVideo = "";
            mIsIdcard = "";
            mSellerType = "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }

    /**
     * 环信消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(UnreadHuanXinMsgCountBean msgCountBean) {
        mChatMessageCount = msgCountBean.getUnreadMsgCount();
        refreshMessageCount();
    }

    /**
     * 系统消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(ReceivedMessageBean ReceivedMessageBean) {
        getSystemMessageNumber();
    }

    private void refreshMessageCount() {
        int totalCount = mChatMessageCount + mSystemMessageCount;
        if (totalCount > 99) {
            totalCount = 99;
        }
        mPointNumber.setVisibility(totalCount == 0 ? View.GONE : View.VISIBLE);
        mPointNumber.setText(String.valueOf(totalCount));
    }

    private void getSystemMessageNumber() {
        RetrofitUtil.createService(SettingService.class)
                .getUserSmsCnt(SpUtils.getUserToken(getActivity()))
                .enqueue(new RetrofitCallback<UserSmsCntBean>() {
                    @Override
                    public void onSuccess(Call<UserSmsCntBean> call, Response<UserSmsCntBean> response) {
                        if (response.isSuccessful()) {
                            UserSmsCntBean body = response.body();
                            if (body.getCode() == 1) {
                                mSystemMessageCount = body.getNewMsg();
                                refreshMessageCount();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSmsCntBean> call, Throwable t) {
                    }
                });
    }

    /**
     * 头像审核
     */
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
                                        mDialogFragment = new ImageHendDialogFragment(mFragment.getContext(), mFragment, 1);
                                        mDialogFragment.show(getChildFragmentManager(), "1");
                                    } else if ("0".equals(successfulBean.getIsHead())) { //审核中
                                        mDialogFragment = new ImageHendDialogFragment(mFragment.getContext(), mFragment, 2);
                                        mDialogFragment.show(getChildFragmentManager(), "2");
                                    } else if ("2".equals(successfulBean.getIsHead())) { //通过
                                        startActivity(new Intent(getActivity(), TaskActivity.class));
                                    }
                                } else {
                                    startActivity(new Intent(getActivity(), TaskActivity.class));
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
        mActivity.startActivityForResult(mIntent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void PhotoGraph() { // 拍照
        String state = Environment.getExternalStorageState(); //拿到sdcard是否可用的状态码
        if (state.equals(Environment.MEDIA_MOUNTED)) {   //如果可用
            mIntent = new Intent(mActivity, UseCameraActivity.class);
            mActivity.startActivityForResult(mIntent, RESULT_LOAD_IMAGE2);
        } else {
            Toasty.normal(mActivity, "sdcard不可用").show();
        }
    }

    private int PHOTO_REQUEST_CUT = 400;//截取图片
    private int RESULT_LOAD_IMAGE2 = 500; //拍照
    private int RESULT_LOAD_IMAGE = 600; //选择图片

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        LogUtil.i("TAG", getImageContentUri(mActivity, file) + "裁剪照片的真实地址");
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(getImageContentUri(mActivity, file), "image/*");//自己使用Content Uri替换File Uri
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
            Toast.makeText(mActivity, errorMessage, Toast.LENGTH_LONG).show();
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
