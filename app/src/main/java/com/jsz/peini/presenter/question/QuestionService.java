package com.jsz.peini.presenter.question;

import com.jsz.peini.model.question.QuestionEnterBean;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by th on 2016/12/22.
 */

public interface QuestionService {
    /**
     * 接口地址: http://192.168.150.182:8080/pnservice/setQuestion
     * 入参：
     * 名称	类型	长度	说明
     * token	Int		举报人id
     * content	String		举报内容
     * reportType	Int		举报类型（1问题反馈2举报人3举报商家4举报任务5举报照片）
     * reportId	Int		被举报对象id
     * reportCause	Int		举报原因（
     * 1商家不允许使用金币支付
     * 2商家说可用优惠价支付现金
     * 3头像/资料作假
     * 4骚扰广告
     * 5诈骗/托setQuestion
     * 6色情低俗
     * 7恶意骚扰/语言不文明）
     */

    @POST("setQuestionNull")
    Call<QuestionEnterBean> setQuestionNull(
            @Query("userToken") String userToken,
            @Query("content") String content,
            @Query("reportType") String reportType,
            @Query("reportId") String reportId,
            @Query("reportCause") String reportCause);

    @Multipart
    @POST("setQuestion")
    Call<QuestionEnterBean> setQuestion(
            @Query("userToken") String userToken,
            @Query("content") String content,
            @Query("reportType") String reportType,
            @Query("reportId") String reportId,
            @Query("reportCause") String reportCause,
            @Part MultipartBody.Part[] file);


}
