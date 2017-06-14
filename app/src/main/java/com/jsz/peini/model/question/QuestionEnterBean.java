package com.jsz.peini.model.question;

/**
 * Created by th on 2016/12/22.
 */
public class QuestionEnterBean {

    /**
     * reportInfoId : 13
     * resultCode : 1
     * resultDesc : 成功
     */

    private int reportInfoId;
    private int resultCode;
    private String resultDesc;

    public int getReportInfoId() {
        return reportInfoId;
    }

    public void setReportInfoId(int reportInfoId) {
        this.reportInfoId = reportInfoId;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    @Override
    public String toString() {
        return "QuestionEnterBean{" +
                "reportInfoId=" + reportInfoId +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }
}
