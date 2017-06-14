package com.jsz.peini.model.news;

import java.util.List;

/**
 * Created by th on 2017/1/2.
 */

public class NewsList {


    /**
     * resultCode : 1
     * resultDesc : 成功
     * objectList : [{"id":13,"userId":3,"nickname":"3","imageHead":"/upload/user/2/20161211187698.png","doubleDesc":"双方关注"},{"id":3,"userId":4,"nickname":"小鹿","imageHead":"/upload/user/2/20161211187698.png","doubleDesc":"双方关注"},{"id":4,"userId":5,"nickname":"5","imageHead":"/upload/user/2/20161211187698.png","doubleDesc":""},{"id":5,"userId":6,"nickname":"6","imageHead":"/upload/user/2/20161211187698.png","doubleDesc":""},{"id":6,"userId":7,"nickname":"7","imageHead":"/upload/user/2/20161211187698.png","doubleDesc":""}]
     * fansNum : 4
     * imageHead : /upload/user/2/20161211187698.png
     * myImageHead : /upload/user/1/20161211145644.png
     * myNickname : 大驴
     */

    private int resultCode;
    private String resultDesc;
    private int fansNum;
    private String imageHead;
    private String myImageHead;
    private String myNickname;
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

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public String getImageHead() {
        return imageHead;
    }

    public void setImageHead(String imageHead) {
        this.imageHead = imageHead;
    }

    public String getMyImageHead() {
        return myImageHead;
    }

    public void setMyImageHead(String myImageHead) {
        this.myImageHead = myImageHead;
    }

    public String getMyNickname() {
        return myNickname;
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
    }

    public List<ObjectListBean> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<ObjectListBean> objectList) {
        this.objectList = objectList;
    }

    @Override
    public String toString() {
        return "NewsList{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", fansNum=" + fansNum +
                ", imageHead='" + imageHead + '\'' +
                ", myImageHead='" + myImageHead + '\'' +
                ", myNickname='" + myNickname + '\'' +
                ", objectList=" + objectList +
                '}';
    }

    public static class ObjectListBean {
        /**
         * id : 13
         * userId : 3
         * nickname : 3
         * imageHead : /upload/user/2/20161211187698.png
         * doubleDesc : 双方关注
         */

        private int id;
        private String userId;
        private String nickname;
        private String imageHead;
        private String doubleDesc;

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

        public String getDoubleDesc() {
            return doubleDesc;
        }

        public void setDoubleDesc(String doubleDesc) {
            this.doubleDesc = doubleDesc;
        }

        @Override
        public String toString() {
            return "ObjectListBean{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", nickname='" + nickname + '\'' +
                    ", imageHead='" + imageHead + '\'' +
                    ", doubleDesc='" + doubleDesc + '\'' +
                    '}';
        }
    }
}
