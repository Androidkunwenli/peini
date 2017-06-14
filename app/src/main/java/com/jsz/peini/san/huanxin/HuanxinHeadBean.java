package com.jsz.peini.san.huanxin;

import java.util.List;

/**
 * Created by 15089 on 2017/2/16.
 */
public class HuanxinHeadBean {

    /**
     * data : [{"headImg":"","nickName":"系统管理员","sex":""}]
     * resultCode : 1
     * resultDesc : 成功
     */

    private int resultCode;
    private String resultDesc;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HuanxinHeadBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * headImg :
         * nickName : 系统管理员
         * sex :
         */

        private String headImg;
        private String nickName;
        private String sex;
        private String userPhone;

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "headImg='" + headImg + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }
    }
}
