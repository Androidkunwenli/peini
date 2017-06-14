package com.jsz.peini.ui.activity.square;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.Glide;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseActivity;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.ImageListBean;
import com.jsz.peini.model.square.UserImageAllByUserId;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.adapter.square.MIPhotoAdapter;
import com.jsz.peini.ui.view.ImageSelector.ImageLoader;
import com.jsz.peini.ui.view.ImageSelector.ImgSelActivity;
import com.jsz.peini.ui.view.ImageSelector.ImgSelConfig;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.widget.recyclerview.LoadMoreFooterView;
import com.jsz.peini.widget.recyclerview.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by th on 2017/1/5.
 */
public class MiPhotoAlbumManagementActivity extends BaseActivity {

    private static final int REQUEST_CODE = 13579;

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.right_button)
    TextView mRightButton;

    @InjectView(R.id.album_upload)
    TextView mAlbumUpload;
    @InjectView(R.id.album_delete_number)
    TextView mAlbumDeleteNumber;
    @InjectView(R.id.album_delete_button)
    TextView mAlbumDeleteButton;
    @InjectView(R.id.album_delete)
    LinearLayout mAlbumDelete;
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
    @InjectView(R.id.toolbar)
    LinearLayout mToolbar;

    private MiPhotoAlbumManagementActivity mActivity;
    private MIPhotoAdapter mMiPhotoAdapter;
    private List<UserImageAllByUserId.UserImageListBean> mUserImageList = new ArrayList<>();

    /*是否管理*/
    boolean isManage = false;
    /**
     * 记录
     */
    boolean isManageNext = false;
    /**
     * 图片的集合
     */
    private ArrayList<ImageListBean> mList;
    private boolean mListShouldInit = true;
    private int mPageNumbe = 1;
    private int mSize;

    @Override
    public int initLayoutId() {
        return R.layout.activity_album_management;
    }

    @Override
    public void initView() {
        super.initView();
        mActivity = this;
        mTitle.setText("我的照片");
        mRightButton.setText("管理");
        mRightButton.setTextColor(getResources().getColor(R.color.RED_FB4E30));
        mSwipeTarget.setBackgroundResource(R.color.white000);
        /**初始化数据*/
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mMiPhotoAdapter = new MIPhotoAdapter(mActivity, mUserImageList);
        mSwipeTarget.setAdapter(mMiPhotoAdapter);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                        //当屏幕停止滚动，加载图片
                        try {
                            if (mActivity != null) Glide.with(mActivity).resumeRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                        //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                    case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to hong final position while not under outside control.
                        //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                        try {
                            if (mActivity != null) Glide.with(mActivity).pauseRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

/**上啦加载*/
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                inItNetWork(true);
            }
        });
