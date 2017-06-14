package com.jsz.peini.model.square;

/**
 * Created by th on 2017/1/17.
 */

public class MyCreditBean {

    private int resultCode;
    private String resultDesc;
    private IsMyCreditBean myCredit;

    public int getResultCode() {
        return resultCode;
    }


    public String getResultDesc() {
        return resultDesc;
    }

    public IsMyCreditBean getMyCredit() {
        return myCredit;
    }

    @Override
    public String toString() {
        return "MyCreditBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", myCredit=" + myCredit +
                '}';
    }

    public static class IsMyCreditBean {
        private int creditNum;
        private int idcardNum;
        private int selfNum;
        private int taskNum;
        private String updateTime;

        public int getCreditNum() {
            return creditNum;
        }

        public int getIdcardNum() {
            return idcardNum;
        }

        public int getSelfNum() {
            return selfNum;
        }

        public int getTaskNum() {
            return taskNum;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        @Override
        public String toString() {
            return "IsMyCreditBean{" +
                    "idcardNum=" + idcardNum +
                    ", creditNum=" + creditNum +
                    ", selfNum=" + selfNum +
                    ", taskNum=" + taskNum +
                    '}';
        }
    }
}
