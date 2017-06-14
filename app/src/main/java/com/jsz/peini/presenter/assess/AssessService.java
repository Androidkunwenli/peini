package com.jsz.peini.presenter.assess;

import com.jsz.peini.model.setting.SuccessfulBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/20.
 */

public interface AssessService {
    /**
     * 接口地址: http://192.168.150.182:8080/pnservice/sellerJudge
     * 入参：
     * 名称	类型	长度	说明
     * sellerInfoId	Int 		店铺id
     * sellerServer	Int		生活服务评分(需要乘以20)
     * sellerCondition	Int		坏境条件评分(需要乘以20)
     * sellerMeal	Int		口味评分(需要乘以20)
     * orderId	Int		订单id
     */

    @POST("sellerJudge")
    Call<SuccessfulBean> sellerJudge(
            @Query("userId") String userId,
            @Query("sellerInfoId") String sellerInfoId,
            @Query("sellerServer") String sellerServer,
            @Query("sellerCondition") String sellerCondition,
            @Query("sellerMeal") String sellerMeal,
            @Query("orderId") String orderId);

    /**
     * 接口说明：任务买单用户和店铺评价接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/sellerAndUserJudge
     * 入参：
     * 名称	类型	长度	说明
     * sellerInfoId	Int 		店铺id
     * taskId	Int		任务id
     * sellerServer	Int		生活服务评分(需要乘以20)
     * sellerCondition	Int		坏境条件评分(需要乘以20)
     * sellerMeal	Int		口味评分(需要乘以20)
     * userId	Int		评分用户id
     * otherUserId	Int		被评分用户id
     * userLife	Int		生活品味评分(需要乘以20)
     * userImage	Int		形象评分(需要乘以20)
     * userDate	Int		交往评分(需要乘以20)
     * gold	Int		打赏金额(需要乘以100)
     * 注:无打赏金额传0
     * orderId	Int		订单id
     */
    @POST("sellerAndUserJudge")
    Call<SuccessfulBean> sellerAndUserJudge(
            @Query("sellerInfoId") String sellerInfoId,
            @Query("taskId") String taskId,
            @Query("sellerServer") String sellerServer,
            @Query("sellerCondition") String sellerCondition,
            @Query("sellerMeal") String sellerMeal,
            @Query("userId") String userId,
            @Query("otherUserId") String otherUserId,
            @Query("userLife") String userLife,
            @Query("userImage") String userImage,
            @Query("userDate") String userDate,
            @Query("orderId") String orderId);
}
