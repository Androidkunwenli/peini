package com.jsz.peini.model.map;

import java.io.Serializable;
import java.util.List;

/**
 * Created by th on 2016/12/14.
 */

public class BaiduMapBean implements Serializable {

    /**
     * taskMapList : [{"id":502,"sum":3,"idStr":"502,503,506,","rankType":0,"taskObject":[{"id":502,"userId":"4aa4596afa6d4489a0a1bfcb3a12b5e5","imageHead":"/upload/headImg/17731027936.png2017/3/16/617fa59645694806baa11e8c9bf13504.png","xpoint":"38.046842","ypoint":"114.519207","sex":2,"sellerType":5,"status":0}]},{"id":491,"sum":1,"idStr":"491,","rankType":0,"taskObject":[{"id":491,"userId":"821118ad1f204ff999b3bffba91cfd8c","imageHead":"","xpoint":"38.029747","ypoint":"114.549974","sex":2,"sellerType":1,"status":0}]}]
     * distance : 500
     * resultCode : 1
     * resultDesc : 成功
     */

    private int distance;
    private int resultCode;
    private String resultDesc;
    private List<TaskMapListBean> taskMapList;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
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

    public List<TaskMapListBean> getTaskMapList() {
        return taskMapList;
    }

    public void setTaskMapList(List<TaskMapListBean> taskMapList) {
        this.taskMapList = taskMapList;
    }

    public static class TaskMapListBean {
        /**
         * id : 502
         * sum : 3
         * idStr : 502,503,506,
         * rankType : 0
         * taskObject : [{"id":502,"userId":"4aa4596afa6d4489a0a1bfcb3a12b5e5","imageHead":"/upload/headImg/17731027936.png2017/3/16/617fa59645694806baa11e8c9bf13504.png","xpoint":"38.046842","ypoint":"114.519207","sex":2,"sellerType":5,"status":0}]
         */

        private String id;
        private String sum;
        private String idStr;
        private String rankType;
        private List<TaskObjectBean> taskObject;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getIdStr() {
            return idStr;
        }

        public void setIdStr(String idStr) {
            this.idStr = idStr;
        }

        public String getRankType() {
            return rankType;
        }

        public void setRankType(String rankType) {
            this.rankType = rankType;
        }

        public List<TaskObjectBean> getTaskObject() {
            return taskObject;
        }

        public void setTaskObject(List<TaskObjectBean> taskObject) {
            this.taskObject = taskObject;
        }

        public static class TaskObjectBean {
            /**
             * id : 502
             * userId : 4aa4596afa6d4489a0a1bfcb3a12b5e5
             * imageHead : /upload/headImg/17731027936.png2017/3/16/617fa59645694806baa11e8c9bf13504.png
             * xpoint : 38.046842
             * ypoint : 114.519207
             * sex : 2
             * sellerType : 5
             * status : 0
             */

            private String id;
            private String userId;
            private String imageHead;
            private String xpoint;
            private String ypoint;
            private String sex;
            private int sellerType;
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getImageHead() {
                return imageHead;
            }

            public void setImageHead(String imageHead) {
                this.imageHead = imageHead;
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

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public int getSellerType() {
                return sellerType;
            }

            public void setSellerType(int sellerType) {
                this.sellerType = sellerType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
