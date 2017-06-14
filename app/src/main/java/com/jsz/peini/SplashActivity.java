package com.jsz.peini;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jsz.peini.base.BaseNotSlideActivity;
import com.jsz.peini.base.NonGestureLockInterface;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.JsonResponse;
import com.jsz.peini.model.ad.AdModel;
import com.jsz.peini.model.setting.VersionNoBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.ad.Ad;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.ui.activity.GuideActivity;
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.ui.activity.login.IsLoginActivity;
import com.jsz.peini.ui.activity.web.WebAllActivity;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.StringUtils;
import com.jsz.peini.utils.Ticker;

import java.util.List;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends BaseNotSlideActivity implements NonGestureLockInterface, Ticker.OnTickListener {

    @InjectView(R.id.iv_ad_image)
    ImageView mIvAdImage;
    @InjectView(R.id.tv_jump_over)
    TextView mTvJumpOver;
    @InjectView(R.id.iv_logo)
    ImageView mIvLogo;
    @InjectView(R.id.fl_adfirst)
    FrameLayout mFlAdfirst;
    @InjectView(R.id.iv_splash_logo)
    ImageView mIvSplashLogo;
    @InjectView(R.id.fl_splash_logo)
    FrameLayout mFlSplashLogo;

    private SplashActivity mActivity;

    private long currentTime = 0;
    // 间隔
    private static final int WAITING_INTERVAL_TIME = 2000;

    private static final int REQUEST_PERMISSION_CODE = 1021;
    private String[] permissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private String mDownloadPath;
    private boolean isUpdating = false;

    private Ticker mTicker;
    private String mAdLinkUrl;
    private AdModel.AdvertiseListBean mBean;
    private int mCount = 2;
    private Intent mIntent;

    @Override
    public int initLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        mActivity = this;
        mTicker = new Ticker(this, 1000);
    }

    @Override
    public void initData() {
        initUpdate();
        appStart();
    }

    @Override
    public void showAllVisual(boolean b) {
        super.showAllVisual(true);
    }

    /**
     * 启动页数据采集接口
     */
    private void appStart() {
        RetrofitUtil.createService(SquareService.class)
                .appStart(SpUtils.getUserToken(mActivity), PeiNiApp.getUniquePsuedoID(),
                        PeiNiApp.getChannelNo(), "1")
                .enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                    }
                });


    }

    //版本更新
    private void initUpdate() {
        final int versionCode = PeiNiUtils.getVersionCode(mActivity);
        currentTime = System.currentTimeMillis();
        RetrofitUtil.createService(SettingService.class).appVersionUpdate("1", String.valueOf(versionCode))
                .enqueue(new RetrofitCallback<VersionNoBean>() {
                    @Override
                    public void onSuccess(Call<VersionNoBean> call, Response<VersionNoBean> response) {
                        if (response.isSuccessful()) {
                            VersionNoBean.DataBean dataBean = response.body().getData();
                            if (response.body().getResultCode() == 1) {
                                if (dataBean != null && versionCode < dataBean.getVersion()) {
                                    mDownloadPath = dataBean.getAddress();
                                    if (dataBean.getForceUpdate() == 1) {
                                        showForceUpdateDialog();
                                    } else if (dataBean.getForceUpdate() == 0) {
                                        isUpdating = true;
                                        showUpdateDialog();
                                    }
                                } else {
                                    welcome();
                                }
                            } else if (response.body().getResultCode() == 0) {
                                requestSplashAd();
                            } else {
                                showNetErrorDialog();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VersionNoBean> call, Throwable t) {
                        showNetErrorDialog();
                    }
                });
    }

    private void requestSplashAd() {
        currentTime = System.currentTimeMillis();
        RetrofitUtil.createService(Ad.class).getAdvertise("1").enqueue(new Callback<AdModel>() {
            @Override
            public void onResponse(Call<AdModel> call, Response<AdModel> response) {
                if (response.isSuccessful()) {
                    AdModel body = response.body();
                    if (body.getResultCode() == 1) {
                        List<AdModel.AdvertiseListBean> advertiseList = body.getAdvertiseList();
                        if (advertiseList != null && advertiseList.size() > 0) {
                            mBean = body.getAdvertiseList().get(0);
                            mAdLinkUrl = mBean.getAdLink();
                            String adImgUrl = mBean.getAdImgUrl();
                            if (TextUtils.isEmpty(adImgUrl)) {
                                mFlAdfirst.setVisibility(View.GONE);
                                mFlSplashLogo.setVisibility(View.VISIBLE);
                                mTvJumpOver.setVisibility(View.GONE);
                                welcome();
                            } else {
                                Glide.with(mActivity.getApplicationContext())
                                        .load(IpConfig.HttpPic + adImgUrl)
                                        .asBitmap()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(new SimpleTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                mIvAdImage.setImageBitmap(bitmap);
                                                mFlAdfirst.setVisibility(View.VISIBLE);
                                                mFlSplashLogo.setVisibility(View.GONE);
                                                mTvJumpOver.setVisibility(View.VISIBLE);
                                                startTick();
                                            }

                                            @Override
                                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                                super.onLoadFailed(e, errorDrawable);
                                                mTvJumpOver.setVisibility(View.GONE);
                                                mFlAdfirst.setVisibility(View.GONE);
                                                mFlSplashLogo.setVisibility(View.VISIBLE);
                                                welcome();
                                            }
                                        });
                            }
                        } else {
                            mTvJumpOver.setVisibility(View.GONE);
                            mFlAdfirst.setVisibility(View.GONE);
                            mFlSplashLogo.setVisibility(View.VISIBLE);
                            welcome();
                        }
                    } else {
                        mTvJumpOver.setVisibility(View.GONE);
                        mFlAdfirst.setVisibility(View.GONE);
                        mFlSplashLogo.setVisibility(View.VISIBLE);
                        welcome();
                    }
                }
            }

            @Override
            public void onFailure(Call<AdModel> call, Throwable t) {
                Toasty.normal(mActivity, "当前网络不可用，请稍后重试").show();
                mFlAdfirst.setVisibility(View.GONE);
                mTvJumpOver.setVisibility(View.GONE);
                mFlSplashLogo.setVisibility(View.VISIBLE);
                showNetErrorDialog();
            }
        });
    }


    private void showForceUpdateDialog() {
        if (TextUtils.isEmpty(mDownloadPath)) {
            return;
        }
        new AlertDialog.Builder(mActivity)
                .setTitle("升级提示")
                .setMessage("检测到有新版本,请下载安装更新")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(permissionList, REQUEST_PERMISSION_CODE);
                        } else {
                            downloadApk();
                            dialog.dismiss();
                            finish();
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showUpdateDialog() {
        if (TextUtils.isEmpty(mDownloadPath)) {
            return;
        }
        new AlertDialog.Builder(mActivity)
                .setTitle("升级提示")
                .setMessage("检测到有新版本更新,是否更新?")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(permissionList, REQUEST_PERMISSION_CODE);
                        } else {
                            downloadApk();
                            requestSplashAd();
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestSplashAd();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void downloadApk() {
        DownloadManager downloadManager = (DownloadManager) mActivity.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mDownloadPath));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "陪你.apk");
        downloadManager.enqueue(request);
        Toasty.normal(mActivity, "开始下载更新,请稍等").show();
    }

    private void showNetErrorDialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("警告")
                .setMessage("当前网络不可用，请检查您的设置")
                .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("重试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        initUpdate();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean isGestureLock() {
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE
                && Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0])) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadApk();
                if (isUpdating) {
                    welcome();
                }
            } else {
                Toasty.normal(mActivity, "请授予权限后再试，或者直接下载安装最新版本").show();
            }
            finish();
        }
    }

    @Override
    public void onTick() {
        if (mCount > 0) {
            mTvJumpOver.setText(String.format(Locale.CHINA, "跳过(%d)", mCount));
        } else {
            startHome();
            stopTick();
        }
        mCount--;
    }

    @OnClick({R.id.iv_ad_image, R.id.tv_jump_over})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_ad_image:
                jumpToOutLink(mAdLinkUrl);
                break;
            case R.id.tv_jump_over:
                startHome();
                break;
        }
    }

    private void jumpToOutLink(String linkUrl) {
        if (TextUtils.isEmpty(linkUrl)) {
            return;
        }
        String targetUrl;
        if (!linkUrl.contains("://")) {
            targetUrl = "http://" + linkUrl;
        } else {
            targetUrl = linkUrl;
        }
        if (StringUtils.isHttpPath(targetUrl)) {
            String userId = SpUtils.getUserToken(mActivity);
            String xPoint = SpUtils.getXpoint(mActivity);
            String yPoint = SpUtils.getYpoint(mActivity);
            targetUrl = targetUrl + userId + "&xpoint=" + xPoint + "&ypoint=" + yPoint;
            Intent intent = new Intent(mActivity, WebAllActivity.class);
            if (null != mBean) {
                intent.putExtra("adItemBean", mBean);
            }
            intent.putExtra("adLink", targetUrl);
            mActivity.startActivityForResult(intent, 10000);
        }
    }

    private void welcome() {
        long delayMillis = WAITING_INTERVAL_TIME + currentTime - System.currentTimeMillis();
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startHome();
            }
        }, delayMillis);
    }

    private void startHome() {
        boolean guidedFlag = SpUtils.isInstalledRightNow(mActivity);
        if (!guidedFlag) {
            if (!TextUtils.isEmpty(SpUtils.getUserToken(mActivity))) {
                mIntent = new Intent(mActivity, HomeActivity.class);
                mActivity.startActivity(mIntent);
                finish();
            } else {
                mIntent = new Intent(mActivity, IsLoginActivity.class);
                mActivity.startActivity(mIntent);
                finish();
            }
        } else {
            mIntent = new Intent(mActivity, GuideActivity.class);
            startActivity(mIntent);
            finish();
        }
    }

    private void stopTick() {
        mTicker.stop();
    }

    private void startTick() {
        mTicker.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTick();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        startTick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTick();
        mTvJumpOver.setText("跳过");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000) {
            startTick();
        }
    }
}
