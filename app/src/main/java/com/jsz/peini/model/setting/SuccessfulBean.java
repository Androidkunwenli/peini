package com.jsz.peini.model.setting;

/**
 * Created by th on 2016/12/17.
 */

public class SuccessfulBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     */

    private int resultCode;
    private String resultDesc;
    private String data;
    private String isHead;
    private String imageUrl;

    @Override
    public String toString() {
        return "SuccessfulBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", data='" + data + '\'' +
                ", isHead='" + isHead + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsHead() {
        return isHead;
    }

    public void setIsHead(String isHead) {
        this.isHead = isHead;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

}
