package com.jsz.peini.presenter.setting;

import com.jsz.peini.model.CrtyBean;
import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.setting.UserSmsCntBean;
import com.jsz.peini.model.setting.VersionNoBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by th on 2016/12/17.
 */

public interface SettingService {
    /**
     * 省份	sellerProvince
     * 城市	sellerCity
     * userToken
     * sellerName
     * 类型	sellerType
     * contactName
     * contactPhone
     */
    @POST("sellerJoin")
    Call<SuccessfulBean> sellerJoin(
            @Query("userToken") String userToken,
            @Query("sellerProvince") String sellerProvince,
            @Query("sellerCity") String sellerCity,
            @Query("sellerName") String sellerName,
            @Query("sellerType") String sellerType,
            @Query("contactName") String contactName,
            @Query("contactPhone") String contactPhone);

    /**
     * 获取程序城市列表
     */
    @POST("getCityList")
    Call<CrtyBean> getCityList();

    /**
     * 修改原始密码
     * resetPass
     */
    @POST("resetPass")
    Call<SuccessfulBean> resetPass(@Query("userToken") String userToken,
                                   @Query("oldPass") String oldPass,
                                   @Query("newPass") String newPass);


    /**
     * 修改支付密码获取验证码
     * resetPass
     */
    @POST("smsSendPay")
    Call<GainSmsBean> smsSendPay(@Query("userToken") String userToken);

    /**
     * 修改支付密码获取验证码
     * resetPass
     */
    @POST("updatePayPassword")
    Call<SuccessfulBean> updatePayPassword(@Query("userPassword") String userPassword,
                                           @Query("userToken") String userToken,
                                           @Query("yzm") String yzm);

    /**
     * app升级接口
     */
    @POST("share/appVersionUpdate")
    Call<VersionNoBean> appVersionUpdate(@Query("type") String type,
                                         @Query("versionNo") String versionNo);

    /**
     * 获取所有未读消息数量
     */
    @POST("getUserSmsCnt")
    Call<UserSmsCntBean> getUserSmsCnt(@Query("userId") String userId);

    /**
     * 获取未读消息数量
     *
     * @param userId token || userId
     * @param type   不传为所有；1、系统消息；2、小秘书
     * @return 消息数量
     */
    @POST("getUserSmsCnt")
    Call<UserSmsCntBean> getUserSmsCnt(
            @Query("userId") String userId,
            @Query("type") int type);
}
