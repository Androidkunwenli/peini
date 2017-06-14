package com.jsz.peini.model.ad;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by th on 2017/1/11.
 */

public class AdModel implements Parcelable {


    private int resultCode;
    private String resultDesc;
    private List<AdvertiseListBean> advertiseList;

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

    public List<AdvertiseListBean> getAdvertiseList() {
        return advertiseList;
    }

    public void setAdvertiseList(List<AdvertiseListBean> advertiseList) {
        this.advertiseList = advertiseList;
    }

    @Override
    public String toString() {
        return "AdModel{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", advertiseList=" + advertiseList +
                '}';
    }

    public static class AdvertiseListBean implements Parcelable {
        private String adId;
        private int adType;
        private String adTitle;
        private String adLink;
        private String adImgUrl;
        private int adStatus;

        public String getAdId() {
            return adId;
        }

        public void setAdId(String adId) {
            this.adId = adId;
        }

        public int getAdType() {
            return adType;
        }

        public void setAdType(int adType) {
            this.adType = adType;
        }

        public String getAdTitle() {
            return adTitle;
        }

        public void setAdTitle(String adTitle) {
            this.adTitle = adTitle;
        }

        public String getAdLink() {
            return adLink;
        }

        public void setAdLink(String adLink) {
            this.adLink = adLink;
        }

        public String getAdImgUrl() {
            return adImgUrl;
        }

        public void setAdImgUrl(String adImgUrl) {
            this.adImgUrl = adImgUrl;
        }

        public int getAdStatus() {
            return adStatus;
        }

        public void setAdStatus(int adStatus) {
            this.adStatus = adStatus;
        }

        @Override
        public String toString() {
            return "AdvertiseListBean{" +
                    "adId='" + adId + '\'' +
                    ", adType=" + adType +
                    ", adTitle='" + adTitle + '\'' +
                    ", adLink='" + adLink + '\'' +
                    ", adImgUrl='" + adImgUrl + '\'' +
                    ", adStatus=" + adStatus +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.adId);
            dest.writeInt(this.adType);
            dest.writeString(this.adTitle);
            dest.writeString(this.adLink);
            dest.writeString(this.adImgUrl);
            dest.writeInt(this.adStatus);
        }

        public AdvertiseListBean() {
        }

        protected AdvertiseListBean(Parcel in) {
            this.adId = in.readString();
            this.adType = in.readInt();
            this.adTitle = in.readString();
            this.adLink = in.readString();
            this.adImgUrl = in.readString();
            this.adStatus = in.readInt();
        }

        public static final Creator<AdvertiseListBean> CREATOR = new Creator<AdvertiseListBean>() {
            @Override
            public AdvertiseListBean createFromParcel(Parcel source) {
                return new AdvertiseListBean(source);
            }

            @Override
            public AdvertiseListBean[] newArray(int size) {
                return new AdvertiseListBean[size];
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
        dest.writeList(this.advertiseList);
    }

    public AdModel() {
    }

    protected AdModel(Parcel in) {
        this.resultCode = in.readInt();
        this.resultDesc = in.readString();
        this.advertiseList = new ArrayList<AdvertiseListBean>();
        in.readList(this.advertiseList, AdvertiseListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<AdModel> CREATOR = new Parcelable.Creator<AdModel>() {
        @Override
        public AdModel createFromParcel(Parcel source) {
            return new AdModel(source);
        }

        @Override
        public AdModel[] newArray(int size) {
            return new AdModel[size];
        }
    };
}
