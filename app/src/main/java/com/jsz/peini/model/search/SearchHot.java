package com.jsz.peini.model.search;

import java.util.List;

/**
 * Created by th on 2016/12/16.
 */

public class SearchHot {


    private int resultCode;
    private String resultDesc;
    private List<SellerListBean> sellerList;

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

    public List<SellerListBean> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<SellerListBean> sellerList) {
        this.sellerList = sellerList;
    }

    public static class SellerListBean {

        private String id;
        private String sellerName;
        private String sellerAddress;
        private int distance;
        private int searchType;
        private int listNum;
        private String couponMj;
        private String couponJb;

        public String getCouponMj() {
            return couponMj;
        }

        public void setCouponMj(String couponMj) {
            this.couponMj = couponMj;
        }

        public String getCouponJb() {
            return couponJb;
        }

        public void setCouponJb(String couponJb) {
            this.couponJb = couponJb;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getSellerAddress() {
            return sellerAddress;
        }

        public void setSellerAddress(String sellerAddress) {
            this.sellerAddress = sellerAddress;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getSearchType() {
            return searchType;
        }

        public void setSearchType(int searchType) {
            this.searchType = searchType;
        }

        public int getListNum() {
            return listNum;
        }

        public void setListNum(int listNum) {
            this.listNum = listNum;
        }
    }
}
