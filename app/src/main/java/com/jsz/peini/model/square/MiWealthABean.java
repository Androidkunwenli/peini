package com.jsz.peini.model.square;

import java.math.BigDecimal;

/**
 * Created by th on 2017/1/11.
 */
public class MiWealthABean {

    private UserInfoBean userInfo;
    private int resultCode;
    private String resultDesc;

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
        return "MiWealthABean{" +
                "userInfo=" + userInfo +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }

    public static class UserInfoBean {
        private String imageHead;
        private BigDecimal gold;
        private BigDecimal score;

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
        }

        public BigDecimal getGold() {
            return gold;
        }

        public void setGold(BigDecimal gold) {
            this.gold = gold;
        }

        public BigDecimal getScore() {
            return score;
        }

        public void setScore(BigDecimal score) {
            this.score = score;
        }

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "imageHead='" + imageHead + '\'' +
                    ", gold='" + gold + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
}
