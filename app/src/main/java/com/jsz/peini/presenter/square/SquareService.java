package com.jsz.peini.presenter.square;

import com.jsz.peini.model.JsonResponse;
import com.jsz.peini.model.setting.SuccessfulBean;
import com.jsz.peini.model.square.AddCommentListBean;
import com.jsz.peini.model.square.AddLikeListBean;
import com.jsz.peini.model.square.CouponInfoList;
import com.jsz.peini.model.square.CouponInfoListAllUnGetByScore;
import com.jsz.peini.model.square.CouponInfoUnGet;
import com.jsz.peini.model.square.DonationDelData;
import com.jsz.peini.model.square.IDEntityBean;
import com.jsz.peini.model.square.MiBillBean;
import com.jsz.peini.model.square.MiLabelBean;
import com.jsz.peini.model.square.MiSignBean;
import com.jsz.peini.model.square.MiSignDataBean;
import com.jsz.peini.model.square.MiWealthABean;
import com.jsz.peini.model.square.MyBackgroundBean;
import com.jsz.peini.model.square.MyCreditBean;
import com.jsz.peini.model.square.MyTaskAllBean;
import com.jsz.peini.model.square.RecentDonationData;
import com.jsz.peini.model.square.ScoreHistoryListBean;
import com.jsz.peini.model.square.SquareBean;
import com.jsz.peini.model.square.TaCreditBean;
import com.jsz.peini.model.square.UpdateUserBgImgBean;
import com.jsz.peini.model.square.UserImageAllByUserId;
import com.jsz.peini.model.square.UserInfoByOtherId;
import com.jsz.peini.model.square.UserInfoCodesBean;
import com.jsz.peini.model.square.UserInfoIdBean;
import com.jsz.peini.model.square.VerifyDataBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/23.
 */

public interface SquareService {
    /**
     * 空间详情
     */
    @POST("getSquareInfoList")
    Call<SquareBean> getSquareInfoList(
            @Query("userId") String userId,
            @Query("otherId") String otherId,
            @Query("type") String type,
            @Query("pageNow") int pageNow,
            @Query("pageSize") int pageSize);

    /**
     * 接口地址：http://192.168.150.182:8080/pnservice/goComment
     * 入参：
     * 名称	类型	长度	说明
     * squareInfoId	Int		广场id
     * userId	Int		评论用户id
     * toUserId	Int		被回复用户id
     * content	String		评论内容
     * 回参：
     * 名称	类型	长度	说明
     * commentList	Json		评论详情列表
     */
    @POST("goComment")
    Call<AddCommentListBean> goComment(
            @Query("squareInfoId") String squareInfoId,
            @Query("userId") String userId,
            @Query("toUserId") String toUserId,
            @Query("content") String content);

    /**
     * 广场增加(取消)点赞接口
     * 接口说明：广场增加(取消)点赞接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/goSquareLike
     * 入参：
     * 名称	类型	长度	说明
     * squareId	Int		广场id
     * userId	Int		点赞用户id
     * 回参：
     * 名称	类型	长度	说明
     * userList	Json		点赞用户列表
     * userCount	Int		点赞人数
     */
    @POST("goSquareLike")
    Call<AddLikeListBean> goSquareLike(
            @Query("squareId") String squareId,
            @Query("userId") String userId);

    /**
     * 删除广场评论接口
     * 接口说明：删除广场评论接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/delComment
     * 入参：
     * 名称	类型	长度	说明
     * id	Int		评论唯一id
     * squareInfoId	Int		广场id
     * 回参：
     * 名称	类型	长度	说明
     * commentList	Json		评论详情列表
     */
    @POST("delComment")
    Call<SuccessfulBean> delComment(
            @Query("id") String id,
            @Query("userToken") String squareInfoId);

    /**
     * 接口说明：广场动态发布接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/setSquareInfoBySquareInfo
     * 入参：
     * 名称	类型	长度	说明
     * userId	Int		用户id
     * xpoint	String		坐标x
     * ypoint	String		坐标y
     * content	String		信息内容
     * address	String		地址
     */
    @Multipart
    @POST("setSquareInfoBySquareInfo")
    Call<SuccessfulBean> setSquareInfoBySquareInfo(
            @Query("userId") String userId,
            @Query("xpoint") String xpoint,
            @Query("ypoint") String ypoint,
            @Query("content") String content,
            @Query("address") String address,
            @Part MultipartBody.Part[] file);

    /**
     * /**
     * 上传一张图片
     *
     * @return
     * @Multipart
     * @POST("/upload") Call<String> uploadImage(@Part("fileName") String description,
     * @Part("file\"; filename=\"image.png\"") RequestBody imgs);
     */


//            @Part("file\"; filename=\"image.png") List<MultipartBody.Part> parts);

