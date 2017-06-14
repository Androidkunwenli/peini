package com.jsz.peini.san.huanxin;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 15089 on 2017/2/16.
 */

public interface HuanXinService {
    /**
     * 1获取用户头像
     */
    @POST("getEmUserHeadAndNickname")
    Call<HuanxinHeadBean> getEmUserHeadAndNickname(
            @Query("nameStr") String nameStr);
}
