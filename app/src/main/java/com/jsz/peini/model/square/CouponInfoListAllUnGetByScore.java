package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by th on 2017/1/20.
 */
public class CouponInfoListAllUnGetByScore {

    private UserInfoBean userInfo;
    private int resultCode;
    private String resultDesc;
    private List<CouponListBean> couponList;

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

    public List<CouponListBean> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponListBean> couponList) {
        this.couponList = couponList;
    }

    @Override
    public String toString() {
        return "CouponInfoListAllUnGetByScore{" +
                "userInfo=" + userInfo +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", couponList=" + couponList +
                '}';
    }

    public static class UserInfoBean {
        private int score;
        private int buyList;
        private int integrityList;
        private int goldList;
        private String nickName;
        private String imageHead;

        public String getImageStr() {
            return imageHead;
        }

        public void setImageStr(String imageStr) {
            this.imageHead = imageStr;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getBuyList() {
            return buyList;
        }

        public void setBuyList(int buyList) {
            this.buyList = buyList;
        }

        public int getIntegrityList() {
            return integrityList;
        }

        public void setIntegrityList(int integrityList) {
            this.integrityList = integrityList;
        }

        public int getGoldList() {
            return goldList;
        }

        public void setGoldList(int goldList) {
            this.goldList = goldList;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        @Override
        public String toString() {
            return "UserInfoBean{" +
                    "score=" + score +
                    ", buyList=" + buyList +
                    ", integrityList=" + integrityList +
                    ", goldList=" + goldList +
                    ", nickName='" + nickName + '\'' +
                    '}';
        }
    }

    public static class CouponListBean {
        private int id;
        private String coupon_name;
        private int coupon_money;
        private int rule_money;
        private String couponImg;
        private int get_num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public int getCoupon_money() {
            return coupon_money;
        }

        public void setCoupon_money(int coupon_money) {
            this.coupon_money = coupon_money;
        }

        public int getRule_money() {
            return rule_money;
        }

        public void setRule_money(int rule_money) {
            this.rule_money = rule_money;
        }

        public String getCouponImg() {
            return couponImg;
        }

        public void setCouponImg(String couponImg) {
            this.couponImg = couponImg;
        }

        public int getGet_num() {
            return get_num;
        }

        public void setGet_num(int get_num) {
            this.get_num = get_num;
        }

        @Override
        public String toString() {
            return "CouponListBean{" +
                    "id=" + id +
                    ", coupon_name='" + coupon_name + '\'' +
                    ", coupon_money=" + coupon_money +
                    ", rule_money=" + rule_money +
                    ", couponImg='" + couponImg + '\'' +
                    ", get_num=" + get_num +
                    '}';
        }
    }
}
