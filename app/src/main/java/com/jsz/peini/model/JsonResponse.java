package com.jsz.peini.model;

/**
 * 响应基类
 * Created by huizhe.ju on 2017/03/04.
 */

public class JsonResponse {

    private int resultCode;
    private String resultDesc;
    private boolean isSign;
    private String isRank;

    public String getIsRank() {
        return isRank;
    }

    public void setIsRank(String isRank) {
        this.isRank = isRank;
    }

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
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
}
