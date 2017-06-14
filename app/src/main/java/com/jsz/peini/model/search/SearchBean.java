package com.jsz.peini.model.search;

import java.util.List;

/**
 * Created by th on 2016/12/16.
 */

public class SearchBean {


    /**
     * resultCode : 1
     * resultDesc : 成功
     * data : [{"id":40,"hotName":"白佛客运站","hotNum":20,"type":1},{"id":12,"hotName":"大驴的肉食","hotNum":0,"type":1},{"id":13,"hotName":"疯子电影院s","hotNum":0,"type":1},{"id":14,"hotName":"Spring汽修","hotNum":0,"type":1},{"id":15,"hotName":"Hibernates网吧","hotNum":0,"type":1},{"id":16,"hotName":"s子咖啡","hotNum":0,"type":1},{"id":17,"hotName":"店铺2017","hotNum":0,"type":1},{"id":18,"hotName":"新店铺","hotNum":0,"type":1},{"id":19,"hotName":"网票","hotNum":0,"type":1}]
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

    public static class DataBean {
        /**
         * id : 40
         * hotName : 白佛客运站
         * hotNum : 20
         * type : 1
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
    }
}
