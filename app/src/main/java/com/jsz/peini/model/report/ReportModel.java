package com.jsz.peini.model.report;

import java.util.List;

/**
 * Created by th on 2017/1/12.
 */

public class ReportModel {
    private int resultCode;
    private String resultDesc;
    private List<ReportReasonBean> reportReason;

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

    public List<ReportReasonBean> getReportReason() {
        return reportReason;
    }

    public void setReportReason(List<ReportReasonBean> reportReason) {
        this.reportReason = reportReason;
    }

    @Override
    public String toString() {
        return "ReportModel{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", reportReason=" + reportReason +
                '}';
    }

    public static class ReportReasonBean {
        private int resid;
        private String resname;
        private int typeid;

        public int getResid() {
            return resid;
        }

        public void setResid(int resid) {
            this.resid = resid;
        }

        public String getResname() {
            return resname;
        }

        public void setResname(String resname) {
            this.resname = resname;
        }

        public int getTypeid() {
            return typeid;
        }

        public void setTypeid(int typeid) {
            this.typeid = typeid;
        }

        @Override
        public String toString() {
            return "ReportReasonBean{" +
                    "resid=" + resid +
                    ", resname='" + resname + '\'' +
                    ", typeid=" + typeid +
                    '}';
        }
    }
}
