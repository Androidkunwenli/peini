package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by th on 2017/1/5.
 */

public class UserImageAllByUserId {

    private int resultCode;
    private String resultDesc;
    private List<UserImageListBean> userImageList;

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

    public List<UserImageListBean> getUserImageList() {
        return userImageList;
    }

    public void setUserImageList(List<UserImageListBean> userImageList) {
        this.userImageList = userImageList;
    }

    @Override
    public String toString() {
        return "UserImageAllByUserId{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", userImageList=" + userImageList +
                '}';
    }

    public static class UserImageListBean {
        private String imageTime;
        private List<ImageListBean> userImageAll;

        public String getImageTime() {
            return imageTime;
        }

        public void setImageTime(String imageTime) {
            this.imageTime = imageTime;
        }

        public List<ImageListBean> getUserImageAll() {
            return userImageAll;
        }

        public void setUserImageAll(List<ImageListBean> userImageAll) {
            this.userImageAll = userImageAll;
        }

        @Override
        public String toString() {
            return "UserImageListBean{" +
                    "imageTime='" + imageTime + '\'' +
                    ", userImageAll=" + userImageAll +
                    '}';
        }
    }
}
