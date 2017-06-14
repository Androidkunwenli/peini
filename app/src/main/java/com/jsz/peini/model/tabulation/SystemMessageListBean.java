package com.jsz.peini.model.tabulation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jandl on 2017/3/17.
 */

public class SystemMessageListBean {

    /**
     * code : 1
     * data : [{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-17 09:44","senddel":0,"smscontent":"您有4张优惠券即将过期！","smsid":"39a2d2d0a4e747a2940141d79cd0ff25","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-16 08:49","senddel":0,"smscontent":"您有4张优惠券即将过期！","smsid":"cd6dedd807ce435ca43d4c7dc42b86b3","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-15 10:05","senddel":0,"smscontent":"您有4张优惠券即将过期！","smsid":"9afc9c8296c94956bb6e9e9c6cfee6a9","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-14 09:24","senddel":0,"smscontent":"您有3张优惠券即将过期！","smsid":"d1a00975f956462a90fa3730bfa64d34","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-13 08:55","senddel":0,"smscontent":"您有3张优惠券即将过期！","smsid":"558744d3785245f592c027182b2c3f23","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-11 08:38","senddel":0,"smscontent":"您有3张优惠券即将过期！","smsid":"828bada9767b4b74bae84b94e9b4c023","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-10 09:47","senddel":0,"smscontent":"您有3张优惠券即将过期！","smsid":"a5d336c2f4c24e5896337a135c39e696","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-09 08:39","senddel":0,"smscontent":"您有2张优惠券即将过期！","smsid":"3dcf395b2732467285cfa8b3e6d7e63e","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-08 08:54","senddel":0,"smscontent":"您有2张优惠券即将过期！","smsid":"30acc26b0552421c961a4f61562d4c17","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"},{"fromid":"","isremind":1,"paramid":"","receivdel":0,"sendTime":"03-07 08:19","senddel":0,"smscontent":"您有1张优惠券即将过期！","smsid":"d8a6edfac8f449cb86a32147ecdf1553","smstype":2,"toid":"5ba34a0f5cd847c5a34b1fe76b22c9f3"}]
     * msg : success
     */

    @SerializedName("code")
    private int resultCode;
    @SerializedName("msg")
    private String resultDesc;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * fromid :
         * isremind : 1
         * paramid :
         * receivdel : 0
         * sendTime : 03-17 09:44
         * senddel : 0
         * smscontent : 您有4张优惠券即将过期！
         * smsid : 39a2d2d0a4e747a2940141d79cd0ff25
         * smstype : 2
         * toid : 5ba34a0f5cd847c5a34b1fe76b22c9f3
         */

        private String from;

        @Override
        public String toString() {
            return "DataBean{" +
                    "from='" + from + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", isremind=" + isremind +
                    ", paramid='" + paramid + '\'' +
                    ", receivdel=" + receivdel +
                    ", sendTime='" + sendTime + '\'' +
                    ", senddel=" + senddel +
                    ", smscontent='" + smscontent + '\'' +
                    ", smsid='" + smsid + '\'' +
                    ", smstype=" + smstype +
                    ", toid='" + toid + '\'' +
                    ", newMsg='" + newMsg + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }

        private String fromid;
        private int isremind;
        private String paramid;
        private int receivdel;
        private String sendTime;
        private int senddel;
        private String smscontent;
        private String smsid;
        private int smstype;
        private String toid;
        private String newMsg;
        private String title;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNewMsg() {
            return newMsg;
        }

        public void setNewMsg(String newMsg) {
            this.newMsg = newMsg;
        }

        public String getFromid() {
            return fromid;
        }

        public void setFromid(String fromid) {
            this.fromid = fromid;
        }

        public int getIsremind() {
            return isremind;
        }

        public void setIsremind(int isremind) {
            this.isremind = isremind;
        }

        public String getParamid() {
            return paramid;
        }

        public void setParamid(String paramid) {
            this.paramid = paramid;
        }

        public int getReceivdel() {
            return receivdel;
        }

        public void setReceivdel(int receivdel) {
            this.receivdel = receivdel;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public int getSenddel() {
            return senddel;
        }

        public void setSenddel(int senddel) {
            this.senddel = senddel;
        }

        public String getSmscontent() {
            return smscontent;
        }

        public void setSmscontent(String smscontent) {
            this.smscontent = smscontent;
        }

        public String getSmsid() {
            return smsid;
        }

        public void setSmsid(String smsid) {
            this.smsid = smsid;
        }

        public int getSmstype() {
            return smstype;
        }

        public void setSmstype(int smstype) {
            this.smstype = smstype;
        }

        public String getToid() {
            return toid;
        }

        public void setToid(String toid) {
            this.toid = toid;
        }
    }
}
