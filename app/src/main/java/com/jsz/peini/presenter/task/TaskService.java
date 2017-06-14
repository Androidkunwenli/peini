package com.jsz.peini.presenter.task;

import com.jsz.peini.model.ActivityRegisterBean;
import com.jsz.peini.model.filter.HotWordBean;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.setting.VersionNoBean;
import com.jsz.peini.model.square.TaTaskBean;
import com.jsz.peini.model.square.VerifyDataBean;
import com.jsz.peini.model.tabulation.SystemMessageListBean;
import com.jsz.peini.model.tabulation.TabulationMessageBean;
import com.jsz.peini.model.tabulation.TaskListBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/19.
 */

public interface TaskService {
    /**
     * userToken	String		token
     * taskProvince	Int		任务所在省
     * taskCity	Int		任务所在市
     * sellerInfoId	Int		店铺id
     * sellerInfoName	String		店铺名称
     * taskAppointedTime	String		任务约定时间
     * taskDesc	String		任务描述
     * otherSex	Int		性别（1男2女3不限）
     * otherLowAge	Int		低年龄
     * otherHignAge	Int		高年龄
     * otherBuy	Int		买单（1我买单2他买单3AA制）
     * otherGo	Int		出行（1我接ta 2 ta接我3自由行）
     * xpoint	String		x坐标
     * ypoint	String		y坐标
     * sellerBigType			商家大类别id
     * sellerBigName	Int		商家大类别名称
     * sellerSmallType	Int		商家小类别标签id
     * sellerSmallName	String		商家小类别标签名称
     */
    @POST("setTaskInfo")
    Call<SuccessfulBean> setTaskInfo(
            @Query("userId") String userToken,
            @Query("taskProvince") String taskProvince,
            @Query("taskCity") String taskCity,
            @Query("sellerInfoId") String sellerInfoId,
            @Query("taskAppointedTime") String taskAppointedTime,
            @Query("taskDesc") String taskDesc,
            @Query("otherSex") String otherSex,
            @Query("otherLowAge") String otherLowAge,
            @Query("otherHignAge") String otherHignAge,
            @Query("otherBuy") String otherBuy,
            @Query("otherGo") String otherGo,
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint);

