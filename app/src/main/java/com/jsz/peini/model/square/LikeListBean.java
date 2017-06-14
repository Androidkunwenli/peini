package com.jsz.peini.model.square;

import java.io.Serializable;

/**
 * Created by th on 2016/12/26.
 */

public class LikeListBean implements Serializable {
    private String userNickname;
    private String imageHead;
    private String userId;
    private String sex;

    public LikeListBean(String userNickname, String imageHead, String userId, String sex) {
        this.userNickname = userNickname;
        this.imageHead = imageHead;
        this.userId = userId;
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getImageHead() {
        return imageHead;
    }

    public void setImageHead(String imageHead) {
        this.imageHead = imageHead;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
                "userNickname:'" + userNickname + '\'' +
                ", imageHead:'" + imageHead + '\'' +
                ", userId:'" + userId + '\'' +
                ", sex:'" + sex + '\'' +
                '}';
    }
}