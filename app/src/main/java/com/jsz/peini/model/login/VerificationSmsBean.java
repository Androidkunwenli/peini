package com.jsz.peini.model.login;

/**
 * Created by th on 2016/12/12.
 */

public class VerificationSmsBean {

    /**
     * resultCode : 0
     * resultDesc : 验证码错误或已过期
     */

    private int resultCode;
    private String resultDesc;

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
        return "VerificationSmsBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }
}
