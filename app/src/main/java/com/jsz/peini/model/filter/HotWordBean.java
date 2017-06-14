package com.jsz.peini.model.filter;

import java.util.List;

/**
 * Created by 15089 on 2017/2/18.
 */
public class HotWordBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * data : [{"id":1,"hotName":"北国商城","hotNum":15,"type":2},{"id":10,"hotName":"怀特家具城","hotNum":2,"type":2},{"id":4,"hotName":"新百广场","hotNum":1,"type":2},{"id":11,"hotName":"红星美凯龙","hotNum":1,"type":2},{"id":9,"hotName":"美丽华大酒店","hotNum":1,"type":2},{"id":2,"hotName":"河北省博物馆","hotNum":0,"type":2},{"id":3,"hotName":"海龙电子城","hotNum":0,"type":2},{"id":5,"hotName":"白佛客运站","hotNum":0,"type":2},{"id":6,"hotName":"太和电子城","hotNum":0,"type":2}]
     */

    private int resultCode;
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

    @Override
    public String toString() {
        return "HotWordBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * id : 1
         * hotName : 北国商城
         * hotNum : 15
         * type : 2
         */

        private int id;
        private String hotName;
        private int hotNum;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHotName() {
            return hotName;
        }

        public void setHotName(String hotName) {
            this.hotName = hotName;
        }

        public int getHotNum() {
            return hotNum;
        }

        public void setHotNum(int hotNum) {
            this.hotNum = hotNum;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", hotName='" + hotName + '\'' +
                    ", hotNum=" + hotNum +
                    ", type=" + type +
                    '}';
        }
    }
}