/**下拉刷新*/
        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                inItNetWork(false);
            }
        });
        mMiPhotoAdapter.setOnCheckedChangedListener(new MIPhotoAdapter.onCheckedChangedListener() {
            @Override
            public void setClickPhoto(int position, int index) {
                int showIndex = 0;
                if (mList == null) {
                    mList = new ArrayList<>();
                    mListShouldInit = true;
                }
                if (mListShouldInit) {
                    mList.clear();
                }

                for (int i = 0; i < mUserImageList.size(); i++) {
                    List<ImageListBean> userImageAll = mUserImageList.get(i).getUserImageAll();
                    if (mListShouldInit) {
                        mList.addAll(userImageAll);
                    }

                    if (i < position && userImageAll != null) {
                        showIndex += userImageAll.size();
                    }
                }
                showIndex += index;
                mListShouldInit = false;

                Intent intent = new Intent(mActivity, LargerImageActivity.class);
                intent.putExtra(Conversion.LARGERIMAGEACTIVITY, mList);
                intent.putExtra(Conversion.SHOWINDEX, showIndex);
                intent.putExtra(Conversion.TYPE, 0);
                mActivity.startActivity(intent);
            }

            @Override
            public void setCheckedPhoto() {
                int checkedPhotoNumber = getCheckedPhotoNumber();
                mAlbumDeleteNumber.setText("已选择 " + checkedPhotoNumber + " 张照片");
            }
        });
        inItNetWork(true);
    }

    /**
     * 发起网络请求
     */
    private void inItNetWork(final boolean isFirst) {
        if (isFirst) {
            mPageNumbe = 1;
        } else {
            mPageNumbe++;
        }
        RetrofitUtil.createService(SquareService.class).
                getUserImageAllByUserId(mUserToken, mPageNumbe, 5)
                .enqueue(new Callback<UserImageAllByUserId>() {
                    @Override
                    public void onResponse(Call<UserImageAllByUserId> call, Response<UserImageAllByUserId> response) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        if (response.isSuccessful()) {
                            UserImageAllByUserId body = response.body();
                            if (body != null && body.getResultCode() == 1) {
                                /**更新数据*/
                                List<UserImageAllByUserId.UserImageListBean> userImageList = response.body().getUserImageList();
                                mListShouldInit = true;
                                if (isFirst) {
                                    mUserImageList.clear();
                                    mUserImageList.addAll(userImageList);
                                } else {
                                    mUserImageList.addAll(userImageList);
                                }
                                mMiPhotoAdapter.notifyDataSetChanged();
                                mAlbumDeleteNumber.setText("已选择 0 张照片");
                            } else if (body.getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(mActivity);
                            } else {
                                Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserImageAllByUserId> call, Throwable t) {
                        mSwipeToLoadLayout.setRefreshing(false);//停止刷新
                        mSwipeToLoadLayout.setLoadingMore(false);//停止下拉
                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                    }
                });
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.toolbar, R.id.right_button, R.id.album_upload, R.id.album_delete_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.right_button:
                managePhoto();
                break;
            case R.id.album_upload:
                isOpenPhoto();
                break;
            case R.id.album_delete_button:
                new AlertDialog.Builder(mActivity)
                        .setTitle("温馨提示")
                        .setMessage("您是否要删除照片?")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                inItNetWorkDelete();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.toolbar: //返回的事件

                setResult(10001, null);
                finish();
                break;
        }
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
                .setType(1)
                .build();
        // 跳转到图片选择器
        ImgSelActivity.startActivity(this, config, REQUEST_CODE);
    }

    /**
     * 删除照片的接口
     */
    private void inItNetWorkDelete() {
        String imageIdStr = getCheckedPhotoIds();
        if (TextUtils.isEmpty(imageIdStr)) {
            return;
        }
        RetrofitUtil.createService(SquareService.class).deleteUserImage(imageIdStr).enqueue(new Callback<SuccessfulBean>() {
            @Override
            public void onResponse(Call<SuccessfulBean> call, Response<SuccessfulBean> response) {
                if (response.isSuccessful() && response.body().getResultCode() == 1) {
                    //重新请求网络
                    inItNetWork(true);
                }
            }

            @Override
            public void onFailure(Call<SuccessfulBean> call, Throwable t) {
            }
        });
    }

    /**
     * 管理相册的按钮
     */
    private void managePhoto() {
        isManageNext = !isManage;
        isManage = !isManage;
        LogUtil.d(getLocalClassName(), "" + isManageNext);
        if (isManageNext) {
            mTitle.setText("管理照片");
            mRightButton.setText("取消");
            mRightButton.setTextColor(getResources().getColor(R.color.RED_FB4E30));
            mMiPhotoAdapter.setImageManage(isManageNext);
            mAlbumUpload.setVisibility(View.GONE);
            mAlbumDelete.setVisibility(View.VISIBLE);
            mAlbumDeleteNumber.setText("已选择 0 张照片");
            mToolbar.setVisibility(View.GONE);
        } else {
            mTitle.setText("我的照片");
            mRightButton.setText("管理");
            mRightButton.setTextColor(getResources().getColor(R.color.text333));
            mMiPhotoAdapter.setImageManage(isManageNext);
            mAlbumUpload.setVisibility(View.VISIBLE);
            mAlbumDelete.setVisibility(View.GONE);
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            for (String path : pathList) {
                LogUtil.d("返回的地址", path + "\n");
            }
        } else {
            inItNetWork(true);
        }
    }

    /**
     * 获取已选中的图片数量
     *
     * @return 已选中的图片数量
     */
    private int getCheckedPhotoNumber() {
        int totalNumber = 0;
        if (mUserImageList != null && mUserImageList.size() > 0) {
            for (UserImageAllByUserId.UserImageListBean userImageListBean : mUserImageList) {
                if (userImageListBean != null && userImageListBean.getUserImageAll() != null
                        && userImageListBean.getUserImageAll().size() > 0) {
                    for (ImageListBean itemBean : userImageListBean.getUserImageAll()) {
                        if (itemBean.isChecked()) {
                            totalNumber++;
                        }
                    }
                }
            }
        }
        return totalNumber;
    }

    /**
     * 获取已选中的图片Ids 拼接的字符串（用逗号分隔）
     *
     * @return Ids 拼接的字符串
     */
    private String getCheckedPhotoIds() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mUserImageList != null && mUserImageList.size() > 0) {
            for (UserImageAllByUserId.UserImageListBean userImageListBean : mUserImageList) {
                if (userImageListBean != null && userImageListBean.getUserImageAll() != null
                        && userImageListBean.getUserImageAll().size() > 0) {
                    for (ImageListBean itemBean : userImageListBean.getUserImageAll()) {
                        if (itemBean.isChecked()) {
                            if (stringBuilder.length() > 0) {
                                stringBuilder.append(",");
                            }
                            stringBuilder.append(itemBean.getId());
                        }
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}
