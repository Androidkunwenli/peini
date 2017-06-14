package com.jsz.peini.model.pay;

/**
 * 商家折扣
 * Created by huizhe.ju on 2017/2/28.
 */
public class SellerDiscountBean {

    /**
     * date : {"gold":100,"wxpay":100,"alipay":100}
     * resultCode : 1
     * resultDesc : 成功
     */

    private DateBean date;
    private int resultCode;
    private String resultDesc;

    public DateBean getDate() {
        return date;
    }

    public void setDate(DateBean date) {
        this.date = date;
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

    public static class DateBean {
        /**
         * gold : 100
         * wxpay : 100
         * alipay : 100
         */

        private int gold;
        private int wxpay;
        private int alipay;

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getWxpay() {
            return wxpay;
        }

        public void setWxpay(int wxpay) {
            this.wxpay = wxpay;
        }

        public int getAlipay() {
            return alipay;
        }

        public void setAlipay(int alipay) {
            this.alipay = alipay;
        }
    }
}
