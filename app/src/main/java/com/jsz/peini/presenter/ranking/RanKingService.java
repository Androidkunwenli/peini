package com.jsz.peini.presenter.ranking;

import com.jsz.peini.model.ranking.RanKingBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2017/1/19.
 * 排行榜接口
 */

public interface RanKingService {
    /**
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/ getRank
     * 入参：
     * 名称	类型	长度	说明
     * dType	Int		0:总排行榜 1:月排行榜
     * rType	Int		1:金币榜2:土豪榜3:诚信榜
     * userId	String		用户id
     */
    @POST("getRank")
    Call<RanKingBean> getRank(
            @Query("dType") String dType,
            @Query("rType") String rType,
            @Query("userId") String userId);
}
