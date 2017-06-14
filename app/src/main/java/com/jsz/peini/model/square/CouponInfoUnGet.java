package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by th on 2017/1/20.
 */
public class CouponInfoUnGet {

    private int isGet;
    private CouponInfoBean couponInfo;
    private int resultCode;
    private String resultDesc;

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public CouponInfoBean getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(CouponInfoBean couponInfo) {
        this.couponInfo = couponInfo;
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
        return "CouponInfoUnGet{" +
                "isGet=" + isGet +
                ", couponInfo=" + couponInfo +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }

    public static class CouponInfoBean {
//        public CouponInfoBean(String couponName, String couponDesc, String couponImg, int couponMoney, int ruleMoney, String lastDate, String remindText, List<CouponRangeBean> couponRange) {
//            this.couponName = couponName;
//            this.couponDesc = couponDesc;
//            this.couponImg = couponImg;
//            this.couponMoney = couponMoney;
//            this.ruleMoney = ruleMoney;
//            this.lastDate = lastDate;
//            this.remindText = remindText;
//            this.couponRange = couponRange;
//        }

        private String couponName;
        private String couponDesc;
        private String couponImg;
        private int couponMoney;
        private int ruleMoney;
        private String lastDate;
        private String remindText;
        private List<CouponRangeBean> couponRange;

        public String getCouponName() {
            return couponName;
        }

        public void setCouponName(String couponName) {
            this.couponName = couponName;
        }

        public String getCouponDesc() {
            return couponDesc;
        }

        public void setCouponDesc(String couponDesc) {
            this.couponDesc = couponDesc;
        }

        public String getCouponImg() {
            return couponImg;
        }

        public void setCouponImg(String couponImg) {
            this.couponImg = couponImg;
        }

        public int getCouponMoney() {
            return couponMoney;
        }

        public void setCouponMoney(int couponMoney) {
            this.couponMoney = couponMoney;
        }

        public int getRuleMoney() {
            return ruleMoney;
        }

        public void setRuleMoney(int ruleMoney) {
            this.ruleMoney = ruleMoney;
        }

        public String getLastDate() {
            return lastDate;
        }

        public void setLastDate(String lastDate) {
            this.lastDate = lastDate;
        }

        public String getRemindText() {
            return remindText;
        }

        public void setRemindText(String remindText) {
            this.remindText = remindText;
        }

        public List<CouponRangeBean> getCouponRange() {
            return couponRange;
        }

        public void setCouponRange(List<CouponRangeBean> couponRange) {
            this.couponRange = couponRange;
        }

        @Override
        public String toString() {
            return "CouponInfoBean{" +
                    "couponName='" + couponName + '\'' +
                    ", couponDesc='" + couponDesc + '\'' +
                    ", couponImg='" + couponImg + '\'' +
                    ", couponMoney=" + couponMoney +
                    ", ruleMoney=" + ruleMoney +
                    ", lastDate='" + lastDate + '\'' +
                    ", remindText='" + remindText + '\'' +
                    ", couponRange=" + couponRange +
                    '}';
        }

        public static class CouponRangeBean {
            public CouponRangeBean(String name) {
                this.name = name;
            }

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "CouponRangeBean{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }

    }
}
