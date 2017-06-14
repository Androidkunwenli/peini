package com.jsz.peini.model.square;

import java.io.Serializable;

/**
 * Created by th on 2016/12/26.
 */

public class CommentListBean implements Serializable {

    /**
     * "id": 10,
     * "userId": 1,
     * "userNickname": "大驴",
     * "toUserId": 2,
     * "toUserNickname": "6",
     * "content": "巴巴爸爸不不不不"
     */
    private int id;
    private String userId;
    private String userNickname;
    private String toUserId;
    private String toUserNickname;
    private String content;

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

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserNickname() {
        return toUserNickname;
    }

    public void setToUserNickname(String toUserNickname) {
        this.toUserNickname = toUserNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", userId:" + userId +
                ", userNickname:'" + userNickname + '\'' +
                ", toUserId:" + toUserId +
                ", toUserNickname:'" + toUserNickname + '\'' +
                ", content:'" + content + '\'' +
                '}';
    }
}
