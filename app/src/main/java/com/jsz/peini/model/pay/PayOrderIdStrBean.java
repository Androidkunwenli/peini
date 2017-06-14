package com.jsz.peini.model.pay;

/**
 * Created by th on 2017/2/4.
 */
public class PayOrderIdStrBean {

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

    @Override
    public String toString() {
        return "PayOrderIdStrBean{" +
                "data=" + data +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                '}';
    }

    public static class DataBean {
        private String payInfo;

        public String getPayInfo() {
            return payInfo;
        }

        public void setPayInfo(String payInfo) {
            this.payInfo = payInfo;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "payInfo='" + payInfo + '\'' +
                    '}';
        }
    }
}
