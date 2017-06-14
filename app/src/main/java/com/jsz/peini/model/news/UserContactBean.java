package com.jsz.peini.model.news;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2017/6/7.
 */

public class UserContactBean {
    private String name;
    private String phone;
    private int isFollerOrInvite;
    private String PinYin;
    private String FirstPinYin;
    private Bitmap mBitmap;

    @Override
    public String toString() {
        return "UserContactBean{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", PinYin='" + PinYin + '\'' +
                ", FirstPinYin='" + FirstPinYin + '\'' +
                ", mBitmap=" + mBitmap +
                '}';
    }

    public UserContactBean() {
    }

    public UserContactBean(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public UserContactBean(String name, String phone, Bitmap bitmap) {
        this.name = name;
        this.phone = phone;
        mBitmap = bitmap;
    }

    public UserContactBean(String name, String phone, String pinYin, String firstPinYin, Bitmap bitmap) {
        this.name = name;
        this.phone = phone;
        PinYin = pinYin;
        FirstPinYin = firstPinYin;
        mBitmap = bitmap;
    }

    public int getIsFollerOrInvite() {
        return isFollerOrInvite;
    }

    public void setIsFollerOrInvite(int isFollerOrInvite) {
        this.isFollerOrInvite = isFollerOrInvite;
    }

    public String getPinYin() {
        return PinYin;
    }

    public void setPinYin(String pinYin) {
        PinYin = pinYin;
    }

    public String getFirstPinYin() {
        return FirstPinYin;
    }

    public void setFirstPinYin(String firstPinYin) {
        FirstPinYin = firstPinYin;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
