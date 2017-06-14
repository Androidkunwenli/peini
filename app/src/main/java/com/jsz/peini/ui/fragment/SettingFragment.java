package com.jsz.peini.ui.fragment;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;
import com.jsz.peini.R;
import com.jsz.peini.base.BaseFragment;
import com.jsz.peini.listener.RetrofitCallback;
import com.jsz.peini.model.JsonResponse;
import com.jsz.peini.model.setting.VersionNoBean;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.presenter.setting.SettingService;
import com.jsz.peini.presenter.square.SquareService;
import com.jsz.peini.service.FloatViewService;
import com.jsz.peini.ui.activity.HelpActivity;
import com.jsz.peini.ui.activity.login.IsLoginActivity;
import com.jsz.peini.ui.activity.report.ReportActivity;
import com.jsz.peini.ui.activity.setting.AboutUsActivity;
import com.jsz.peini.ui.activity.setting.GesturePasswordActivity;
import com.jsz.peini.ui.activity.setting.LoginPasswordActivity;
import com.jsz.peini.ui.activity.setting.MerchantsSettledActivity;
import com.jsz.peini.ui.activity.setting.PayPasswordActivity;
import com.jsz.peini.ui.view.Popwindou;
import com.jsz.peini.utils.CacheActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.LoginDialogUtils;
import com.jsz.peini.utils.PeiNiUtils;
import com.jsz.peini.utils.RetrofitUtil;
import com.jsz.peini.utils.ShareUtils;
import com.jsz.peini.utils.SpUtils;
import com.jsz.peini.utils.UiUtils;
import com.jsz.peini.utils.deletedata.DeleteDataManager;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.InjectView;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by kunwe on 2016/11/29.
 * 设置界面
 */

public class SettingFragment extends BaseFragment {
    @InjectView(R.id.button_1)
    TextView button1;
    @InjectView(R.id.button_2)
    TextView button2;
    @InjectView(R.id.button_3)
    TextView button3;
    @InjectView(R.id.button_4)
    TextView button4;
    @InjectView(R.id.button_6)
    TextView button6;
    @InjectView(R.id.button_7)
    TextView button7;
    @InjectView(R.id.button_8)
    TextView button8;
    @InjectView(R.id.button_9)
    LinearLayout button9;
    @InjectView(R.id.button_10)
    TextView button10;
    @InjectView(R.id.button_11)
    Button button11;
    @InjectView(R.id.ceshi)
    CheckBox ceshi;
    @InjectView(R.id.tv_version_name)
    TextView tvVersionName;
    @InjectView(R.id.filter_ideoauthentication)
    CheckBox mFilterIdeoauthentication;
    public Intent mIntent;
    private Popwindou mPopwindou;

    @Override
    public View initViews() {
        return UiUtils.inflate(mActivity, R.layout.fragment_setting);
    }

