package com.jsz.peini.model;

/**
 * 42、活动报名接口
 * Created by huizhe.ju on 2017/3/11.
 */
public class ActivityRegisterBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * orderIdStr : 如果值为空不需要支付，流程结束
     * 如果值不为空需要进行支付，走支付流
     */
    private int resultCode;
    private String resultDesc;
    /**
     * 如果值为空不需要支付，流程结束
     * 如果值不为空需要进行支付，走支付流
     */
    private String orderIdStr;


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

    /**
     * @return 如果值为空不需要支付，流程结束;如果值不为空需要进行支付，走支付流
     */
    public String getOrderIdStr() {
        return orderIdStr;
    }

    public void setOrderIdStr(String orderIdStr) {
        this.orderIdStr = orderIdStr;
    }
}
