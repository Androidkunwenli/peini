package com.jsz.peini.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;
import com.jsz.peini.ui.activity.login.LoginActivity;

/**
 * Created by th on 2017/2/3.
 */

public class LoginDialogUtils {


    /**
     * token失效问题
     */
    public static void isNewLogin(final Activity activity) {
//        SpUtils.remove(activity, "b");
//        SpUtils.remove(activity, "phone");
//        SpUtils.remove(activity, "password");
        SpUtils.remove(activity, "lock");
        SpUtils.remove(activity, "mUserToken");
        SpUtils.remove(activity, "imageHead");
        SpUtils.remove(activity, Conversion.CANCELTASKNUMBER);
        SpUtils.remove(activity, Conversion.CANCELPHONENUMBER);
        SpUtils.remove(activity, Conversion.STORE_MANAGE_ID);
        SpUtils.remove(activity, Conversion.RANKING);
        //注销别名
        boolean unBindAlias = PushManager.getInstance().unBindAlias(activity, SpUtils.getPhone(activity), false);
        if (unBindAlias) {
            LogUtil.d("解绑定别名成功了-----");
        } else {
            LogUtil.d("解绑定别名失败了-----");
        }
        //关闭推送
        PushManager.getInstance().turnOffPush(activity.getApplicationContext());
        PushManager.getInstance().stopService(activity.getApplicationContext());
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
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle("下线通知")
                .setMessage("您的账号在另一台设备上登录,如非本人操作,请修改密码")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                }).show();
    }

//    /**登录成功返回的数据*/
//    /**
//     * 保存用户信息
//     */
//    public static void saveUserInformation(Activity mActivity, LoginSuccess response, int resultCode) {
//        LogUtil.d("登录返回的信息" + response.toString());
//        if (resultCode != 7) {
//            SpUtils.put(mActivity, "mUserToken", String.valueOf(response.getUserToken()));
//            SpUtils.put(mActivity, "id", response.getUserInfo().getId() + "");
//            SpUtils.put(mActivity, "userLoginId", response.getUserInfo().getUserLoginId() + "");
//            SpUtils.put(mActivity, "nickname", response.getUserInfo().getNickname() + "");
//            SpUtils.put(mActivity, "imageHead", response.getUserInfo().getImageHead() + "");
//            SpUtils.put(mActivity, "sex", response.getUserInfo().getSex() + "");
//            SpUtils.put(mActivity, "phone", response.getUserInfo().getUserPhone() + "");
//        }
//        SpUtils.putServerB(mActivity, response.getServerB());
//
//        if (response.getResultCode() == 7) {
//            Intent intent = new Intent(mActivity, SanPhoneNextActivity.class);
//            intent.putExtra(Conversion.INFORMATION, resultCode);
//            intent.putExtra("mSanToken", response.getUserToken());
//            mActivity.startActivity(intent);
//        } else {
//            boolean guidedFlag = SpUtils.isInstalledRightNow(mActivity);
//            if (!guidedFlag) {
//                Intent intent = new Intent(mActivity, HomeActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                mActivity.startActivity(intent);
//            } else {
//                Intent intent = new Intent(mActivity, GuidanceActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                mActivity.startActivity(intent);
//            }
//        }
//    }

//    public static void mSanLoginSuccess(Activity mActivity, SanLoginSuccess loginSuccess) {
//        LogUtil.d("三方登录返回的信息" + loginSuccess.toString());
//        SpUtils.put(mActivity, "mUserToken", loginSuccess.getUserToken() + "");
//        SpUtils.put(mActivity, "id", loginSuccess.getUserInfo().getId() + "");
//        SpUtils.put(mActivity, "userLoginId", loginSuccess.getUserInfo().getUserLoginId() + "");
//        SpUtils.put(mActivity, "nickname", loginSuccess.getUserInfo().getNickname() + "");
//        SpUtils.put(mActivity, "imageHead", loginSuccess.getUserInfo().getImageHead() + "");
//        SpUtils.put(mActivity, "sex", loginSuccess.getUserInfo().getSex() + "");
//        SpUtils.put(mActivity, "phone", loginSuccess.getUserInfo().getPhone() + "");
//        SpUtils.putServerB(mActivity, loginSuccess.getServerB());
//
//        boolean guidedFlag = SpUtils.isInstalledRightNow(mActivity);
//        if (!guidedFlag) {
//            Intent intent = new Intent(mActivity, HomeActivity.class)
//                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mActivity.startActivity(intent);
//        } else {
//            Intent intent = new Intent(mActivity, GuidanceActivity.class)
//                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            mActivity.startActivity(intent);
//        }
//    }
}
