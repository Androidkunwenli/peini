package com.jsz.peini.model.pay;

import java.util.List;

/**
 * Created by 15089 on 2017/2/17.
 */
public class ConversionListBean {
    private int resultCode;
    private String resultDesc;
    private List<ListBean> data;

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

    public List<ListBean> getData() {
        return data;
    }

    public void setData(List<ListBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ConversionListBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", data=" + data +
                '}';
    }

    public class ListBean {
        private float goldNum;
        private int id;
        private float payNum;

        @Override
        public String toString() {
            return "ListBean{" +
                    "goldNum=" + goldNum +
                    ", id=" + id +
                    ", payNum=" + payNum +
                    '}';
        }

        public float getGoldNum() {
            return goldNum;
        }

        public void setGoldNum(float goldNum) {
            this.goldNum = goldNum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public float getPayNum() {
            return payNum;
        }

        public void setPayNum(float payNum) {
            this.payNum = payNum;
        }
    }
}
