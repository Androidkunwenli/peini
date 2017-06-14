package com.jsz.peini.model.square;

import java.util.List;

/**
 * Created by th on 2016/12/26.
 */

public class AddCommentListBean {

    /**
     * resultCode : 1
     * resultDesc : 成功
     * commentList : [{"id":32,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":33,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":34,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":35,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":36,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":37,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":38,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":39,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":40,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":41,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":42,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":43,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":44,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":45,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"},{"id":97,"userId":1,"userNickname":"大驴","toUserId":2,"toUserNickname":"美屡","content":"你好啊,梦乐乐同志,最近在干嘛啊"}]
     */

    private int resultCode;
    private String resultDesc;
    private List<CommentListBean> commentList;

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

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "AddCommentListBean{" +
                "resultCode=" + resultCode +
                ", resultDesc='" + resultDesc + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}
