package com.jsz.peini.model.pay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 15089 on 2017/2/15.
 */
public class WeiXinPayOrderIdStrBean {

    /**
     * data : {"appid":"wx4a856dc3b666fe4d","code_url":"","noncestr":"yMyFwSbHT4OJfpHgtgltcokYkbM2HYXd","package":"Sign=WXPay","partnerid":"1437434602","prepayid":"","sign":"17535EC9B40B5C409D802D1ED71B3695","timestamp":"1487144496"}
     * resultCode : 1
     * resultDesc :
     */

    private DataBean data;
    private int resultCode;
    private String resultDesc;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * appid : wx4a856dc3b666fe4d
         * code_url :
         * noncestr : yMyFwSbHT4OJfpHgtgltcokYkbM2HYXd
         * package : Sign=WXPay
         * partnerid : 1437434602
         * prepayid :
         * sign : 17535EC9B40B5C409D802D1ED71B3695
         * timestamp : 1487144496
         */

        private String appid;
        private String code_url;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private String sign;
        private String timestamp;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getCode_url() {
            return code_url;
        }

        public void setCode_url(String code_url) {
            this.code_url = code_url;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}