    /**
     * 任务列表筛选接口
     * 接口说明：任务列表筛选接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/selectTaskInfoBySort
     * 入参：
     * 名称	类型	长度	说明
     * xpoint	String		X坐标
     * <p>
     * ypoint	String		Y坐标
     * <p>
     * sort	Int		排序方式
     * <p>
     * （1时间（默认1）2距离）
     * <p>
     * otherSex	Int		性别（不限不传1男2女）
     * <p>
     * otherLowAge	Int		年龄最低限（没有不传）
     * <p>
     * otherHignAge	Int		年龄最高限（没有不传）
     * <p>
     * otherLowheight	Int		身高最低限（没有不传）
     * <p>
     * otherHignheight	Int		身高最高限（没有不传）
     * <p>
     * isVideo	Int		是否视频验证（1或不传）
     * <p>
     * isIdcard	Int		是否身份认证（1或不传）
     * <p>
     * sellerType	Int		商家类别（全部为不传）
     * <p>
     * 回参：
     * 名称	类型	长度	说明
     * taskMapList	Json		用户数据
     * distanceNum	Int		距离
     * userLabel	Json		用户标签
     * http://192.168.150.158:8089/pnservice/selectTaskInfoBySort?
     * xpoint=38.045669&
     * ypoint=114.531096&
     * sort=1
     * &otherSex=
     * &otherLowAge=
     * &otherHignAge
     * &otherLowheight
     * &otherHignheight
     * &isVideo&
     * isIdcard
     * &sellerType
     * &taskCity=130100
     */
    @POST("selectTaskInfoBySort")
    Call<TaskListBean> selectTaskInfoBy(
            @Query("userId") String userid,
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
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

    /*点击列表*/
    @POST("selectTaskInfoBySort")
    Call<TaskListBean> selectTaskInfoBySort(
            @Query("userId") String userId,
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("sort") String sort,
            @Query("taskCity") String taskCity,
            @Query("idStr") String idStr,
            @Query("otherSex") String otherSex,
            @Query("otherLowAge") String otherLowAge,
            @Query("otherHignAge") String otherHignAge,
            @Query("otherLowheight") String otherLowheight,
            @Query("otherHignheight") String otherHignheight,
            @Query("isVideo") String isVideo,
            @Query("isIdcard") String isIdcard,
            @Query("sellerType") String sellerType);

    /**
     * 任务（详情）接口
     * 接口说明：任务（详情）接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/getTaskInfo
     * 入参：
     * 名称	类型	长度	说明
     * id 	Int 		任务id
     * userId	Int 		userId
     * xpointMe	String		当前x坐标
     * ypointMe	Stirng		当前y坐标
     */
    //http://192.168.150.158:8089/pnservice/getTaskInfo?id=62&taskStatus=1&xpointMe=38.045847&ypointMe=114.531498
    @POST("getTaskInfo")
    Call<TabulationMessageBean> getTaskInfo(
            @Query("id") String id,
            @Query("userId") String userId,
            @Query("xpointMe") String xpointMe,
            @Query("ypointMe") String ypointMe,
            @Query("type") String type,
            @Query("orderId") String orderId);

    /**
     * 任务（参加）接口
     * 接口说明：任务（参加）接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/updateTaskInfo
     * 入参：
     * 名称	类型	长度	说明
     * otherUserId	Int 		当前用户id
     * id	Int 		任务id
     * taskStatus	Int		任务状态
     * 回参：
     */
    @POST("updateTaskInfo")
    Call<SuccessfulBean> updateTaskInfo(
            @Query("otherUserId") String otherUserId,
            @Query("id") String id,
            @Query("taskStatus") String taskStatus);

    /**
     * 取消任务
     */
    @POST("cancelTaskInfo")
    Call<SuccessfulBean> cancelTaskInfo(
            @Query("userId") String userId,
            @Query("taskId") String taskId,
            @Query("cancleType") String cancleType);

    /**
     * 34、获取热门搜索词列表接口
     */
    @POST("getHotWord")
    Call<HotWordBean> getHotWord(@Query("type") String type);

    /**
     * 35、热词搜索告知服务器接口
     */
    @POST("searchByHotWord")
    Call<SuccessfulBean> searchByHotWord(
            @Query("hotName") String taskId,
            @Query("type") int type);

    /*cancelTaskInfo_result	输入参数
        说明	名称	类型
            userId	String
        任务id	taskId	int
        类型	type	int	1同意；0拒绝
    */
    @POST("cancelTaskInfo_result")
    Call<SuccessfulBean> cancelTaskInfoResult(
            @Query("userId") String userId,
            @Query("taskId") String taskId,
            @Query("type") String type);


    /**
     * delTaskInfo	输入参数
     * 说明	名称	类型
     * userId
     * taskId
     */

    @POST("delTaskInfo")
    Call<SuccessfulBean> delTaskInfo(
            @Query("userId") String userId,
            @Query("taskId") int taskId);

    /**
     * 他人任务
     */
    @POST("getTaskInfoByUserId")
    Call<TaTaskBean> getTaskInfoByUserId(
            @Query("userId") String userId,
            @Query("sort") String sort,
            @Query("taskStatus") String taskStatus,
            @Query("pageNow") int pageNow,
            @Query("pageSize") String pageSize
    );

    /**
     * activityRegister	42、活动报名接口
     * userId           userId
     * acId             活动Id
     */
    @POST("activity/register")
    Call<ActivityRegisterBean> activityRegister(
            @Query("userId") String userId,
            @Query("acId") String activityId);

    /**
     * activityRegister	40、获取系统消息接口
     * userId           userId
     * page             page
     * rows             rows
     */
    @POST("getUserSmsList")
    Call<SystemMessageListBean> getUserSmsList(
            @Query("userId") String userId,
            @Query("page") int page,
            @Query("rows") int rows);

    /**
     * 活动分享
     */
    @POST("share/getActivityShare")
    Call<VersionNoBean> shareGetActivityShare(
            @Query("id") String userId);


    /**
     * 头像判断
     */
    @POST("userIsHead")
    Call<SuccessfulBean> userIsHead(
            @Query("userId") String userId);
}
