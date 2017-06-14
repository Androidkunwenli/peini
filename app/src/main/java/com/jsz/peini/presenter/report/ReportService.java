package com.jsz.peini.presenter.report;

import com.jsz.peini.model.report.ReportModel;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by th on 2017/1/12.
 */

public interface ReportService {
    /**
     * 1问题反馈；2用户举报；3举报商家；4任务举报；5图片举报
     */
    @POST("getReportReasons")
    Call<ReportModel> getReportReasons(
            @Query("type") String type);
}
