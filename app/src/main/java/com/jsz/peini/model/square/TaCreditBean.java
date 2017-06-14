package com.jsz.peini.model.square;

public class TaCreditBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * otherCredit : {"idcardNum":40,"creditNum":14,"updateTime":"2017-02-23 14:36:29","selfNum":20,"taskNum":0}
     * myCredit : {"idcardNum":40,"creditNum":23,"updateTime":"2017-02-22 10:33:32","selfNum":50,"taskNum":1}
     */

    private int resultCode;
    private String resultDesc;
    private OtherCreditBean otherCredit;
    private MyCreditBean myCredit;

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

    public OtherCreditBean getOtherCredit() {
        return otherCredit;
    }

    public void setOtherCredit(OtherCreditBean otherCredit) {
        this.otherCredit = otherCredit;
    }

    public MyCreditBean getMyCredit() {
        return myCredit;
    }

    public void setMyCredit(MyCreditBean myCredit) {
        this.myCredit = myCredit;
    }

    public static class OtherCreditBean {
        /**
         * idcardNum : 40
         * creditNum : 14
         * updateTime : 2017-02-23 14:36:29
         * selfNum : 20
         * taskNum : 0
         */

        private int idcardNum;
        private int creditNum;
        private String updateTime;
        private String nickName;
        private String imgHead;
        private int selfNum;
        private int taskNum;


        public int getIdcardNum() {
            return idcardNum;
        }

        public void setIdcardNum(int idcardNum) {
            this.idcardNum = idcardNum;
        }

        public int getCreditNum() {
            return creditNum;
        }

        public void setCreditNum(int creditNum) {
            this.creditNum = creditNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getImgHead() {
            return imgHead;
        }

        public void setImgHead(String imgHead) {
            this.imgHead = imgHead;
        }

        public int getSelfNum() {
            return selfNum;
        }

        public void setSelfNum(int selfNum) {
            this.selfNum = selfNum;
        }

        public int getTaskNum() {
            return taskNum;
        }

        public void setTaskNum(int taskNum) {
            this.taskNum = taskNum;
        }

        @Override
        public String toString() {
            return "OtherCreditBean{" +
                    "idcardNum=" + idcardNum +
                    ", creditNum=" + creditNum +
                    ", updateTime='" + updateTime + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", imgHead='" + imgHead + '\'' +
                    ", selfNum=" + selfNum +
                    ", taskNum=" + taskNum +
                    '}';
        }
    }

    public static class MyCreditBean {
        /**
         * idcardNum : 40
         * creditNum : 23
         * updateTime : 2017-02-22 10:33:32
         * selfNum : 50
         * taskNum : 1
         */

        private int idcardNum;
        private int creditNum;
        private String updateTime;
        private String nickName;
        private String imgHead;
        private int selfNum;
        private int taskNum;

        public int getIdcardNum() {
            return idcardNum;
        }

        public void setIdcardNum(int idcardNum) {
            this.idcardNum = idcardNum;
        }

        public int getCreditNum() {
            return creditNum;
        }

        public void setCreditNum(int creditNum) {
            this.creditNum = creditNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getImgHead() {
            return imgHead;
        }

        public void setImgHead(String imgHead) {
            this.imgHead = imgHead;
        }

        public int getSelfNum() {
            return selfNum;
        }

        public void setSelfNum(int selfNum) {
            this.selfNum = selfNum;
        }

        public int getTaskNum() {
            return taskNum;
        }

        public void setTaskNum(int taskNum) {
            this.taskNum = taskNum;
        }

        @Override
        public String toString() {
            return "MyCreditBean{" +
                    "idcardNum=" + idcardNum +
                    ", creditNum=" + creditNum +
                    ", updateTime='" + updateTime + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", imgHead='" + imgHead + '\'' +
                    ", selfNum=" + selfNum +
                    ", taskNum=" + taskNum +
                    '}';
        }
    }
}
