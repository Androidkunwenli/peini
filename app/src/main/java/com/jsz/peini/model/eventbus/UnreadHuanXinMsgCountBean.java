package com.jsz.peini.model.eventbus;

/**
 * 环信未读消息的数量
 */

public class UnreadHuanXinMsgCountBean {

    private int mUnreadMsgCount;
    private String mMsg;

    public UnreadHuanXinMsgCountBean(int unreadMsgCount, String msg) {
        mUnreadMsgCount = unreadMsgCount;
        mMsg = msg;
    }

    public int getUnreadMsgCount() {
        return mUnreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        mUnreadMsgCount = unreadMsgCount;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String msg) {
        mMsg = msg;
    }
}
