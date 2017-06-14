package com.jsz.peini.model.eventbus;

/**
 * Created by lenovo on 2017/3/11.
 */

public class SquareReleaseRefreshSquarList {
    private boolean isRefresh;

    public SquareReleaseRefreshSquarList(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
