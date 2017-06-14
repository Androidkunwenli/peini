package com.jsz.peini.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.jsz.peini.presenter.IpConfig;
import com.jsz.peini.san.huanxin.HuanXinService;
import com.jsz.peini.san.huanxin.HuanxinHeadBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huizhe.ju on 2017/03/07.
 */

public class EaseUtils implements EaseUI.EaseUserProfileProvider {

    private static Map<String, HuanxinHeadBean.DataBean> usersInfoMap;

    private static EaseUtils mInstance = new EaseUtils();

    public static void setUserInfoProvider(final Activity activity, String myPhone, String userPhone) {
        if (TextUtils.isEmpty(myPhone) || TextUtils.isEmpty(userPhone)) {
            return;
        }
        String requestPhoneStr = myPhone + "," + userPhone;
        RetrofitUtil.createService(HuanXinService.class)
                .getEmUserHeadAndNickname(requestPhoneStr)
                .enqueue(new Callback<HuanxinHeadBean>() {
                    @Override
                    public void onResponse(Call<HuanxinHeadBean> call, Response<HuanxinHeadBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResultCode() == 1) {
                                initMapData(response.body().getData());
                            } else if (response.body().getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(activity);
                            } else {
                                Toasty.normal(activity, response.body().getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HuanxinHeadBean> call, Throwable t) {

                    }
                });
        setUserProvider();
    }

    public static void setUserInfoProvider(final Activity activity, String myPhone, EaseConversationList conversationListView) {

        StringBuilder stringBuilder = new StringBuilder(myPhone);
        for (int i = 0; i < conversationListView.getCount(); i++) {
            EMConversation item = conversationListView.getItem(i);
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(item.conversationId());
        }
        String phones = stringBuilder.toString();

        if (TextUtils.isEmpty(phones)) {
            return;
        }

        RetrofitUtil.createService(HuanXinService.class)
                .getEmUserHeadAndNickname(phones)
                .enqueue(new Callback<HuanxinHeadBean>() {
                    @Override
                    public void onResponse(Call<HuanxinHeadBean> call, Response<HuanxinHeadBean> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getResultCode() == 1) {
                                initMapData(response.body().getData());
                            } else if (response.body().getResultCode() == 9) {
                                LoginDialogUtils.isNewLogin(activity);
                            } else {
                                Toasty.normal(activity, response.body().getResultDesc()).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HuanxinHeadBean> call, Throwable t) {
                        Toasty.normal(activity, Conversion.NETWORKERROR).show();
                    }
                });
        setUserProvider();
    }

    @Override
    public EaseUser getUser(String username) {
        if (!TextUtils.isEmpty(username) && usersInfoMap != null) {
            HuanxinHeadBean.DataBean dataBean = usersInfoMap.get(username);
            if (dataBean == null) {
                return null;
            }
            String userAvatarPath = dataBean.getHeadImg();
            String userNickname = dataBean.getNickName();
            if (!TextUtils.isEmpty(userAvatarPath)) {
                EaseUser easeUser = new EaseUser(username);
                easeUser.setAvatar(IpConfig.HttpPic + userAvatarPath);
                easeUser.setNickname(userNickname);
                return easeUser;
            }
        }
        return null;
    }

    private static void setUserProvider() {
        if (EaseUI.getInstance().getUserProfileProvider() == null) {
            EaseUI.getInstance().setUserProfileProvider(mInstance);
        }
    }

    private static void initMapData(List<HuanxinHeadBean.DataBean> usersInfoList) {
        if (usersInfoList == null || usersInfoList.size() == 0) {
            return;
        }
        if (usersInfoMap == null) {
            usersInfoMap = new HashMap<>();
        }

        for (HuanxinHeadBean.DataBean dataBean : usersInfoList) {
            if (dataBean != null && !TextUtils.isEmpty(dataBean.getUserPhone()) && !TextUtils.isEmpty(dataBean.getHeadImg())) {
                usersInfoMap.put(dataBean.getUserPhone(), dataBean);
            }
        }

    }
}
