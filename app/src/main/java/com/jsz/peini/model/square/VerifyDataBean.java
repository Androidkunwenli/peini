package com.jsz.peini.model.square;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 15089 on 2017/2/21.
 */
public class VerifyDataBean implements Parcelable {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * data : {"headImg":"/upload/headImg/13263181110.png2017/2/16/8559aaea52ef4326ac8711c687e1a744.jpg","cityName":"石家庄市","sex":"1","userPhone":"13263181110","provinceName":"河北省","userId":"8bca55172e5548f59be77c45954e1ae7","userID":"10024","mAge":"19","relation":"陌生人"}
     */

    private int resultCode;
    private String resultDesc;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VerifyDataBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean implements Parcelable {
        /**
         * headImg : /upload/headImg/13263181110.png2017/2/16/8559aaea52ef4326ac8711c687e1a744.jpg
         * cityName : 石家庄市
         * sex : 1
         * userPhone : 13263181110
         * provinceName : 河北省
         * userId : 8bca55172e5548f59be77c45954e1ae7
         * userID : 10024
         * mAge : 19
         * relation : 陌生人
         */

        private String headImg;
        private String nickName;
        private String cityName;
        private String sex;
        private String userPhone;
        private String provinceName;
        private String userId;
        private String ID;
        private String age;
        private String relation;
        /**
         * spaceBgImg :
         */

        private String spaceBgImg;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
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

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "headImg='" + headImg + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", cityName='" + cityName + '\'' +
                    ", sex='" + sex + '\'' +
                    ", userPhone='" + userPhone + '\'' +
                    ", provinceName='" + provinceName + '\'' +
                    ", userId='" + userId + '\'' +
                    ", ID='" + ID + '\'' +
                    ", mAge='" + age + '\'' +
                    ", relation='" + relation + '\'' +
                    ", spaceBgImg='" + spaceBgImg + '\'' +
                    '}';
        }

        public String getSpaceBgImg() {
            return spaceBgImg;
        }

        public void setSpaceBgImg(String spaceBgImg) {
            this.spaceBgImg = spaceBgImg;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.headImg);
            dest.writeString(this.nickName);
            dest.writeString(this.cityName);
            dest.writeString(this.sex);
            dest.writeString(this.userPhone);
            dest.writeString(this.provinceName);
            dest.writeString(this.userId);
            dest.writeString(this.ID);
            dest.writeString(this.age);
            dest.writeString(this.relation);
            dest.writeString(this.spaceBgImg);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.headImg = in.readString();
            this.nickName = in.readString();
            this.cityName = in.readString();
            this.sex = in.readString();
            this.userPhone = in.readString();
            this.provinceName = in.readString();
            this.userId = in.readString();
            this.ID = in.readString();
            this.age = in.readString();
            this.relation = in.readString();
            this.spaceBgImg = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
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
        dest.writeParcelable(this.data, flags);
    }

    public VerifyDataBean() {
    }

    protected VerifyDataBean(Parcel in) {
        this.resultCode = in.readInt();
        this.resultDesc = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<VerifyDataBean> CREATOR = new Parcelable.Creator<VerifyDataBean>() {
        @Override
        public VerifyDataBean createFromParcel(Parcel source) {
            return new VerifyDataBean(source);
        }

        @Override
        public VerifyDataBean[] newArray(int size) {
            return new VerifyDataBean[size];
        }
    };
}
