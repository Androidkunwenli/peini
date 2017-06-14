package com.jsz.peini.model.pay;

/**
 * Created by th on 2017/2/4.
 */
public class PaySuccessfulBean {

    /**
     * orderIdStr : 69
     * resultCode : 1
     * resultDesc : 成功
     */

    private String orderIdStr;
    private int resultCode;
    private String resultDesc;

    public String getOrderIdStr() {
        return orderIdStr;
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
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
        return "PaySuccessfulBean{" +
                "orderIdStr='" + orderIdStr + '\'' +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }
}
