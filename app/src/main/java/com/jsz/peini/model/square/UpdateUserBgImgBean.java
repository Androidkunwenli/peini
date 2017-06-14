package com.jsz.peini.model.square;

/**
 * Created by th on 2017/1/21.
 */
public class UpdateUserBgImgBean {
    /**
     * 图像路径	spaceBgImg	String
     * 结果码	resultCode	String
     * 结果说明	resultDesc	String
     */
    int resultCode;
    String resultDesc;
    String spaceBgImg;

    public int getResultCode() {
        return resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public String getSpaceBgImg() {
        return spaceBgImg;
    }

    @Override
    public String toString() {
        return "UpdateUserBgImgBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", spaceBgImg='" + spaceBgImg + '\'' +
                '}';
    }
}
