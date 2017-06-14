package com.jsz.peini.model.eventbus;

/**
 * Created by lenovo on 2017/3/16.
 */

public class LoodingData {
    private boolean looding;
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isLooding() {
        return looding;
    }

    public void setLooding(boolean looding) {
        this.looding = looding;
    }

    public LoodingData(boolean looding) {
        this.looding = looding;
    }

    public LoodingData(boolean looding, int page) {
        this.looding = looding;
        this.page = page;
    }
}
