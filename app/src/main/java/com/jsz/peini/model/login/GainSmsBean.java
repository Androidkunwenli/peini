package com.jsz.peini.model.login;

/**
 * Created by th on 2016/12/12.
 */

public class GainSmsBean {

    /**
     * resultCode : 1
     * userName : 15544771389
     * resultDesc : 已发送
     */

    private int resultCode;
    private String userName;
    private String resultDesc;
    /**
     * smsCode : 378845
     */

    private String smsCode;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "GainSmsBean{" +
                "resultCode=" + resultCode +
                ", userName='" + userName + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", smsCode='" + smsCode + '\'' +
                '}';
    }
}
