package com.jsz.peini.presenter.news;

import com.jsz.peini.model.news.MyFans;
import com.jsz.peini.model.news.NewsList;
import com.jsz.peini.model.news.TaskInfoByPhonesBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2017/1/2.
 */

public interface NewService {
    /**
     * 我关注的人列表接口
     * 接口说明：我关注的人列表接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/myConcern
     * 入参：
     * 名称	类型	长度	说明
     * userId	Int		用户id
     * 回参：
     * 名称	类型	长度	说明
     * objectList	Json		我关注的人的列表
     */
    @POST("myConcern")
    Call<NewsList> myConcern(
            @Query("userId") String userId,
            @Query("page") int page,
            @Query("rows") int rows

    );

    /**
     * 我的粉丝列表接口
     * 接口说明：我的粉丝列表接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/myFans
     * 入参：
     * 名称	类型	长度	说明
     * userId	Int		用户id
     * 回参：
     * 名称	类型	长度	说明
     * objectList	Json		我的粉丝列表
     */
    @POST("myFans")
    Call<MyFans> myFans(
            @Query("userId") String userId,
            @Query("page") int page,
            @Query("rows") int rows);

    /**
     * 聊天界面获取任务借口
     */

    @POST("getTaskInfoByPhones")
    Call<TaskInfoByPhonesBean> getTaskInfoByPhones(
            @Query("userId") String userId,
            @Query("nameStr") String nameStr,
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint);
}
