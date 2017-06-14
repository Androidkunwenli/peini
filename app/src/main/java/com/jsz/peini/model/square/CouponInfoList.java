package com.jsz.peini.model.square;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券
 * Created by huizhe.ju on 2017/2/28.
 */
public class CouponInfoList implements Parcelable {

    /**
     * totalCnt : 18
     * data : [{"couponId":1048,"couponName":"直抵15","couponDesc":"直抵15简介","couponImg":"/upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png","couponMoney":5,"ruleMoney":0,"couponRule":"1,2","lastDate":"2017-05-31","userId":"a7e6016b5228492fb269ea870333f56f","couponRange":[{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}],"rangId":25},{"couponId":1048,"couponName":"直抵15","couponDesc":"直抵15简介","couponImg":"/upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png","couponMoney":5,"ruleMoney":0,"couponRule":"1,2","lastDate":"2017-05-31","userId":"a7e6016b5228492fb269ea870333f56f","couponRange":[{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}],"rangId":25},{"couponId":1048,"couponName":"直抵15","couponDesc":"直抵15简介","couponImg":"/upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png","couponMoney":5,"ruleMoney":0,"couponRule":"1,2","lastDate":"2017-05-31","userId":"a7e6016b5228492fb269ea870333f56f","couponRange":[{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}],"rangId":25},{"couponId":1048,"couponName":"直抵15","couponDesc":"直抵15简介","couponImg":"/upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png","couponMoney":5,"ruleMoney":0,"couponRule":"1,2","lastDate":"2017-05-31","userId":"a7e6016b5228492fb269ea870333f56f","couponRange":[{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}],"rangId":25},{"couponId":1048,"couponName":"直抵15","couponDesc":"直抵15简介","couponImg":"/upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png","couponMoney":5,"ruleMoney":0,"couponRule":"1,2","lastDate":"2017-05-31","userId":"a7e6016b5228492fb269ea870333f56f","couponRange":[{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}],"rangId":25},{"couponId":1048,"couponName":"直抵15","couponDesc":"直抵15简介","couponImg":"/upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png","couponMoney":5,"ruleMoney":0,"couponRule":"1,2","lastDate":"2017-05-31","userId":"a7e6016b5228492fb269ea870333f56f","couponRange":[{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}],"rangId":25}]
     * resultCode : 1
     * resultDesc : 成功
     */

    private int resultCode;
    private String resultDesc;
    private List<CouponInfoListBean> data;
    private String totalCnt;

    public String getTotalCnt() {
        return totalCnt;
    }

