package com.jsz.peini.model.tabulation;

import java.util.List;

/**
 * Created by th on 2017/1/20.
 */

public class TaskListBean {

    private int resultCode;
    private String resultDesc;
    private List<TaskAllListBean> taskAllList;

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

    public List<TaskAllListBean> getTaskAllList() {
        return taskAllList;
    }

    public void setTaskAllList(List<TaskAllListBean> taskAllList) {
        this.taskAllList = taskAllList;
    }

    @Override
    public String toString() {
        return "TaskListBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", taskAllList=" + taskAllList +
                '}';
    }

    public static class TaskAllListBean {
        private int id;
        private String userId;
        private String sellerInfoName;
        private String taskAppointedTime;
        private int sellerSmallType;
        private String sellerSmallName;
        private int otherLowAge;
        private int otherHignAge;
        private int otherSex;
        private int otherGo;
        private int otherBuy;
        private int taskStatus;
        private String xpoint;
        private String ypoint;
        private String othernickName;
        private int sex;
        private int age;
        private int goldList;
        private int buyList;
        private int integrityList;
        private int reputation;
        private int publishType;
        private int distanceNum;
        private String imageHead;
        private String sellerAddress;
        private List<StringList> userLabel;

        public String getSellerAddress() {
            return sellerAddress;
        }

        public void setSellerAddress(String sellerAddress) {
            this.sellerAddress = sellerAddress;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSellerInfoName() {
            return sellerInfoName;
        }

        public void setSellerInfoName(String sellerInfoName) {
            this.sellerInfoName = sellerInfoName;
        }

        public String getTaskAppointedTime() {
            return taskAppointedTime;
        }

        public void setTaskAppointedTime(String taskAppointedTime) {
            this.taskAppointedTime = taskAppointedTime;
        }

        public int getSellerSmallType() {
            return sellerSmallType;
        }

        public void setSellerSmallType(int sellerSmallType) {
            this.sellerSmallType = sellerSmallType;
        }

        public String getSellerSmallName() {
            return sellerSmallName;
        }

        public void setSellerSmallName(String sellerSmallName) {
            this.sellerSmallName = sellerSmallName;
        }

        public int getOtherLowAge() {
            return otherLowAge;
        }

        public void setOtherLowAge(int otherLowAge) {
            this.otherLowAge = otherLowAge;
        }

        public int getOtherHignAge() {
            return otherHignAge;
        }

        public void setOtherHignAge(int otherHignAge) {
            this.otherHignAge = otherHignAge;
        }

        public int getOtherSex() {
            return otherSex;
        }

        public void setOtherSex(int otherSex) {
            this.otherSex = otherSex;
        }

        public int getOtherGo() {
            return otherGo;
        }

        public void setOtherGo(int otherGo) {
            this.otherGo = otherGo;
        }

        public int getOtherBuy() {
            return otherBuy;
        }

        public void setOtherBuy(int otherBuy) {
            this.otherBuy = otherBuy;
        }

        public int getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(int taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getXpoint() {
            return xpoint;
        }

        public void setXpoint(String xpoint) {
            this.xpoint = xpoint;
        }

        public String getYpoint() {
            return ypoint;
        }

        public void setYpoint(String ypoint) {
            this.ypoint = ypoint;
        }

        public String getOthernickName() {
            return othernickName;
        }

        public void setOthernickName(String othernickName) {
            this.othernickName = othernickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getGoldList() {
            return goldList;
        }

        public void setGoldList(int goldList) {
            this.goldList = goldList;
        }

        public int getBuyList() {
            return buyList;
        }

        public void setBuyList(int buyList) {
            this.buyList = buyList;
        }

        public int getIntegrityList() {
            return integrityList;
        }

        public void setIntegrityList(int integrityList) {
            this.integrityList = integrityList;
        }

        public int getReputation() {
            return reputation;
        }

        public void setReputation(int reputation) {
            this.reputation = reputation;
        }

        public int getPublishType() {
            return publishType;
        }

        public void setPublishType(int publishType) {
            this.publishType = publishType;
        }

        public int getDistanceNum() {
            return distanceNum;
        }

        public void setDistanceNum(int distanceNum) {
            this.distanceNum = distanceNum;
        }

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
        }

        public List<StringList> getUserLabel() {
            return userLabel;
        }

        public void setUserLabel(List<StringList> userLabel) {
            this.userLabel = userLabel;
        }

        @Override
        public String toString() {
            return "TaskAllListBean{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", sellerInfoName='" + sellerInfoName + '\'' +
                    ", taskAppointedTime='" + taskAppointedTime + '\'' +
                    ", sellerSmallType=" + sellerSmallType +
                    ", sellerSmallName='" + sellerSmallName + '\'' +
                    ", otherLowAge=" + otherLowAge +
                    ", otherHignAge=" + otherHignAge +
                    ", otherSex=" + otherSex +
                    ", otherGo=" + otherGo +
                    ", otherBuy=" + otherBuy +
                    ", taskStatus=" + taskStatus +
                    ", xpoint='" + xpoint + '\'' +
                    ", ypoint='" + ypoint + '\'' +
                    ", othernickName='" + othernickName + '\'' +
                    ", sex=" + sex +
                    ", mAge=" + age +
                    ", goldList=" + goldList +
                    ", buyList=" + buyList +
                    ", integrityList=" + integrityList +
                    ", reputation=" + reputation +
                    ", publishType=" + publishType +
                    ", distanceNum=" + distanceNum +
                    ", imageHead='" + imageHead + '\'' +
                    ", userLabel=" + userLabel +
                    '}';
        }

        public class StringList {
            public String labelName;

            public String getLabelName() {
                return labelName;
            }

            public void setLabelName(String labelName) {
                this.labelName = labelName;
            }

            @Override
            public String toString() {
                return "StringList{" +
                        "labelName='" + labelName + '\'' +
                        '}';
            }
        }
    }
}


