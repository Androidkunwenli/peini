package com.jsz.peini.model.filter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 15089 on 2017/2/14.
 */

public class FilterRecycleviewBean implements Parcelable {
    private int mInt;
    private String mString;
    private String mId;

    public FilterRecycleviewBean(int anInt, String string, String id) {
        mInt = anInt;
        mString = string;
        mId = id;
    }

    public FilterRecycleviewBean(int anInt, String string) {
        mInt = anInt;
        mString = string;
    }

    @Override
    public String toString() {
        return "FilterRecycleviewBean{" +
                "mInt=" + mInt +
                ", mString='" + mString + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getInt() {
        return mInt;
    }

    public void setInt(int anInt) {
        mInt = anInt;
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mInt);
        dest.writeString(this.mString);
        dest.writeString(this.mId);
    }

    protected FilterRecycleviewBean(Parcel in) {
        this.mInt = in.readInt();
        this.mString = in.readString();
        this.mId = in.readString();
    }

    public static final Parcelable.Creator<FilterRecycleviewBean> CREATOR = new Parcelable.Creator<FilterRecycleviewBean>() {
        @Override
        public FilterRecycleviewBean createFromParcel(Parcel source) {
            return new FilterRecycleviewBean(source);
        }

        @Override
        public FilterRecycleviewBean[] newArray(int size) {
            return new FilterRecycleviewBean[size];
        }
    };
}
