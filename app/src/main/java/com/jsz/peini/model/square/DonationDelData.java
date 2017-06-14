package com.jsz.peini.model.square;

/**
 * Created by 15089 on 2017/2/25.
 */
public class DonationDelData {

    /**
     * data : {"serverB":"1001055"}
     * resultCode : 1
     * resultDesc : SUCCESS
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
         * serverB : 1001055
         */

        private String serverB;

        public String getServerB() {
            return serverB;
        }

        public void setServerB(String serverB) {
            this.serverB = serverB;
        }
    }
}
