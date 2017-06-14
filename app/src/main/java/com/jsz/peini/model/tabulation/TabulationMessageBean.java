package com.jsz.peini.model.tabulation;

import java.util.List;

/**
 * Created by th on 2016/12/20.
 */

public class TabulationMessageBean {

    /**
     * taskInfoList : [{"age":19,"arr":[],"buyList":0,"cancelUserId":"","cancleUserId":"","distance":369,"goldList":9,"id":731,"imageHead":"/upload/headImg/13263181110.png2017/3/21/21ee5e4686a847aeabbeefed4a5ce22d.jpg","integrityList":5,"isIdcard":0,"isVideo":0,"isdelete":0,"nickName":"我就知道","orderId":"","otherBuy":1,"otherBuyList":0,"otherGo":1,"otherGoldList":0,"otherHignAge":0,"otherHignheight":0,"otherIntegrityList":0,"otherLowAge":0,"otherLowheight":0,"otherOrderId":0,"otherReputation":0,"otherSex":3,"otherSexDesc":"","otherUserAge":0,"otherUserId":"","otherUserLabel":[],"otherUserPhone":"","otherUserSex":0,"othernickName":"","reputation":35,"sellerBigName":"","sellerBigType":1,"sellerImage":[{"id":1,"imageSrc":"/upload/business/sellerInfo/123/2017/3/10/b404c061e92c479e9ab53e7f481e4089.jpg"}],"sellerInfoId":105,"sellerInfoName":"123","sellerInfoSellerType":0,"sellerSmallName":"一起去自助餐","sellerSmallType":11,"sellerType":"","sex":1,"signWord":"","sort":0,"taskAppointedTime":"2017-03-25 22:00","taskCancleTime":"2017-03-23 06:28:23","taskCancleType":0,"taskCity":64,"taskCounty":0,"taskDesc":"","taskEndTime":null,"taskIds":[],"taskOtherStatus":"","taskProvince":3,"taskRecvTime":"2017-03-23 06:28:23","taskStatus":1,"taskTime":"2017-03-23 06:27:23","userId":"a7e6016b5228492fb269ea870333f56f","userLabel":[{"id":471,"labelName":"ZXC"},{"id":472,"labelName":"ASD"}],"userPhone":"13263181110","xpoint":"38.048684","ypoint":"114.533045","publishType":1}]
     * resultCode : 1
     * resultDesc : 成功
     */

    private int resultCode;
    private String resultDesc;
    private String isHead;
    private List<TaskInfoListBean> taskInfoList;

    @Override
    public String toString() {
        return "TabulationMessageBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", isHead='" + isHead + '\'' +
                ", taskInfoList=" + taskInfoList +
                '}';
    }

    public String getIsHead() {
        return isHead;
    }