    /*RequestBody imgFile = RequestBody.create(MediaType.parse("image/*"), imgFile);*/
    @POST("setSquareInfoBySquareInfoNull")
    Call<SuccessfulBean> setSquareInfoBySquareInfo(@Query("userId") String userId,
                                                   @Query("xpoint") String xpoint,
                                                   @Query("ypoint") String ypoint,
                                                   @Query("content") String content,
                                                   @Query("address") String address);

    /**
     * 广场信息删除接口
     * 接口说明：广场信息删除接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/deleteSquareAll
     * 入参：
     * 名称	类型	长度	说明
     * squareInfoId	Int		信息id
     */
    @POST("deleteSquareAll")
    Call<SuccessfulBean> deleteSquareAll(
            @Query("userToken") String userToken,
            @Query("squareInfoId") String squareInfoId);

    /**
     * 获取个人主页信息
     */
    @POST("getUserAllInfo")
    Call<UserInfoByOtherId> getUserAllInfo(
            @Query("userToken") String userId);

    @POST("getNicknameAndImgHead")
    Call<UserInfoByOtherId> getNicknameAndImgHead(
            @Query("userId") String userId);

    /**
     * 查询他人空间详细信息接口
     * 接口说明：查询他人空间详细信息接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/getUserInfoByOtherId
     * 入参：
     * 名称	类型	长度	说明
     * userId	Int		用户id
     * myId	Int		本人用户id
     */
    @POST("getUserInfoByOtherId")
    Call<UserInfoByOtherId> getUserInfoByOtherId(
            @Query("userId") String userId,
            @Query("myId") String myId);

    /**
     * 关注接口
     * 接口说明：关注他人
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/goConcern
     * 入参：
     * 名称	类型	长度	说明
     * userId	Int		用户id
     * otherId	Int		被关注人id
     */
    @POST("goConcern")
    Call<SuccessfulBean> goConcern(
            @Query("userId") String userId,
            @Query("otherId") String otherId);

    /**
     * 取消关注接口
     * 接口说明：取消关注他人
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/delConcern
     * 入参：
     * 名称	类型	长度	说明
     * userId	Int		用户id
     * otherId	Int		被关注人id
     */
    @POST("delConcern")
    Call<SuccessfulBean> delConcern(
            @Query("userId") String userId,
            @Query("otherId") String otherId);

    /**
     * 身份认证接口
     */
    @POST("isIdcard")
    Call<IDEntityBean> isIdcard(
            @Query("userId") String userId,
            @Query("idcardName") String idcardName,
            @Query("idcardId") String idcardId);

    /* 获取个人信息*/
    @POST("getUserInfo")
    Call<UserInfoIdBean> getUserInfo(
            @Query("userId") String userId);

    /* 获取个人信息相册*/
    @POST("getUserImageAllByUserId")
    Call<UserImageAllByUserId> getUserImageAllByUserId(
            @Query("userId") String userId,
            @Query("page") int page,
            @Query("rows") int rows
    );

    /**
     * 照片删除接口
     * 接口说明：照片删除接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/deleteUserImage
     * 入参：
     * 名称	类型	长度	说明
     * imageId	String		图片id(逗号分割)
     */

    @POST("deleteUserImage")
    Call<SuccessfulBean> deleteUserImage(
            @Query("imageId") String userId);

    /**
     * 照片上传接口
     * 接口说明：照片上传接口
     * 调用方式：post
     * 接口地址: http://192.168.150.182:8080/pnservice/setUserImages
     * 入参：
     * 名称	类型	长度	说明
     * userInfoId	String		用户id
     * file	File		文件
     */
    @Multipart
    @POST("setUserImages")
    Call<SuccessfulBean> setUserImages(
            @Query("userInfoId") String userInfoId,
            @Part MultipartBody.Part file);

    /**
     * 财富积分查询接口
     * 接口说明：财富积分查询接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/getUserGoldAndScore
     */
    @POST("getUserGoldAndScore")
    Call<MiWealthABean> getUserGoldAndScore(
            @Query("userId") String userId);

    /**
     * 更新用户头像地址接口
     * 接口说明：更新用户头像地址接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/updateUserImageHead
     * 入参：
     * 名称	类型	长度	说明
     * file	file		头像文件
     * userId	String		用户id
     */
    @Multipart
    @POST("updateUserImageHead")
    Call<SuccessfulBean> updateUserImageHead(
            @Part MultipartBody.Part file,
            @Query("userId") String userId);

    /**
     * 我的信誉值
     */
    @POST("getCredit")
    Call<MyCreditBean> getCredit(
            @Query("userId") String userId);

