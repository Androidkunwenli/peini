package com.jsz.peini.model.pay;

/**
 * Created by th on 2017/2/6.
 */
public class PayJinBiOrderIdStrBean {
    private JsonObjBean data;
    private int resultCode;
    private String resultDesc;
    private String orderId;

    @Override
    public String toString() {
        return "PayJinBiOrderIdStrBean{" +
                "data=" + data +
                ", resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public JsonObjBean getData() {
        return data;
    }

    public void setData(JsonObjBean data) {
        this.data = data;
    }

    public static class JsonObjBean {
        private String payId;
        private String serverB;

        @Override
        public String toString() {
            return "JsonObjBean{" +
                    "payId='" + payId + '\'' +
                    ", serverB='" + serverB + '\'' +
                    '}';
        }

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getServerB() {
            return serverB;
        }

        public void setServerB(String serverB) {
            this.serverB = serverB;
        }
    }
}
