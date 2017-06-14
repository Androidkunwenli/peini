package com.jsz.peini.model.search;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by lenovo on 2017/3/28.
 */

public class PoiInfoBean {
    private String name;
    private String address;
   private  LatLng mLatLng;

    public PoiInfoBean(String name, String address, LatLng latLng) {
        this.name = name;
        this.address = address;
        mLatLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }
}
