package com.jsz.peini.model.eventbus;

/**
 * 任务相关的所有刷新
 * Created by lenovo on 2017/3/11.
 */

public class TaskReleaseRefreshTaskAndMapList {
    private boolean isRefresh;

    public TaskReleaseRefreshTaskAndMapList(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }
}
