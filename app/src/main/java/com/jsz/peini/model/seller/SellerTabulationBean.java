package com.jsz.peini.model.seller;

import java.util.List;

/**
 * Created by lenovo on 2017/3/4.
 */
public class SellerTabulationBean {


    /**
     * resultCode : 1
     * resultDesc : 成功
     * sellerList : [{"id":2,"sellerName":"滇峰米线（万达店）","sellerHead":"/upload/business/sellerInfo/滇峰米线（万达店）/2017/3/6/9472bf0438684fa490b420a5ab0c0621.jpg","weatherorder":null,"sellerPhone":"15081858004","weathersms":null,"price":20,"sellerScore":0,"provinceid":null,"cityCode":null,"distid":null,"districtCode":26,"districtName":null,"sellerAddress":"万达广场三号门对面西行20米路北","xpoint":"38.029747","ypoint":"114.549974","sellerType":1,"labelsId":"10","labelsName":null,"sellerServer":0,"sellerCondition":0,"sellerMeal":0,"status":1,"information":"滇峰米线一直秉承\u201c弘扬民族饮食文化\u201d的发展宗旨， 一直把\u201c爱、健康、美味传递给亿万人\u201d作为发展使命，一直将\u201c成为民族餐饮优秀品牌，做健康的中式快餐\u201d作为信念，积极为广大消费者提供健康营养的美食、安全舒适的就餐环境、优质热情的服务。","companyId":null,"sellerKey":null,"isWifi":null,"isParking":null,"smsmobile":null,"accname":null,"accpass":null,"accmail":null,"accmobile":null,"sellerstatus":null,"remindtext":null,"sellerImg":null,"imageSrc":"/upload/business/sellerInfo/滇峰米线（万达店）/2017/3/6/b8c301f0f69d46a09d89021170dcf8d2.jpg","couponMj":null,"couponJb":"金币买单1折,微信买单1折,支付宝买单1折","distance":2439,"sort":null,"searchWord":null,"searchType":1,"listNum":null,"imageList":null},{"id":3,"sellerName":"滇峰米线（祥隆泰店）","sellerHead":"/upload/business/sellerInfo/滇峰米线（祥隆泰店）/2017/3/6/61e0fe69e001426fa7527f1bf924561c.jpg","weatherorder":null,"sellerPhone":"15081858004","weathersms":null,"price":20,"sellerScore":0,"provinceid":null,"cityCode":null,"distid":null,"districtCode":10,"districtName":null,"sellerAddress":"红旗大街366号","xpoint":"38.018935","ypoint":"114.465778","sellerType":1,"labelsId":"10","labelsName":null,"sellerServer":0,"sellerCondition":0,"sellerMeal":0,"status":1,"information":"滇峰米线一直秉承\u201c弘扬民族饮食文化\u201d的发展宗旨， 一直把\u201c爱、健康、美味传递给亿万人\u201d作为发展使命，一直将\u201c成为民族餐饮优秀品牌，做健康的中式快餐\u201d作为信念，积极为广大消费者提供健康营养的美食、安全舒适的就餐环境、优质热情的服务。","companyId":null,"sellerKey":null,"isWifi":null,"isParking":null,"smsmobile":null,"accname":null,"accpass":null,"accmail":null,"accmobile":null,"sellerstatus":null,"remindtext":null,"sellerImg":null,"imageSrc":"/upload/business/sellerInfo/滇峰米线（祥隆泰店）/2017/3/6/d43f7c5836274476b5a1cd05caa73598.jpg","couponMj":null,"couponJb":"金币买单1折,微信买单1折,支付宝买单1折","distance":6468,"sort":null,"searchWord":null,"searchType":1,"listNum":null,"imageList":null}]
     */

    private int resultCode;
    private String resultDesc;
    private List<SellerInfoBean> sellerList;

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

    public List<SellerInfoBean> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<SellerInfoBean> sellerList) {
        this.sellerList = sellerList;
    }
}
