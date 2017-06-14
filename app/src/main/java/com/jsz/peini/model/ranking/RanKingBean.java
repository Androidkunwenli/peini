package com.jsz.peini.model.ranking;

import java.util.List;

/**
 * Created by th on 2017/1/19.
 */

public class RanKingBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * rankList : [{"num":2.31,"userId":"faa57ee7c7724281b5cc3814f0d5cf97","nickname":"微微","imageHead":"/upload/headImg/18610454505.png2017/3/7/d571d7f9f01a485cb77341abbf6cbaa4.png","mAge":24,"sex":2,"industry":""},{"num":0.99,"userId":"7fbbc47bef414cae9c4ff8e3e2d52bfd","nickname":"求索","imageHead":"/upload/headImg/13315185682.png2017/3/2/80f11cc75c244b4595391e7390712fbe.jpg","mAge":18,"sex":1,"industry":""},{"num":0.11,"userId":"821118ad1f204ff999b3bffba91cfd8c","nickname":"测试人员1","imageHead":"/upload/headImg/18202230518.png2017/2/24/73a30f07e3d940ada64a183720e74ec4.png","mAge":20,"sex":2,"industry":""},{"num":0.11,"userId":"ec94d2f38c644b14807ae6c62513af2c","nickname":"21703101707022035","imageHead":"/upload/headImg/13582155331.png2017/3/11/b32954a89a494240833e56be1b13d87a.png","mAge":26,"sex":1,"industry":""},{"num":0,"userId":"8bca55172e5548f59be77c45954e1ae7","nickname":"陪你","imageHead":"","mAge":66,"sex":1,"industry":""},{"num":0,"userId":"ced5672fab364ded8992466a70d1e232","nickname":"乐乐","imageHead":"/upload/headImg/13703398714.png2017/2/24/877aa43a7e20404f9aaa529c7359b887.png","mAge":19,"sex":2,"industry":"计算机/互联网/通信"},{"num":0,"userId":"a7e6016b5228492fb269ea870333f56f","nickname":"。。。","imageHead":"/upload/headImg/13263181110.png2017/3/9/b11e4d1b65cb43d0b11f8a2d5135b14d.jpg","mAge":19,"sex":1,"industry":""},{"num":0,"userId":"77432045b50f42a795e7ac48c17cd174","nickname":"春花秋落","imageHead":"/upload/headImg/13703398713.png2017/2/25/722be44817eb4a0c883bff97d0bf1b4c.png","mAge":18,"sex":1,"industry":""},{"num":0,"userId":"dacc0a53fc92487bb1b1632798f6313f","nickname":"61703111102359336","imageHead":"","mAge":18,"sex":2,"industry":""},{"num":0,"userId":"92c2f097be08484bb8d48b92316c288f","nickname":"哈哈","imageHead":"/upload/headImg/13630818673.png2017/3/9/e6525c80dae64a8182e710897c404dcc.png","mAge":18,"sex":2,"industry":"计算机/互联网/通信"}]
     * myRank : {"rowNo":5,"num":0,"sex":1,"nickname":"。。。","imageHead":"/upload/headImg/13263181110.png2017/3/9/b11e4d1b65cb43d0b11f8a2d5135b14d.jpg","industry":"","userId":"a7e6016b5228492fb269ea870333f56f","mAge":19}
     */

    private int resultCode;
    private String resultDesc;
    private MyRankBean myRank;
    private List<RankListBean> rankList;

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

    public MyRankBean getMyRank() {
        return myRank;
    }

    public void setMyRank(MyRankBean myRank) {
        this.myRank = myRank;
    }

    public List<RankListBean> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankListBean> rankList) {
        this.rankList = rankList;
    }

    public static class MyRankBean {
        /**
         * rowNo : 5.0
         * num : 0.0
         * sex : 1
         * nickname : 。。。
         * imageHead : /upload/headImg/13263181110.png2017/3/9/b11e4d1b65cb43d0b11f8a2d5135b14d.jpg
         * industry :
         * userId : a7e6016b5228492fb269ea870333f56f
         * mAge : 19
         */

        private int rowNo;
        private String num;
        private int sex;
        private String nickname;
        private String imageHead;
        private String industry;
        private String userId;
        private int age;

        public int getRowNo() {
            return rowNo;
        }

        public void setRowNo(int rowNo) {
            this.rowNo = rowNo;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
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

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public static class RankListBean {
        /**
         * num : 2.31
         * userId : faa57ee7c7724281b5cc3814f0d5cf97
         * nickname : 微微
         * imageHead : /upload/headImg/18610454505.png2017/3/7/d571d7f9f01a485cb77341abbf6cbaa4.png
         * mAge : 24
         * sex : 2
         * industry :
         */

        private String num;
        private String userId;
        private String nickname;
        private String imageHead;
        private int age;
        private int sex;
        private String industry;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }
    }
}
