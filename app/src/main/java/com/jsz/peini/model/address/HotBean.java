package com.jsz.peini.model.address;

/**
 * Created by th on 2017/2/10.
 */

public class HotBean {

    private int placeCode;
    private String placeName;

    public HotBean(int placeCode, String placeName) {
        this.placeCode = placeCode;
        this.placeName = placeName;
    }

    public int getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(int placeCode) {
        this.placeCode = placeCode;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    @Override
    public String toString() {
        return "HotBean{" +
                "placeCode=" + placeCode +
                ", placeName='" + placeName + '\'' +
                '}';
    }
}
