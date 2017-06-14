package com.jsz.peini.presenter;

import com.jsz.peini.model.pay.PaySuccessfulBean;
import com.jsz.peini.model.setting.SuccessfulBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2017/4/26.
 */

public interface ApiService {
    /**
     * 活动推送  activity/getNotifyLink
     */
    @POST("activity/getNotifyLink")
    Call<SuccessfulBean> getNotifyLink(@Query("id") String id, @Query("userId") String userId);
}