    /* /**
     * 我的信誉值
     */
    @POST("getCredit")
    Call<TaCreditBean> getCredit(
            @Query("userId") String userId,
            @Query("otherId") String otherId);


    /**
     * 优惠券
     */
    @POST("getCouponInfoList_allUnGet_byScore")
    Call<CouponInfoListAllUnGetByScore> getCouponInfoList_allUnGet_byScore(
            @Query("userToken") String userToken,
            @Query("page") int page,
            @Query("rows") int rows);

    /**
     * 10、优惠券获取接口
     */
    @POST("getCouponInfoByReceive")
    Call<JsonResponse> getCouponInfoByReceive(
            @Query("userId") String userId,
            @Query("id") String id,
            @Query("getType") String getType);

    /**
     * 优惠券详情
     */
    @POST("getCouponInfo_unGet")
    Call<CouponInfoUnGet> getCouponInfo_unGet(
            @Query("userToken") String userToken,
            @Query("id") String di,
            @Query("getType") String getType);

    /**
     * 空间背景
     */
    @POST("getSysBagImages")
    Call<MyBackgroundBean> getSysBagImages(
    );

    /**
     * 设置空间背景 设置
     */
    @POST("updateUserBgImg")
    Call<UpdateUserBgImgBean> updateUserBgImg(
            @Query("userToken") String userToken,
            @Query("filePath") String filePath);

    /**
     * 图框选择
     */
    @Multipart
    @POST("updateUserBgImg")
    Call<UpdateUserBgImgBean> updateUserBgImg(
            @Query("userToken") String userToken,
            @Part MultipartBody.Part file,
            @Query("filePath") String filePath);


    /**
     * 积分明细
     */
    @POST("getScoreHistoryList")
    Call<ScoreHistoryListBean> getScoreHistoryList(
            @Query("page") int page,
            @Query("rows") int rows,
            @Query("userId") String userId,
            @Query("type") String type);

    /**
     * 删除积分明细
     */
    @POST("delScoreHistory")
    Call<SuccessfulBean> delScoreHistory(
            @Query("userId") String userId,
            @Query("idStr") String idStr);

    /**
     * 我的任务列表
     * http://60.205.168.94:8080/pnservice/getTaskInfoByUserId?userId=d8f2ac9d5dc0432bb3e1cf52375b20cb
     * sort	Int 		查询类别（全部为空1发布任务2接受任务）
     * taskStatus	Int		任务状态（全部为空1发布中2进行中3待评价4已完成5已关闭）
     */

    @POST("getTaskInfoByUserId")
    Call<MyTaskAllBean> getTaskInfoByUserId(
            @Query("userId") String userId,
            @Query("sort") String sort,
            @Query("taskStatus") String taskStatus,
            @Query("pageNow") int pageNow,
            @Query("pageSize") String pageSize
    );


    /**
     * 我去我的空间修改接口
     */
    @POST("getUserInfoCodes")
    Call<UserInfoCodesBean> getUserInfoCodes();

    /**
     * 更改我的饿信息接口
     * id	String	 	用户账号id即userId
     * nickname	String		昵称
     * sex	int		1:男 2:女
     * height	int		身高
     * weight	int		体重
     * now_province	int		所在地省
     * now_city	int		所在地市
     * now_county	int		所在地区
     * old_province	int		故乡省
     * old_city	int		故乡市
     * old_county	int		故乡区
     * constellation	int		星座
     * nation	int		民族
     * emotion	int		情感
     * small_income	int		最小收入
     * big_income	int		最大收入
     * degree	int		学历
     * industry	int		行业
     * is_house	int		是否购房
     * is_car	int		是否购车
     */
    @POST("updateUserInfo")
    Call<UserInfoCodesBean> updateUserInfo(
            @Query("id") String id,
            @Query("nickname") String nickname,
            @Query("sex") String sex,
            @Query("height") String height,
            @Query("weight") String weight,
            @Query("nowProvince") String now_province,
            @Query("nowCity") String nowCity,
            @Query("nowCounty") String now_county,
            @Query("oldProvince") String old_province,
            @Query("oldCity") String old_city,
            @Query("oldCounty") String old_county,
            @Query("constellation") String constellation,
            @Query("nation") String nation,
            @Query("emotion") String emotion,
            @Query("smallIncome") String small_income,
            @Query("bigIncome") String big_income,
            @Query("degree") String degree,
            @Query("industry") String industry,
            @Query("isHouse") String is_house,
            @Query("isCar") String is_car
    );
    /**
     *{
     bigIncome = "";
     constellation = "";
     degree = "";
     emotion = "";
     height = "";
     id = 763a8327b8984c4b8d319760bfad5f78;
     industry = "";
     isCar = 2;
     isHouse = 2;
     nation = "";
     nickname = "\U6d4b\U8bd5\U4eba\U54581";
     nowCity = "";
     nowCounty = "";
     nowProvince = "";
     oldCity = "";
     oldCounty = "";
     oldProvince = "";
     sex = 2;
     smallIncome = "";
     weight = "";
     }
     */

