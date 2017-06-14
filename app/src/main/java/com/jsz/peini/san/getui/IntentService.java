package com.jsz.peini.san.getui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jsz.peini.R;
import com.jsz.peini.model.eventbus.ReceivedMessageBean;
import com.jsz.peini.service.FloatViewService;
import com.jsz.peini.ui.activity.TaskMessageActivity;
import com.jsz.peini.ui.activity.home.HomeActivity;
import com.jsz.peini.ui.activity.login.CompleteUserInfoActivity;
import com.jsz.peini.ui.activity.news.FansActivity;
import com.jsz.peini.ui.activity.seller.SellerMessageActivity;
import com.jsz.peini.ui.activity.square.MiCouponActivity;
import com.jsz.peini.ui.activity.square.MiSquareActivity;
import com.jsz.peini.ui.activity.square.MyWealthActivity;
import com.jsz.peini.ui.activity.square.OfficialActivity;
import com.jsz.peini.ui.activity.square.TaPhotoImageActivity;
import com.jsz.peini.ui.activity.square.TaSquareHomepageActivity;
import com.jsz.peini.ui.activity.task.TaskDetailActivity;
import com.jsz.peini.utils.Conversion;
import com.jsz.peini.utils.LogUtil;
import com.jsz.peini.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class IntentService extends GTIntentService {
    public Intent mIntent;
    private Context mContext;
    private int mType;
    private String mParamId;
    private String mFrom;
    private String mTitle;
    private String mContent;
    private NotificationManager mMNotificationManager;

    public IntentService() {
    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        mContext = context;
        byte[] payload = msg.getPayload();
        String s1 = new String(payload);
        Gson gson = new Gson();
        SecretContacts json = gson.fromJson(s1, new TypeToken<SecretContacts>() {
        }.getType());
        mType = json.getType();
        mParamId = json.getParamId();
        mFrom = json.getFrom();
        mTitle = json.getTitle();
        mContent = TextUtils.isEmpty(json.getContent()) ? "系统消息" : json.getContent();
        LogUtil.d("接受到的消息" + json.toString());
        mMNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(String.valueOf(mContent));
        style.setBigContentTitle(String.valueOf(mTitle));
        mBuilder.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setTicker(String.valueOf(mContent)) //通知首次出现在通知栏，带上升动画效果的
                .setContentTitle(String.valueOf(mTitle))//设置通知栏标题 .setContentText() //<span style="font-family: Arial;">/设置通知栏显示内容</span>
                .setContentText(String.valueOf(mContent))//小小内容
                .setStyle(style)
                .setContentIntent(getDefalutIntent(PendingIntent.FLAG_CANCEL_CURRENT)) //设置通知栏点击意图
                .setSmallIcon(R.mipmap.ic_launchers)//设置通知小ICON
                .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launchers)).getBitmap())
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_MAX) //设置该通知优先级
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_ALL);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        mMNotificationManager.notify(mType, mBuilder.build());


        if (mType == 2 && canDrawOverlays()) {
            SpUtils.put(mContext, Conversion.CANCELPHONENUMBER, mFrom);
            SpUtils.put(mContext, Conversion.CANCELTASKNUMBER, mParamId);
            mIntent = new Intent(mContext, FloatViewService.class);
            mIntent.putExtra(Conversion.TASKID, mParamId + "");
            mIntent.putExtra(Conversion.PHONE, mFrom + "");
            startService(mIntent);
        } else {
            showDrawOverlaysDialog();
        }

        EventBus.getDefault().post(new ReceivedMessageBean());
    }

    private void showDrawOverlaysDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("温馨提示")
                .setMessage("新增桌面悬浮窗/开启应用发现新体验")
                .setCancelable(false)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestDrawOverLays();
                    }
                })
                .create().show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestDrawOverLays() {
        if ("Meizu".equals(Build.BRAND)) {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
            intent.putExtra("packageName", mContext.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + mContext.getPackageName()));
            startActivity(intent);
        }
    }

    private boolean canDrawOverlays() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(mContext);
        } else {
            return checkCallingOrSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private PendingIntent getDefalutIntent(int flags) {
        switch (mType) {
            case 1://跳转-对话页面
            case 4://跳转任务详情
            case 5:
            case 6:
            case 7:
            case 8://跳转任务详情
            case 103://跳转任务详情
            case 104://未评价跳转-评价页面---已评价跳转-任务详情
            case 107://跳转任务详情
            case 108://跳转任务详情
            case 109://跳转任务详情
            case 116://  2017/5/5 韩总说加的,消息也是116....
                mIntent = new Intent(mContext, TaskDetailActivity.class);
                mIntent.putExtra(Conversion.ID, mParamId);
                mIntent.putExtra(Conversion.TYPE, "1");
                break;
            case 2://任务取消通知
                mIntent = new Intent(mContext, TaskMessageActivity.class);
                mIntent.putExtra(Conversion.TASKID, mParamId);
                mIntent.putExtra(Conversion.PHONE, mFrom);
                break;
            case 3://优惠券过期通知
            case 10://优惠券通知
                mIntent = new Intent(mContext, MiCouponActivity.class);
                break;
            case 102://跳转-店铺详情
            case 101: //未评价跳转-评价店铺页----已评价跳转-店铺详情
                mIntent = new Intent(mContext, SellerMessageActivity.class);
                mIntent.putExtra(Conversion.ID, mParamId);
                mIntent.putExtra(Conversion.TYPE, "1");
                break;
            case 110: //我的财富
            case 111: //我的财富
            case 112: //我的财富
                mIntent = new Intent(mContext, MyWealthActivity.class);
                break;
            case 115://跳转活动详情
            case 209://跳转活动详情
                mIntent = new Intent(mContext, OfficialActivity.class);
                mIntent.putExtra(Conversion.URl, mParamId);
                break;

            //200 以上
            case 201: //完善信息
                if (!SpUtils.isCompleteUserInfo(mContext)) {
                    String userToken = SpUtils.getUserToken(mContext);
                    if (!TextUtils.isEmpty(userToken)) {
                        mIntent = new Intent(mContext, CompleteUserInfoActivity.class);
                        mIntent.putExtra("extra_user_id_flag", userToken);
                    }
                }
                break;
            case 202:
            case 203:
            case 210://我的空间
                if (SpUtils.isCompleteUserInfo(mContext)) {
                    mIntent = new Intent(mContext, MiSquareActivity.class);
                } else {
                    mIntent = new Intent(mContext, CompleteUserInfoActivity.class);
                    mIntent.putExtra("extra_user_id_flag", SpUtils.getUserToken(mContext));
                }
                break;
            case 204:  //首页
                mIntent = new Intent(mContext, HomeActivity.class);
                mIntent.putExtra(Conversion.TYPE, 10);
                break;
            case 205: //他人空间
                mIntent = new Intent(mContext, TaSquareHomepageActivity.class);
                mIntent.putExtra(Conversion.TOKEN, mParamId);
                break;
            case 206://任务
                mIntent = new Intent(mContext, TaskDetailActivity.class);
                mIntent.putExtra(Conversion.ID, mParamId);
                break;
            case 207://他人相册
                mIntent = new Intent(mContext, TaPhotoImageActivity.class);
                mIntent.putExtra("extra_other_user_id", mParamId);
                break;
            case 208://每天首次登录 粉丝
                mIntent = new Intent(mContext, FansActivity.class);
                break;
        }
        return PendingIntent.getActivity(mContext, mType, mIntent, flags);
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtil.e(TAG, "个推的服务开启的标志=====" + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {

    }
}