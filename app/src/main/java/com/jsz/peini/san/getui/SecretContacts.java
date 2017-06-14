package com.jsz.peini.san.getui;

/**
 * Created by th on 2017/1/23.
 */
public class SecretContacts {

    private String content;
    private String paramId;
    private String title;
    private String from;
    private int type;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SecretContacts{" +
                "content='" + content + '\'' +
                ", paramId='" + paramId + '\'' +
                ", title='" + title + '\'' +
                ", from='" + from + '\'' +
                ", type=" + type +
                '}';
    }
}