    public void setTotalCnt(String totalCnt) {
        this.totalCnt = totalCnt;
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

    public List<CouponInfoListBean> getCouponInfoList() {
        return data;
    }

    public void setCouponInfoList(List<CouponInfoListBean> data) {
        this.data = data;
    }

    public static class CouponInfoListBean implements Parcelable {
        /**
         * couponId : 1048
         * couponName : 直抵15
         * couponDesc : 直抵15简介
         * couponImg : /upload/activityRun/couponInfo/满15元减5元/2017/2/24/78d4ffc9e05b45b5a7bdcabf6c70f557.png
         * couponMoney : 5
         * ruleMoney : 0
         * couponRule : 1,2
         * lastDate : 2017-05-31
         * userId : a7e6016b5228492fb269ea870333f56f
         * couponRange : [{"name":"消费满0元可抵现金使用"},{"name":"优惠范围不参与积分赠送"},{"name":"河北省石家庄市"}]
         * rangId : 25
         * getId :
         */

        private int couponId;
        private String couponName;
        private String couponDesc;
        private String couponImg;
        private String couponMoney;
        private String ruleMoney;
        private String couponRule;
        private String lastDate;
        private String userId;
        private int days;
        private String rangId;
        private String getId;
        private String remindText;
        private ArrayList<CouponRangeBean> couponRange;

        @Override
        public String toString() {
            return "CouponInfoListBean{" +
                    "couponId=" + couponId +
                    ", couponName='" + couponName + '\'' +
                    ", couponDesc='" + couponDesc + '\'' +
                    ", couponImg='" + couponImg + '\'' +
                    ", couponMoney='" + couponMoney + '\'' +
                    ", ruleMoney='" + ruleMoney + '\'' +
                    ", couponRule='" + couponRule + '\'' +
                    ", lastDate='" + lastDate + '\'' +
                    ", userId='" + userId + '\'' +
                    ", days=" + days +
                    ", rangId='" + rangId + '\'' +
                    ", getId='" + getId + '\'' +
                    ", remindText='" + remindText + '\'' +
                    ", couponRange=" + couponRange +
                    '}';
        }

        public String getRemindText() {
            return remindText;
        }

        public void setRemindText(String remindText) {
            this.remindText = remindText;
        }

        public String getGetId() {
            return getId;
        }

        public void setGetId(String getId) {
            this.getId = getId;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

        public int getCouponId() {
            return couponId;
        }

        public void setCouponId(int couponId) {
            this.couponId = couponId;
        }

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

        public String getCouponMoney() {
            return couponMoney;
        }

        public void setCouponMoney(String couponMoney) {
            this.couponMoney = couponMoney;
        }

        public String getRuleMoney() {
            return ruleMoney;
        }

        public void setRuleMoney(String ruleMoney) {
            this.ruleMoney = ruleMoney;
        }

        public String getCouponRule() {
            return couponRule;
        }

        public void setCouponRule(String couponRule) {
            this.couponRule = couponRule;
        }

        public String getLastDate() {
            return lastDate;
        }

        public void setLastDate(String lastDate) {
            this.lastDate = lastDate;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getRangId() {
            return rangId;
        }

        public void setRangId(String rangId) {
            this.rangId = rangId;
        }

        public ArrayList<CouponRangeBean> getCouponRange() {
            return couponRange;
        }

        public void setCouponRange(ArrayList<CouponRangeBean> couponRange) {
            this.couponRange = couponRange;
        }

        public static class CouponRangeBean implements Parcelable {
            /**
             * name : 消费满0元可抵现金使用
             */

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.name);
            }

            public CouponRangeBean() {
            }

            protected CouponRangeBean(Parcel in) {
                this.name = in.readString();
            }

            public static final Creator<CouponRangeBean> CREATOR = new Creator<CouponRangeBean>() {
                @Override
                public CouponRangeBean createFromParcel(Parcel source) {
                    return new CouponRangeBean(source);
                }

                @Override
                public CouponRangeBean[] newArray(int size) {
                    return new CouponRangeBean[size];
                }
            };
        }

        public CouponInfoListBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.couponId);
            dest.writeString(this.couponName);
            dest.writeString(this.couponDesc);
            dest.writeString(this.couponImg);
            dest.writeString(this.couponMoney);
            dest.writeString(this.ruleMoney);
            dest.writeString(this.couponRule);
            dest.writeString(this.lastDate);
            dest.writeString(this.userId);
            dest.writeInt(this.days);
            dest.writeString(this.rangId);
            dest.writeString(this.getId);
            dest.writeString(this.remindText);
            dest.writeTypedList(this.couponRange);
        }

        protected CouponInfoListBean(Parcel in) {
            this.couponId = in.readInt();
            this.couponName = in.readString();
            this.couponDesc = in.readString();
            this.couponImg = in.readString();
            this.couponMoney = in.readString();
            this.ruleMoney = in.readString();
            this.couponRule = in.readString();
            this.lastDate = in.readString();
            this.userId = in.readString();
            this.days = in.readInt();
            this.rangId = in.readString();
            this.getId = in.readString();
            this.remindText = in.readString();
            this.couponRange = in.createTypedArrayList(CouponRangeBean.CREATOR);
        }

        public static final Creator<CouponInfoListBean> CREATOR = new Creator<CouponInfoListBean>() {
            @Override
            public CouponInfoListBean createFromParcel(Parcel source) {
                return new CouponInfoListBean(source);
            }

            @Override
            public CouponInfoListBean[] newArray(int size) {
                return new CouponInfoListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resultCode);
        dest.writeString(this.resultDesc);
        dest.writeTypedList(this.data);
        dest.writeString(this.totalCnt);
    }

    public CouponInfoList() {
    }

    protected CouponInfoList(Parcel in) {
        this.resultCode = in.readInt();
        this.resultDesc = in.readString();
        this.data = in.createTypedArrayList(CouponInfoListBean.CREATOR);
        this.totalCnt = in.readString();
    }

    public static final Parcelable.Creator<CouponInfoList> CREATOR = new Parcelable.Creator<CouponInfoList>() {
        @Override
        public CouponInfoList createFromParcel(Parcel source) {
            return new CouponInfoList(source);
        }

        @Override
        public CouponInfoList[] newArray(int size) {
            return new CouponInfoList[size];
        }
    };
}
