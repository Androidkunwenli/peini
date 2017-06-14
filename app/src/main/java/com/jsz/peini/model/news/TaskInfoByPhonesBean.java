package com.jsz.peini.model.news;

/**
 * Created by lenovo on 2017/3/1.
 */
public class TaskInfoByPhonesBean {

    /**
     * data : {"headImg":"/upload/headImg/13263181110.png2017/3/20/add2ecf556a3406ebf8fc8e4cd6e71c5.jpg","otherId":"a7e6016b5228492fb269ea870333f56f","taskAppointedTime":"2017-03-19 20:30","nickName":"。。。","otherBuy":"1","sellerName":"晶广线影城","industry":"","targetDesc":"","taskId":"599","taskTime":"2017-03-19 08:30","status":"5"}
     * resultCode : 1
     * resultDesc : 成功
     */

    private DataBean data;
    private int resultCode;
    private String resultDesc;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * headImg : /upload/headImg/13263181110.png2017/3/20/add2ecf556a3406ebf8fc8e4cd6e71c5.jpg
         * otherId : a7e6016b5228492fb269ea870333f56f
         * taskAppointedTime : 2017-03-19 20:30
         * nickName : 。。。
         * otherBuy : 1
         * sellerName : 晶广线影城
         * industry :
         * targetDesc :
         * taskId : 599
         * taskTime : 2017-03-19 08:30
         * status : 5
         */

        private String headImg;
        private String otherId;
        private String taskAppointedTime;
        private String nickName;
        private String otherBuy;
        private String sellerName;
        private String industry;
        private String targetDesc;
        private String taskId;
        private String taskTime;
        private String status;
        private int distance;
        private String publishType;
        private String taskOtherStatus;

        public String getTaskOtherStatus() {
            return taskOtherStatus;
        }

        public void setTaskOtherStatus(String taskOtherStatus) {
            this.taskOtherStatus = taskOtherStatus;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getOtherId() {
            return otherId;
        }

        public void setOtherId(String otherId) {
            this.otherId = otherId;
        }

        public String getTaskAppointedTime() {
            return taskAppointedTime;
        }

        public void setTaskAppointedTime(String taskAppointedTime) {
            this.taskAppointedTime = taskAppointedTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOtherBuy() {
            return otherBuy;
        }

        public void setOtherBuy(String otherBuy) {
            this.otherBuy = otherBuy;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getTargetDesc() {
            return targetDesc;
        }

        public void setTargetDesc(String targetDesc) {
            this.targetDesc = targetDesc;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskTime() {
            return taskTime;
        }

        public void setTaskTime(String taskTime) {
            this.taskTime = taskTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public String getPublishType() {
            return publishType;
        }

        public void setPublishType(String publishType) {
            this.publishType = publishType;
        }
    }
}
