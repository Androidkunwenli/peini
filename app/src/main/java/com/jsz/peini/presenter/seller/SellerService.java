package com.jsz.peini.presenter.seller;

import com.jsz.peini.model.address.SellerAddress;
import com.jsz.peini.model.seller.SellerBean;
import com.jsz.peini.model.seller.SellerCodesBySellerCodesBean;
import com.jsz.peini.model.seller.SellerMessageInfoBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/15.
 */

public interface SellerService {
    @POST("getDistrictList")
    Call<SellerAddress> getCityList();

    @POST("getSellerCodesBySellerCodes")
    Call<SellerCodesBySellerCodesBean> getSellerCodesBySellerCodesBean();


    @POST("getSellerInfoBySellerInfo")
    Call<SellerBean> getSellerInfoBySellerInfo(
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("sellerType") String xpsellerTypeoint,
            @Query("distance") String distance,
            @Query("districtCode") String districtCode,
            @Query("labelsId") String labelsId,
            @Query("sort") String sort,
            @Query("pageNow") int pageNow,
            @Query("pageSize") String pageSize,
            @Query("cityCode") String cityCode);

    /**
     * 店铺详细信息
     */
    @POST("getSellerInfo")
    Call<SellerMessageInfoBean> getSellerInfo(
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("id") String id,
            @Query("type") String type,
            @Query("orderId") String orderId);

    /**
     * 商家文职信息
     */
    @POST("getDistrictList")
    Call<SellerAddress> getDistrictList();
}
