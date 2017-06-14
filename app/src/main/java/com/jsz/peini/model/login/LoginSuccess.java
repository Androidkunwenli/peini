package com.jsz.peini.model.login;

/**
 * Created by th on 2016/12/13.
 */

public class LoginSuccess {

    private int addUserInfo;
    private int resultCode;
    private String serverB;
    private String userToken;
    private UserInfoBean userInfo;

    public void setAddUserInfo(int addUserInfo) {
        this.addUserInfo = addUserInfo;
    }

    public int getAddUserInfo() {
        return addUserInfo;
    }

    public void setServerB(String serverB) {
        this.serverB = serverB;
    }

    public String getServerB() {
        return serverB;
    }

    private String resultDesc;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
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

    @Override
    public String toString() {
        return "LoginSuccess{" +
                "userToken='" + userToken + '\'' +
                ", userInfo=" + userInfo +
                ", resultCode=" + resultCode +
                ", serverB=" + serverB +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }

    public static class UserInfoBean {
        private String id;
        private String userLoginId;
        private String nickname;
        private String imageHead;
        private String sex;
        private String userPhone;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(String userLoginId) {
            this.userLoginId = userLoginId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
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
    }

}