    public void setIsHead(String isHead) {
        this.isHead = isHead;
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

    public List<TaskInfoListBean> getTaskInfoList() {
        return taskInfoList;
    }

    public void setTaskInfoList(List<TaskInfoListBean> taskInfoList) {
        this.taskInfoList = taskInfoList;
    }

    public static class TaskInfoListBean {
        /**
         * age : 19
         * arr : []
         * buyList : 0
         * cancelUserId :
         * cancleUserId :
         * distance : 369
         * goldList : 9
         * id : 731
         * imageHead : /upload/headImg/13263181110.png2017/3/21/21ee5e4686a847aeabbeefed4a5ce22d.jpg
         * integrityList : 5
         * isIdcard : 0
         * isVideo : 0
         * isdelete : 0
         * nickName : 我就知道
         * orderId :
         * otherBuy : 1
         * otherBuyList : 0
         * otherGo : 1
         * otherGoldList : 0
         * otherHignAge : 0
         * otherHignheight : 0
         * otherIntegrityList : 0
         * otherLowAge : 0
         * otherLowheight : 0
         * otherOrderId : 0
         * otherReputation : 0
         * otherSex : 3
         * otherSexDesc :
         * otherUserAge : 0
         * otherUserId :
         * otherUserLabel : []
         * otherUserPhone :
         * otherUserSex : 0
         * othernickName :
         * reputation : 35
         * sellerBigName :
         * sellerBigType : 1
         * sellerImage : [{"id":1,"imageSrc":"/upload/business/sellerInfo/123/2017/3/10/b404c061e92c479e9ab53e7f481e4089.jpg"}]
         * sellerInfoId : 105
         * sellerInfoName : 123
         * sellerInfoSellerType : 0
         * sellerSmallName : 一起去自助餐
         * sellerSmallType : 11
         * sellerType :
         * sex : 1
         * signWord :
         * sort : 0
         * taskAppointedTime : 2017-03-25 22:00
         * taskCancleTime : 2017-03-23 06:28:23
         * taskCancleType : 0
         * taskCity : 64
         * taskCounty : 0
         * taskDesc :
         * taskEndTime : null
         * taskIds : []
         * taskOtherStatus :
         * taskProvince : 3
         * taskRecvTime : 2017-03-23 06:28:23
         * taskStatus : 1
         * taskTime : 2017-03-23 06:27:23
         * userId : a7e6016b5228492fb269ea870333f56f
         * userLabel : [{"id":471,"labelName":"ZXC"},{"id":472,"labelName":"ASD"}]
         * userPhone : 13263181110
         * xpoint : 38.048684
         * ypoint : 114.533045
         * publishType : 1
         */

        private int age;
        private int buyList;
        private String cancelUserId;
        private String cancleUserId;
        private int distance;
        private int goldList;
        private int id;
        private String imageHead;
        private int integrityList;
        private int isIdcard;
        private int isVideo;
        private int isdelete;
        private String nickName;
        private String orderId;
        private int otherBuy;
        private int otherBuyList;
        private int otherGo;
        private int otherGoldList;
        private int otherHignAge;
        private int otherHignheight;
        private int otherIntegrityList;
        private int otherLowAge;
        private int otherLowheight;
        private int otherOrderId;
        private int otherReputation;
        private int otherSex;
        private String otherSexDesc;
        private int otherUserAge;
        private String otherUserId;
        private String otherUserPhone;
        private int otherUserSex;
        private String othernickName;
        private int reputation;
        private String sellerBigName;
        private int sellerBigType;
        private int sellerInfoId;
        private String sellerInfoName;
        private int sellerInfoSellerType;
        private String sellerSmallName;
        private int sellerSmallType;
        private String sellerType;
        private int sex;
        private String signWord;
        private int sort;
        private String taskAppointedTime;
        private String taskCancleTime;
        private int taskCancleType;
        private int taskCity;
        private int taskCounty;
        private String taskDesc;
        private Object taskEndTime;
        private String taskOtherStatus;
        private int taskProvince;
        private String taskRecvTime;
        private int taskStatus;
        private String taskTime;
        private String userId;
        private String userPhone;
        private String xpoint;
        private String ypoint;
        private int publishType;
        private String sellerTypeImg;
        private List<?> arr;
        private List<UserLabelBean> otherUserLabel;
        private List<SellerImageBean> sellerImage;
        private List<?> taskIds;
        private List<UserLabelBean> userLabel;
        private String shareTitle;
        private String shareContent;

        public String getShareTitle() {
            return shareTitle;
        }

        public void setShareTitle(String shareTitle) {
            this.shareTitle = shareTitle;
        }

        public String getShareContent() {
            return shareContent;
        }

        public void setShareContent(String shareContent) {
            this.shareContent = shareContent;
        }

        public String getSellerTypeImg() {
            return sellerTypeImg;
        }

        public void setSellerTypeImg(String sellerTypeImg) {
            this.sellerTypeImg = sellerTypeImg;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getBuyList() {
            return buyList;
        }

        public void setBuyList(int buyList) {
            this.buyList = buyList;
        }

        public String getCancelUserId() {
            return cancelUserId;
        }

        public void setCancelUserId(String cancelUserId) {
            this.cancelUserId = cancelUserId;
        }

        public String getCancleUserId() {
            return cancleUserId;
        }

        public void setCancleUserId(String cancleUserId) {
            this.cancleUserId = cancleUserId;
        }

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getGoldList() {
            return goldList;
        }

        public void setGoldList(int goldList) {
            this.goldList = goldList;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
        }

        public int getIntegrityList() {
            return integrityList;
        }

        public void setIntegrityList(int integrityList) {
            this.integrityList = integrityList;
        }

        public int getIsIdcard() {
            return isIdcard;
        }

        public void setIsIdcard(int isIdcard) {
            this.isIdcard = isIdcard;
        }

        public int getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(int isVideo) {
            this.isVideo = isVideo;
        }

        public int getIsdelete() {
            return isdelete;
        }

        public void setIsdelete(int isdelete) {
            this.isdelete = isdelete;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public int getOtherBuy() {
            return otherBuy;
        }

        public void setOtherBuy(int otherBuy) {
            this.otherBuy = otherBuy;
        }

        public int getOtherBuyList() {
            return otherBuyList;
        }

        public void setOtherBuyList(int otherBuyList) {
            this.otherBuyList = otherBuyList;
        }

        public int getOtherGo() {
            return otherGo;
        }

        public void setOtherGo(int otherGo) {
            this.otherGo = otherGo;
        }

        public int getOtherGoldList() {
            return otherGoldList;
        }

        public void setOtherGoldList(int otherGoldList) {
            this.otherGoldList = otherGoldList;
        }

        public int getOtherHignAge() {
            return otherHignAge;
        }

        public void setOtherHignAge(int otherHignAge) {
            this.otherHignAge = otherHignAge;
        }

        public int getOtherHignheight() {
            return otherHignheight;
        }

        public void setOtherHignheight(int otherHignheight) {
            this.otherHignheight = otherHignheight;
        }

        public int getOtherIntegrityList() {
            return otherIntegrityList;
        }

        public void setOtherIntegrityList(int otherIntegrityList) {
            this.otherIntegrityList = otherIntegrityList;
        }

        public int getOtherLowAge() {
            return otherLowAge;
        }

        public void setOtherLowAge(int otherLowAge) {
            this.otherLowAge = otherLowAge;
        }

        public int getOtherLowheight() {
            return otherLowheight;
        }

        public void setOtherLowheight(int otherLowheight) {
            this.otherLowheight = otherLowheight;
        }

        public int getOtherOrderId() {
            return otherOrderId;
        }

        public void setOtherOrderId(int otherOrderId) {
            this.otherOrderId = otherOrderId;
        }

        public int getOtherReputation() {
            return otherReputation;
        }

        public void setOtherReputation(int otherReputation) {
            this.otherReputation = otherReputation;
        }

        public int getOtherSex() {
            return otherSex;
        }

        public void setOtherSex(int otherSex) {
            this.otherSex = otherSex;
        }

        public String getOtherSexDesc() {
            return otherSexDesc;
        }

        public void setOtherSexDesc(String otherSexDesc) {
            this.otherSexDesc = otherSexDesc;
        }

        public int getOtherUserAge() {
            return otherUserAge;
        }

        public void setOtherUserAge(int otherUserAge) {
            this.otherUserAge = otherUserAge;
        }

        public String getOtherUserId() {
            return otherUserId;
        }

        public void setOtherUserId(String otherUserId) {
            this.otherUserId = otherUserId;
        }

        public String getOtherUserPhone() {
            return otherUserPhone;
        }

        public void setOtherUserPhone(String otherUserPhone) {
            this.otherUserPhone = otherUserPhone;
        }

        public int getOtherUserSex() {
            return otherUserSex;
        }

        public void setOtherUserSex(int otherUserSex) {
            this.otherUserSex = otherUserSex;
        }

        public String getOthernickName() {
            return othernickName;
        }

        public void setOthernickName(String othernickName) {
            this.othernickName = othernickName;
        }

        public int getReputation() {
            return reputation;
        }

        public void setReputation(int reputation) {
            this.reputation = reputation;
        }

        public String getSellerBigName() {
            return sellerBigName;
        }

        public void setSellerBigName(String sellerBigName) {
            this.sellerBigName = sellerBigName;
        }

        public int getSellerBigType() {
            return sellerBigType;
        }

        public void setSellerBigType(int sellerBigType) {
            this.sellerBigType = sellerBigType;
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

        public int getSellerInfoSellerType() {
            return sellerInfoSellerType;
        }

        public void setSellerInfoSellerType(int sellerInfoSellerType) {
            this.sellerInfoSellerType = sellerInfoSellerType;
        }

        public String getSellerSmallName() {
            return sellerSmallName;
        }

        public void setSellerSmallName(String sellerSmallName) {
            this.sellerSmallName = sellerSmallName;
        }

        public int getSellerSmallType() {
            return sellerSmallType;
        }

        public void setSellerSmallType(int sellerSmallType) {
            this.sellerSmallType = sellerSmallType;
        }

        public String getSellerType() {
            return sellerType;
        }

        public void setSellerType(String sellerType) {
            this.sellerType = sellerType;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getSignWord() {
            return signWord;
        }

        public void setSignWord(String signWord) {
            this.signWord = signWord;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getTaskAppointedTime() {
            return taskAppointedTime;
        }

        public void setTaskAppointedTime(String taskAppointedTime) {
            this.taskAppointedTime = taskAppointedTime;
        }

        public String getTaskCancleTime() {
            return taskCancleTime;
        }

        public void setTaskCancleTime(String taskCancleTime) {
            this.taskCancleTime = taskCancleTime;
        }

        public int getTaskCancleType() {
            return taskCancleType;
        }

        public void setTaskCancleType(int taskCancleType) {
            this.taskCancleType = taskCancleType;
        }

        public int getTaskCity() {
            return taskCity;
        }

        public void setTaskCity(int taskCity) {
            this.taskCity = taskCity;
        }

        public int getTaskCounty() {
            return taskCounty;
        }

        public void setTaskCounty(int taskCounty) {
            this.taskCounty = taskCounty;
        }

        public String getTaskDesc() {
            return taskDesc;
        }

        public void setTaskDesc(String taskDesc) {
            this.taskDesc = taskDesc;
        }

        public Object getTaskEndTime() {
            return taskEndTime;
        }

        public void setTaskEndTime(Object taskEndTime) {
            this.taskEndTime = taskEndTime;
        }

        public String getTaskOtherStatus() {
            return taskOtherStatus;
        }

        public void setTaskOtherStatus(String taskOtherStatus) {
            this.taskOtherStatus = taskOtherStatus;
        }

        public int getTaskProvince() {
            return taskProvince;
        }

        public void setTaskProvince(int taskProvince) {
            this.taskProvince = taskProvince;
        }

        public String getTaskRecvTime() {
            return taskRecvTime;
        }

        public void setTaskRecvTime(String taskRecvTime) {
            this.taskRecvTime = taskRecvTime;
        }

        public int getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(int taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getTaskTime() {
            return taskTime;
        }

        public void setTaskTime(String taskTime) {
            this.taskTime = taskTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
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

        public int getPublishType() {
            return publishType;
        }

        public void setPublishType(int publishType) {
            this.publishType = publishType;
        }

        public List<?> getArr() {
            return arr;
        }

        public void setArr(List<?> arr) {
            this.arr = arr;
        }

        public List<UserLabelBean> getOtherUserLabel() {
            return otherUserLabel;
        }

        public void setOtherUserLabel(List<UserLabelBean> otherUserLabel) {
            this.otherUserLabel = otherUserLabel;
        }

        public List<SellerImageBean> getSellerImage() {
            return sellerImage;
        }

        public void setSellerImage(List<SellerImageBean> sellerImage) {
            this.sellerImage = sellerImage;
        }

        public List<?> getTaskIds() {
            return taskIds;
        }

        public void setTaskIds(List<?> taskIds) {
            this.taskIds = taskIds;
        }

        public List<UserLabelBean> getUserLabel() {
            return userLabel;
        }

        public void setUserLabel(List<UserLabelBean> userLabel) {
            this.userLabel = userLabel;
        }

        public static class SellerImageBean {
            /**
             * id : 1
             * imageSrc : /upload/business/sellerInfo/123/2017/3/10/b404c061e92c479e9ab53e7f481e4089.jpg
             */

            private int id;
            private String imageSrc;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImageSrc() {
                return imageSrc;
            }

            public void setImageSrc(String imageSrc) {
                this.imageSrc = imageSrc;
            }
        }

        public static class UserLabelBean {
            /**
             * id : 471
             * labelName : ZXC
             */

            private int id;
            private String labelName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLabelName() {
                return labelName;
            }

            public void setLabelName(String labelName) {
                this.labelName = labelName;
            }
        }
    }
}
