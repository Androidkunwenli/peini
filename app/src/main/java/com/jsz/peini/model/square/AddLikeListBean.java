package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by th on 2016/12/26.
 */

public class AddLikeListBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * likeList : [{"userNickname":"大驴","imageHead":"/upload/user/1/20161211145644.png","userId":1}]
     * likeCount : 1
     */

    private int resultCode;
    private String resultDesc;
    private int likeCount;
    private List<LikeListBean> likeList;

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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<LikeListBean> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<LikeListBean> likeList) {
        this.likeList = likeList;
    }
}