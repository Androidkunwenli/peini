package com.jsz.peini.model.news;

import java.util.List;

/**
 * Created by th on 2017/1/2.
 */

public class MyFans {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * objectList : [{"id":8,"userId":3,"nickname":"3","imageHead":"/upload/user/2/20161211187698.png"},{"id":9,"userId":4,"nickname":"小鹿","imageHead":"/upload/user/2/20161211187698.png"},{"id":10,"userId":11,"nickname":"17","imageHead":"/upload/user/1/20161211145644.png"},{"id":11,"userId":12,"nickname":"18","imageHead":"/upload/user/1/20161211145644.png"}]
     */

    private int resultCode;
    private String resultDesc;
    private List<ObjectListBean> objectList;

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

    public List<ObjectListBean> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<ObjectListBean> objectList) {
        this.objectList = objectList;
    }

    public static class ObjectListBean {
        /**
         * id : 8
         * userId : 3
         * nickname : 3
         * imageHead : /upload/user/2/20161211187698.png
         */

        private int id;
        private String userId;
        private String nickname;
        private String imageHead;
        private String userPhone;
        private String createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getImageHead() {
            return imageHead;
        }

        public void setImageHead(String imageHead) {
            this.imageHead = imageHead;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
