package com.jsz.peini.presenter.map;

import com.jsz.peini.model.map.BaiduMapBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/14.
 */

public interface BaiduMapService {
    /**
     * 地图任务合并列表获取接口
     * 接口说明：地图任务合并列表获取接口
     * 调用方式：post
     * 接口地址：http://192.168.150.140:8089/pnservice/getTaskMapList
     * 入参：
     * 名称	类型	长度	说明
     * xpoint	String		X坐标
     * ypoint	String		Y坐标
     * sort	Int		排序方式
     * （不传）
     * otherSex	Int		性别（不限不传1男2女）
     * otherLowAge	Int		年龄最低限（没有不传）
     * otherHignAge	Int		年龄最高限（没有不传）
     * otherLowheight	Int		身高最低限（没有不传）
     * otherHignheight	Int		身高最高限（没有不传）
     * isVideo	Int		是否视频验证（1或不传）
     * isIdcard	Int		是否身份认证（1或不传）
     * sellerType	Int		商家类别（全部为不传（101 美食102 唱歌103 电影104 运动健身105 休闲娱乐106 酒店107 丽人108 生活服务109 其他））
     */
    @POST("getTaskMapList")
    Call<BaiduMapBean> getTaskMapList(
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("distance") int distance,
            @Query("sort") String sort,
            @Query("otherSex") String otherSex,
            @Query("otherLowAge") String otherLowAge,
            @Query("otherHignAge") String otherHignAge,
            @Query("otherLowheight") String otherLowheight,
            @Query("otherHignheight") String otherHignheight,
            @Query("isVideo") String isVideo,
            @Query("isIdcard") String isIdcard,
            @Query("sellerType") String sellerType,
            @Query("taskCity") String taskCity);
}
