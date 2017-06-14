package com.jsz.peini.model.login;

/**
 * Created by th on 2016/12/13.
 */

public class updataPassword {

    /**
     * resultCode : 0
     * resultDesc : 密码不能为空
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
}
