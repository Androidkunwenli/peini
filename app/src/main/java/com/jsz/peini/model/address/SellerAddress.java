package com.jsz.peini.model.address;

import java.util.List;

/**
 * Created by th on 2016/12/15.
 */

public class SellerAddress {


    private int resultCode;
    private String resultDesc;
    private List<DistrictHotListBean> districtHotList;
    private List<DistrictListBean> districtList;

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

    public List<DistrictHotListBean> getDistrictHotList() {
        return districtHotList;
    }

    public void setDistrictHotList(List<DistrictHotListBean> districtHotList) {
        this.districtHotList = districtHotList;
    }

    public List<DistrictListBean> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<DistrictListBean> districtList) {
        this.districtList = districtList;
    }

    public static class DistrictHotListBean {
        private int placeCode;
        private String placeName;

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
    }

    public static class DistrictListBean {
        private int countyId;
        private String countyName;
        private List<DistrictObjectBean> districtObject;

        public int getCountyId() {
            return countyId;
        }

        public void setCountyId(int countyId) {
            this.countyId = countyId;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }

        public List<DistrictObjectBean> getDistrictObject() {
            return districtObject;
        }

        public void setDistrictObject(List<DistrictObjectBean> districtObject) {
            this.districtObject = districtObject;
        }

        public static class DistrictObjectBean {
            private int districtId;
            private String districtName;

            public int getDistrictId() {
                return districtId;
            }

            public void setDistrictId(int districtId) {
                this.districtId = districtId;
            }

            public String getDistrictName() {
                return districtName;
            }

            public void setDistrictName(String districtName) {
                this.districtName = districtName;
            }
        }
    }
}
