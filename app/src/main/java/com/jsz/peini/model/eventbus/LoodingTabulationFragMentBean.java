package com.jsz.peini.model.eventbus;

/**
 * Created by lenovo on 2017/4/4.
 */

public class LoodingTabulationFragMentBean {
    private boolean looding;
    private String Title;
    private String isFirst;

    public LoodingTabulationFragMentBean(boolean looding) {
        this.looding = looding;
    }

    public LoodingTabulationFragMentBean(boolean looding, String title) {
        this.looding = looding;
        Title = title;
    }

    public LoodingTabulationFragMentBean(boolean looding, String title, String isFirst) {
        this.looding = looding;
        Title = title;
        this.isFirst = isFirst;
    }

    public boolean isLooding() {
        return looding;
    }

    public void setLooding(boolean looding) {
        this.looding = looding;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }

    @Override
    public String toString() {
        return "LoodingTabulationFragMentBean{" +
                "looding=" + looding +
                ", Title='" + Title + '\'' +
                ", isFirst='" + isFirst + '\'' +
                '}';
    }
}