    /**
     * 签到
     */
    @POST("insertSignInfo")
    Call<MiSignBean> insertSignInfo(
            @Query("userId") String userId,
            @Query("setDate") String setDate
    );

    /**
     * 获取签到
     */
    @POST("getUserSignInfo")
    Call<MiSignDataBean> getUserSignInfo(
            @Query("userId") String userId,
            @Query("start") String start,
            @Query("end") String end
    );


    /**
     * 27、账单明细接口
     * userId
     * orderType		1任务买单；3到店买单；4打赏；5金币转赠
     * status		-1已取消；2已完成；0待付款 1待评价
     * payType		1金币；2微信；3支付宝
     */
    @POST("getMyOrderList")
    Call<MiBillBean> getMyOrderList(
            @Query("userId") String userId,
            @Query("orderType") String orderType,
            @Query("status") String status,
//            @Query("stream") String stream,     * stream		1收入；0支出
            @Query("payType") String payType,
            @Query("pageNow") int pageNow,
            @Query("pageSize") int pageSize
    );

    /**
     * 获取标签接口
     */
    @POST("getLabelInfoList")
    Call<MiLabelBean> getLabelInfoList(
            @Query("userId") String userId
    );

    /**
     * 切换标签接口
     */
    @POST("setLabelInfoBylabelState")
    Call<SuccessfulBean> setLabelInfoBylabelState(
            @Query("userId") String userId,
            @Query("labelInfo") String labelInfo);

    /**
     * 修改签名
     */
    @POST("updateUserSignWord")
    Call<SuccessfulBean> updateUserSignWord(
            @Query("id") String id,
            @Query("signWord") String signWord);

    /**
     * 金币转增确认接口
     * userId
     * otherId		他人id
     * other		用户id或者手机号
     */
    @POST("payGoldSureUserInfo")
    Call<VerifyDataBean> payGoldSureUserInfo(
            @Query("userId") String userId,
            @Query("otherId") String otherId,
            @Query("other") String other,
            @Query("taskId") String taskId
    );

    /**
     * recentDonation	输入参数
     * 说明	名称	类型
     * userId
     * token		32位密钥
     * appA		交换参数
     * page
     * rows
     * 最近转账信息
     */
    @POST("payGold/recentDonation")
    Call<RecentDonationData> recentDonation(
            @Query("userId") String userId,
            @Query("token") String token,
            @Query("appA") String appA,
            @Query("page") int page,
            @Query("rows") String rows);

    /*删除金币转账接口*/
    @POST("payGold/recentDonationDel")
    Call<DonationDelData> recentDonationDel(
            @Query("userId") String userId,
            @Query("token") String token,
            @Query("appA") String appA,
            @Query("hisId") String hisId);


    /**
     * 38、我的优惠券列表接口
     *
     * @param userId userId
     * @param type   不传所有；-1过期
     * @param page
     * @param rows   @return
     */
    @POST("getCouponInfoByAll")
    Call<CouponInfoList> getCouponInfoByAll(
            @Query("userId") String userId,
            @Query("type") String type,
            @Query("page") int page,
            @Query("rows") int rows
    );

    /**
     * 39、获取即将过期优惠券推送接口
     *
     * @param userId userId
     * @return
     */
    @POST("getCouponInfoNotify")
    Call<JsonResponse> getCouponInfoNotify(
            @Query("userId") String userId);

    /**
     * 48、启动页数据采集接口
     *
     * @param userId   userId（非必填）
     * @param deviceNo 设备号
     * @param channel  渠道号
     * @param userId   设备类型 1.Android 2.iOS
     * @return
     */
    @POST("app/appStart")
    Call<JsonResponse> appStart(
            @Query("userId") String userId,
            @Query("devNo") String deviceNo,
            @Query("channel") String channel,
            @Query("devtype") String devType
    );

    /**
     * 获取排行版接口
     *
     * @param userId userId（非必填）
     * @return
     */
    @POST("getSwitchRank")
    Call<JsonResponse> getSwitchRank(
            @Query("userId") String userId
    );

    /**
     * 获取排行版接口
     *
     * @param userId userId（非必填）
     * @return
     */
    @POST("updateSwitchRank")
    Call<JsonResponse> updateSwitchRank(
            @Query("userId") String userId,
            @Query("isRank") String isRank
    );
}
