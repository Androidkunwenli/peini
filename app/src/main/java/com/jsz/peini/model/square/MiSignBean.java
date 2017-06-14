package com.jsz.peini.model.square;

/**
 * Created by 15089 on 2017/2/15.
 */
public class MiSignBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * dayAward : 第10次补签到扣除金币10
     */

    private int resultCode;
    private String resultDesc;
    private String dayAward;

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

    public String getDayAward() {
        return dayAward;
    }

    public void setDayAward(String dayAward) {
        this.dayAward = dayAward;
    }

    @Override
    public String toString() {
        return "MiSignBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", dayAward='" + dayAward + '\'' +
                '}';
    }
}
