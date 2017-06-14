package com.jsz.peini.presenter.ad;

import com.jsz.peini.model.ad.AdModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2017/1/11.
 */

public interface Ad {
    /**
     * 广告接口
     * String adType 广告类型：1启动广告；2首页弹窗；3商家轮播；4广场轮播；5活动轮播；6优惠券轮播
     */
    @POST("getAdvertise")
    Call<AdModel> getAdvertise(
            @Query("adType") String adType);
}
