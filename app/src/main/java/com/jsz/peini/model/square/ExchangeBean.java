package com.jsz.peini.model.square;


import com.jsz.peini.model.ad.AdModel;

/**
 * Created by lenovo on 2017/5/11.
 */

public class ExchangeBean {
    private CouponInfoListAllUnGetByScore mScore;
    private AdModel mModel;

    public ExchangeBean(CouponInfoListAllUnGetByScore score, AdModel model) {
        mScore = score;
        mModel = model;
    }

    public ExchangeBean() {

    }

    public ExchangeBean(CouponInfoListAllUnGetByScore score) {
        mScore = score;
    }

    public ExchangeBean(AdModel model) {
        mModel = model;
    }

    public CouponInfoListAllUnGetByScore getScore() {
        return mScore;
    }

    public void setScore(CouponInfoListAllUnGetByScore score) {
        mScore = score;
    }

    public AdModel getModel() {
        return mModel;
    }

    public void setModel(AdModel model) {
        mModel = model;
    }

    @Override
    public String toString() {
        return "ExchangeBean{" +
                "mScore=" + mScore +
                ", mModel=" + mModel +
                '}';
    }
}
