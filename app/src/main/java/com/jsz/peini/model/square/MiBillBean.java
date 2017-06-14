package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by 15089 on 2017/2/16.
 */
public class MiBillBean {
    /**
     * resultCode : 1
     * resultDesc : 成功
     */

    private int resultCode;
    private String resultDesc;
    private List<OrderInfoListBean> orderInfoList;

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

    public List<OrderInfoListBean> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<OrderInfoListBean> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    public static class OrderInfoListBean {
        /**
         * orderType : 5
         * orderId : 868
         * userPhone : 18202230521
         * orderStatus : 2
         * title : 小孩儿
         * otherNickName : 小孩儿
         * orderTime : 2017-03-09 07:21
         * sellerId :
         * payType : 1
         * totalPay : 1.0E7
         * payMoney : 1.0E7
         * orderCode : 139-5-17-03-09868
         * payId : 88140469cd6b47a38508c2fa69e92f40
         * imgSrc : /upload/headImg/18202230521.png2017/3/9/0a44825e0bdc45edad71f01e68dcaedc.png
         * otherUserId : 98109049d55b49f396824386277d1abf
         * taskId :
         */

        private String orderType;
        private String orderId;
        private String userPhone;
        private String orderStatus;
        private String title;
        private String otherNickName;
        private String orderTime;
        private String sellerId;
        private String payType;
        private String totalPay;
        private String payMoney;
        private String orderCode;
        private String payId;
        private String imgSrc;
        private String otherUserId;
        private String taskId;

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOtherNickName() {
            return otherNickName;
        }

        public void setOtherNickName(String otherNickName) {
            this.otherNickName = otherNickName;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getTotalPay() {
            return totalPay;
        }

        public void setTotalPay(String totalPay) {
            this.totalPay = totalPay;
        }

        public String getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(String payMoney) {
            this.payMoney = payMoney;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getOtherUserId() {
            return otherUserId;
        }

        public void setOtherUserId(String otherUserId) {
            this.otherUserId = otherUserId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }
    }
}
