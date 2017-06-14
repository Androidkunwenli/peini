package com.jsz.peini.presenter.login;

import com.jsz.peini.model.login.GainSmsBean;
import com.jsz.peini.model.login.LoginSuccess;
import com.jsz.peini.model.login.SanLoginSuccess;
import com.jsz.peini.model.login.VerificationSmsBean;
import com.jsz.peini.model.login.updataPassword;
import com.jsz.peini.model.setting.SuccessfulBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by kunwe on 2016/12/1.
 */

public interface LoginService {

    /**
     * 新注册接口
     *
     * @param phone    手机号
     * @param password 密码
     * @param appA     互换的加密码
     * @param deviceNo 设备号
     * @param channel  渠道号
     * @param devType  安卓固定为 "1"
     * @param smsCode  手机验证码
     * @param recId    邀请人Id
     */
    @POST("registerUserLoginNew")
    Call<LoginSuccess> register(
            @Query("userName") String phone,
            @Query("userPassword") String password,
            @Query("yzm") String smsCode,
            @Query("appA") String appA,
            @Query("devNo") String deviceNo,
            @Query("channel") String channel,
            @Query("devtype") String devType,
            @Query("recId") String recId,
            @Query("thirdType") int thirdType,
            @Query("thirdName") String thirdName

    );

//    /*注册接口*/
//    @POST("registerUserLogin")
//    Call<LoginSuccess> regisetr(@Query("userName") String user_name,
//                                @Query("userPassword") String user_password,
//                                @Query("sex") String sex,
//                                @Query("birthday") String birthday,
//                                @Query("nowProvince") String now_province,
//                                @Query("nowCity") String no_city,
//                                @Query("age") int age,
//                                @Query("appA") String appA,
//                                @Query("devNo") String deviceNo,
//                                @Query("channel") String channel,
//                                @Query("devtype") String devType
//    );

    /**
     * 获取验证码
     * 新密码
     * http://192.168.150.182:8080/pnservice/smsSendRegister?userName=15544771389
     */
    @POST("smsSendRegister")
    Call<GainSmsBean> smsSendRegister(@Query("userName") String user_name);

    /**
     * 找回密码获取验证码
     * 旧密码
     * http://192.168.150.182:8080/pnservice/smsSendRegister?userName=15544771389
     */
    @POST("smsSendFind")
    Call<GainSmsBean> smsSendFind(@Query("userName") String user_name);

    /**
     * 验证码和手机号验证接口
     * 手机号码
     * 验证码
     * http://192.168.150.182:8080/pnservice/smsSendRegister?userName=15544771389
     */
    @POST("checkSmsCode")
    Call<VerificationSmsBean> checkSmsCode(
            @Query("userName") String user_name,
            @Query("yzm") String yzm);

    /**
     * 这个是确认密码的接口找回密码的接口
     * http://192.168.150.182:8080/pnservice/smsSendRegister?userName=15544771389
     */
    @POST("updatePassword")
    Call<updataPassword> updatePassword(@Query("userName") String user_name, @Query("userPassword") String yzm);

    /**
     * 接口说明：更换绑定手机号接口
     * 调用方式：post
     * 接口地址：http://192.168.150.182:8080/pnservice/updateLoginName
     * 入参：
     * 名称	类型	长度	说明
     * userLoginId	Int		用户登录id
     * userName	String		用户手机号
     */
    @POST("updateLoginName")
    Call<SuccessfulBean> updateLoginName(
            @Query("userId") String userLoginId,
            @Query("userName") String userName
    );

    /**
     * 这个是登录的接口
     */
    @POST("userLogin")
    Call<LoginSuccess> userLogin(
            @Query("userName") String userLogin,
            @Query("userPassword") String userPassword,
            @Query("appA") String appA);

//    /**
//     * 第三方注册的接口
//     */
//    @Multipart
//    @POST("registerThirdUserLoginNew")
//    Call<SanLoginSuccess> registerThirdUserLoginNew(
//            @Query("yzm") String yzm,
//            @Query("userName") String userName,
//            @Query("thirdType") String thirdType,   //三方类型：1.QQ，2.微信，3新浪微博
//            @Query("thirdName") String thirdName,
//            @Query("appA") String appA,
//            @Query("devNo") String deviceNo,
//            @Query("channel") String channel,
//            @Query("devtype") String devType
//    );

//    /**
//     * 第三方注册的接口
//     */
//    @Multipart
//    @POST("registerThirdUserLogin")
//    Call<SanLoginSuccess> registerThirdUserLogin(
//            @Query("thirdName") String thirdName,
//            @Query("sex") String sex,
//            @Query("birthday") String birthday,
//            @Query("nowProvince") String nowProvince,
//            @Query("nowCity") String nwCity,
//            @Query("age") int age,
//            @Query("userName") String userName,
//            @Query("thirdType") String thirdType,
//            @Query("nickname") String nickname,
//            @Part MultipartBody.Part part,
//            @Query("devNo") String deviceNo,
//            @Query("channel") String channel,
//            @Query("devtype") String devType,
//            @Query("appA") String appA);

    /**
     * 补全信息接口
     */
    @Multipart
    @POST("addUserInfoNew")
    Call<LoginSuccess> addUserInfoNew(
            @Query("userId") String userId,
            @Query("sex") String sex,
            @Query("birthday") String birthday,
            @Query("nickName") String nickName,
            @Query("age") int age,
            @Part MultipartBody.Part part,
            @Query("appA") String appA,
            @Query("devNo") String deviceNo,
            @Query("devtype") String devType
    );

//    /**
//     * 补全信息接口
//     */
//    @POST("addUserInfo")
//    Call<LoginSuccess> addUserInfo(
//            @Query("userId") String userId,
//            @Query("sex") String sex,
//            @Query("birthday") String birthday,
//            @Query("nowProvince") String nowProvince,
//            @Query("nowCity") String nowCity,
//            @Query("age") int age,
//            @Query("appA") String appA
//    );

    /**
     * 接口说明：手机第三方帐号登录接口
     * 调用方式：post
     * 接口地址：http://192.168.150.140:8089/pnservice/userLoginByThird
     * 入参：
     * 名称	类型	长度	说明
     * thirdName	String		第三方的token
     * 回参：
     * 名称	类型	长度	说明
     * mUserToken	String		用户身份验证密匙
     * userInfo	Json		用户信息
     * userInfo：
     * 名称	类型 	长度	说明
     * id	String		用户id
     * userLoginId	Int		用户帐号表id
     * nickname	String		用户昵称
     * imageHead	String		用户头像
     * sex	Int		性别
     */

    @POST("userLoginByThird")
    Call<SanLoginSuccess> userLoginByThird(
            @Query("thirdName") String thirdName,
            @Query("appA") String appA);
}