    @Override
    public void initData() {
        final boolean checked = (boolean) SpUtils.get(mActivity, Conversion.RANKING, true);
        if (checked) {//排行榜

        } else {

        }
        mFilterIdeoauthentication.setChecked(checked);
        mFilterIdeoauthentication.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                if (b) {
                    setIsRank("0", b);
                } else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("温馨提示")
                            .setMessage("确认要关闭排行榜功能吗？关闭该功能可能会错失很多交友机会?")
                            .setCancelable(false)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mFilterIdeoauthentication.setChecked(false);
                                    setIsRank("1", b);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    mFilterIdeoauthentication.setChecked(true);
                                }
                            }).show();

                }
            }
        });
        tvVersionName.setText(PeiNiUtils.getVersionName(mActivity));
    }

    private void setIsRank(final String s, final boolean b) {
        RetrofitUtil.createService(SquareService.class).updateSwitchRank(SpUtils.getUserToken(mActivity), s).enqueue(new RetrofitCallback<JsonResponse>() {
            @Override
            public void onSuccess(Call<JsonResponse> call, Response<JsonResponse> response) {
                if (response.isSuccessful()) {
                    JsonResponse body = response.body();
                    if (body.getResultCode() == 1) {
                        SpUtils.put(mActivity, Conversion.RANKING, b);
                    } else if (body.getResultCode() == 9) {
                        LoginDialogUtils.isNewLogin(mActivity);
                    }
//                    } else if (body.getResultCode() == 0) {
//                        Toasty.normal(mActivity, body.getResultDesc()).show();
//                    } else {
//                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
//                    }
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.button_1, R.id.button_2,
            R.id.button_3, R.id.button_4,
            R.id.button_6,
            R.id.button_7, R.id.button_8,
            R.id.button_9, R.id.button_10,
            R.id.button_11})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_1: //邀请好友
                ShowShred();
//                startActivity(new Intent(mActivity, KunShareActivity.class));
                break;
            case R.id.button_2://修改登录密码
                startActivity(new Intent(mActivity, LoginPasswordActivity.class));
                break;
            case R.id.button_3://修改支付密码
                startActivity(new Intent(mActivity, PayPasswordActivity.class));
                break;
            case R.id.button_4://设置手势密码
                startActivity(new Intent(mActivity, GesturePasswordActivity.class));
                break;
            case R.id.button_6://清除缓存
                deleteCache();
                break;
            case R.id.button_7://关于我们
                startActivity(new Intent(mActivity, AboutUsActivity.class));
                break;
            case R.id.button_8://问题反馈
                mIntent = new Intent(mActivity, ReportActivity.class);
                mIntent.putExtra("type", "1");
                startActivity(mIntent);
                break;
            case R.id.button_9://当前版本
                InitUpdate();
                break;
            case R.id.button_10://商家入驻
                startActivity(new Intent(mActivity, MerchantsSettledActivity.class));
                break;
            case R.id.button_11://注销
                dialog();
                break;
        }
    }

    //版本更新
    private void InitUpdate() {
        final int versionCode = PeiNiUtils.getVersionCode(mActivity);
        RetrofitUtil.createService(SettingService.class)
                .appVersionUpdate("1", String.valueOf(versionCode))
                .enqueue(new RetrofitCallback<VersionNoBean>() {
                    @Override
                    public void onSuccess(Call<VersionNoBean> call, Response<VersionNoBean> response) {
                        if (response.isSuccessful()) {
                            VersionNoBean.DataBean dataBean = response.body().getData();
                            if (response.body().getResultCode() == 1) {
                                if (dataBean != null && !TextUtils.isEmpty(dataBean.getAddress())
                                        && versionCode < dataBean.getVersion()) {
                                    showUpdateDialog(dataBean.getAddress());
                                } else {
                                    Toasty.normal(mActivity, "当前已经是最新版本").show();
                                }
                            } else if (response.body().getResultCode() == 0) {
                                Toasty.normal(mActivity, "当前已经是最新版本").show();
                            } else {
//                                Toasty.normal(mActivity, response.message()).show();
                                Toasty.normal(mActivity, "当前已经是最新版本").show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VersionNoBean> call, Throwable t) {
//                        Toasty.normal(mActivity, Conversion.NETWORKERROR).show();
                        Toasty.normal(mActivity, "当前已经是最新版本").show();
                    }
                });
    }

    private void showUpdateDialog(final String address) {
        new AlertDialog.Builder(mActivity)
                .setMessage("检测到有新版本更新,是否更新?")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        downloadApk(address);
                    }
                })
                .setNegativeButton("下次吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    private void downloadApk(String address) {
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(address));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "陪你.apk");
        downloadManager.enqueue(request);
    }

    /*清除缓存*/
    private void deleteCache() {
        new AlertDialog.Builder(mActivity)
                .setTitle("清除缓存吗?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteDataManager.cleanInternalCache(mActivity);
                        DeleteDataManager.cleanFiles(mActivity);
                        deleteDataImage();
                        Toasty.normal(mActivity, "清除缓存成功").show();

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

    private void deleteDataImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.d("删除压缩后的图片===" + DeleteDataManager.deleteDirectory(Conversion.LOCAL_IMAGE_CACHE_PATH));
            }
        }).start();
    }

    protected void dialog() {
        new AlertDialog.Builder(mActivity)
                .setTitle("注销登录")
                .setMessage("确定注销登录吗")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        //停止任务辅助光圈
                        mActivity.stopService(new Intent(mActivity, FloatViewService.class));

                        SpUtils.remove(mActivity, "lock");
                        SpUtils.remove(mActivity, "mUserToken");
                        SpUtils.remove(mActivity, "imageHead");
                        SpUtils.remove(mActivity, Conversion.CANCELTASKNUMBER);
                        SpUtils.remove(mActivity, Conversion.CANCELPHONENUMBER);
                        SpUtils.remove(mActivity, Conversion.STORE_MANAGE_ID);
                        SpUtils.remove(mActivity, Conversion.RANKING);
                        //注销别名
                        boolean unBindAlias = PushManager.getInstance().unBindAlias(mActivity, SpUtils.getPhone(mActivity), false);
                        if (unBindAlias) {
                            LogUtil.d("解绑定别名成功了-----");
                        } else {
                            LogUtil.d("解绑定别名失败了-----");
                        }
                        //关闭推送
                        PushManager.getInstance().turnOffPush(mActivity.getApplicationContext());
                        PushManager.getInstance().stopService(mActivity.getApplicationContext());
                        //注销环信
                        EMClient.getInstance().logout(true, new EMCallBack() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                            }
                        });
                        CacheActivity.finishActivity();
                        Intent intent = new Intent(mActivity, IsLoginActivity.class);
                        startActivity(intent);
                        mActivity.finish();

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

    //显示分享底部分享栏目
    private void ShowShred() {
        UMImage thumb = new UMImage(mActivity, R.mipmap.ic_launcher);
        String url = IpConfig.HttpPeiniIp + "invite/index?userPhone=" + SpUtils.getPhone(mActivity);
        final UMWeb web = new UMWeb(url);
        web.setTitle("我正在使用“陪你”，推荐给你");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("好朋友，我陪你！好玩好用还有好福利");//描述
        new ShareUtils(mActivity, web);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(mActivity).onActivityResult(requestCode, resultCode, data);//完成回调
    }
}
