package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by lenovo on 2017/3/3.
 */
public class TaTaskBean {
    /**
     * resultCode : 1
     * resultDesc : 成功
     */

    private CompletenessBean completeness;
    private int resultCode;
    private String resultDesc;
    private List<TaskInfoByUserIdListBean> taskInfoByUserIdList;

    public CompletenessBean getCompleteness() {
        return completeness;
    }

    public void setCompleteness(CompletenessBean completeness) {
        this.completeness = completeness;
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

    public List<TaskInfoByUserIdListBean> getTaskInfoByUserIdList() {
        return taskInfoByUserIdList;
    }

    public void setTaskInfoByUserIdList(List<TaskInfoByUserIdListBean> taskInfoByUserIdList) {
        this.taskInfoByUserIdList = taskInfoByUserIdList;
    }

    public static class CompletenessBean {
        /**
         * regCnt : 9
         * finishRatio : 44.44444444444444
         */

        private int regCnt;
        private double finishRatio;

        public int getRegCnt() {
            return regCnt;
        }

        public void setRegCnt(int regCnt) {
            this.regCnt = regCnt;
        }

        public double getFinishRatio() {
            return finishRatio;
        }

        public void setFinishRatio(double finishRatio) {
            this.finishRatio = finishRatio;
        }
    }

    public static class TaskInfoByUserIdListBean {
        /**
         * id : 247
         * nickName : 乐乐
         * sex : 2
         * mAge : 19
         * imageHead : /upload/headImg/13703398714.png2017/2/24/877aa43a7e20404f9aaa529c7359b887.png
         * goldList : 0
         * buyList : 0
         * integrityList : 0
         * userId : ced5672fab364ded8992466a70d1e232
         * publishType : 0
         * sellerInfoId : 129
         * sellerInfoName : 米莎贝尔谈固店
         * taskStatus : 2
         * otherLowAge : 18
         * otherHighAge : 26
         * otherSex : 3
         * otherGo : 1
         * otherBuy : 1
         * userPhone : 13703398714
         * otherUserId : a7e6016b5228492fb269ea870333f56f
         * taskAppointedTime : 2017-02-28 02:00:00
         * otherPhone : 13263181110
         * otherNickName : 陪你到永远
         */

        private int id;
        private String nickName;
        private int sex;
        private int age;
        private String imageHead;
        private int goldList;
        private int buyList;
        private int integrityList;
        private String userId;
        private int publishType;
        private int sellerInfoId;
        private String sellerInfoName;
        private int taskStatus;
        private int otherLowAge;
        private int otherHighAge;
        private int otherSex;
        private int otherGo;
        private int otherBuy;
        private String userPhone;
        private String otherUserId;
        private String taskAppointedTime;
        private String otherPhone;
        private String otherNickName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
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

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getPublishType() {
            return publishType;
        }

        public void setPublishType(int publishType) {
            this.publishType = publishType;
        }

        public int getSellerInfoId() {
            return sellerInfoId;
        }

        public void setSellerInfoId(int sellerInfoId) {
            this.sellerInfoId = sellerInfoId;
        }

        public String getSellerInfoName() {
            return sellerInfoName;
        }

        public void setSellerInfoName(String sellerInfoName) {
            this.sellerInfoName = sellerInfoName;
        }

        public int getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(int taskStatus) {
            this.taskStatus = taskStatus;
        }

        public int getOtherLowAge() {
            return otherLowAge;
        }

        public void setOtherLowAge(int otherLowAge) {
            this.otherLowAge = otherLowAge;
        }

        public int getOtherHighAge() {
            return otherHighAge;
        }

        public void setOtherHighAge(int otherHighAge) {
            this.otherHighAge = otherHighAge;
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

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getOtherUserId() {
            return otherUserId;
        }

        public void setOtherUserId(String otherUserId) {
            this.otherUserId = otherUserId;
        }

        public String getTaskAppointedTime() {
            return taskAppointedTime;
        }

        public void setTaskAppointedTime(String taskAppointedTime) {
            this.taskAppointedTime = taskAppointedTime;
        }

        public String getOtherPhone() {
            return otherPhone;
        }

        public void setOtherPhone(String otherPhone) {
            this.otherPhone = otherPhone;
        }

        public String getOtherNickName() {
            return otherNickName;
        }

        public void setOtherNickName(String otherNickName) {
            this.otherNickName = otherNickName;
        }
    }
}
