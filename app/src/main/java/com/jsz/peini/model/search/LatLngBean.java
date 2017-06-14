package com.jsz.peini.model.search;

import java.io.Serializable;

/**
 * Created by 15089 on 2017/2/13.
 */

public class LatLngBean implements Serializable {
    private double latitude;
    private double longitude;

    public LatLngBean(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLngBean{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
