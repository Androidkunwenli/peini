package com.jsz.peini.model.square;

import java.io.Serializable;
import java.util.List;

/**
 * Created by th on 2016/12/23.
 */

public class SquareBean implements Serializable {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * squareList : [{"id":3,"userId":1,"content":"你是谁","address":"石家庄","squareTime":"2016-12-22 11:35","nickname":"大驴","imageHead":"/upload/user/1/20161211145644.png","imageList":[],"likeList":[],"commentList":[]},{"id":6,"userId":1,"content":"你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么你是谁你来干什么","address":"北国赏茶","squareTime":"2016-12-17 14:59","nickname":"大驴","imageHead":"/upload/user/1/20161211145644.png","imageList":[{"id":14,"imageSrc":"/upload/square/1/20161223151938head.png"},{"id":15,"imageSrc":"/upload/square/1/20161223152025head.png"},{"id":16,"imageSrc":"/upload/square/1/20161223152021head.png"},{"id":17,"imageSrc":"/upload/square/1/20161223152040head.png"},{"id":18,"imageSrc":"/upload/square/1/20161223152033head.png"},{"id":19,"imageSrc":"/upload/square/1/20161223152058head.png"},{"id":20,"imageSrc":"/upload/square/1/20161223152125head.png"},{"id":21,"imageSrc":"/upload/square/1/2016122315270553head.png"}],"likeList":[{"userNickname":"大驴","imageHead":"/upload/user/1/20161211145644.png","userId":1},{"userNickname":"小鹿","imageHead":"/upload/user/2/20161211187698.png","userId":4},{"userNickname":"5","imageHead":"/upload/user/2/20161211187698.png","userId":5},{"userNickname":"3","imageHead":"/upload/user/2/20161211187698.png","userId":3},{"userNickname":"美屡","imageHead":"/upload/user/2/20161211187698.png","userId":2},{"userNickname":"6","imageHead":"/upload/user/2/20161211187698.png","userId":6}],"commentList":[{"id":10,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"6","content":"巴巴爸爸不不不不"},{"id":11,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"巴巴爸爸不不不不"},{"id":7,"userId":1,"userNickname":"大驴","toUserId":0,"toUserNickname":"","content":"啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发"},{"id":14,"userId":1,"userNickname":"大驴","toUserId":0,"toUserNickname":"","content":"啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发啊是的发"},{"id":8,"userId":2,"userNickname":"美屡","toUserId":1,"toUserNickname":"大驴","content":"大街上打手机大街上打手机"},{"id":13,"userId":2,"userNickname":"美屡","toUserId":1,"toUserNickname":"大驴","content":"大街上打手机大街上打手机"},{"id":9,"userId":2,"userNickname":"美屡","toUserId":0,"toUserNickname":"","content":"阿斯达所"},{"id":12,"userId":2,"userNickname":"美屡","toUserId":0,"toUserNickname":"","content":"阿斯达所"}]}]
     */

    private int resultCode;
    private String resultDesc;
    private List<SquareListBean> squareList;

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

    public List<SquareListBean> getSquareList() {
        return squareList;
    }

    public void setSquareList(List<SquareListBean> squareList) {
        this.squareList = squareList;
    }

    @Override
    public String toString() {
        return "{" + "resultCode:" + resultCode + ", resultDesc:'" + resultDesc + '\'' + ", squareList:" + squareList + '}';
    }

    public static class SquareListBean implements Serializable {
        /**
         * id : 3
         * userId : 1
         * content : 你是谁
         * address : 石家庄
         * squareTime : 2016-12-22 11:35
         * nickname : 大驴
         * imageHead : /upload/user/1/20161211145644.png
         * imageList : []
         * likeList : []
         * commentList : []
         */
        private int id;
        private String userId;
        private String content;
        private String address;
        private String squareTime;
        private String nickname;
        private String imageHead;
        private String Latitude;
        private String Longitude;
        private boolean local = true;
        private boolean isLike;
        private List<ImageListBean> imageList;
        private List<LikeListBean> likeList;
        private List<CommentListBean> commentList;

        @Override
        public String toString() {
            return "SquareListBean{" +
                    "id=" + id +
                    ", userId='" + userId + '\'' +
                    ", content='" + content + '\'' +
                    ", address='" + address + '\'' +
                    ", squareTime='" + squareTime + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", imageHead='" + imageHead + '\'' +
                    ", Latitude='" + Latitude + '\'' +
                    ", Longitude='" + Longitude + '\'' +
                    ", local='" + local + '\'' +
                    ", isLike=" + isLike +
                    ", imageList=" + imageList +
                    ", likeList=" + likeList +
                    ", commentList=" + commentList +
                    '}';
        }

        public boolean getLocal() {
            return local;
        }

        public void setLocal(boolean local) {
            this.local = local;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String latitude) {
            Latitude = latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String longitude) {
            Longitude = longitude;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSquareTime() {
            return squareTime;
        }

        public void setSquareTime(String squareTime) {
            this.squareTime = squareTime;
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

        public List<ImageListBean> getImageList() {
            return imageList;
        }

        public void setImageList(List<ImageListBean> imageList) {
            this.imageList = imageList;
        }

        public List<LikeListBean> getLikeList() {
            return likeList;
        }

        public void setLikeList(List<LikeListBean> likeList) {
            this.likeList = likeList;
        }

        public List<CommentListBean> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<CommentListBean> commentList) {
            this.commentList = commentList;
        }

    }


}
