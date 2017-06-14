package com.jsz.peini.model.eventbus;

/**
 * Created by lenovo on 2017/3/11.
 */

public class TaskEvaluationSuccess {
    /**
     * 任务评价成功 刷新界面
     */
    private boolean isRefresh;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public TaskEvaluationSuccess(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
}
