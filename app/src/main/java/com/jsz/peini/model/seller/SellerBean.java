package com.jsz.peini.model.seller;

import java.util.List;

/**
 * Created by th on 2016/12/16.
 */

public class SellerBean {
    /**
     * resultCode : 1
     * resultDesc : 成功
     * sellerInfo : [{"id":137,"sellerName":"好声音","sellerScore":0,"districtCode":5,"labels_id":24,"price":20,"sellerLogo":"/upload/business/sellerInfo/好声音/2017/2/28/6e181d7f2bfe4914b7b006a99239f5c7.jpg","imageSrc":"/upload/business/sellerInfo/好声音/2017/2/28/e72250bc52b641b0b221beed14ef5c0b.jpg","districtName":{"id":5,"districtCode":130102,"districtName":"长安区","placeCode":13010205,"plcaeName":"省博物馆","searchCount":6},"distance":206,"labels_name":"Ktv","countJB":""}]
     */

    private int resultCode;
    private String resultDesc;
    private List<SellerInfoBean> sellerInfo;

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

    public List<SellerInfoBean> getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(List<SellerInfoBean> sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    @Override
    public String toString() {
        return "SellerBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", sellerInfo=" + sellerInfo +
                '}';
    }
}
